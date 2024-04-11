package com.example.demo.apiclasses;

import com.example.demo.databaseconnection.DatabaseConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductFilter {
    String[] filters;
    String category;

    public String[] getFilters() {
        return filters;
    }

    public void setFilters(String[] filters) {
        this.filters = filters;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProducts(){
        String jsonString = "";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectNode products = objectMapper.createObjectNode();
        int count =0;

        StringBuilder whereClause = new StringBuilder();
        if (filters != null && filters.length > 0) {
            whereClause.append(" WHERE Model IN (");
            for (int i = 0; i < filters.length; i++) {
                whereClause.append("'" + filters[i] + "'");
                if (i != filters.length - 1) {
                    whereClause.append(", ");
                }
            }
            whereClause.append(")");
        }
        if (category != null && !category.isEmpty()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND");
            } else {
                whereClause.append(" WHERE");
            }
            whereClause.append(" category = '" + category + "'");
        }
        Connection connection = DatabaseConnection.getConnection();

        try{
            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT * FROM katalog" + whereClause.toString();

            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                count++;

                ObjectNode productNode = objectMapper.createObjectNode();
                productNode.put("id", resultSet.getInt("Id"));
                productNode.put("name", resultSet.getString("Name"));
                productNode.put("price", resultSet.getInt("Price"));
                // Преобразуйте image и imagePreview в строковый формат, если необходимо
                productNode.put("image", resultSet.getBytes("Image"));
                productNode.put("imagePreview", resultSet.getBytes("ImagePreview"));
                productNode.put("model", resultSet.getString("Model"));

                products.set(String.valueOf(count),productNode);

            }

            rootNode.set("products",products);


        }
        catch (SQLException e){
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
