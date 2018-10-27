package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.model.Section;
import ru.testservice.serviceapp.service.CourseService;
import ru.testservice.serviceapp.service.SectionService;

import javax.validation.Valid;

@Controller
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final SectionService sectionService;

    @Autowired
    public CourseController(CourseService courseService, SectionService sectionService) {
        this.courseService = courseService;
        this.sectionService = sectionService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String main(@PathVariable Long id, Model model, @PageableDefault(page = 0, size = 15, sort = {"id"}) Pageable pageable) {
        Course course = courseService.getById(id);
        prepareModel(course, model, pageable);
        model.addAttribute("newSection", new Section(course));
        return "course";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public @ResponseBody
    String edit(@RequestBody @Valid Course course,
                BindingResult result) {
        if (result.hasErrors()) {
            return "ERROR";
        }
        courseService.save(course);
        return "SUCCESS";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editForm(@PathVariable Long id, Model model) {
        Course course = courseService.getById(id);
        model.addAttribute("newCourse", course);
        return "blocks/modals::course-edit-form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String main(@PathVariable Long id, Model model,
                       @PageableDefault(page = 0, size = 15, sort = {"id"}) Pageable pageable,
                       @ModelAttribute("newSection") @Valid Section newSection, BindingResult result) {
        Course course = courseService.getById(id);
        if (!result.hasErrors()) {
            sectionService.save(newSection);
            model.addAttribute("newSection", new Section(course));
        }
        prepareModel(course, model, pageable);
        return "course";
    }

    @RequestMapping(value = "/{cid}/deletesecton/{sid}", method = RequestMethod.GET)
    public String deleteSection(@PathVariable Long cid, @PathVariable Long sid) {
        sectionService.deleteById(sid);
        return "redirect:/course/" + cid;
    }

    @RequestMapping(value = "/{cid}/edit/{sid}", method = RequestMethod.GET)
    public String editSection(@PathVariable Long cid, @PathVariable Long sid, Model model,
                              @PageableDefault(page = 0, size = 15, sort = {"id"}) Pageable pageable) {
        Course course = courseService.getById(cid);
        prepareModel(course, model, pageable);
        model.addAttribute("newSection", new Section(course));
        model.addAttribute("editSection", sectionService.getById(sid));
        return "course";

    }

    @RequestMapping(value = "/{cid}/edit/{sid}", method = RequestMethod.POST)
    public String editSectionPost(@PathVariable Long cid, @PathVariable Long sid, Model model,
                                  @PageableDefault(page = 0, size = 15, sort = {"id"}) Pageable pageable,
                                  @ModelAttribute("editSection") @Valid Section editSection, BindingResult result) {
        if (!result.hasErrors()) {
            sectionService.save(editSection);
            return "redirect:/course/" + cid;
        }
        Course course = courseService.getById(cid);
        prepareModel(course, model, pageable);
        model.addAttribute("newSection", new Section(course));
        return "course";

    }

    private void prepareModel(Course course, Model model, @PageableDefault(page = 0, size = 15, sort = {"id"}) Pageable pageable) {
        model.addAttribute("course", course);
        model.addAttribute("sections", sectionService.getByCourseId(course.getId(), pageable));
    }

}
