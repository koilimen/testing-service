package ru.testservice.serviceapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.model.Test;

import java.util.List;

public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
    @Query("select q from Question q where q.test.id = :testId order by RANDOM()")
    Page<Question> findAllByTestId(Long testId, Pageable pageable);
    @Query("select q from Question q where q.test = :test and q.id not in :except order by RANDOM()")
    Page<Question> findAllByTestIdExcpet(@Param("test") Test test,
                                         @Param("except") List<Long> exceptQuestionIds, Pageable pageable);

    Long countQuestionsByTestId(Long id);

    void deleteAllByTestId(Long id);
}
