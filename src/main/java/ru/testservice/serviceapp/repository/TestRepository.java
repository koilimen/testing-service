package ru.testservice.serviceapp.repository;

import org.springframework.data.repository.CrudRepository;
import ru.testservice.serviceapp.model.Test;

public interface TestRepository extends CrudRepository<Test, Long> {
}
