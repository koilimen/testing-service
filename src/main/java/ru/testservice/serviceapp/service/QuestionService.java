package ru.testservice.serviceapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.repository.QuestionRepository;

@Service
public class QuestionService {
    private final QuestionRepository repository;

    @Autowired
    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public Question save(Question question){
        return repository.save(question);
    }
}
