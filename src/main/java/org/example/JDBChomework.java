package org.example;

import java.sql.*;

public class JDBChomework {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            acceptConnection(connection);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void acceptConnection(Connection connection) throws SQLException {
        createTable(connection);
        insert(connection);
        printTable(connection);
        deleteRow(connection, 1);
        System.out.println();
        printTable(connection);
        updateRow(connection, "new_name", 3);
        System.out.println();
        printTable(connection);
    }

    private static void updateRow(Connection connection, String firstName, int id) throws SQLException {
        try (PreparedStatement prest = connection.prepareStatement("update student set first_name = $1 where id = $2")) {
            prest.setInt(2, id);
            prest.setString(1, firstName);
            prest.executeUpdate();

        }
    }

    private static void deleteRow(Connection connection, int id) throws SQLException {
        try (PreparedStatement prest = connection.prepareStatement("delete from student where id = $1")) {
            prest.setInt(1, id);
            prest.executeUpdate();
        }
    }

    private static void printTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select id, first_name, second_name, age from student");
            while(resultSet.next()){
                long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String secondName = resultSet.getString("second_name");
                long age = resultSet.getLong("age");
                System.out.println("id = " + id + ", First name = " + firstName + ", Second name = " + secondName + ", age = " + age);
            }
        }
    }

    private static void insert(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int student = statement.executeUpdate("""
                    insert into student (id, first_name, second_name, age) values
                    (1, 'Tom', 'Sawyer', 12),
                    (2, 'Huckleberry', 'Finn', 14),
                    (3, 'Frodo', 'Baggins', 33),
                    (4, 'Harry', 'Potter', 11),
                    (5, 'Ron', 'Weasley', 11)
                    """);
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    create table student(
                    id bigint,
                    first_name varchar(256),
                    second_name varchar(256),
                    age int
                    )
                    """);
        }
    }
}
