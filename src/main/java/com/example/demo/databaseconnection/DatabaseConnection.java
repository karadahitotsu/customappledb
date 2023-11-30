package com.example.demo.databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;
    private DatabaseConnection(){
        String url="jdbc:postgresql://localhost:5432/katalog";
        String username = "postgres";
        String password = "26463";
        try {
            // Установка соединения
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static synchronized Connection getConnection(){
        if(connection==null){
            new DatabaseConnection();
        }
        return connection;
    }
}
