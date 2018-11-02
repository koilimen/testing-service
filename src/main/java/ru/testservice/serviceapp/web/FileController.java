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

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                          @RequestParam(value = "ftitle", defaultValue = "") String folderTitle,
                          @RequestParam(value = "id", required = false) Long openFolderId

    ) {
        Folder rootFolder = storageService.getRootFolder();
        model.addAttribute("folderExists", folderExists);
        model.addAttribute("emptyTitle", emptyFolderTitle);
        model.addAttribute("folderTitle", folderTitle);
        List<Folder> flatFolders = new ArrayList<>();
        storageService.flattenFolders(rootFolder, flatFolders, 0);
        List<StorageEntity> rootFilderFiles = storageService.getFiles(rootFolder.getId());
        model.addAttribute("rootFolder", rootFolder);
        model.addAttribute("rootChildFolders", storageService.getChildFolders(rootFolder.getId()));
        model.addAttribute("storageEntities", rootFilderFiles);
        model.addAttribute("flatFolders", flatFolders);
        model.addAttribute("isDocs", true);
        model.addAttribute("showNav", true);
        if (openFolderId != null) {
            List<StorageEntity> openedFolderFiles = storageService.getFiles(openFolderId);
            model.addAttribute("openedFolderFiles", openedFolderFiles);
            model.addAttribute("openedFolderChilds", storageService.getChildFolders(openFolderId));
        }
        model.addAttribute("openFolderId", openFolderId);
        return "documents";
    }


    @RequestMapping(value = "/docs/folder/{id}", method = RequestMethod.GET)
    public String getFolder(Model model, @PathVariable("id") Long folderId) {
        Folder folder = storageService.getFolder(folderId);
        List<StorageEntity> rootFilderFiles = storageService.getFiles(folderId);
        model.addAttribute("rootFolder", folder);
        model.addAttribute("childFolders", storageService.getChildFolders(folderId));
        model.addAttribute("storageEntities", rootFilderFiles);
        return "blocks/tree-node::node-body";
    }

    @RequestMapping(value = "/docs/folder/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    String deleteFolder(@PathVariable("id") Long folderId) {
        storageService.removeFolder(folderId);
        return "ok";
    }

    @RequestMapping(value = "/docs/folder-edit/{id}", method = RequestMethod.GET)
    public String editFolder(@PathVariable("id") Long folderId, Model model) {
        Folder folder = storageService.getFolder(folderId);
        List<Folder> flatFolders = new ArrayList<>();
        storageService.flattenFolders(storageService.getRootFolder(), flatFolders, 0);
        Folder parentFolder = flatFolders.stream().filter(f -> f.getId().equals(folder.getParentFolder().getId())).findFirst().get();
        model.addAttribute("flatFolders", flatFolders);
        model.addAttribute("parentFolder", parentFolder);
        model.addAttribute("editFolder", folder);
        return "blocks/modals::folderEdit";
    }

    @RequestMapping(value = "/docs/folder-edit", method = RequestMethod.POST)
    public @ResponseBody String editFolderPOST(@RequestBody @Valid Folder folder) {
        List<Folder> parentFolderChildren = storageService.getChildFolders(folder.getParentFolder().getId());
        long dups = parentFolderChildren.stream().filter(f -> !f.getId().equals(folder.getId()) && f.getTitle().equals(folder.getTitle())).count();
        if(dups > 0){
            return "1";
        }
        storageService.updateFolder(folder);
        return "0";
    }

    @RequestMapping(value = "/docs/file/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    String deleteFile(@PathVariable("id") Long fileId) {
        storageService.removeFile(fileId);
        return "ok";
    }

    @RequestMapping(value = "/docs/{id}/{filename}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getDoc(@PathVariable Long id, @PathVariable("filename") String filename) {
        StorageEntity file = storageService.getFile(id);
        ByteArrayResource resource = new ByteArrayResource(file.getFile());
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition",
                "inline");
        headers.add("Content-Type",
                file.getContentType() + "; charset=utf-8");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType(file.getContentType()))
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
        for (Folder f : storageService.getChildFolders(parentFolderId)) {
            if (f.getTitle().equals(title)) {
                isDuplicate = true;
                break;
            }
        }
        if (isDuplicate) {
            return "redirect:/docs?folder_exists=true&ftitle=" + title;
        }
        storageService.saveFolder(title, parentFolderId);
        return "redirect:/docs";

    }

    @RequestMapping(value = "/docsupload/", method = RequestMethod.POST)
    public @ResponseBody
    String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("folderId") Long folderId) {
        try {
            storageService.store(file, folderId);
        } catch (IOException e) {

            log.error("FileUploading troubles", e);
        }
        return "ok";
    }
}
