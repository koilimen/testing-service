package ru.testservice.serviceapp.repository;

import org.springframework.data.repository.CrudRepository;
import ru.testservice.serviceapp.model.Folder;
import ru.testservice.serviceapp.model.StorageEntity;

import java.util.List;

public interface FolderRepository extends CrudRepository<Folder, Long> {
    List<Folder> findAllByParentFolderId(Long folderId);
    void deleteAllByParentFolderId(Long folderId);
}
