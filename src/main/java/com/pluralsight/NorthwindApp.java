package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import java.util.Scanner;

public class NorthwindApp {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String username = System.getenv("username");
        String password = System.getenv("password");

        startMenu(username, password);

    }

    // this method display the start menu options for the app
    private static void startMenu(String username, String password) {

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/northwind");
        ds.setUsername(username);
        ds.setPassword(password);

        // try to connect to the northwind database using the username and password we provided
        try (Connection connection = ds.getConnection()){

            while (true){
                int input = getAInteger("""
                        What do you want to do?
                            1) Display All Products
                            2) Display All Customers
                            3) Display All Categories
                            0) Exit
                        Select an option:\s""");

                switch (input) {
                    case 0:
                        System.out.println("Goodbye!");
                        System.exit(0);
                    case 1:
                        displayAllProducts(connection);
                        break;
                    case 2:
                        displayAllCustomers(connection);
                        break;
                    case 3:
                        displayAllCategories(connection);
                        displayProductsByCategory(connection, getAInteger("Select a Category ID: "));
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not connect to DB");
            System.exit(1);
        }
    }

    // this method displays all products in the database sorted by product name
    private static void displayAllProducts(Connection connection) {

        try (

                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT
                            ProductID,
                            ProductName,
                            UnitPrice,
                            UnitsInStock
                        FROM
                            Products
                        ORDER BY
                            ProductID;
                        """
                );

                ResultSet results = preparedStatement.executeQuery();

        ) {
            printResults(results);
        } catch (SQLException e) {
            System.out.println("Could not get all the products");
            System.exit(1);
        }

    }

    // this method displays products from a user specified category sorted by product name
    private static void displayProductsByCategory(Connection connection, int categoryID) {

        try (

                PreparedStatement preparedStatement = connection.prepareStatement(String.format("""
                        SELECT
                            ProductID,
                            ProductName,
                            UnitPrice,
                            UnitsInStock
                        FROM
                            Products
                        WHERE
                            CategoryID = %d
                        ORDER BY
                            ProductID;
                        """
                , categoryID));

                ResultSet results = preparedStatement.executeQuery();

        ) {
            printResults(results);
        } catch (SQLException e) {
            System.out.println("Could not get the products");
            System.exit(1);
        }

    }

    // this method displays all customers in the database sorted by country
    private static void displayAllCustomers(Connection connection) {

        try (

                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT
                            ContactName,
                            CompanyName,
                            City,
                            Country,
                            Phone
                        FROM
                            Customers
                        ORDER BY
                            Country;
                        """
                );

                ResultSet results = preparedStatement.executeQuery();

        ) {
            printResults(results);
        } catch (SQLException e) {
            System.out.println("Could not get all the customers");
            System.exit(1);
        }

    }

    // this method displays all products in the database sorted by product name
    private static void displayAllCategories(Connection connection) {

        try (

                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT
                            CategoryID,
                            CategoryName
                        FROM
                            Categories
                        ORDER BY
                            CategoryID;
                        """
                );

                ResultSet results = preparedStatement.executeQuery();

        ) {
            printResults(results);
        } catch (SQLException e) {
            System.out.println("Could not get all the categories");
            System.exit(1);
        }

    }

    // this method will be used in the displayMethods to actually print the results to the screen
    private static void printResults(ResultSet results) throws SQLException {
        // get the metadata so we have access to the field names
        ResultSetMetaData metaData = results.getMetaData();
        // get the number of rows returned
        int columnCount = metaData.getColumnCount();

        // prints an empty line to make the results prettier
        System.out.println();

        while (results.next()) {

            for (int i = 1; i <= columnCount; i++) {
                // gets the current column name
                String columnName = metaData.getColumnName(i);

                // get the current column value
                String value = results.getString(i);

                // print out the column name and column value
                System.out.println(columnName + ": " + value + " ");
            }

            // prints an empty line to make the results prettier
            System.out.println();

        }

    }

    public static int getAInteger(String message) {
        int input;
        while (true) {
            try {
                System.out.print(message);
                input = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Only numbers allowed");
            }
        }
        return input;
    }
}
