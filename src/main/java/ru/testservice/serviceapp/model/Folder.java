package ru.testservice.serviceapp.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "folder", schema = "public")
public class Folder extends AbstractEntity {
    @Column
    private String title;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_folder_id")
    private List<Folder> childFolders;
    private transient String flatTitle;

    public Folder() {
    }

    public Folder(Long folderId) {
        this.setId(folderId);
    }

    public String getFlatTitle() {
        return flatTitle;
    }

    public void setFlatTitle(String flatTitle) {
        this.flatTitle = flatTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Folder> getChildFolders() {
        return childFolders;
    }

    public void setChildFolders(List<Folder> childFolders) {
        this.childFolders = childFolders;
    }


}
