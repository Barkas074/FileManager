package org.example;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    public static final UserRepository USER_REPOSITORY = new UserRepository();
    private final Map<String, UserService> usersBySession = new HashMap<>();
    private final Map<String, UserService> usersByLogin = new HashMap<>();

    public UserService getUserByCookies(Cookie[] cookies) {
        String session = CookieUtil.getValue(cookies, "JSESSIONID");
        return session == null ? null : usersBySession.get(session);
    }

    public void addUserBySession(String session, UserService user) {
        usersBySession.put(session, user);
    }

    public void removeUserBySession(String session) {
        usersBySession.remove(session);
    }

    public UserService getUserByLogin(String login) {
        return usersByLogin.get(login);
    }

    public void addUser(UserService user) {
        usersByLogin.put(user.getLogin(), user);
    }
}
