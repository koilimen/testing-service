package ru.testservice.serviceapp.model;

import javax.persistence.*;

@MappedSuperclass
public abstract class AbstractOrderedEntity extends AbstractEntity {


    @Column(name="orderr", insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order;

    public AbstractOrderedEntity() {
    }



    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
