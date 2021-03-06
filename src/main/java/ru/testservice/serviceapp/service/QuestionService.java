package ru.testservice.serviceapp.service;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.testservice.serviceapp.model.Answer;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.repository.AnswerRepository;
import ru.testservice.serviceapp.repository.QuestionRepository;
import ru.testservice.serviceapp.repository.TestRepository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class QuestionService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final QuestionRepository repository;
    private final AnswerRepository answerRepository;
    private final TestRepository tr;
    private Question buferQuestion;
    private Test testLink;

    @Autowired
    public QuestionService(QuestionRepository repository, AnswerRepository answerRepository, TestRepository tr) {
        this.repository = repository;
        this.answerRepository = answerRepository;
        this.tr = tr;
    }

    public QuestionService with(Test test) {
        this.testLink = test;
        return this;
    }

    public Question save(Question question) {
        boolean isNew = question.getId() == null;
        question = repository.save(question);
        if (isNew) {
            question.getTest().increaseQN();
            tr.save(question.getTest());
        }
        return question;
    }

    public List<Question> getQuestions(List<Long> questionIds) {
        return (List<Question>) repository.findAllById(questionIds);
    }

    private void extract(XWPFParagraph paragraph, List<Question> questions, boolean isQuestionText) {
        String text = paragraph.getText().trim();
        if (text.isEmpty() || text.length() < 3) return;

        if (isQuestionText) {
            // Текст вопроса - нужно создавать новый вопрос
            if (text.indexOf(".") <= 3 && text.indexOf(".") > 0) {
                text = text.substring(text.indexOf(".") + 1);
            }
            if (buferQuestion != null) {
                if (!buferQuestion.getAnswers().isEmpty()) {
                    questions.add(buferQuestion.clone());
                    log.debug("{}", buferQuestion.toString());
                }
            }
            buferQuestion = new Question(text.trim(), new ArrayList<>(), testLink);
        } else {
            // Текст ответа - если создан новый вопрос - добавляем ответ, если нет - прерываемся
            if (text.charAt(1) == ')') text = text.substring(2);
            if (buferQuestion == null) return;
            XWPFRun isItalic = paragraph.getRuns().stream().filter(XWPFRun::isItalic).findFirst().orElse(null);
            Answer ans = new Answer(text.trim(), null != isItalic, buferQuestion);
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
                        }
                    }
                }
            }
            questions.add(this.buferQuestion.clone());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void parseByLists(MultipartFile file, List<Question> questions) {
        try {
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(file.getInputStream()));
            xdoc.getParagraphs().forEach((XWPFParagraph paragraph) -> {
                extract(paragraph, questions, isBold(paragraph) || paragraph.getText().contains("?"));
            });
            questions.add(this.buferQuestion.clone());
        } catch (Exception ex) {
            log.error("ParseByLists exception. File name: {}", file.getOriginalFilename(), ex);
        }

    }

    private boolean isBold(XWPFParagraph paragraph) {
        return paragraph.getRuns().stream().anyMatch(XWPFRun::isBold);
    }

    public void uploadQuestions(List<MultipartFile> files) {
        List<Question> questions = new ArrayList<>();
        files.forEach(f -> {
            boolean tabled = false;
            if (f.getOriginalFilename() == null) {
                log.error("File has null original name. Will try simple strategy");
            } else {
                tabled = f.getOriginalFilename().contains("__tabled");
                log.debug("File {} {} tabled ", f.getOriginalFilename(), "is");
            }
            if (tabled) {
                parseTables(f, questions);
            } else {
                parseByLists(f, questions);
            }
        });
        int size = questions.size();
        log.info("Parsed questions: {}", size);
        testLink.setQuestionsNumber(testLink.getQuestionsNumber() + size);
        for (Question question : questions) {
            question.setOrder(size--);
        }
        repository.saveAll(questions);
        tr.save(testLink);
        testLink = null;
    }

    public Page<Question> getQuestions(@NotNull Long testId, @NotNull Pageable pageable) {
        return repository.findAllByTestId(testId, pageable);
    }

    public Page<Question> getQuestions(@NotNull Test test, List<Long> except, @NotNull Pageable pageable) {
        if (except == null || except.size() == 0) {
            return repository.findAllByTestId(test.getId(), pageable);
        }
        return repository.findAllByTestIdExcpet(test, except, pageable);
    }


    public Long countTestQuestions(Long id) {
        return repository.countQuestionsByTestId(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteByTestId(@NotNull Long id) {
        repository.deleteAllByTestId(id);
    }

    @Transactional
    public Answer saveAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public Question getQuestion(Long id) {
        return repository.findById(id).get();
    }

    public void deleteAnswerById(Long id) {
        answerRepository.deleteById(id);
    }

    public Answer getAnswer(Long id) {
        return answerRepository.findById(id).get();
    }
}
