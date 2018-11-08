package ru.testservice.serviceapp.model;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
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
    @NotEmpty(message = "Название не может быть пустым.")
    @Length(min = 3, max = 1024 , message = "Длина названия должна быть от 3 до 512 символов.")
    private String title;
    @Column
    @NotNull
    @NotEmpty(message = "Описание не может быть пустым.")
    @Length( max = 1024, message = "Длина описнаие должна быть не более 512 символов.")
    private String description;
    @Column
    private Long questionsNumber;
    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;
    @Column(name="test_order", insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order;
    @Column(name = "questions_count")
    @ColumnDefault(value = "5")
    @NotNull(message = "Укажите кол-во вопросов в билете")
    @Min(value = 1, message = "Кол-во вопросов в билете не может быть меньше 1")
    private Integer questionsCount;
    @Column(name = "errors_count")
    @ColumnDefault(value = "1")
    @NotNull(message = "Укажите допустимое кол-во ошибок в билете")
    private Integer errorsCount;
    @Column(name = "tickets_count")
    @NotNull(message = "Укажите кол-во билетов в тесте")
    private Integer ticketsCount;

    public Test(String title, String description, Long questionsNumber) {
        this.title = title;
        this.description = description;
        this.questionsNumber = questionsNumber;
    }

    public Test(Long id, Integer order) {
        this.id = id;
        this.order = order;
    }

    public static Test of(Long id) {
        Test t = new Test();
        t.setId(id);
        return t;
    }

    public Integer getTicketsCount() {
        return ticketsCount;
    }

    public void setTicketsCount(Integer ticketsCount) {
        this.ticketsCount = ticketsCount;
    }

    public Integer getQuestionsCount() {
        return questionsCount;
    }

    public void setQuestionsCount(Integer questionsCount) {
        this.questionsCount = questionsCount;
    }

    public Integer getErrorsCount() {
        return errorsCount;
    }

    public void setErrorsCount(Integer errorsCount) {
        this.errorsCount = errorsCount;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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
        if(questionsNumber == null){
            questionsNumber = 0L;
        }
        return questionsNumber;
    }

    public void setQuestionsNumber(Long questionsNumber) {
        this.questionsNumber = questionsNumber;
    }

    public void increaseQN() {
        if(this.questionsNumber == null){
            this.questionsNumber = 0L;
        }
        this.questionsNumber++;
    }
}
