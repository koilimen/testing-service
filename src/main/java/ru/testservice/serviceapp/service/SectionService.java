package ru.testservice.serviceapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.model.Section;
import ru.testservice.serviceapp.repository.CourseRepository;
import ru.testservice.serviceapp.repository.SectionRepository;

import java.util.List;

@Service
public class SectionService  extends CommonService<SectionRepository, Long>{
    private final SectionRepository repository;

    @Autowired
    public SectionService(SectionRepository repository) {
        this.repository = repository;
    }

    @Override
    public SectionRepository repository() {
        return repository;
    }

    public List<Section> getByCourseId(Long id, Pageable pageable) {
        return repository.findAllByCourseId(id, pageable);
    }
}
