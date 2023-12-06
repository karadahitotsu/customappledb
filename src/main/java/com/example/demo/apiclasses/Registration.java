package com.example.demo.apiclasses;

import com.example.demo.databaseconnection.DatabaseConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Registration {
    String login;
    String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String regist() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        String jsonString = "";
        String status = "adding failed";
        try {
            Connection connection = DatabaseConnection.getConnection();
            String insertUserQuery = "INSERT INTO users (name, password) VALUES (?, ?)";
            PreparedStatement userStatement = connection.prepareStatement(insertUserQuery, new String[]{"id"});
            userStatement.setString(1, login);
            userStatement.setString(2, password);

            int affectedRows = userStatement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = userStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    String insertCartQuery = "INSERT INTO cart (userid, productsid) VALUES (?, ?)";
                    PreparedStatement cartStatement = connection.prepareStatement(insertCartQuery);
                    cartStatement.setInt(1, userId);
                    // Вставьте массив продуктов или оставьте пустым, так как он не инициализирован в вашем коде

                    int cartAffectedRows = cartStatement.executeUpdate();
                    if (cartAffectedRows > 0) {
                        status = "adding complete";
                    }
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        rootNode.put("status", status);
        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
