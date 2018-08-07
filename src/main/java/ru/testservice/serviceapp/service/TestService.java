package ru.testservice.serviceapp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.repository.TestRepository;

import javax.annotation.PostConstruct;

@Service
public class TestService {
    private final TestRepository repository;

    @Autowired
    public TestService(TestRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void onInit(){
        repository.save(new Test("Тест 1", "Описание теста 1", (short)12));
        repository.save(new Test("Тест 2", "Описание теста 2", (short)15));
        repository.save(new Test("Тест 3", "Описание теста 3", (short)11));
    }
    public Iterable<Test> getTests(){
        return repository.findAll();
    }

    public void save(Test test) {
         repository.save(test);
    }

    public void remove(Long testId) {
        repository.deleteById(testId);
    }

    public Test getTest(Long id) {
        return repository.findById(id).orElse(null);
    }
}
