package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.model.TicketDto;
import ru.testservice.serviceapp.service.TestService;

import java.util.Collections;

@Controller
@RequestMapping("/ticket")
public class TicketController {
    private final TestService ts;

    @Autowired
    public TicketController(TestService ts) {
        this.ts = ts;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String ticket(@RequestParam Long testId, @RequestParam Integer ticketNum,  Model model){
        Test test = ts.getTest(testId);
        Collections.shuffle(test.getQuestionList());
        model.addAttribute("questions", test.getQuestionList().subList(0,5));
        model.addAttribute("test", test);
        return "ticket";
    }
    @RequestMapping(value = "/check/", method = RequestMethod.POST)
    public String checkTicket(TicketDto ticket, Model model){
        return "ticket";
    }
}
