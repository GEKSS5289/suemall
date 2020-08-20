package com.sue.jvm.objectpool.commonpool.jdbc;

import java.sql.*;

/**
 * @author sue
 * @date 2020/8/20 8:44
 */

public class JDBCTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.182.150:3306/foodie-shop-dev?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=GMT%2B8",
                "root",
                "ShuShun1558@qq.com");

        PreparedStatement preparedStatement = connection.prepareStatement("select * from `foodie-shop-dev`.orders;");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            System.out.println(resultSet.getString("id"));
            System.out.println(resultSet.getString("user_id"));
        }
        resultSet.close();
        preparedStatement.close();


        PreparedStatement preparedStatement2 = connection.prepareStatement("select * from `foodie-shop-dev`.orders;");
        ResultSet resultSet2 = preparedStatement2.executeQuery();
        while (resultSet2.next()){
            System.out.println(resultSet2.getString(1));
            System.out.println(resultSet2.getString(2));
        }
        resultSet.close();
        preparedStatement.close();



        connection.close();
    }
}
