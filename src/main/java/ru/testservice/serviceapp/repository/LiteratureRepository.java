package ru.testservice.serviceapp.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.testservice.serviceapp.model.Answer;
import ru.testservice.serviceapp.model.TestLiterature;

import java.util.List;

public interface LiteratureRepository extends PagingAndSortingRepository<TestLiterature, Long> {
    List<TestLiterature> findAllByTestId(Long id);

}
