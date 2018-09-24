package ru.testservice.serviceapp.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "questions", schema = "public")
public class Question {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String questionText;
    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Answer> answers;
    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;


    public Question() {
    }

    public Question(Test test) {
        this.test = test;
    }

    public Question(String questionText, List<Answer> answers, Test test) {
        this.questionText = questionText;
        this.answers = answers;
        this.test = test;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public boolean isMultiAnswer() {
        return answers.stream().filter(Answer::isCorrect).count() > 1;
    }


    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Question clone() {
        return new Question(this.getQuestionText(), this.getAnswers(), this.getTest());
    }
}
