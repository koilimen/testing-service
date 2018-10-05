package ru.testservice.serviceapp.model;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(schema = "public", name = "sections")
public class Section extends AbstractEntity {
    @Column(name = "section_name")
    @NotNull(message = "Название раздела не может быть пустым")
    @NotEmpty(message = "Название раздела не может быть пустым")
    private String name;
    @Column(name = "section_code")
    @NotNull(message = "Область аттестации не может быть пустой")
    @NotEmpty(message = "Область аттестации не может быть пустой")
    private String code;
    @OneToMany(mappedBy = "section", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Test> tests;
    @ManyToOne
    @JoinColumn(name ="course_id")
    private Course course;
    @Column(name = "questions_count")
    @ColumnDefault(value = "5")
    @NotNull(message = "Укажите кол-во вопросов в билете")
    @Min(value = 1, message = "Кол-во вопросов в билете не может быть меньше 1")
    private Integer questionsCount;
    @Column(name = "errors_count")
    @ColumnDefault(value = "1")
    @NotNull(message = "Укажите допустимое кол-во ошибок в билете")
    private Integer errorsCount;


    public Section() {
    }

    public Section(Course course) {
        this.course = course;
    }

    public Integer getErrorsCount() {
        return errorsCount;
    }

    public void setErrorsCount(Integer errorsCount) {
        this.errorsCount = errorsCount;
    }

    public Integer getQuestionsCount() {
        return questionsCount;
    }

    public void setQuestionsCount(Integer questionsCount) {
        this.questionsCount = questionsCount;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
