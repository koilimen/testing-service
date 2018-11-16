package ru.testservice.serviceapp.model;

import javax.persistence.*;

@Entity
@Table(name = "test_literature", schema = "public")
public class TestLiterature extends AbstractEntity{
    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;
    @Column(name = "title")
    private String title;

    public TestLiterature() {
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
