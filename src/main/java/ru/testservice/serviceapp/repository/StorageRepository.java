package ru.testservice.serviceapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.testservice.serviceapp.model.StorageEntity;
import ru.testservice.serviceapp.model.Test;

import javax.persistence.NamedNativeQuery;
import java.util.List;

public interface StorageRepository extends CrudRepository<StorageEntity, Long> {
    List<StorageEntity> findAllByFolderId(Long folderId);

    void deleteAllByFolderId(Long folderId);
}
