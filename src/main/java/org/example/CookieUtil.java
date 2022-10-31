package org.example;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void addCookie(HttpServletResponse resp, String key, String value) {
        resp.setContentType("text/html");
        Cookie cookie = new Cookie(key, value);
        if (value == null) {
            cookie.setMaxAge(0);
        }
        else {
            cookie.setMaxAge(60 * 60 * 24);
        }
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    public static String getValue(Cookie[] cookies, String key) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
