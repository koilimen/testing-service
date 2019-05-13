package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.testservice.serviceapp.model.Answer;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.service.CourseService;
import ru.testservice.serviceapp.service.QuestionService;
import ru.testservice.serviceapp.service.TestService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
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

    @RequestMapping(value = "/add-literature", method = RequestMethod.POST)
    public @ResponseBody
    String addLiterature(@RequestParam Long testId, @RequestParam String title) {
        ts.addLiterature(testId, title);
        return "SUCCESS";
    }

    @RequestMapping(value = "/del-literature/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    String addLiterature(@PathVariable Long id) {
        ts.delLiterature(id);
        return "SUCCESS";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getPage(Model model, @RequestParam(required = false) Long editId) {
        model.addAttribute("newTest", new Test());
        List<Test> tests = (List<Test>) ts.getTests();
        model.addAttribute("tests", tests);
        model.addAttribute("editableTest", tests.stream().filter(t -> t.getId().equals(editId)).findFirst().orElseGet(Test::new));
        return "section-page";
    }

    @RequestMapping(value = "/{id}/edit", method = {RequestMethod.GET})
    public String getTestEditPage(Model model, @PathVariable Long id, @PageableDefault(page = 0, size = 15, sort = "order",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Test test = ts.getTest(id);
        test.setQuestionsNumber(qs.countTestQuestions(id));
        model.addAttribute("test", test);
        model.addAttribute("testLiterature", ts.getLiterature(id));
        Question newQuestion = new Question();
        newQuestion.setOrder(test.getQuestionsNumber().intValue() + 1);
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
    public ModelAndView saveNewQuestion(Model model, @PathVariable Long id, @ModelAttribute("newQuestion") Question question,
                                  @PageableDefault(page = 0, size = 15, sort = "order",
                                          direction = Sort.Direction.DESC) Pageable pageable,
                                  BindingResult result) {
        Test test = ts.getTest(id);
        if (!result.hasErrors()) {
            if (question.getId() != null && question.getId() > 0) {
                qs.save(question);
                RedirectView redirectView = new RedirectView("/tests/" + id + "/edit?page=" + pageable.getPageNumber());
                redirectView.setExposeModelAttributes(false);
                return new ModelAndView(redirectView);
            }
            question.setId(null);
            question.setTest(test);
            question.setId(qs.save(question).getId());
            question.getAnswers().forEach(a -> a.setQuestion(question));
            qs.save(question);
            test.setQuestionsNumber(qs.countTestQuestions(test.getId()));
            ts.save(test);
            Question newQuestion = new Question();
            newQuestion.setAnswers(new ArrayList<>());
            newQuestion.getAnswers().add(new Answer());
            newQuestion.getAnswers().add(new Answer());
            newQuestion.getAnswers().add(new Answer());
            model.addAttribute("newQuestion", newQuestion);
            model.addAttribute("pageActive", pageable.getPageNumber());
            model.addAttribute("questions", qs.getQuestions(id, pageable));
        }
        model.addAttribute("test", test);
        return new ModelAndView("test-edit-page", model.asMap());
    }

    @RequestMapping(value = "/{id}/edit-modal", method = {RequestMethod.GET})
    public String getModalEdit(Model model, @PathVariable Long id) {
        model.addAttribute("test", ts.getTest(id));
        return "blocks/modals::test-edit-form";
    }

    @RequestMapping(value = "/edit", method = {RequestMethod.PUT})
    public String editNewQuestion(@RequestBody @Valid Test test, BindingResult result, Model model, HttpServletResponse response) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("test", test);
            return "blocks/modals::test-edit-form";
        }
        ts.save(test);
        response.getWriter().write("OK");
        return null;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getTestPage(Model model, @PathVariable Long id) {
        Test test = ts.getTest(id);
        Set<Course> courses = Collections.singleton(cs.getById(test.getSection().getCourse().getId()));
        test.setQuestionsNumber(qs.countTestQuestions(id));

        prepareCollsCounts(model, test);

        model.addAttribute("htmlTitle", test.getDescriptionTitle());

        model.addAttribute("test", test);
        model.addAttribute("ticketsCount", test.getTicketsCount());
        model.addAttribute("testLiterature", ts.getLiterature(id));
        model.addAttribute("allCourses", courses);
        return "test-page";
    }

    private void prepareCollsCounts(Model model, Test test) {
        if (test.getTicketsCount() % 3 == 0) {
            model.addAttribute("fcc", test.getTicketsCount() / 3);
            model.addAttribute("scc", test.getTicketsCount() * 2 / 3);
            model.addAttribute("tcc", test.getTicketsCount());
        } else {
            model.addAttribute("fcc", test.getTicketsCount() / 2);
            model.addAttribute("scc", test.getTicketsCount() );
            model.addAttribute("tcc", 0);
        }
    }

    private void oldPrepareCollsCounts(Model model, Test test) {
        Integer firstColsCount = 0;
        if (test.getTicketsCount() > 3) {
            firstColsCount = test.getTicketsCount() / 3;
            if (test.getTicketsCount() % 3 != 0) {

                int cc = test.getTicketsCount() / 3;
                int c2c = test.getTicketsCount() - cc;
                if (c2c % 2 != 0) {
                    c2c = test.getTicketsCount() - cc + 1;
                }
                firstColsCount = c2c / 2;
            }
            int secColCount = test.getTicketsCount() > 3 ? firstColsCount * 2 : firstColsCount * 2 - 1;
            model.addAttribute("scc", secColCount);
            model.addAttribute("tcc", test.getTicketsCount() - secColCount);
        } else if (test.getTicketsCount() <= 3) {
            firstColsCount = test.getTicketsCount();
            model.addAttribute("scc", 0);
            model.addAttribute("tcc", 0);

        }
        model.addAttribute("fcc", firstColsCount);

    }

    @RequestMapping(value = "/{id}/delete-all-questions", method = RequestMethod.GET)
    public ModelAndView deleteTestQuestions(@PathVariable Long id) {
        qs.deleteByTestId(id);
        RedirectView redirectView = new RedirectView("/tests/" + id + "/edit");
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String addTest(@ModelAttribute("newTest") @Valid Test test, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ts.save(test);
            model.addAttribute("newTest", new Test());
        }
        model.addAttribute("tests", ts.getTests());
        return "section-page";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateTest(@ModelAttribute("editableTest") @Valid Test test, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ts.save(test);
        }
        model.addAttribute("tests", ts.getTests());
        model.addAttribute("newTest", new Test());
        model.addAttribute("editableTest", new Test());
        return "section-page";
    }

    @RequestMapping(value = "/update-orders", method = RequestMethod.POST)
    public @ResponseBody
    String updateTestOrder
            (@RequestParam("ids[]") List<Long> ids, @RequestParam("orders[]") List<Integer> orders) {
        return ts.uppdateOrders(ids, orders);
    }

    @RequestMapping(value = "/{testId}", method = RequestMethod.DELETE)
    public @ResponseBody
    String removeTest(@PathVariable Long testId) {
        ts.remove(testId);
        return "OK";
    }

    @RequestMapping(value = "/render-answer", method = RequestMethod.GET)
    public String renderAnswer(@RequestParam Integer index, @RequestParam(required = false) Long qid, Model model) {
        model.addAttribute("index", index);
        model.addAttribute("questionId", qid);
        return "blocks/new-question-answer :: new-question-answer";
    }


}
