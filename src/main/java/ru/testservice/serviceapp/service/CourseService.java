package ru.testservice.serviceapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.repository.CourseRepository;

@Service
public class CourseService {
    private final CourseRepository repository;

    @Autowired
    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public Course save(Course course) {
        return repository.save(course);
    }

    public Page<Course> get(Pageable pageable) {
        return repository.findAll(pageable);
    }


    public void deleteCourse(Long id) {
        repository.deleteById(id);
    }

    public Course getById(Long id) {
        return repository.findById(id).orElse(new Course());
    }

    public Iterable<Course> getAll() {
        return repository.findAll();
    }

    public Iterable<Course> getAllExcept(Course course) {
        return repository.findAllExcept(course.getId());
    }
}
