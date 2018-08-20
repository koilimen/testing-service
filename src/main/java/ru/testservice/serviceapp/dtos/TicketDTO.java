package ru.testservice.serviceapp.dtos;

import ru.testservice.serviceapp.model.Question;

import java.util.List;

public class TicketDTO {
    private List<Question> questionList;

    public TicketDTO() {
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
}
