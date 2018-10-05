package ru.testservice.serviceapp.model;

import javax.persistence.*;

@Entity
@Table(name = "folder", schema = "public")
public class Folder extends AbstractEntity {
    @Column
    private String title;
    @OneToOne()
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }
}
