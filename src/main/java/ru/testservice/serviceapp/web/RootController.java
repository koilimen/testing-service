package ru.testservice.serviceapp.web;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.testservice.serviceapp.model.Course;
import ru.testservice.serviceapp.model.Section;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.service.CourseService;
import ru.testservice.serviceapp.service.IStorageService;
import ru.testservice.serviceapp.service.SectionService;
import ru.testservice.serviceapp.service.TestService;

import javax.validation.Valid;
import java.io.File;
import java.net.MalformedURLException;

@Controller
public class RootController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CourseService courseService;
    private final IStorageService IStorageService;
    private final SectionService sectionService;
    private final TestService testService;

    @Autowired
    public RootController(CourseService courseService, IStorageService IStorageService, SectionService sectionService, TestService testService) {
        this.courseService = courseService;
        this.IStorageService = IStorageService;
        this.sectionService = sectionService;
        this.testService = testService;
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
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


    @RequestMapping(value = {"/", ""}, method = RequestMethod.PUT)
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

    //    @RequestMapping(value = "/uikit", method = {RequestMethod.GET, RequestMethod.POST})
    public String uikit(Model model) {
        return "ui-kit";
    }
    @RequestMapping(value = "/confidentials", method = RequestMethod.GET)
    public String confidentials(Model model){
        model.addAttribute("showNav", true);

        return "confid-policy";
    }


    @Scheduled(cron = "0 0 0 * * *")
    @RequestMapping(value = "/gen-sitemap", method = RequestMethod.GET)
    public void generateSitemap() {
        try {
            WebSitemapGenerator wsg = new WebSitemapGenerator("https://prombez24.ru", new File("/opt/www/tomcat"));
            wsg.addUrl("https://prombez24.ru/attestation-organization"); // repeat multiple times
            wsg.addUrl("https://prombez24.ru/splash"); // repeat multiple times
            wsg.addUrl("https://prombez24.ru/commission-creation"); // repeat multiple times
            wsg.addUrl("https://prombez24.ru/contacts"); // repeat multiple times
            wsg.addUrl("https://prombez24.ru/docs"); // repeat multiple times
            for (Course course : courseService.getAll()) {
                wsg.addUrl("https://prombez24.ru/course/" + course.getId()); // repeat multiple times
            }
            for (Section section : sectionService.getAll()) {
                wsg.addUrl("https://prombez24.ru/section/" + section.getId()); // repeat multiple times
            }
            for (Test test : testService.getTests()) {
                wsg.addUrl("https://prombez24.ru/tests/" + test.getId()); // repeat multiple times
                wsg.addUrl("https://prombez24.ru/ticket/?testId=" + test.getId()); // repeat multiple times
            }

            wsg.write();
            logger.info("sitemap generated");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


}
