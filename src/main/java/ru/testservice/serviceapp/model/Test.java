package ru.testservice.serviceapp.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Table(name = "tests", schema = "public")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotNull
    @Pattern(regexp = "[а-яА-Я0-9\\s-_\\.]+", message = "Для ввода допустимы символы А-Я, цифры, пробел, точка, _ -")
    @NotEmpty(message = "Название не может быть пустым.")
    @Length(min=3, max=50, message = "Длина названия должна быть от 3 до 50 символов.")
    private String title;
    @Column
    @NotNull
    @Pattern(regexp = "[а-яА-Я0-9\\s-_\\.]+", message = "Для ввода допустимы символы А-Я, цифры, пробел, точка, _ -")
    @NotEmpty(message = "Описание не может быть пустым.")
    @Length(min=0, max=255, message = "Длина описнаие должна быть не более 255 символов.")
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
