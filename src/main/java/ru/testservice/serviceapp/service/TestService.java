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
        repository.save(new Test("title", "description", (short)12));
        repository.save(new Test("title2", "description2", (short)15));
        repository.save(new Test("title3", "description3", (short)11));
    }
    public Iterable<Test> getTests(){
        return repository.findAll();
    }
}
