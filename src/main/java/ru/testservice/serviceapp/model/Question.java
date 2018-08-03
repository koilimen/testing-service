package ru.testservice.serviceapp.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions", schema = "public")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String questionText;
    @Column
    private boolean multiAnswer;
    @OneToMany(mappedBy = "id")
    private List<Answer> answers;

    public Question() {
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
        return multiAnswer;
    }

    public void setMultiAnswer(boolean multiAnswer) {
        this.multiAnswer = multiAnswer;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
