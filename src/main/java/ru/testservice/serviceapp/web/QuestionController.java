package ru.testservice.serviceapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.testservice.serviceapp.model.Question;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/questions")
public class QuestionController {

}
