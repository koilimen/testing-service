package ru.testservice.serviceapp.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.testservice.serviceapp.model.Folder;
import ru.testservice.serviceapp.model.StorageEntity;

import java.io.IOException;
import java.util.List;

public interface IStorageService {
    List<Folder> getChildFolders(Long folderId);

    void store(MultipartFile file, Long folderId) throws IOException;

    StorageEntity getFile(Long id);

    List<StorageEntity> getAll();

    List<StorageEntity> getFiles();

    Folder getRootFolder();

    void flattenFolders(Folder rootFolder, List<Folder> flatFolders, int depth);

    void flattenFoldersTop(Folder rootFolder, List<Folder> flatFolders);

    @Transactional
    List<StorageEntity> getFiles(Long id);

    Folder getFolder(Long folderId);

    void saveFolder(String title, Long parentFolderId);

    @Transactional
    void removeFolder(Long folderId);

    void removeFile(Long fileId);

    void updateFolder(Folder folder);
}
