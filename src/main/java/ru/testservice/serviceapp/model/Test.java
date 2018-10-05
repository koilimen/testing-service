package ru.testservice.serviceapp.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "tests", schema = "public")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotNull
    @Pattern(regexp = "[а-яА-Я0-9\\s-_.]+", message = "Для ввода допустимы символы А-Я, цифры, пробел, точка, _ -")
    @NotEmpty(message = "Название не может быть пустым.")
    @Length(min = 3, max = 512 , message = "Длина названия должна быть от 3 до 255 символов.")
    private String title;
    @Column
    @NotNull
    @Pattern(regexp = "[а-яА-Я0-9\\s-_.,]+", message = "Для ввода допустимы символы А-Я, цифры, пробел, точка, запятая, _ -")
    @NotEmpty(message = "Описание не может быть пустым.")
    @Length( max = 512, message = "Длина описнаие должна быть не более 512 символов.")
    private String description;
    @Column
    private Long questionsNumber;
    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;


    public Test(String title, String description, Long questionsNumber) {
        this.title = title;
        this.description = description;
        this.questionsNumber = questionsNumber;
    }



    public Test(Section section) {
        this.section = section;
    }



    public Test() {
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
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

    public Long getQuestionsNumber() {
        return questionsNumber;
    }

    public void setQuestionsNumber(Long questionsNumber) {
        this.questionsNumber = questionsNumber;
    }

    public void increaseQN() {
        this.questionsNumber++;
    }
}
