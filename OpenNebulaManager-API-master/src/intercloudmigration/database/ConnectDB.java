///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package intercloudmigration.database;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
///**
// *
// * @author oneadmin
// */
//public class ConnectDB {
//
//    public String getImageBody(String imageName) {
//        Connection connection = null;
//        ResultSet resultSet = null;
//        Statement statement = null;
//
//        try {
//            Class.forName("org.sqlite.JDBC");
//            connection = DriverManager.getConnection("jdbc:sqlite:/var/lib/one/one.db");
//            statement = connection.createStatement();
//            resultSet = statement
//                    .executeQuery("select body "
//                            + "   from image_pool"
//                            + "   where name = '"+imageName+"';" );
//          return resultSet.getString("body");
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                resultSet.close();
//                statement.close();
//                connection.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return "";
//    }
//
//}
