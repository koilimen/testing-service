package ru.testservice.serviceapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.testservice.serviceapp.model.Question;

public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
    Page<Question> findAllByTestId(Long testId, Pageable pageable);

    Long countQuestionsByTestId(Long id);
}
