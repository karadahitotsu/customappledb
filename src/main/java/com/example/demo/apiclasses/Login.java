package com.example.demo.apiclasses;

import com.example.demo.databaseconnection.DatabaseConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
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
    public String loggin(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        String jsonString= "";
        int id = 0;
        String name = "";
        try{


            Connection connection = DatabaseConnection.getConnection();
            String sqlquerry = "SELECT id, name FROM users where name = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlquerry);
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                id = resultSet.getInt("id");
                name = resultSet.getString("name");

            }





        }
        catch (SQLException e){
            e.printStackTrace();
        }
        if(id == 0 && name.equals("")){
            rootNode.put("error","no user");
        }
        else {
            rootNode.put("id",id);
            rootNode.put("name",name);
        }
        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonString;
    }
}
