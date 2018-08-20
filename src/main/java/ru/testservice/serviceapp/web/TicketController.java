package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.testservice.serviceapp.dtos.TicketDTO;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.service.TestService;

import java.util.Collections;
import java.util.List;

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
        model.addAttribute("ticketDto", makeTestDto(test.getQuestionList().subList(0,5)));
        model.addAttribute("test", test);
        return "ticket";
    }

    private TicketDTO makeTestDto(List<Question> questions) {
        TicketDTO t = new TicketDTO();
        t.setQuestionList(questions);
        return t;
    }

    @RequestMapping(value = "/check/", method = RequestMethod.POST)
    public String checkTicket(@ModelAttribute TicketDTO ticket, Model model){

        return "ticket";
    }
}
