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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/section")
public class SectionController {
    private final SectionService sectionService;
    private final TestService testService;
    private final static List<Long> AD_SECTIONS = Arrays.asList(86L,59L,60L,62L,66L,67L,74L);

    @Autowired
    public SectionController( SectionService sectionService, TestService testService) {
        this.sectionService = sectionService;
        this.testService = testService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String main(@PathVariable Long id, @RequestParam(value = "editId", required = false) Long testEditId,
                       Model model, @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"order"}) Pageable pageable) {
        Section section = sectionService.getById(id);
        prepareModel(section, model, testEditId, pageable);
        model.addAttribute("newTest", new Test(section));
        model.addAttribute("htmlTitle", section.getName());
        model.addAttribute("showSectionsAd", AD_SECTIONS.contains(id));
        return "section-page";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String editForm(@PathVariable Long id, Model model) {
        Section section = sectionService.getById(id);
        model.addAttribute("section", section);
        return "blocks/modals::section-edit-form";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editForm(@RequestBody @Valid Section section, BindingResult result, Model model,
                           HttpServletResponse response) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("section", section);
            model.addAttribute("hasErrors", true);
            return "blocks/modals::section-edit-form";
        }
        sectionService.save(section);
        response.getWriter().append("OK");
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String updateTest(@PathVariable Long id, @RequestParam(value = "editId", required = false) Long testEditId,
                             @ModelAttribute("editableTest") @Valid Test editableTest, BindingResult result,
                             Model model, @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"order"}) Pageable pageable) {
        if (!result.hasErrors()) {
            testService.save(editableTest);
            return "redirect:/section/" + id;
        }
        Section section = sectionService.getById(id);
        prepareModel(section, model, null, pageable);
        model.addAttribute("newTest", new Test(section));
        return "section-page";
    }

    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteTest(@PathVariable Long id, @RequestParam(value = "deleteId") Long deleteId) {
        testService.remove(deleteId);
        return "redirect:/section/" + id;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String saveNewTest(@PathVariable Long id, @ModelAttribute("newTest") @Valid Test newTest,
                              BindingResult result,
                              Model model, @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"order"}) Pageable pageable) {
        if (!result.hasErrors()) {
            testService.save(newTest);
            return "redirect:/section/" + id;
        }
        Section section = sectionService.getById(id);
        prepareModel(section, model, null, pageable);
        return "section-page";
    }

    @RequestMapping(value = "/update-orders", method = RequestMethod.POST)
    public @ResponseBody
    String updateOrder(@RequestParam("ids[]") List<Long> ids, @RequestParam("orders[]") List<Integer> orders) {
        return sectionService.uppdateOrders(ids, orders);
    }

    private void prepareModel(Section section, Model model, Long testEditId, Pageable pageable) {
        model.addAttribute("section", section);
        List<Test> tests = testService.findAllBySectionId(section.getId(), pageable).getContent();
        model.addAttribute("tests", tests);
        if (testEditId != null)
            model.addAttribute("editableTest", tests.stream().filter(t -> t.getId().equals(testEditId)).findFirst().orElseGet(Test::new));

    }

}
