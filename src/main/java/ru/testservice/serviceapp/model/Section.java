package ru.testservice.serviceapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(schema = "public", name = "sections")
public class Section extends AbstractOrderedEntity {
    @Column(name = "section_name")
    @NotNull(message = "Название раздела не может быть пустым")
    @NotEmpty(message = "Название раздела не может быть пустым")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column
    private String text1;


    public Section() {
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public Section(Course course) {
        this.course = course;
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

}
