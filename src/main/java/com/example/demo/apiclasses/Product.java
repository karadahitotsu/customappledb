package com.example.demo.apiclasses;

import com.example.demo.databaseconnection.DatabaseConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Data
public class Product {
    String name;
    Integer price;
    byte[] image;
    byte[] imagePreview;
    String Model;


    public String addProduct(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        String jsonString ="";

        String status = "adding failed";
        if (name != null && !name.isEmpty() && price != null && image != null && imagePreview != null && Model != null && !Model.isEmpty()) {
            Connection connection = DatabaseConnection.getConnection();
            try {
                String sqlQuery = "INSERT INTO katalog (name, price, image, imagePreview, Model) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, price);
                preparedStatement.setBytes(3, image);
                preparedStatement.setBytes(4, imagePreview);
                preparedStatement.setString(5, Model);
                preparedStatement.executeUpdate();
                status = "adding complete";

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("error");
        }




        rootNode.put("status",status);

        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return jsonString;
    }
}
