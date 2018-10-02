package ru.testservice.serviceapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.model.Question;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
    @Query("from Course c where c.id <> :id")
    Iterable<Course> findAllExcept(@Param("id") Long id);
}
