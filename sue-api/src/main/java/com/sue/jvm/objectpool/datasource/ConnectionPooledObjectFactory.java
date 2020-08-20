package com.sue.jvm.objectpool.datasource;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.*;

/**
 * @author sue
 * @date 2020/8/20 8:50
 */

public class ConnectionPooledObjectFactory implements PooledObjectFactory<MyConnection> {

    ObjectPool<MyConnection> objectPool;

    public ObjectPool<MyConnection> getObjectPool() {
        return objectPool;
    }

    public void setObjectPool(ObjectPool<MyConnection> objectPool) {
        this.objectPool = objectPool;
    }

    @Override
    public PooledObject<MyConnection> makeObject() throws Exception {
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
        MyConnection myConnection = new MyConnection();
        myConnection.setConnection(connection);
        myConnection.setPool(objectPool);
        return new DefaultPooledObject<>(myConnection);
    }

    @Override
    public void destroyObject(PooledObject<MyConnection> pooledObject) throws Exception {
        pooledObject.getObject().close();
    }

    @Override
    public boolean validateObject(PooledObject<MyConnection> pooledObject) {
        Connection connection = pooledObject.getObject();
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT 1");
            ResultSet resultSet = statement.executeQuery();
            int i = resultSet.getInt(1);
            return i==1;
        } catch (SQLException throwables) {
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<MyConnection> MyConnection) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<MyConnection> pooledObject) throws Exception {
        MyConnection object = pooledObject.getObject();
        Statement statement = object.getStatement();
        if(statement != null){
            statement.close();
        }

    }
}
