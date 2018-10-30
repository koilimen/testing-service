package ru.testservice.serviceapp.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.repository.QuestionRepository;
import ru.testservice.serviceapp.repository.TestRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class QuestionServiceTest {
    private final static String PATH_TO_DOC = "/home/igor/Документы/фриланс/prmbez24/G.2.1.docx";
    private final static String PATH_TO_TABLED = "/home/igor/Документы/фриланс/prmbez24/E.2.1.docx";
    @Mock
    private QuestionRepository rp;
    @Mock
    private TestRepository tr;
    private QuestionService service;

    @Before
    public void wireUp() {
        MockitoAnnotations.initMocks(this);
        Pageable pageable = PageRequest.of(0, 15, Sort.Direction.ASC, "id");
        Long testId = 1L;
        when(rp.findAllByTestId(testId, pageable)).thenReturn(pageQuestions(testId, pageable));
        service = new QuestionService(rp, null, tr);
    }

    private Page<Question> pageQuestions(Long testId, Pageable pageable) {
        ru.testservice.serviceapp.model.Test test = new ru.testservice.serviceapp.model.Test();
        test.setId(testId);
        List<Question> ql = new ArrayList<>(pageable.getPageSize());
        for (int i = 0; i < pageable.getPageSize(); i++) {
            ql.add(new Question(test));
        }
        return new PageImpl<>(ql, pageable, 100);
    }


    @Test
    public void uploadQuestionsTabled() throws IOException {
        MultipartFile mpf = new MockMultipartFile("file__tabled", new FileInputStream(PATH_TO_TABLED));
        List<MultipartFile> files = Collections.singletonList(mpf);
        service.uploadQuestions(files);

    }

    @Test
    public void uploadQuestionsLIST() throws IOException {
        MultipartFile mpf = new MockMultipartFile("file", new FileInputStream(PATH_TO_DOC));
        List<MultipartFile> files = Collections.singletonList(mpf);
        service.uploadQuestions(files);
    }

    @Test
    public void testGetQuestionsByTestIdPageable() {
        Long testId = 1L;
        Pageable pageable = PageRequest.of(0, 15, Sort.Direction.ASC, "id");
        Page<Question> questionsPage = service.getQuestions(testId, pageable);
        assertNotNull("QuestionPage is null", questionsPage);
        assertEquals(15, questionsPage.getSize());
        assertEquals(0, questionsPage.getNumber());
    }
}