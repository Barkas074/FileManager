package org.example.dbService;

import org.example.UserService;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;

public class UsersDAO {
    private final Session session;

    public UsersDAO(Session session) {
        this.session = session;
    }

    public UserService getUser(String login) throws HibernateException {
        return session.byNaturalId(UserService.class).using("login", login).load();
    }

    public UserService getUserBySession(String sessionString) throws HibernateException {
        return processingUserBySession(sessionString);
    }

    public boolean containsUserByLogin(String login) throws HibernateException {
        UserService user = session.byNaturalId(UserService.class).using("login", login).load();
        return user != null;
    }

    public void addUser(String login, String email, String password, String sessionString) throws HibernateException {
        session.save(new UserService(email, login, password, sessionString));
    }

    public void addUserBySession(String sessionString, UserService userService) throws HibernateException {
        userService.setSession(sessionString);
        session.update(userService);
    }

    public void removeUserBySession(String sessionString) throws HibernateException {
        UserService user = processingUserBySession(sessionString);
        user.setSession(null);
        session.update(user);
    }

    private UserService processingUserBySession(String sessionString) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserService> criteria = builder.createQuery(UserService.class);
        Root<UserService> root = criteria.from(UserService.class);
        ParameterExpression<String> sessionParam = builder.parameter(String.class);
        criteria.select(root)
                .where(builder.equal(root.get("session"), sessionParam));

        Query<UserService> query = session.createQuery(criteria);
        query.setParameter(sessionParam, sessionString);
        UserService user = null;
        try {
            user = query.getSingleResult();
        } catch (NoResultException nre) {
            //Ignore this because as per your logic this is ok!
        }
        return user;
    }
}
