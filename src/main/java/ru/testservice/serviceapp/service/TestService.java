package ru.testservice.serviceapp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Test> findAllBySectionId(Long id, Pageable pageable){
        return  repository.findAllBySectionId(id, pageable);
    }
}
