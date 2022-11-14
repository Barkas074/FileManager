package org.example.dbService;

import org.example.UserService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "update";
    private final SessionFactory sessionFactory;

    private enum Task {ADDUSER, ADDUSERBYSESSION, REMOVEUSERBYSESSION, CONTAINSUSERBYLOGIN}

    public DBService() {
        Configuration configuration = getMySqlConfiguration();
        sessionFactory = createSessionFactory(configuration);
    }

    public UserService getUser(String login) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UsersDAO dao = new UsersDAO(session);
            UserService user = dao.getUser(login);
            session.close();
            return user;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public UserService getUserBySession(String sessionString) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UsersDAO dao = new UsersDAO(session);
            UserService user = dao.getUserBySession(sessionString);
            session.close();
            return user;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void addUser(UserService user) throws DBException {
        transaction(Task.ADDUSER, user.getLogin(), user.getEmail(), user.getPassword(), user.getSession(), null);
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

    private boolean transaction(Task task, String login, String email, String password, String sessionString, UserService userService) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UsersDAO dao = new UsersDAO(session);
            boolean check = false;
            if (task == Task.ADDUSER) {
                dao.addUser(login, email, password, sessionString);
            } else if (task == Task.ADDUSERBYSESSION) {
                dao.addUserBySession(sessionString, userService);
            } else if (task == Task.REMOVEUSERBYSESSION) {
                dao.removeUserBySession(sessionString);
            } else if (task == Task.CONTAINSUSERBYLOGIN) {
                check = dao.containsUserByLogin(login);
            }
            transaction.commit();
            session.close();
            return check;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    private Configuration getMySqlConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserService.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/javajdbc");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "root");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
