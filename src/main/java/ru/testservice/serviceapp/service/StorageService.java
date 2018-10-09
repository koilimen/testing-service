package ru.testservice.serviceapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.testservice.serviceapp.model.Folder;
import ru.testservice.serviceapp.model.StorageEntity;
import ru.testservice.serviceapp.repository.FolderRepository;
import ru.testservice.serviceapp.repository.StorageRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StorageService {
    private final StorageRepository repository;
    private final FolderRepository fr;
    private final static String FS_PREFIX = "/opt/contents/";
    private final static String LINK_PREFIX = "/docs/";
    private final static DateTimeFormatter dtf = DateTimeFormatter.
            ofPattern("yyyyMMddhhmmss");

    @Autowired
    public StorageService(StorageRepository repository, FolderRepository fr) {
        this.repository = repository;
        this.fr = fr;
    }

    public void store(MultipartFile file, Long folderId) throws IOException {
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        StorageEntity entity = new StorageEntity(originalFilename, LINK_PREFIX + originalFilename, contentType, file.getBytes());
        if(folderId != null) {
            Folder folder = new Folder(folderId);
            entity.setFolder(folder);
        }
        repository.save(entity);
    }

    public StorageEntity getFile(Long id) {
        return repository.findById(id).get();
    }

    public List<StorageEntity> getAll() {
        return (List<StorageEntity>) repository.findAll();
    }

    public List<StorageEntity> getFiles() {
        return (List<StorageEntity>) repository.findAll();
    }

    public Folder getRootFolder() {
        return fr.findById(1L).orElse(null);
    }

    @Transactional
    public List<StorageEntity> getFiles(Long id) {
        return repository.findAllByFolderId(id);
    }

    public Folder getFolder(Long folderId) {
        return fr.findById(folderId).orElse(null);
    }

    public void saveFolder(String title, Folder parentFolder) {
        Folder f = new Folder();
        f.setTitle(title);

        parentFolder.getChildFolders().add(f);
        fr.save(parentFolder);
    }
}
