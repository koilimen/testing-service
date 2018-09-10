package ru.testservice.serviceapp.model;

import javax.persistence.*;

@Entity
@Table(name = "storages", schema = "public")
public class StorageEntity extends AbstractEntity {
    @Column
    private String name;
    @Column
    private String link;
    @Column
    private String contentType;
    @Lob
    private byte[] file;

    public StorageEntity() {
    }

    public StorageEntity(String name, String link, String contentType, byte[] file) {
        this.name = name;
        this.link = link;
        this.contentType = contentType;
        this.file = file;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
