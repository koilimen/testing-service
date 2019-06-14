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
    @Length(min = 3, max = 1024, message = "Длина названия должна быть от 3 до 1024 символов.")
    private String title;
    @Column(name = "code")
    private String code;
    @Column(name = "cipher")
    @NotNull
    @NotEmpty(message = "Шифр не может быть пустым.")
    @Length(max = 255, message = "Длина шифра должна быть не более 255 символов.")
    private String cipher;
    @Column
    private Long questionsNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;
    @Column(name = "test_order", insertable = false)
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
    @Column(name="descr_title")
    @Length(max=255, message = "Слишком длинный заголовок. Максимальная длина - 255 символов.")
    public String descriptionTitle;
    @Column(name="descr_content")
    public String descriptionContent;
    @Column(name="random_text", columnDefinition = "text")
    public String randomText;

    public Test(String title, String cipher, Long questionsNumber) {
        this.title = title;
        this.cipher = cipher;
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

    public String getRandomText() {
        return randomText;
    }

    public void setRandomText(String randomText) {
        this.randomText = randomText;
    }

    public String getDescriptionTitle() {
        return descriptionTitle;
    }

    public void setDescriptionTitle(String descriptionTitle) {
        this.descriptionTitle = descriptionTitle;
    }

    public String getDescriptionContent() {
        return descriptionContent;
    }

    public void setDescriptionContent(String descriptionContent) {
        this.descriptionContent = descriptionContent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getTicketsCount() {
        if (ticketsCount == null) return 0;
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


    public Test(Long id) {
        this.id = id;
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

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public Long getQuestionsNumber() {
        if (questionsNumber == null) {
            questionsNumber = 0L;
        }
        return questionsNumber;
    }

    public void setQuestionsNumber(Long questionsNumber) {
        this.questionsNumber = questionsNumber;
    }

    public void increaseQN() {
        if (this.questionsNumber == null) {
            this.questionsNumber = 0L;
        }
        this.questionsNumber++;
    }
}
