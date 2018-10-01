package ru.testservice.serviceapp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final QuestionService service;

    @Autowired
    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, HttpServletRequest request){
        service.deleteById(id);
        String referer = request.getHeader(HttpHeaders.REFERER);
        return "redirect:"+referer;
    }
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    String upload(@RequestParam("testId") Long testId, @RequestParam("docxFiles") List<MultipartFile> files) {
        Test test = new Test();
        test.setId(testId);
        service.with(test).uploadQuestions(files);
        return "ok";
    }

}
