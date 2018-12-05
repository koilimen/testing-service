package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.service.CourseService;
import ru.testservice.serviceapp.service.IStorageService;
import ru.testservice.serviceapp.service.SectionService;

import javax.validation.Valid;

@Controller
public class RootController {
    private final CourseService courseService;
    private final IStorageService IStorageService;
    private final SectionService sectionService;

    @Autowired
    public RootController(CourseService courseService, IStorageService IStorageService, SectionService sectionService) {
        this.courseService = courseService;
        this.IStorageService = IStorageService;
        this.sectionService = sectionService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(Model model, @PageableDefault(page = 0, size = 15, sort = {"order"}) Pageable pageable) {
        model.addAttribute("newCourse", new Course());
        model.addAttribute("showNav", true);
        prepareMainModel(model, pageable);
        return "main";
    }

    @RequestMapping(value = "/attestation-organization", method = RequestMethod.GET)
    public String attestationOrganization(Model model) {
        model.addAttribute("showNav", true);
        return "attestation-organization";
    }

    @RequestMapping(value = "/state-tax", method = RequestMethod.GET)
    public String stateTax(Model model) {
        model.addAttribute("showNav", true);
        return "state-tax";
    }

    @RequestMapping(value = "/attestation-order", method = RequestMethod.GET)
    public String attestationOrder(Model model) {
        model.addAttribute("showNav", true);
        return "attestation-order";
    }

    @RequestMapping(value = "/commission-creation", method = RequestMethod.GET)
    public String commissionCreation(Model model) {
        model.addAttribute("showNav", true);
        return "commission-creation";
    }
    @RequestMapping(value = "/contacts", method = RequestMethod.GET)
    public String contacts(Model model) {
        model.addAttribute("showNav", true);
        return "contacts";
    }


    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public String saveNewCourse(@ModelAttribute("newCourse") @Valid Course newCourse, BindingResult result) {
        if (!result.hasErrors()) {
            courseService.save(newCourse);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editCourse(@PathVariable Long id, Model model, @PageableDefault(page = 0, size = 15, sort = {"order"}) Pageable pageable) {
        prepareMainModel(model, pageable);
        Course course = courseService.getById(id);
        model.addAttribute("editCourse", course);
        model.addAttribute("newCourse", new Course());
        return "main";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editCoursePost(@PathVariable Long id, Model model,
                                 @ModelAttribute("editCourse") @Valid Course course,
                                 BindingResult result,
                                 @PageableDefault(page = 0, size = 15, sort = {"order"}) Pageable pageable) {
        if (!result.hasErrors()) {
            courseService.save(course);
            return "redirect:/";
        }
        prepareMainModel(model, pageable);
        if (course == null)
            course = courseService.getById(id);
        model.addAttribute("editCourse", course);
        model.addAttribute("newCourse", new Course());
        return "main";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteCourse(@PathVariable Long id, Model model) {
        sectionService.deleteByCourseId(id);
        courseService.deleteCourse(id);
        return "redirect:/";
    }

    private void prepareMainModel(Model model, Pageable pageable) {
        model.addAttribute("courses", courseService.get(pageable).getContent());
        model.addAttribute("isMain", true);
        model.addAttribute("pageable", pageable);
    }

    @RequestMapping(value = "/splash", method = {RequestMethod.GET, RequestMethod.POST})
    public String splash(Model model, @RequestParam(value = "error", defaultValue = "false") Boolean error) {
        model.addAttribute("error", error);
        return "splash";
    }

    @RequestMapping(value = "/uikit", method = {RequestMethod.GET, RequestMethod.POST})
    public String uikit(Model model) {
        return "ui-kit";
    }
}
