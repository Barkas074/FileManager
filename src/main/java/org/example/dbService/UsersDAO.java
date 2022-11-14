package org.example.dbService;

import org.example.UserService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {
    private final Executor executor;

    public UsersDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public UserService getUser(String filter, String arg) throws SQLException {
        return executor.execQuery("SELECT email, login, password  FROM users WHERE " + filter + " = '" + arg + "'", result -> {
            if (result.next()) {
                return new UserService(
                        result.getString("email"),
                        result.getString("login"),
                        result.getString("password")
                );
            } else {
                return null;
            }
        });
    }

    public boolean containsUserByLogin(String login) throws SQLException {
        return executor.execQuery("SELECT id FROM users WHERE login = '" + login + "'", ResultSet::next);
    }

    public void addUser(String login, String email, String password) throws SQLException {
        executor.execUpdate("INSERT INTO users (login, email, password) values ('" + login + "', '" + email + "', '" + password + "')");
    }

    public void addUserBySession(String session, UserService userService) throws SQLException {
        executor.execUpdate("UPDATE users SET session = '" + session + "' WHERE login = '" + userService.getLogin() + "'");
    }

    public void removeUserBySession(String session) throws SQLException {
        executor.execUpdate("UPDATE users SET session = " + null + " WHERE session = '" + session + "'");
    }

    public void createTable() throws SQLException {
        executor.execUpdate("CREATE TABLE IF NOT EXISTS users (id bigint auto_increment, login varchar(256), email varchar(256), password varchar(256), session varchar(256), PRIMARY KEY (id))");
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("DROP TABLE users");
    }
}
