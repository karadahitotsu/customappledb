package com.example.demo.apiclasses;

import com.example.demo.databaseconnection.DatabaseConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.*;

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
                Array productsArray = resultSet.getArray("productsid");
                if(productsArray!=null){
                    Integer[] array = (Integer[])productsArray.getArray();
                    if(array!=null){
                        for(Integer productId:array){
                            itemsArray.add(productId);
                        }
                    }
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
