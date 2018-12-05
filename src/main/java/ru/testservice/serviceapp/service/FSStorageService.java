package ru.testservice.serviceapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.testservice.serviceapp.model.Folder;
import ru.testservice.serviceapp.model.StorageEntity;
import ru.testservice.serviceapp.repository.FolderRepository;
import ru.testservice.serviceapp.repository.StorageRepository;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.thymeleaf.util.StringUtils.repeat;

@Service
@Profile({"prod1", "dev"})
public class FSStorageService implements IStorageService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final StorageRepository repository;
    private final FolderRepository fr;
    private final static String FS_PREFIX = "/opt/content/";

    @Autowired
    public FSStorageService(StorageRepository repository, FolderRepository fr) {
        this.repository = repository;
        this.fr = fr;
    }

    public List<Folder> getChildFolders(Long folderId) {
        return fr.findAllByParentFolderId(folderId);
    }

    @Override
    public void store(MultipartFile file, Long folderId) throws IOException {
        Folder whereToSave = fr.findById(folderId).get();
        String folderPath = getFolderPath(whereToSave);
        File fsFolder = new File(folderPath);
        boolean folderExists = true;
        if (!fsFolder.exists()) {
            folderExists = fsFolder.mkdirs();
        }
        if (folderExists) {
            String filePath = folderPath + file.getOriginalFilename();
            File storageFile = new File(filePath);
            if (!storageFile.exists()) {
                file.transferTo(storageFile);
                String contentType = file.getContentType();
                String originalFilename = file.getOriginalFilename();
                StorageEntity entity = new StorageEntity(originalFilename, filePath.replace(FS_PREFIX, ""), contentType);
                if (folderId != null) {
                    Folder folder = new Folder(folderId);
                    entity.setFolder(folder);
                }
                repository.save(entity);
            }
        }

    }

    private String getFolderPath(Folder whereToSave) {
        String path = whereToSave.getTitle() + "/";
        Folder pfolder = whereToSave.getParentFolder();
        while (pfolder != null) {
            path = pfolder.getTitle() + "/" + path;
            pfolder = pfolder.getParentFolder();
        }
        return FS_PREFIX + path;
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
        f.setParentFolder(fr.findById(parentFolderId).get());
        String folderPath = getFolderPath(f.getParentFolder()) + "/" + title;
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdirs();
            fr.save(f);
        }
    }

    @Transactional
    public void removeFolder(Long folderId) {
        File rmFolder = new File(getFolderPath(fr.findById(folderId).get()));
        if (rmFolder.exists()) {
            try {
                deleteDirectoryStream(rmFolder.toPath());
            } catch (IOException e) {
                log.error("Error deleting folder {}", rmFolder.getAbsolutePath(), e);
            }
        }
        fr.deleteAllByParentFolderId(folderId);
        repository.deleteAllByFolderId(folderId);
        fr.deleteById(folderId);
    }

    private void deleteDirectoryStream(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public void removeFile(Long fileId) {
        StorageEntity storageEntity = repository.findById(fileId).get();
        File file = new File(storageEntity.getLink());
        if (file.exists()) {
            file.delete();
        }
        repository.deleteById(fileId);
    }

    public void updateFolder(Folder folder) {
        Optional<Folder> oldFolder = fr.findById(folder.getId());
        if(oldFolder.isPresent()){
            File oldFolderFile = new File(getFolderPath(oldFolder.get()));
            if(oldFolderFile.exists()){
                try {
                    log.info("MOving {} to {}",oldFolderFile.getAbsolutePath(),  getFolderPath(folder));
                    Files.move(oldFolderFile.toPath(), new File(getFolderPath(folder)).toPath());
                } catch (IOException e) {
                    log.error("err", e);
                }
            }
        }
        fr.save(folder);
    }
}
