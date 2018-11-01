package ru.testservice.serviceapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import ru.testservice.serviceapp.dtos.TicketDTO;
import ru.testservice.serviceapp.model.Answer;
import ru.testservice.serviceapp.model.Question;
import ru.testservice.serviceapp.model.Test;
import ru.testservice.serviceapp.service.QuestionService;
import ru.testservice.serviceapp.service.TestService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public String ticket(@RequestParam Long testId, @RequestParam Integer ticketNum,
                         @CookieValue(value = "questions", required = false) String viewedQuestions,
                         @CookieValue(value = "testId", required = false) String testIdSaved,
                         HttpServletResponse response,
                         Model model) {
        Test test = ts.getTest(testId);
        Pageable pg = PageRequest.of(0, test.getSection().getQuestionsCount(), Sort.Direction.ASC, "id");

        List<Long> vqIds = viewedQuestions != null ? Arrays.stream(viewedQuestions.split("\\|")).map(Long::parseLong).collect(Collectors.toList()) : Collections.emptyList();
        Long count = qs.countTestQuestions(test.getId());
        if (!testId.toString().equals(testIdSaved)) {
            Cookie testIdCookie = new Cookie("testId", testId.toString());
            testIdCookie.setMaxAge(3600);
            testIdCookie.setPath("/ticket");
            testIdCookie.setHttpOnly(true);
            response.addCookie(testIdCookie);
            vqIds = Collections.emptyList();
            viewedQuestions = "";
        }
        if (count <= vqIds.size()) {
            viewedQuestions = "";
            vqIds = Collections.emptyList();
        }

        TicketDTO ticketDTO = makeTicketDto(test.getId(), test.getSection().getErrorsCount(), qs.getQuestions(test, vqIds, pg).getContent());
        StringBuilder sb = new StringBuilder();
        ticketDTO.getQuestionList().forEach(q -> {
            sb.append(q.getId()).append("|");
        });
        if (viewedQuestions != null) {
            sb.append(viewedQuestions);
        } else {
            if (sb.length() > 1)
                sb.deleteCharAt(sb.length() - 1);
        }

        Cookie cookie = new Cookie("questions", sb.toString());
        cookie.setMaxAge(3600);
        cookie.setPath("/ticket");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        model.addAttribute("ticketNum", ticketNum);
        model.addAttribute("allCourses", Collections.singleton(test.getSection().getCourse()));
        model.addAttribute("ticketDto", ticketDTO);
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
    public String checkTicket(@ModelAttribute TicketDTO ticketDto, @RequestParam("ticketNum") Integer ticketNum, Model model) {
        Collection<Answer> ticketAnswers = getAnswersList(ticketDto.getQuestionList());
        long incorrectCount = ticketAnswers.stream().filter(a -> a.isCorrect() != a.isChecked()).count();
        long correctCount = ticketAnswers.stream().filter(Answer::isCorrect).count();
        long correctCheckedCount = ticketAnswers.stream().filter(a -> a.isCorrect() && a.isChecked()).count();
        model.addAttribute("checkedCorrect", correctCheckedCount);
        model.addAttribute("corrects", correctCount);
        model.addAttribute("ticketNum", ticketNum);
        model.addAttribute("incorrectCount", incorrectCount);
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
