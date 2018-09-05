package ru.testservice.serviceapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(schema = "public", name = "sections")
public class Section extends AbstractEntitry {
    @Column(name = "section_name")
    @NotNull
    @NotEmpty
    private String name;
    @Column(name = "section_code")
    private String code;
    @ManyToOne
    @JoinColumn(name ="course_id")
    private Course course;

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

    public void setCode(String code) {
        this.code = code;
    }
}
