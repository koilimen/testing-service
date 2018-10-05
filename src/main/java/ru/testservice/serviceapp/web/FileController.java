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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.testservice.serviceapp.model.StorageEntity;
import ru.testservice.serviceapp.service.StorageService;

import java.io.IOException;
import java.nio.file.Files;
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
    public String getDocs(Model model){
        List<StorageEntity> files = storageService.getFiles();
        model.addAttribute("files", files);
        return "documents";
    }

    @RequestMapping(value = "/docs/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getDoc(@PathVariable Long id){
        StorageEntity file = storageService.getFile(id);
        ByteArrayResource resource = new ByteArrayResource(file.getFile());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                String.format("inline; filename=\"" + file.getName() + "\""));
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
    @RequestMapping(value = "/docsupload/", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            storageService.store(file);
        } catch (IOException e) {

            log.error("FileUploading troubles", e);
        }
        return "redirect:/";
    }
}
