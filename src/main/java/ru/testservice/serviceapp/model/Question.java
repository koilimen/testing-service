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
    @Column(columnDefinition = "text")
    private String questionText;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
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
        this.answers.forEach( a -> {
            a.setQuestion(this);
        });
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

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        this.answers.forEach( a -> {
            b.append('\t').append(a.getAnswerText()).append('\n');
        });
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", test=" + test +
                "}:\n"+b.toString();
    }
}
