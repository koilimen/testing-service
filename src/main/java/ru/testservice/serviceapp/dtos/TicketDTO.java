package ru.testservice.serviceapp.dtos;

import ru.testservice.serviceapp.model.Question;

import java.util.List;

public class TicketDTO {
    private List<Question> questionList;

    private Long testId;

    public TicketDTO() {
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
}
