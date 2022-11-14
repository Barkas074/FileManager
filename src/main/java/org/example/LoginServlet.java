package org.example;

import org.example.dbService.DBException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService user = null;
        try {
            user = UserRepository.USER_REPOSITORY.getUserByCookies(req.getCookies());
        } catch (DBException e) {
            e.printStackTrace();
        }
        if (user != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.setAttribute("registration", req.getContextPath() + "/registration");

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("login.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if (login == null || password == null) {
            return;
        }

        UserService user = null;
        try {
            user = UserRepository.USER_REPOSITORY.getUserByLogin(login);
        } catch (DBException e) {
            e.printStackTrace();
        }
        if (user == null || !user.getPassword().equals(password)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            UserRepository.USER_REPOSITORY.addUserBySession(CookieUtil.getValue(req.getCookies(), "JSESSIONID"), user);
        } catch (DBException e) {
            e.printStackTrace();
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
