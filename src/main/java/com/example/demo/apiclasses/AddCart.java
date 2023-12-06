package com.example.demo.apiclasses;

import com.example.demo.databaseconnection.DatabaseConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddCart {
    int userId;
    int productId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String addToCart() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        String jsonString = "";

        try {
            Connection connection = DatabaseConnection.getConnection();
            List<Integer> currentProducts = getCurrentProducts(connection, userId);

            currentProducts.add(productId);

            updateCart(connection, userId, currentProducts);


            rootNode.put("status", "adding complete");

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

    private List<Integer> getCurrentProducts(Connection connection, int userId) throws SQLException {
        List<Integer> currentProducts = new ArrayList<>();

        String sqlQuery = "SELECT productsid FROM cart WHERE userid = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setInt(1, userId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int[] productsArray = (int[]) resultSet.getArray("productsid").getArray();
            for (int prodId : productsArray) {
                currentProducts.add(prodId);
            }
        }

        resultSet.close();
        preparedStatement.close();

        return currentProducts;
    }

    private void updateCart(Connection connection, int userId, List<Integer> updatedProducts) throws SQLException {
        String updateQuery = "UPDATE cart SET productsid = ? WHERE userid = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

        Integer[] productsArray = updatedProducts.toArray(new Integer[0]);
        updateStatement.setArray(1, connection.createArrayOf("integer", productsArray));
        updateStatement.setInt(2, userId);

        updateStatement.executeUpdate();

        updateStatement.close();
    }
}
