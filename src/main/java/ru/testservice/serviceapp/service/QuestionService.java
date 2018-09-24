package ru.testservice.serviceapp.service;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.testservice.serviceapp.model.Answer;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class QuestionService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final QuestionRepository repository;
    private Question buferQuestion;
    private Test testLink;

    @Autowired
    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public QuestionService with(Test test) {
        this.testLink = test;
        return this;
    }

    public Question save(Question question) {
        return repository.save(question);
    }

    public List<Question> getQuestions(List<Long> questionIds) {
        return (List<Question>) repository.findAllById(questionIds);
    }

    private void extract(XWPFParagraph paragraph, List<Question> questions) {
        String text = paragraph.getText().trim();
        XWPFRun isColored = paragraph.getRuns().stream().filter(r -> r.getColor() != null).findFirst().orElse(null);
        XWPFRun isBold = paragraph.getRuns().stream().filter(r -> r.isBold()).findFirst().orElse(null);
        if (isBold != null || (isColored != null && isColored.getColor().equals("00B050"))) {
            // Текст вопроса - нужно создавать новый вопрос
            if (buferQuestion != null) {
                questions.add(buferQuestion.clone());
                log.info("Question added with test: {}", buferQuestion.getQuestionText());
            }
            buferQuestion = new Question(text, new ArrayList<>(), testLink);
        } else {
            // Текст ответа - если создан новый вопрос - добавляем ответ, если нет - прерываемся
            if (buferQuestion == null) return;
            XWPFRun isItalic = paragraph.getRuns().stream().filter(XWPFRun::isItalic).findFirst().orElse(null);
            Answer ans = new Answer(text, null != isItalic, buferQuestion);
            buferQuestion.getAnswers().add(ans);
            log.info("Answer added with text: {}", ans.getAnswerText());
        }
    }

    private void parse(MultipartFile file, List<Question> questions) {
        try {
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(file.getInputStream()));
            Iterator bodyElementIterator = xdoc.getBodyElementsIterator();
            while (bodyElementIterator.hasNext()) {
                IBodyElement element = (IBodyElement) bodyElementIterator.next();

                if (BodyElementType.TABLE == element.getElementType()) {
                    List<XWPFTable> tableList = element.getBody().getTables();
                    for (XWPFTable table : tableList) {
                        for (int i = 0; i < table.getRows().size(); i++) {
                            XWPFTableRow row = table.getRow(i);
                            XWPFTableCell cell = row.getCell(0);
                            XWPFParagraph paragraph = cell.getParagraphArray(0);
                            if (paragraph.getText().trim().isEmpty()) continue;
                            extract(paragraph, questions);
                        }
                    }
                } else if (BodyElementType.PARAGRAPH == element.getElementType()) {
                    for (XWPFParagraph paragraph : element.getBody().getParagraphs()) {
                        if (paragraph.getText().trim().isEmpty()) continue;
                        extract(paragraph, questions);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("errr", ex);
        }

    }

    public HttpStatus uploadQuestions(List<MultipartFile> files) {
        List<Question> questions = new ArrayList<>();
        files.stream().forEach(f -> {
            parse(f, questions);
        });
        log.info("Parsed questions: {}", questions.size());
        testLink = null;
        return HttpStatus.OK;
    }
}
