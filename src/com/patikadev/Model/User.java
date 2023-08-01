package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private String username;
    private String password;
    private String userTypee;

    public User(){

    }

    public User(int id, String name, String username, String password, String userType) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.userTypee = userType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserTypee() {
        return userTypee;
    }

    public void setUserTypee(String userType) {
        this.userTypee = userType;
    }

    public static ArrayList<User> getList(){
        ArrayList<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user";
        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){   //Must be the same as the column names in the database!
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUsername(rs.getString("username"));
                obj.setPassword(rs.getString("password"));
                obj.setUserTypee(rs.getString("user_type"));
                userList.add(obj);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userList;
    }

    public static boolean add(String name,String username,String password, String userTypee){  //add element to table
        String query = "INSERT INTO user (name,username,password,user_type) VALUES (?,?,?,?)";
        User findUser = User.getFetch(username);

        if (findUser != null){
            Helper.showMessage("Please enter a different username. ");
            return false;
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);
            pr.setString(2,username);
            pr.setString(3,password);
            pr.setString(4,userTypee);
            int response = pr.executeUpdate();
            if (response == -1){
                Helper.showMessage("Error");
            }
            return response != -1;


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;

    }

    public static User getFetch(String username){
        User obj = null;
        String query = "SELECT * FROM user WHERE username = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,username);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUsername(rs.getString("username"));
                obj.setPassword(rs.getString("password"));
                obj.setUserTypee(rs.getString("user_type"));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return obj;

    }

    public static boolean delete(int id){
        String query = "DELETE FROM user WHERE id =?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    public static boolean update(int id,String name, String username, String password, String type){
        String query = "UPDATE user SET name=?, username=?,password=?, user_type=? WHERE id =?";
        User findUser = User.getFetch(username);
        if (findUser != null && findUser.getId() != id ){
            Helper.showMessage("Please enter a different username.");
            return false;
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);
            pr.setString(2,username);
            pr.setString(3,password);
            pr.setString(4,type);
            pr.setInt(5,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    public static ArrayList<User> searchUserList(String query){
        ArrayList<User> userList = new ArrayList<>();

        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){   //must be the same as the column names in the database!!!
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUsername(rs.getString("username"));
                obj.setPassword(rs.getString("password"));
                obj.setUserTypee(rs.getString("user_type"));
                userList.add(obj);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userList;
    }

    public static String searchQuery(String name, String uname, String type){
        String query = "SELECT * FROM user WHERE username LIKE '%{{username}}%' AND name LIKE '%{{name}}%'";
        query = query.replace("{{username}}",uname);
        query = query.replace("{{name}}",name);

        if (!type.isEmpty()){
            query += "AND user_type ='{{user_type}}'";
            query = query.replace("{{user_type}}",type);
        }

        return query;



    }




}
