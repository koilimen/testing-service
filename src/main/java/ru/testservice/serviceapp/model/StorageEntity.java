package ru.testservice.serviceapp.model;

import javax.persistence.*;

@Entity
@Table(name = "storages", schema = "public")
public class StorageEntity extends AbstractEntity {
    @Column(name="file_name")
    private String name;
    @Column
    private String link;
    @Column
    private String contentType;
    @OneToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    public StorageEntity() {
    }

    public StorageEntity(String name, String link, String contentType) {
        this.name = name;
        this.link = link;
        this.contentType = contentType;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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
