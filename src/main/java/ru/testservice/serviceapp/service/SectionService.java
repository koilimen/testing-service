package ru.testservice.serviceapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.testservice.serviceapp.model.Section;
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

    @Transactional
    public void deleteByCourseId(Long id) {
        repository.deleteAllByCourseId(id);
    }

    public Iterable<Section> getAll() {
        return repository.findAll();
    }
}
