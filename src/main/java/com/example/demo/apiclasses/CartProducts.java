package com.example.demo.apiclasses;

import com.example.demo.databaseconnection.DatabaseConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CartProducts {
    int[] ids;

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }
    public String getProducts(){
        String jsonString = "";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectNode products = objectMapper.createObjectNode();
        StringBuilder whereClause = new StringBuilder();
        int count =0;


        whereClause.append(" WHERE id IN (");
        for (int i = 0; i < ids.length; i++) {
            whereClause.append("'" + ids[i] + "'");
            if (i != ids.length - 1) {
                whereClause.append(", ");
            }
        }
        whereClause.append(")");
        Connection connection = DatabaseConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT * FROM katalog" + whereClause.toString();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                count++;

                ObjectNode productNode = objectMapper.createObjectNode();
                productNode.put("id", resultSet.getInt("Id"));
                productNode.put("name", resultSet.getString("Name"));
                productNode.put("price", resultSet.getInt("Price"));
                productNode.put("model", resultSet.getString("Model"));

                products.set(String.valueOf(count), productNode);

            }

            rootNode.set("products", products);
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonString;

    }
}
