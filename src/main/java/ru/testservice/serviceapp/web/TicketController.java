package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.testservice.serviceapp.dtos.TicketDTO;
import ru.testservice.serviceapp.model.Answer;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.service.QuestionService;
import ru.testservice.serviceapp.service.TestService;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ticket")
public class TicketController {
    private final TestService ts;
    private final QuestionService qs;

    @Autowired
    public TicketController(TestService ts, QuestionService qs) {
        this.ts = ts;
        this.qs = qs;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String ticket(@RequestParam Long testId, @RequestParam Integer ticketNum, Model model) {
        Test test = ts.getTest(testId);
        Pageable pg = PageRequest.of(ticketNum, test.getSection().getQuestionsCount(), Sort.Direction.ASC, "id");
        model.addAttribute("ticketDto", makeTicketDto(test.getId(), test.getSection().getErrorsCount(), qs.getQuestions(testId, pg).getContent()));
        model.addAttribute("test", test);
        model.addAttribute("ticketChecked", false);
        return "ticket";
    }

    private TicketDTO makeTicketDto(Long testId, Integer errorsCount, List<Question> questions) {
        TicketDTO t = new TicketDTO();
        t.setQuestionList(questions);
        t.setTestId(testId);
        t.setErrorsCount(errorsCount);
        return t;
    }

    @RequestMapping(value = "/check/", method = RequestMethod.POST)
    public String checkTicket(@ModelAttribute TicketDTO ticketDto, Model model) {
        Collection<Answer> ticketAnswers = getAnswersList(ticketDto.getQuestionList());
        long incorrectCount = ticketAnswers.stream().filter(a -> a.isChecked() && a.isCorrect() != a.isChecked()).count();
        model.addAttribute("incorrectCount", incorrectCount );
        model.addAttribute("ticketDto", ticketDto);
        model.addAttribute("test", ts.getTest(ticketDto.getTestId()));
        model.addAttribute("ticketChecked", true);
        return "ticket";
    }



    private Collection<Answer> getAnswersList(List<Question> questions) {
        List<Answer> answers = new ArrayList<>();
        questions.forEach(q -> {
            answers.addAll(q.getAnswers());
        });
        return answers;
    }

}
