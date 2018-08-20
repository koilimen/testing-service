package ru.testservice.serviceapp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.testservice.serviceapp.model.Answer;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.repository.TestRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {
    private final TestRepository repository;

    @Autowired
    public TestService(TestRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void onInit(){
        Test test1 = repository.save(new Test("Тест 1", "Описание теста 1", (short)0));
        repository.save(new Test("Тест 2", "Описание теста 2", (short)0));
        repository.save(new Test("Тест 3", "Описание теста 3", (short)0));
        test1.setQuestionList(new ArrayList<>());
        for(int i =0; i< 10; i++){
            Question q = new Question();
            q.setQuestionText("Text "+i);
            List<Answer> ans = new ArrayList<>();
            for(int j= 0; j< 3;j++){
                ans.add(new Answer("answer", i%2 ==0, q));
            }
            q.setAnswers(ans);
            q.setTest(test1);
            test1.getQuestionList().add(q);
        }
        repository.save(test1);
    }
    public Iterable<Test> getTests(){
        return repository.findAll();
    }

    public Test save(Test test) {
         return repository.save(test);
    }

    public void remove(Long testId) {
        repository.deleteById(testId);
    }

    public Test getTest(Long id) {
        return repository.findById(id).orElse(null);
    }
}
