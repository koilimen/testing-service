package ru.testservice.serviceapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.testservice.serviceapp.model.Test;

import java.util.List;

public interface TestRepository extends CrudRepository<Test, Long> {
    Page<Test> findAllBySectionId(Long sectionId, Pageable pageable);
}
