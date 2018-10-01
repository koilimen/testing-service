package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public String getTestEditPage(Model model, @PathVariable Long id, @PageableDefault(page = 0, size = 15) Pageable pageable) {
        Test test = ts.getTest(id);
        test.setQuestionsNumber(qs.countTestQuestions(id));
        model.addAttribute("test", test);
        Question newQuestion = new Question();
        newQuestion.setAnswers(new ArrayList<>());
        newQuestion.getAnswers().add(new Answer());
        newQuestion.getAnswers().add(new Answer());
        newQuestion.getAnswers().add(new Answer());
        model.addAttribute("newQuestion", newQuestion);
        model.addAttribute("pageActive", pageable.getPageNumber());
        model.addAttribute("questions", qs.getQuestions(id, pageable));
        return "test-edit-page";
    }

    @RequestMapping(value = "/{id}/edit", method = {RequestMethod.PUT})
    public String saveNewQuestion(Model model, @PathVariable Long id, @ModelAttribute("newQuestion") Question question,
                                  BindingResult result) {
        Test test = ts.getTest(id);
        if (!result.hasErrors()) {
            question.setId(null);
            question.setTest(test);
            question.setId(qs.save(question).getId());
            question.getAnswers().forEach(a -> a.setQuestion(question));
            qs.save(question);
            test.setQuestionsNumber(qs.countTestQuestions(test.getId()));
            test = ts.save(test);
            Question newQuestion = new Question();
            newQuestion.setAnswers(new ArrayList<>());
            newQuestion.getAnswers().add(new Answer());
            newQuestion.getAnswers().add(new Answer());
            newQuestion.getAnswers().add(new Answer());
            model.addAttribute("newQuestion", newQuestion);
        }
        model.addAttribute("test", test);
        return "test-edit-page";
    }
    @RequestMapping(value="/render/answer", method = RequestMethod.GET)
    public String renderAnswerLine(@RequestParam Integer index, Model model){
        model.addAttribute("index", index);
        return "/partials/question :: question";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getTestPage(Model model, @PathVariable Long id) {
        Test test = ts.getTest(id);
        test.setQuestionsNumber(qs.countTestQuestions(id));
        model.addAttribute("test", test);
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
