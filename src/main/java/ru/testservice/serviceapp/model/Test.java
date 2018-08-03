package ru.testservice.serviceapp.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tests", schema = "public")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private short questionsNumber;
    @OneToMany(mappedBy = "id")
    private List<Question> questionList;

    public Test(String title, String description, short questionsNumber) {
        this.title = title;
        this.description = description;
        this.questionsNumber = questionsNumber;
    }

    public Test() {
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public short getQuestionsNumber() {
        return questionsNumber;
    }

    public void setQuestionsNumber(short questionsNumber) {
        this.questionsNumber = questionsNumber;
    }
}
