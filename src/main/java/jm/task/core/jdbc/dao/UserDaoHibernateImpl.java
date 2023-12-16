package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.*;
import java.util.List;

import static jm.task.core.jdbc.dao.UserDaoJDBCImpl.TABLE_NAME;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {
    }


    @Override
    public void createUsersTable() {
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();

            String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "lastName VARCHAR(255) NOT NULL, " +
                    "age smallint NOT NULL)";

            session.createSQLQuery(query).executeUpdate();

            transaction.commit();
            System.out.println("Збсь! Таблица «" + TABLE_NAME + "» Захиберначена");
        } catch (HibernateException e) {
            System.out.println("Йопрст! Не могу Заиберначить таблицу «" + TABLE_NAME + "»" +
                    "\nПричина:" + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.format("DROP TABLE IF EXISTS %s", TABLE_NAME)
             )
        ) {
            preparedStatement.executeUpdate();
            System.out.println("Таблица «" + TABLE_NAME + "» удалена с особым цинизмом Хибернейта");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        Session session = Util.getSession();
            session.beginTransaction();

                User user = new User(name, lastName, age);
                session.save(user);

            session.getTransaction().commit();
        session.close();

        System.out.println("Пользователя «" + name + "» со свистом захиберанчило прямо в базу данных");

    }

    @Override
    public void removeUserById(long id) {
        Session session = Util.getSession();
        session.beginTransaction();

        User user = session.get(User.class, id);
        session.delete(user);

        session.getTransaction().commit();
        session.close();
        System.out.println("Пользователя с id - " + id + " вырвало из таблицы прямо на Хибернатора");
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        Session session = Util.getSession();
        session.beginTransaction();


        users = session.createQuery("FROM User", User.class).getResultList();

        System.out.println(users);

        session.getTransaction().commit();
        session.close();
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = Util.getSession();
        session.beginTransaction();

        session.createQuery("DELETE FROM User").executeUpdate();

        System.out.println("И восстали Хибернейты из пепла ядерного огня… и уничтожили таблицу к чертям собачим");
        session.getTransaction().commit();
        session.close();
    }
}
