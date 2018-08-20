package ru.testservice.serviceapp.model;

import javax.persistence.*;

@Entity
@Table(name = "answers", schema = "public")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String answerText;
    @Column
    private boolean correct;
    @Transient
    private boolean checked;
    @ManyToOne
    @JoinColumn(name ="question_id")
    private Question question;


    public Answer() {
    }

    public Answer(String answerText, boolean correct, Question question) {
        this.answerText = answerText;
        this.correct = correct;
        this.question = question;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
