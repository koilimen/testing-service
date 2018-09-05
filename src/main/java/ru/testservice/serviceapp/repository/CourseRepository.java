package ru.testservice.serviceapp.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.model.Question;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
}
