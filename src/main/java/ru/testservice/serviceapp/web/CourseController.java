package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.spring5.view.ThymeleafView;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.model.Section;
import ru.testservice.serviceapp.service.CourseService;
import ru.testservice.serviceapp.service.SectionService;
import ru.testservice.serviceapp.service.TestService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final SectionService sectionService;
    private final TestService testService;

    @Autowired
    public CourseController(CourseService courseService, SectionService sectionService, TestService testService) {
        this.courseService = courseService;
        this.sectionService = sectionService;
        this.testService = testService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView main(@PathVariable Long id, Model model, @PageableDefault(size = 15, sort = {"order"}) Pageable pageable,
                     HttpServletRequest request) {
        Course course = courseService.getById(id);

        List<Section> sections = prepareModel(course, model, pageable);
        if (request.isUserInRole("ROLE_ADMIN")) {
            model.addAttribute("newSection", new Section(course));
        }
        if(sections.size() == 1 && !request.isUserInRole("ROLE_ADMIN")){
            RedirectView redirectView = new RedirectView("/section/" + sections.get(0).getId());
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);

        }
        model.addAttribute("htmlTitle", course.getName());

        return new ModelAndView("course", model.asMap());
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
                       @PageableDefault(page = 0, size = 15, sort = {"order"}) Pageable pageable,
                       @ModelAttribute("newSection") @Valid Section newSection, BindingResult result) {
        Course course = courseService.getById(id);
        if (!result.hasErrors()) {
            sectionService.save(newSection);
            return "redirect:/course/"+id;
        }
        prepareModel(course, model, pageable);
        return "course";
    }

    @RequestMapping(value = "/{cid}/deletesecton/{sid}", method = RequestMethod.GET)
    public String deleteSection(@PathVariable Long cid, @PathVariable Long sid) {
        testService.removeBySectionId(sid);
        sectionService.deleteById(sid);
        return "redirect:/course/" + cid;
    }

    @RequestMapping(value = "/{cid}/edit/{sid}", method = RequestMethod.GET)
    public String editSection(@PathVariable Long cid, @PathVariable Long sid, Model model,
                              @PageableDefault(page = 0, size = 15, sort = {"order"}) Pageable pageable) {
        Course course = courseService.getById(cid);
        prepareModel(course, model, pageable);
        model.addAttribute("newSection", new Section(course));
        model.addAttribute("editSection", sectionService.getById(sid));
        return "course";

    }

    @RequestMapping(value = "/{cid}/edit/{sid}", method = RequestMethod.POST)
    public String editSectionPost(@PathVariable Long cid, @PathVariable Long sid, Model model,
                                  @PageableDefault(page = 0, size = 15, sort = {"order"}) Pageable pageable,
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
    @RequestMapping(value = "/update-orders", method = RequestMethod.POST)
    public @ResponseBody
    String updateOrder(@RequestParam("ids[]") List<Long> ids, @RequestParam("orders[]") List<Integer> orders) {
        return courseService.uppdateOrders(ids, orders);
    }
    private List<Section> prepareModel(Course course, Model model, @PageableDefault(page = 0, size = 15, sort = {"id"}) Pageable pageable) {
        model.addAttribute("course", course);
        List<Section> sections = sectionService.getByCourseId(course.getId(), pageable);
        model.addAttribute("sections", sections);
        return sections;
    }

}
