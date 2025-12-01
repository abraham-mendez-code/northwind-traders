package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class NorthwindApp {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String username = System.getenv("username");
        String password = System.getenv("password");

        Scanner scanner = new Scanner(System.in);

        startMenu(username, password, scanner);

    }

    private static void startMenu(String username, String password, Scanner scanner) {
        // try to connect to the northwind database using the username and password we provided
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", username, password)){

            while (true){
                System.out.print("""
                        What do you want to do?
                            1) Display All Products
                            2) Display All Customers
                            0) Exit
                        Select an option:\s""");

                switch (scanner.nextInt()) {
                    case 0:
                        System.out.println("Goodbye!");
                        System.exit(0);
                    case 1:
                        displayAllProducts(connection);
                        break;
                    case 2:
                        //displayAllCustomers(connection);
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
                            ProductName;
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


    //this method will be used in the displayMethods to actually print the results to the screen
    private static void printResults(ResultSet results) throws SQLException {
        //get the metadata so we have access to the field names
        ResultSetMetaData metaData = results.getMetaData();
        //get the number of rows returned
        int columnCount = metaData.getColumnCount();

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

}
