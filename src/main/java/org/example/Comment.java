package org.example;

import javax.persistence.*;

@Entity
public class Comment extends IDProvider {

    @Column
    private String text;

    @Column
    private int userID;

    @Column
    private int postID;

    public Comment() {
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

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    @Override
    public String toString() {
        return "Comment: " + getId() +
                " Text: " + text;
    }
}
