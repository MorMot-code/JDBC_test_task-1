package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь

        User user_1 = new User("Вася", "Пупкин", (byte) 14);
        User user_2 = new User("Сара", "Коннор", (byte) 25);
        User user_3 = new User("Лена", "Головач", (byte) 18);
        User user_4 = new User("Стоша", "Говнозад", (byte) 99);

        if (Util.testConnection()) {
            DaoJDBC_process(user_1, user_2, user_3, user_4);
            Hibernate_process(user_1, user_2, user_3, user_4);
        }

    }

    private static void DaoJDBC_process(User user_1, User user_2, User user_3, User user_4) {
        System.out.println("---- starting DAO JDBC process ----");
        UserDaoJDBCImpl userDaoJDBC = new UserDaoJDBCImpl();
        userDaoJDBC.createUsersTable();

        System.out.println("-----------------");
        userDaoJDBC.saveUser(user_1.getName(), user_1.getLastName(), user_1.getAge());
        userDaoJDBC.saveUser(user_2.getName(), user_2.getLastName(), user_2.getAge());
        userDaoJDBC.saveUser(user_3.getName(), user_3.getLastName(), user_3.getAge());
        userDaoJDBC.saveUser(user_4.getName(), user_4.getLastName(), user_4.getAge());

        System.out.println("---- список пользователей ДО удаления ----");
        userDaoJDBC.getAllUsers();

        System.out.println("----- удаляем пользователя с id = 2 ----");
        userDaoJDBC.removeUserById(2);
        System.out.println("---- список пользователей ПОСЛЕ удаления ----");
        userDaoJDBC.getAllUsers();
        System.out.println("-----------------");
        userDaoJDBC.cleanUsersTable();
        userDaoJDBC.dropUsersTable();

        System.out.println("---- finished DAO JDBC process ----");
    }

    private static void Hibernate_process(User user_1, User user_2, User user_3, User user_4) {
        System.out.println("\n\n---- starting Hibernate JDBC process ----");
        UserDaoHibernateImpl userDaoHibernate = new UserDaoHibernateImpl();
        userDaoHibernate.createUsersTable();

        userDaoHibernate.saveUser(user_1.getName(), user_1.getLastName(), user_1.getAge());
        userDaoHibernate.saveUser(user_2.getName(), user_2.getLastName(), user_2.getAge());
        userDaoHibernate.saveUser(user_3.getName(), user_3.getLastName(), user_3.getAge());
        userDaoHibernate.saveUser(user_4.getName(), user_4.getLastName(), user_4.getAge());

        System.out.println("---- список пользователей ДО удаления ----");
        userDaoHibernate.getAllUsers();

        System.out.println("----- удаляем пользователя с id = 1 ----");
        userDaoHibernate.removeUserById(1);

        System.out.println("---- список пользователей ПОСЛЕ удаления ----");
        userDaoHibernate.getAllUsers();

        System.out.println("---- массовая зачистка ----");
        userDaoHibernate.cleanUsersTable();

        System.out.println("---- список пользователей ПОСЛЕ зачистки ----");
        userDaoHibernate.getAllUsers();

        userDaoHibernate.dropUsersTable();
        System.out.println("---- finished Hibernate process ----");
    }
}
