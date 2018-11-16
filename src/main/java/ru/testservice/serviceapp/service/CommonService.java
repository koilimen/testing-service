package ru.testservice.serviceapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.testservice.serviceapp.model.AbstractEntity;
import ru.testservice.serviceapp.model.AbstractOrderedEntity;
import ru.testservice.serviceapp.model.Course;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public  String uppdateOrders(List<Long> ids, List<Integer> orders){
        for(int i = 0; i< ids.size(); i++){
            Optional<AbstractOrderedEntity> byId = repository().findById(ids.get(i));
            if(byId.isPresent()){
                AbstractOrderedEntity e = byId.get();
                e.setOrder(orders.get(i));
                repository().save(e);
            }
        }
        return "SUCCESS";
    };
}
