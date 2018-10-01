package ru.testservice.serviceapp.service;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.testservice.serviceapp.model.Answer;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.repository.QuestionRepository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    private  void  extract(XWPFParagraph paragraph, List<Question> questions, boolean isQuestionText) {
        String text = paragraph.getText().trim();
        if (text.isEmpty() || text.length() < 3) return;
        if (isQuestionText) {
            // Текст вопроса - нужно создавать новый вопрос
            if (buferQuestion != null) {
                if (!buferQuestion.getAnswers().isEmpty())
                    questions.add(buferQuestion.clone());
                log.debug("{}", buferQuestion.toString());
            }
            buferQuestion = new Question(text, new ArrayList<>(), testLink);
        } else {
            // Текст ответа - если создан новый вопрос - добавляем ответ, если нет - прерываемся
            if (buferQuestion == null) return;
            XWPFRun isItalic = paragraph.getRuns().stream().filter(XWPFRun::isItalic).findFirst().orElse(null);
            Answer ans = new Answer(text, null != isItalic, buferQuestion);
            buferQuestion.getAnswers().add(ans);
        }
    }


    private void parseTables(MultipartFile file, List<Question> questions) {
        try {
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(file.getInputStream()));
            Iterator bodyElementIterator = xdoc.getBodyElementsIterator();
            while (bodyElementIterator.hasNext()) {
                IBodyElement element = (IBodyElement) bodyElementIterator.next();
                if ("TABLE".equalsIgnoreCase(element.getElementType().name())) {
                    List<XWPFTable> tableList = element.getBody().getTables();
                    for (XWPFTable table : tableList) {
                        for (int i = 0; i < table.getRows().size(); i++) {
                            XWPFTableRow row = table.getRow(i);
                            XWPFTableCell cell = row.getCell(0);
                            XWPFParagraph paragraph = cell.getParagraphArray(0);
                            String numFmt = paragraph.getNumFmt();
                            extract(paragraph, questions, numFmt != null && numFmt.equalsIgnoreCase("decimal"));
                            if(questions.size() == 15){
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    private void parseByLists(MultipartFile file, List<Question> questions) {
        try {
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(file.getInputStream()));
            xdoc.getParagraphs().stream().forEach((XWPFParagraph paragraph) -> {
                extract(paragraph, questions, paragraph.getNumFmt() == null);

            });
        } catch (Exception ex) {
            log.error("ParseByLists exception. File name: {}", file.getOriginalFilename(), ex);
        }

    }

    public void uploadQuestions(List<MultipartFile> files) {
        List<Question> questions = new ArrayList<>();
        files.forEach(f -> {
            boolean tabled = f.getName().contains("__tabled");
            log.debug("File {} {} tabled ", f.getName(), tabled ? "is" : "is not");
            if (tabled) {
                parseTables(f, questions);
            } else {
                parseByLists(f, questions);
            }
        });
        log.info("Parsed questions: {}", questions.size());
        testLink = null;
        repository.saveAll(questions);
    }

    public Page<Question> getQuestions(@NotNull Long testId, @NotNull Pageable pageable) {
        return repository.findAllByTestId(testId, pageable);
    }

    public Long countTestQuestions(Long id) {
        return repository.countQuestionsByTestId(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
