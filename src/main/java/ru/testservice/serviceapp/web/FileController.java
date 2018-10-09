package ru.testservice.serviceapp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import ru.testservice.serviceapp.model.Folder;
import ru.testservice.serviceapp.model.StorageEntity;
import ru.testservice.serviceapp.service.StorageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.thymeleaf.util.StringUtils.repeat;

@Controller
public class FileController {
    private final static Logger log = LoggerFactory.getLogger(FileController.class);
    private final StorageService storageService;

    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;

    }

    @RequestMapping(value = "/docs", method = RequestMethod.GET)
    public String getDocs(Model model, @RequestParam(required = false, defaultValue = "false",
            name = "folder_exists") Boolean folderExists,
                          @RequestParam(value = "empty_title", defaultValue = "false") Boolean emptyFolderTitle,
                          @RequestParam(value = "ftitle", defaultValue = "") String folderTitle
    ) {
        Folder rootFolder = storageService.getRootFolder();
        model.addAttribute("folderExists", folderExists);
        model.addAttribute("emptyTitle", emptyFolderTitle);
        model.addAttribute("folderTitle", folderTitle);
        List<Folder> flatFolders = new ArrayList();
        flattenFolders(rootFolder, flatFolders, 0);
        List<StorageEntity> rootFilderFiles = storageService.getFiles(rootFolder.getId());
        model.addAttribute("rootFolder", rootFolder);
        model.addAttribute("storageEntities", rootFilderFiles);
        model.addAttribute("flatFolders", flatFolders);
        return "documents";
    }

    private void flattenFolders(Folder rootFolder, List<Folder> flatFolders, int depth) {
        flatFolders.add(rootFolder);
        String prefix = depth == 0 ? "" : repeat("--", depth);
        rootFolder.setFlatTitle(prefix.concat(rootFolder.getTitle()));
        rootFolder.getChildFolders().forEach(f -> flattenFolders(f, flatFolders, depth + 1));

    }

    @RequestMapping(value = "/docs/folder/{id}", method = RequestMethod.GET)
    public String getFolder(Model model, @PathVariable("id") Long folderId) {
        Folder folder = storageService.getFolder(folderId);
        List<StorageEntity> rootFilderFiles = storageService.getFiles(folderId);
        model.addAttribute("rootFolder", folder);
        model.addAttribute("storageEntities", rootFilderFiles);
        return "blocks/tree-node::node-body";
    }

    @RequestMapping(value = "/docs/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getDoc(@PathVariable Long id) {
        StorageEntity file = storageService.getFile(id);
        ByteArrayResource resource = new ByteArrayResource(file.getFile());
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition",
                "inline; filename=\"" + file.getName() + "\"");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @RequestMapping(value = "/folder/add", method = RequestMethod.POST)
    public String getDoc(@RequestParam("folderTitle") String title,
                         @RequestParam("parentFolderId") Long parentFolderId
    ) {
        Folder parentFolder = storageService.getFolder(parentFolderId);
        boolean isDuplicate = false;
        if (StringUtils.isEmpty(title)) {
            return "redirect:/docs?empty_title=true";
        }
        for (Folder f : parentFolder.getChildFolders()) {
            if (f.getTitle().equals(title)) {
                isDuplicate = true;
                break;
            }
        }
        if (isDuplicate) {
            return "redirect:/docs?folder_exists=true&ftitle=" + title;
        }
        storageService.saveFolder(title, parentFolder);
        return "redirect:/docs";

    }

    @RequestMapping(value = "/docsupload/", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("folderId") Long folderId) {
        try {
            storageService.store(file, folderId);
        } catch (IOException e) {

            log.error("FileUploading troubles", e);
        }
        return "redirect:/docs";
    }
}
