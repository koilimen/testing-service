package ru.testservice.serviceapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.testservice.serviceapp.model.AbstractEntity;

public abstract class CommonService<T extends PagingAndSortingRepository, ID > {
    public abstract T repository();
    public <E extends AbstractEntity> E save(E e) {
        return (E) repository().save(e);
    }

    public <E extends AbstractEntity>  Page<E> get(Pageable pageable) {
        return repository().findAll(pageable);
    }

    public void deleteById(ID id) {
        repository().deleteById(id);
    }

    public <E extends AbstractEntity> E getById(ID id) {
        return (E) (repository().findById(id).orElse(null));
    }
}
