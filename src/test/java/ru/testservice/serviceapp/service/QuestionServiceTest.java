package ru.testservice.serviceapp.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.testservice.serviceapp.repository.QuestionRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class QuestionServiceTest {
    private final static String PATH_TO_DOC = "/home/igor/Документы/фриланс/prmbez24/G.2.1.docx";
    private final static String PATH_TO_TABLED = "/home/igor/Документы/фриланс/prmbez24/E.2.1.docx";
    @Mock
    private QuestionRepository rp;
    private QuestionService service;

    @Before
    public void wireUp() {
        MockitoAnnotations.initMocks(this);
        service = new QuestionService(rp);
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
}