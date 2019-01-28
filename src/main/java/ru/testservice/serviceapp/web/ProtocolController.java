package ru.testservice.serviceapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.testservice.serviceapp.dtos.TicketDTO;
import ru.testservice.serviceapp.model.Answer;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProtocolController {
    @RequestMapping(value = "/protocol", method = RequestMethod.POST)
    public String protocol(@ModelAttribute TicketDTO ticketDTO, Model model) {
        ticketDTO.getQuestionList().forEach(q -> {
            List<Answer> collect = q.getAnswers().stream().filter(Answer::isChecked).collect(Collectors.toList());
            if (!collect.isEmpty())
                q.setCorrectForProtocol(true);
            for (Answer answer : collect) {
                if (answer.isChecked() && !answer.isCorrect()) {
                    q.setCorrectForProtocol(false);
                    break;
                }
            }
            q.setAnswers(collect);
        });
        return "protocol-print";
    }

}
