package com.pluralsight;

import java.sql.*;

public class NorthwindApp {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String username = System.getenv("username");
        String password = System.getenv("password");

        // load the MySQL Driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 1. open a connection to the database
        // use the database URL to point to the correct database
        Connection connection;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/northwind",
                username,
                password);

        // create statement
        // the statement is tied to the open connection
        Statement statement = connection.createStatement();
        // define your query
        String query = "SELECT * FROM products ";

        // 2. Execute your query
        ResultSet results = statement.executeQuery(query);

        // process the results
        while (results.next()) {
            String city = results.getString("ProductName");
            System.out.println(city);
        }

        // 3. Close the connection
        connection.close();

    }

}
