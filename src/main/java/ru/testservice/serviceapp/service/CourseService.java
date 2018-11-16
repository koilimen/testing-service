package ru.testservice.serviceapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.repository.CourseRepository;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public String uppdateOrders(List<Long> ids, List<Integer> orders) {
        for(int i = 0; i< ids.size(); i++){
            Optional<Course> byId = repository.findById(ids.get(i));
            if(byId.isPresent()){
                Course course = byId.get();
                course.setOrder(orders.get(i));
                repository.save(course);
            }
        }
        return "SUCCESS";
    }
}
