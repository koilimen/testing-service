package ru.testservice.serviceapp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.model.Section;

import java.util.List;

public interface SectionRepository extends PagingAndSortingRepository<Section, Long> {
    List<Section> findAllByCourseId(Long courseId, Pageable pageable);
    void deleteAllByCourseId(Long id);
}
