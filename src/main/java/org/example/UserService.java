package org.example;


import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class UserService implements Serializable {
    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email")
    private String email;

    @NaturalId
    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "session")
    private String session;

    @SuppressWarnings("UnusedDeclaration")
    public UserService() {

    }

    public UserService(String email, String login, String password, String session) {
        this.setId(-1);
        this.setEmail(email);
        this.setLogin(login);
        this.setPassword(password);
        this.setSession(session);
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
