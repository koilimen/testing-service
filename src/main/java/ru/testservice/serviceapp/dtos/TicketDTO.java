package ru.testservice.serviceapp.dtos;

import ru.testservice.serviceapp.model.Question;

import java.util.List;

public class TicketDTO {
    private List<Question> questionList;
    private Integer errorsCount;
    private String fio;
    private String organization;
    private String unit;
    private String position;
    private String subject;

    private Long testId;
    private boolean formProtocol;

    public TicketDTO() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isFormProtocol() {
        return formProtocol;
    }

    public void setFormProtocol(boolean formProtocol) {
        this.formProtocol = formProtocol;
    }

    public Integer getErrorsCount() {
        return errorsCount;
    }

    public void setErrorsCount(Integer errorsCount) {
        this.errorsCount = errorsCount;
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
