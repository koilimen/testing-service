package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.testservice.serviceapp.model.Section;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.service.CourseService;
import ru.testservice.serviceapp.service.SectionService;
import ru.testservice.serviceapp.service.TestService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/section")
public class SectionController {
    private final CourseService courseService;
    private final SectionService sectionService;
    private final TestService testService;

    @Autowired
    public SectionController(CourseService courseService, SectionService sectionService, TestService testService) {
        this.courseService = courseService;
        this.sectionService = sectionService;
        this.testService = testService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String main(@PathVariable Long id, @RequestParam(value = "editId", required = false) Long testEditId,
                       Model model, @PageableDefault(page = 0, size = 15, sort = {"order"}) Pageable pageable) {
        Section section = sectionService.getById(id);
        prepareModel(section, model, testEditId, pageable);
        model.addAttribute("newTest", new Test(section));
        return "tests";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String updateTest(@PathVariable Long id, @RequestParam(value = "editId", required = false) Long testEditId,
                             @ModelAttribute("editableTest") @Valid Test editableTest, BindingResult result,
                             Model model, @PageableDefault(page = 0, size = 15, sort = {"order"}) Pageable pageable) {
        if (!result.hasErrors()) {
            testService.save(editableTest);
            return "redirect:/section/" + id;
        }
        Section section = sectionService.getById(id);
        prepareModel(section, model, null, pageable);
        model.addAttribute("newTest", new Test(section));
        return "tests";
    }
    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteTest(@PathVariable Long id, @RequestParam(value = "deleteId") Long deleteId) {
        testService.remove(deleteId);
        return "redirect:/section/"+id;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String saveNewTest(@PathVariable Long id, @ModelAttribute("newTest") @Valid Test newTest,
                              BindingResult result,
                              Model model, @PageableDefault(page = 0, size = 15, sort = {"order"}) Pageable pageable) {
        if (!result.hasErrors()) {
            testService.save(newTest);
            return "redirect:/section/" + id;
        }
        Section section = sectionService.getById(id);
        prepareModel(section, model, null, pageable);
        return "tests";
    }

    private void prepareModel(Section section, Model model, Long testEditId, Pageable pageable) {
        model.addAttribute("section", section);
        List<Test> tests = testService.findAllBySectionId(section.getId(), pageable).getContent();
        model.addAttribute("tests", tests);
        if (testEditId != null)
            model.addAttribute("editableTest", tests.stream().filter(t -> t.getId().equals(testEditId)).findFirst().orElseGet(Test::new));

    }

}
