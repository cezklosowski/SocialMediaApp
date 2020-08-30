package org.example;

import javax.persistence.*;
import java.util.List;

@Entity
public class User extends IDProvider {


    @Column
    private String login;

    @Column
    private String password;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Post> posts;


    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
