package ru.testservice.serviceapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(schema = "public", name = "courses")
public class Course extends AbstractOrderedEntity {
    @Column(name = "course_name")
    @NotNull(message = "Название курса не может быть пустым")
    @NotEmpty(message = "Название курса не может быть пустым")
    private String name;

    @Column
    private String text1;

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
