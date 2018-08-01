package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.testservice.serviceapp.service.TestService;

@Controller
@RequestMapping("/tests")
public class TestsController {
    private final TestService ts;

    @Autowired
    public TestsController(TestService ts) {
        this.ts = ts;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getPage(Model model) {
        model.addAttribute("tests", ts.getTests());
        return "tests";
    }
}
