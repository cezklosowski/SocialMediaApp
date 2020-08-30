package org.example;

import javax.persistence.*;

@Entity
public class Post extends IDProvider{


    private String text;

    private int userID;

    public Post() {
    }

    public Post(String text) {
        this.text = text;
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
}
