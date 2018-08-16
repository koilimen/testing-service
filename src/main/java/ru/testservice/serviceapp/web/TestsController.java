package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.testservice.serviceapp.model.Answer;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.service.QuestionService;
import ru.testservice.serviceapp.service.TestService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tests")
public class TestsController {
    private final TestService ts;
    private final QuestionService qs;

    @Autowired
    public TestsController(TestService ts, QuestionService qs) {
        this.ts = ts;
        this.qs = qs;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getPage(Model model, @RequestParam(required = false) Long editId) {
        model.addAttribute("newTest", new Test());
        List<Test> tests = (List<Test>) ts.getTests();
        model.addAttribute("tests", tests);
        model.addAttribute("editableTest", tests.stream().filter(t -> t.getId().equals(editId)).findFirst().orElseGet(Test::new));
        return "tests";
    }

    @RequestMapping(value = "/{id}/edit", method = {RequestMethod.GET})
    public String getTestEditPage(Model model, @PathVariable Long id) {
        Test test = ts.getTest(id);
        model.addAttribute("test", test);
        Question newQuestion = new Question();
        newQuestion.setAnswers(new ArrayList<>());
        newQuestion.getAnswers().add(new Answer());
        newQuestion.getAnswers().add(new Answer());
        newQuestion.getAnswers().add(new Answer());
        model.addAttribute("newQuestion", newQuestion);
        return "test-edit-page";
    }

    @RequestMapping(value = "/{id}/edit", method = {RequestMethod.PUT})
    public String saveNewQuestion(Model model, @PathVariable Long id, @ModelAttribute("newQuestion") Question question,
                                  BindingResult result) {
        Test test = ts.getTest(id);
        model.addAttribute("test", test);
        if (!result.hasErrors()) {
            question.setId(null);
            question.setTest(test);
            question.setId(qs.save(question).getId());
            question.getAnswers().forEach(a -> a.setQuestion(question));
            qs.save(question);
            Question newQuestion = new Question();
            newQuestion.setAnswers(new ArrayList<>());
            newQuestion.getAnswers().add(new Answer());
            newQuestion.getAnswers().add(new Answer());
            newQuestion.getAnswers().add(new Answer());
            model.addAttribute("newQuestion", newQuestion);
        }
        return "test-edit-page";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getTestPage(Model model, @PathVariable Long id) {
        model.addAttribute("test", ts.getTest(id));
        return "test-page";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String addTest(@ModelAttribute("newTest") @Valid Test test, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ts.save(test);
            model.addAttribute("newTest", new Test());
        }
        model.addAttribute("tests", ts.getTests());
        return "tests";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateTest(@ModelAttribute("editableTest") @Valid Test test, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ts.save(test);
        }
        model.addAttribute("tests", ts.getTests());
        model.addAttribute("newTest", new Test());
        model.addAttribute("editableTest", new Test());
        return "tests";
    }

    @RequestMapping(value = "/{testId}", method = RequestMethod.DELETE)
    public @ResponseBody
    String removeTest(@PathVariable Long testId) {
        ts.remove(testId);
        return "OK";
    }

}
