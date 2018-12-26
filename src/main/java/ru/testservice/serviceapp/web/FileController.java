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
import ru.testservice.serviceapp.service.IStorageService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileController {
    private final static Logger log = LoggerFactory.getLogger(FileController.class);
    private final IStorageService IStorageService;

    @Autowired
    public FileController(IStorageService IStorageService) {
        this.IStorageService = IStorageService;

    }

    @RequestMapping(value = "/docs", method = RequestMethod.GET)
    public String getDocs(Model model, @RequestParam(required = false, defaultValue = "false",
            name = "folder_exists") Boolean folderExists,
                          @RequestParam(value = "empty_title", defaultValue = "false") Boolean emptyFolderTitle,
                          @RequestParam(value = "ftitle", defaultValue = "") String folderTitle,
                          @RequestParam(value = "id", required = false) Long openFolderId

    ) {
        Folder rootFolder = IStorageService.getRootFolder();
        model.addAttribute("folderExists", folderExists);
        model.addAttribute("emptyTitle", emptyFolderTitle);
        model.addAttribute("folderTitle", folderTitle);
        List<Folder> flatFolders = new ArrayList<>();
        IStorageService.flattenFolders(rootFolder, flatFolders, 0);
        List<StorageEntity> rootFilderFiles = IStorageService.getFiles(rootFolder.getId());
        model.addAttribute("rootFolder", rootFolder);
        model.addAttribute("rootChildFolders", IStorageService.getChildFolders(rootFolder.getId()));
        model.addAttribute("storageEntities", rootFilderFiles);
        model.addAttribute("flatFolders", flatFolders);
        model.addAttribute("isDocs", true);
        model.addAttribute("showNav", true);
        if (openFolderId != null) {
            List<StorageEntity> openedFolderFiles = IStorageService.getFiles(openFolderId);
            model.addAttribute("openedFolderFiles", openedFolderFiles);
            model.addAttribute("openedFolderChilds", IStorageService.getChildFolders(openFolderId));
        }
        model.addAttribute("openFolderId", openFolderId);
        return "documents";
    }


    @RequestMapping(value = "/docs/folder/{id}", method = RequestMethod.GET)
    public String getFolder(Model model, @PathVariable("id") Long folderId) {
        Folder folder = IStorageService.getFolder(folderId);
        List<StorageEntity> rootFilderFiles = IStorageService.getFiles(folderId);
        model.addAttribute("rootFolder", folder);
        model.addAttribute("childFolders", IStorageService.getChildFolders(folderId));
        model.addAttribute("storageEntities", rootFilderFiles);
        return "blocks/tree-node::node-body";
    }

    @RequestMapping(value = "/docs/folder/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    String deleteFolder(@PathVariable("id") Long folderId) {
        IStorageService.removeFolder(folderId);
        return "ok";
    }

    @RequestMapping(value = "/docs/folder-edit/{id}", method = RequestMethod.GET)
    public String editFolder(@PathVariable("id") Long folderId, Model model) {
        Folder folder = IStorageService.getFolder(folderId);
        List<Folder> flatFolders = new ArrayList<>();
        IStorageService.flattenFolders(IStorageService.getRootFolder(), flatFolders, 0);
        Folder parentFolder = flatFolders.stream().filter(f -> f.getId().equals(folder.getParentFolder().getId())).findFirst().get();
        model.addAttribute("flatFolders", flatFolders);
        model.addAttribute("parentFolder", parentFolder);
        model.addAttribute("editFolder", folder);
        return "blocks/modals::folderEdit";
    }

    @RequestMapping(value = "/docs/folder-edit", method = RequestMethod.POST)
    public @ResponseBody String editFolderPOST(@RequestBody @Valid Folder folder) {
        List<Folder> parentFolderChildren = IStorageService.getChildFolders(folder.getParentFolder().getId());
        long dups = parentFolderChildren.stream().filter(f -> !f.getId().equals(folder.getId()) && f.getTitle().equals(folder.getTitle())).count();
        if(dups > 0){
            return "1";
        }
        IStorageService.updateFolder(folder);
        return "0";
    }

    @RequestMapping(value = "/docs/file/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    String deleteFile(@PathVariable("id") Long fileId) {
        IStorageService.removeFile(fileId);
        return "ok";
    }


    @RequestMapping(value = "/folder/add", method = RequestMethod.POST)
    public String getDoc(@RequestParam("folderTitle") String title,
                         @RequestParam("parentFolderId") Long parentFolderId
    ) {
        Folder parentFolder = IStorageService.getFolder(parentFolderId);
        boolean isDuplicate = false;
        if (StringUtils.isEmpty(title)) {
            return "redirect:/docs?empty_title=true";
        }
        for (Folder f : IStorageService.getChildFolders(parentFolderId)) {
            if (f.getTitle().equals(title)) {
                isDuplicate = true;
                break;
            }
        }
        if (isDuplicate) {
            return "redirect:/docs?folder_exists=true&ftitle=" + title;
        }
        IStorageService.saveFolder(title, parentFolderId);
        return "redirect:/docs";

    }

    @RequestMapping(value = {"/docsupload/", "/docsupload"}, method = RequestMethod.POST)
    public @ResponseBody
    String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("folderId") Long folderId) {
        try {
            IStorageService.store(file, folderId);
        } catch (IOException e) {

            log.error("FileUploading troubles", e);
        }
        return "ok";
    }
}
