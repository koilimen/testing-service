package ru.testservice.serviceapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.testservice.serviceapp.model.StorageEntity;
import ru.testservice.serviceapp.model.Test;

public interface StorageRepository extends CrudRepository<StorageEntity, Long> {
}
