package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.TABLE_NAME;

public class UserDaoJDBCImpl implements UserDao {


    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                    // насколько адекватно пользоваться SERIAL для id?
                    "name VARCHAR(255) NOT NULL, " +
                    "lastName VARCHAR(255) NOT NULL, " +
                    "age smallint NOT NULL)";

            statement.executeUpdate(query);

            System.out.println("Всё ОК! Таблица «" + TABLE_NAME + "» создана");
        } catch (SQLException e) {
            System.out.println("ОШИБКА! Не могу создать таблицу «" + TABLE_NAME + "»" +
                    "\nПричина:" + e.getMessage());
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
            statement.executeUpdate(query);
            System.out.println("Таблица «" + TABLE_NAME + "» удалена");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(
                     String.format("SELECT COUNT(*) FROM %s WHERE name = ?", TABLE_NAME)
             );
             PreparedStatement insertStatement = connection.prepareStatement(
                     String.format("INSERT INTO %s (name, lastName, age) VALUES (?,?,?)", TABLE_NAME)
             )) {

            selectStatement.setString(1, name);
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            int userCount = resultSet.getInt(1);
            resultSet.close();

            if (userCount > 0) {
                System.out.println("Пользователь «" + name + "» уже есть в этой таблице");
                return;
            }

            insertStatement.setString(1, name);
            insertStatement.setString(2, lastName);
            insertStatement.setByte(3, age);
            insertStatement.executeUpdate();
            System.out.println("Пользователь «" + name + "» добавлен");
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении пользователя «" + name + "» в таблицу");
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME)
             )) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            System.out.println("Пользователь под номером " + id + " больше не будет нам докучать");
        } catch (SQLException e) {
            System.out.println("Оу май гад! Итс Джейсон Борн!" +
                    "\nУстранить пользователя " + id + " не удалось");
        }
    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.format("SELECT * FROM %s", TABLE_NAME)
             )) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastname");
                byte age = resultSet.getByte("age");
                User user = new User();
                user.setId(id);
                user.setName(name);
                user.setLastName(lastName);
                user.setAge(age);
                users.add(user);
                System.out.println(user);
            }

        } catch (SQLException e) {
            System.out.println("ОШИБКА! Пользователи испугались вышек 5G. Гонца со списком сожгли на площади");
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            String query = "DELETE FROM " + TABLE_NAME;
            statement.executeUpdate(query);

            System.out.println("Все свидетели были устранены. Никакое дерево в лесу не падало");
        } catch (SQLException e) {
            System.out.println("Это фиаско, братан! Все никчёмные людишки умудрились выжить");
        }
    }

}
