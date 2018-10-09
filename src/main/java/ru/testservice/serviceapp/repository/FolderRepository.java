package ru.testservice.serviceapp.repository;

import org.springframework.data.repository.CrudRepository;
import ru.testservice.serviceapp.model.Folder;
import ru.testservice.serviceapp.model.StorageEntity;

public interface FolderRepository extends CrudRepository<Folder, Long> {
}
