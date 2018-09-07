package ru.testservice.serviceapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(schema = "public", name = "courses")
public class Course extends AbstractEntity {
    @Column(name = "course_name")
    @NotNull(message = "Название курса не может быть пустым")
    @NotEmpty(message = "Название курса не может быть пустым")
    private String name;
    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Section> sectionList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
