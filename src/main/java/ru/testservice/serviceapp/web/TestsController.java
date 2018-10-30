package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.testservice.serviceapp.model.Answer;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.service.CourseService;
import ru.testservice.serviceapp.service.QuestionService;
import ru.testservice.serviceapp.service.TestService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/tests")
public class TestsController {
    private final TestService ts;
    private final QuestionService qs;
    private final CourseService cs;

    @Autowired
    public TestsController(TestService ts, QuestionService qs, CourseService cs) {
        this.ts = ts;
        this.qs = qs;
        this.cs = cs;
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


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getTestPage(Model model, @PathVariable Long id) {
        Test test = ts.getTest(id);
        Set<Course> courses = Collections.singleton(cs.getById(test.getSection().getCourse().getId()));
        test.setQuestionsNumber(qs.countTestQuestions(id));
        int ticketsCount = test.getQuestionsNumber().intValue() / test.getSection().getQuestionsCount();
        if (ticketsCount <= 10) {
            model.addAttribute("colsCount", 1);
        } else if (ticketsCount <= 20) {
            model.addAttribute("colsCount", 2);
        } else {
            model.addAttribute("colsCount", 3);
        }
        model.addAttribute("test", test);
        model.addAttribute("ticketsCount", ticketsCount);
        model.addAttribute("allCourses", courses);
        return "test-page";
    }

    @RequestMapping(value = "/{id}/delete-all-questions", method = RequestMethod.GET)
    public String deleteTestQuestions(@PathVariable Long id) {
        qs.deleteByTestId(id);
        return "redirect:/tests/" + id + "/edit";
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

    @RequestMapping(value = "/update-orders", method = RequestMethod.POST)
    public @ResponseBody
    String updateTestOrder(@RequestParam("ids[]") List<Long> ids, @RequestParam("orders[]") List<Integer> orders) {
        return ts.uppdateOrders(ids, orders);
    }

    @RequestMapping(value = "/{testId}", method = RequestMethod.DELETE)
    public @ResponseBody
    String removeTest(@PathVariable Long testId) {
        ts.remove(testId);
        return "OK";
    }

}
