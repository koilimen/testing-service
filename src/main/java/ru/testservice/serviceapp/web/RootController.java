package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.service.CourseService;
import ru.testservice.serviceapp.service.StorageService;

import javax.validation.Valid;

@Controller
public class RootController {
    private final CourseService courseService;
    private final StorageService storageService;

    @Autowired
    public RootController(CourseService courseService, StorageService storageService) {
        this.courseService = courseService;
        this.storageService = storageService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(Model model, @PageableDefault(page = 0, size = 15, sort = {"id"}) Pageable pageable) {
        model.addAttribute("newCourse", new Course());
        model.addAttribute("files", storageService.getAll());
        prepareMainModel(model, pageable);
        return "main";
    }


    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public String saveNewCourse(@ModelAttribute("newCourse") @Valid Course newCourse, BindingResult result, Model model,
                                @PageableDefault(page = 0, size = 15, sort = {"id"}) Pageable pageable) {
        if (!result.hasErrors()) {
            courseService.save(newCourse);
            model.addAttribute("newCourse", new Course());
        }
        prepareMainModel(model, pageable);
        return "main";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editCourse(@PathVariable Long id, Model model, @PageableDefault(page = 0, size = 15, sort = {"id"}) Pageable pageable) {
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
                                 @PageableDefault(page = 0, size = 15, sort = {"id"}) Pageable pageable) {
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
        courseService.deleteCourse(id);
        return "redirect:/";
    }

    private void prepareMainModel(Model model, Pageable pageable) {
        model.addAttribute("courses", courseService.get(pageable).getContent());
        model.addAttribute("isMain", true);
        model.addAttribute("pageable", pageable);
    }

    @RequestMapping(value = "/splash", method = {RequestMethod.GET, RequestMethod.POST})
    public String splash(Model model) {
        return "splash";
    }
}
