package org.example;

import org.example.dbService.DBException;
import org.example.dbService.DBService;

import javax.servlet.http.Cookie;

public class UserRepository {
    public static final UserRepository USER_REPOSITORY = new UserRepository();
    DBService dbService = new DBService();

    public UserService getUserByCookies(Cookie[] cookies) throws DBException {
        String session = CookieUtil.getValue(cookies, "JSESSIONID");

        return session == null ? null : dbService.getUserBySession(session);
    }

    public void addUserBySession(String session, UserService user) throws DBException {
        dbService.addUserBySession(session, user);
    }

    public void removeUserBySession(String session) throws DBException {
        dbService.removeUserBySession(session);
    }

    public UserService getUserByLogin(String login) throws DBException {
        return dbService.getUser(login);
    }

    public void addUser(UserService user) throws DBException {
        dbService.addUser(user);
    }

    public boolean containsUserByLogin(String login) throws DBException {
        return dbService.containsUserByLogin(login);
    }
}
