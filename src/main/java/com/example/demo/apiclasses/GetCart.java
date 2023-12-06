package com.example.demo.apiclasses;

import com.example.demo.databaseconnection.DatabaseConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetCart {
    int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getItems() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        String jsonString = "";


        try {
            Connection connection = DatabaseConnection.getConnection();
            String sqlQuery = "SELECT productsid FROM cart WHERE userid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayNode itemsArray = objectMapper.createArrayNode();

            while (resultSet.next()) {
                // Получаем массив товаров пользователя по его ID
                int[] productsArray = (int[]) resultSet.getArray("productsid").getArray();

                for (int productId : productsArray) {
                    itemsArray.add(productId);
                }
            }

            rootNode.set("items", itemsArray);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
