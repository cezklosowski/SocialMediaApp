package org.example;

import javax.persistence.*;
import java.util.List;

@Entity
public class Post extends IDProvider{

    @Column
    private String title;

    @Column
    private String text;

    @Column
    private int userID;

    @OneToMany
    @JoinColumn
    private List<Comment> comments;

    public Post() {
    }

    public Post(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Post: " + getId() +
                " Title: " + title;
    }
}
