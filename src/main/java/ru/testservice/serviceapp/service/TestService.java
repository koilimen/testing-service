package ru.testservice.serviceapp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.model.TestLiterature;
import ru.testservice.serviceapp.repository.LiteratureRepository;
import ru.testservice.serviceapp.repository.TestRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {
    private final TestRepository repository;
    private final LiteratureRepository litRepo;

    @Autowired
    public TestService(TestRepository repository, LiteratureRepository litRepo) {
        this.repository = repository;
        this.litRepo = litRepo;
    }


    public Iterable<Test> getTests() {
        return repository.findAll(new Sort("order"));
    }

    public Test save(Test test) {
        return repository.save(test);
    }

    public void remove(Long testId) {
        repository.deleteById(testId);
    }

    public Test getTest(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Page<Test> findAllBySectionId(Long id, Pageable pageable) {
        return repository.findAllBySectionId(id, pageable);
    }

    @Transactional
    public String uppdateOrders(List<Long> ids, List<Integer> orders) {
        for(int i = 0; i< ids.size(); i++){
            Optional<Test> byId = repository.findById(ids.get(i));
            if(byId.isPresent()){
                Test test = byId.get();
                test.setOrder(orders.get(i));
                repository.save(test);
            }
        }
        return "SUCCESS";
    }

    @Transactional
    public void removeBySectionId(Long sid) {
        repository.deleteAllBySectionId(sid);
    }

    public void addLiterature(Long id, String title) {
        if(StringUtils.isEmpty(title)) return;
        TestLiterature lit = new TestLiterature();
        lit.setTitle(title);
        lit.setTest(new Test(id));
        litRepo.save(lit);
    }

    public List<TestLiterature> getLiterature(Long id) {
        return litRepo.findAllByTestId(id);
    }

    public void delLiterature(Long id) {
        litRepo.deleteById(id);
    }
}
