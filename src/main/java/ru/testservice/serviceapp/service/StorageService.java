package ru.testservice.serviceapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.testservice.serviceapp.model.Folder;
import ru.testservice.serviceapp.model.StorageEntity;
import ru.testservice.serviceapp.repository.FolderRepository;
import ru.testservice.serviceapp.repository.StorageRepository;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.thymeleaf.util.StringUtils.repeat;

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

    public List<Folder> getChildFolders(Long folderId){
        return fr.findAllByParentFolderId(folderId);
    }

    public void store(MultipartFile file, Long folderId) throws IOException {
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        StorageEntity entity = new StorageEntity(originalFilename, LINK_PREFIX + originalFilename, contentType, file.getBytes());
        if (folderId != null) {
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


    public void flattenFolders(Folder rootFolder, List<Folder> flatFolders, int depth) {
        flatFolders.add(rootFolder);
        String prefix = depth == 0 ? "" : repeat("--", depth);
        rootFolder.setFlatTitle(prefix.concat(rootFolder.getTitle()));
        getChildFolders(rootFolder.getId()).forEach(f -> flattenFolders(f, flatFolders, depth + 1));

    }

    public void flattenFoldersTop(Folder rootFolder, List<Folder> flatFolders) {
        flatFolders.add(rootFolder);
        rootFolder.setFlatTitle(rootFolder.getTitle());
        getChildFolders(rootFolder.getId()).forEach(f -> {
            f.setFlatTitle(f.getTitle());
            flatFolders.add(f);
        });

    }

    @Transactional
    public List<StorageEntity> getFiles(Long id) {
        return repository.findAllByFolderId(id);
    }

    public Folder getFolder(Long folderId) {
        return fr.findById(folderId).orElse(null);
    }

    public void saveFolder(String title, Long parentFolderId) {
        Folder f = new Folder();
        f.setTitle(title);
        f.setParentFolder(new Folder(parentFolderId));
        fr.save(f);
    }

    @Transactional
    public void removeFolder(Long folderId) {
        fr.deleteAllByParentFolderId(folderId);
        repository.deleteAllByFolderId(folderId);
        fr.deleteById(folderId);
    }

    public void removeFile(Long fileId) {
        repository.deleteById(fileId);
    }

    public void updateFolder(Folder folder) {
        fr.save(folder);
    }
}
