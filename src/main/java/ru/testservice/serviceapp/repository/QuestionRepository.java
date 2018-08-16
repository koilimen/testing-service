package ru.testservice.serviceapp.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.testservice.serviceapp.model.Question;

public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
}
