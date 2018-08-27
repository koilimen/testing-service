package ru.testservice.serviceapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RootController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(Model model){
        model.addAttribute("isMain", true);
        return "main";
    }

    @RequestMapping(value = "/splash", method = {RequestMethod.GET, RequestMethod.POST})
    public String splash(Model model){
            return "splash";
    }
}
