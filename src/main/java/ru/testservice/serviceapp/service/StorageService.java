package ru.testservice.serviceapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.testservice.serviceapp.model.StorageEntity;
import ru.testservice.serviceapp.repository.StorageRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StorageService {
    private final StorageRepository repository;
    private final static String FS_PREFIX = "/opt/contents/";
    private final static String LINK_PREFIX = "/docs/";
    private final static DateTimeFormatter dtf = DateTimeFormatter.
            ofPattern("yyyyMMddhhmmss");

    @Autowired
    public StorageService(StorageRepository repository) {
        this.repository = repository;
    }

    public void store(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        repository.save(new StorageEntity(originalFilename, LINK_PREFIX + originalFilename, contentType, file.getBytes()));
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
}
