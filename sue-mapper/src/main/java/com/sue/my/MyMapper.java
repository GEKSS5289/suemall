package com.sue.my;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author sue
 * @date 2020/7/31 12:18
 */

public interface MyMapper<T> extends Mapper<T>, MySqlMapper {
}
