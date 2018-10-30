package ru.testservice.serviceapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.testservice.serviceapp.model.Answer;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.model.Test;

import java.util.List;

public interface AnswerRepository extends PagingAndSortingRepository<Answer, Long> {

}
