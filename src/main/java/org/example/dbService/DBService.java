package org.example.dbService;

import org.example.UserService;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService {
    private final Connection connection;

    private enum Task {ADDUSER, ADDUSERBYSESSION, REMOVEUSERBYSESSION, CONTAINSUSERBYLOGIN}

    public DBService() {
        this.connection = getMysqlConnection();
    }

    public UserService getUser(String filter, String arg) throws DBException {
        try {
            UsersDAO dao = new UsersDAO(connection);
            dao.createTable();
            return (dao.getUser(filter, arg));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void addUser(String login, String email, String password) throws DBException {
        transaction(Task.ADDUSER, login, email, password, null, null);
    }

    public void addUserBySession(String session, UserService userService) throws DBException {
        transaction(Task.ADDUSERBYSESSION, null, null, null, session, userService);
    }

    public void removeUserBySession(String session) throws DBException {
        transaction(Task.REMOVEUSERBYSESSION, null, null, null, session, null);
    }

    public boolean containsUserByLogin(String login) throws DBException {
        return transaction(Task.CONTAINSUSERBYLOGIN, login, null, null, null, null);
    }

    public void cleanUp() throws DBException {
        UsersDAO dao = new UsersDAO(connection);
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private boolean transaction(Task task, String login, String email, String password, String session, UserService userService) throws DBException {
        try {
            connection.setAutoCommit(false);
            UsersDAO dao = new UsersDAO(connection);
            dao.createTable();
            if (task == Task.ADDUSER) {
                dao.addUser(login, email, password);
            } else if (task == Task.ADDUSERBYSESSION) {
                dao.addUserBySession(session, userService);
            } else if (task == Task.REMOVEUSERBYSESSION) {
                dao.removeUserBySession(session);
            }
            connection.commit();
            return dao.containsUserByLogin(login);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("javajdbc?").          //db name
                    append("user=root&").          //login
                    append("password=root");       //password

            System.out.println("URL: " + url + "\n");

            return DriverManager.getConnection(url.toString());
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
