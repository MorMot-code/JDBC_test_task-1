package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД
    final static String DB_URL = "jdbc:postgresql://localhost:8080/TEST";
    final private static String DB_USERNAME = "postgres";
    final private static String DB_PASSWORD = "asdqwe";
    private static Connection connection;


    public static void testConnection() {

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            System.out.println("Поздравляю, ты взломал Пентагон!");
        } catch (SQLException e) {
            System.out.println("Даже чукотские хакеры взломали счёты… а ты НЕТ");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        }

    public static Session getSession() {
        Configuration configuration = new Configuration().addAnnotatedClass(User.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        return sessionFactory.openSession();

    }
}
