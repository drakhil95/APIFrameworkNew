package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


import config.ConfigReader;

public class DBUtills {
    private ConfigReader config = new ConfigReader();
    private Connection connection;


    // Parameterized constructor : To connect to different DBs if needed
    public DBUtills(String dbUrl, String dbUser, String dbPassword) throws SQLException, ClassNotFoundException {  
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // To ensure MySQL driver is loaded
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            LoggerUtils.success("Database connection established successfully");
        } catch (SQLException e) {
            LoggerUtils.error("Failed to establish database connection: " + e.getMessage());
            throw e;
        }

    }

    // Default constructor : Reads DB config from properties file to connect to the environment-specific DB
    public DBUtills() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure MySQL driver is loaded
        String dbUrl = System.getProperty("db.url", config.getDbUrl());
        String dbUser = System.getProperty("db.user", config.getDbUsername());        
        String dbPassword = System.getProperty("db.password", config.getDbPassword());

        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            LoggerUtils.success("Database connection established successfully");
        } catch (SQLException e) {
            LoggerUtils.error("Failed to establish database connection: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }


    public ResultSet executeQuery(String query) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            LoggerUtils.error("SQL query execution failed: " + e.getMessage());
            throw e;
        }
    }

    /*
     * Execute an update/insert/delete SQL statement
     */
    public int executeUpdate(String query) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            LoggerUtils.error("SQL update execution failed: " + e.getMessage());
            throw e;
        }
    }

    /*
     * Close the database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                LoggerUtils.success("Database connection closed successfully");
            } catch (SQLException e) {
                LoggerUtils.error("Failed to close database connection: " + e.getMessage());
            }
        }
    }

}
