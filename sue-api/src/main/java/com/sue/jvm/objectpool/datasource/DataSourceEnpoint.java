package com.sue.jvm.objectpool.datasource;

import com.google.common.collect.Maps;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sue
 * @date 2020/8/20 9:00
 */

@Endpoint(id = "datasource")
public class DataSourceEnpoint {
    private SueDataSource dataSource;

    public DataSourceEnpoint(){

    }

    public DataSourceEnpoint(SueDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @ReadOperation
    public Map<String,Object> pool(){
        GenericObjectPool<MyConnection> pool = dataSource.getPool();
        HashMap<String, Object> objectObjectHashMap = Maps.newHashMap();
        objectObjectHashMap.put("numActive",pool.getNumActive());
        objectObjectHashMap.put("numIdle",pool.getNumIdle());
        objectObjectHashMap.put("createdCount",pool.getCreatedCount());
        return objectObjectHashMap;
    }
}
