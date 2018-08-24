package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Collections.shuffle(test.getQuestionList());
        model.addAttribute("ticketDto", makeTicketDto(test.getId(), test.getQuestionList().subList(0, 5)));
        model.addAttribute("test", test);
        model.addAttribute("checked", false);
        return "ticket";
    }

    private TicketDTO makeTicketDto(Long testId, List<Question> questions) {
        TicketDTO t = new TicketDTO();
        t.setQuestionList(questions);
        t.setTestId(testId);
        return t;
    }

    @RequestMapping(value = "/check/", method = RequestMethod.POST)
    public String checkTicket(@ModelAttribute TicketDTO ticketDto, Model model) {
        List<Long> questionIds = ticketDto.getQuestionList().stream().map(Question::getId).collect(Collectors.toList());
        List<Question> questions = qs.getQuestions(questionIds);
        Map<Long, Answer> ticketAnswers = getAnswers(ticketDto.getQuestionList());
        Map<Long, Answer> correctAnswers = getAnswers(questions);
        ticketAnswers.forEach((id, ans) -> {
            Answer correctAnswer = correctAnswers.get(id);
            ans.setCorrect(correctAnswer.isCorrect());
        });
        model.addAttribute("ticketDto", ticketDto);
        model.addAttribute("test", ts.getTest(ticketDto.getTestId()));
        model.addAttribute("checked", true);
        return "ticket";
    }

    private Map<Long, Answer> getAnswers(List<Question> questions) {
        Map<Long, Answer> answers = new HashMap<>();
        questions.forEach(q -> {
            q.getAnswers().forEach(a -> {
                answers.put(a.getId(), a);
            });
        });
        return answers;
    }
}
