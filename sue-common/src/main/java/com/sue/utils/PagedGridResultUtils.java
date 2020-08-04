package com.sue.utils;

import com.github.pagehelper.PageInfo;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author sue
 * @date 2020/8/4 11:55
 */

public class PagedGridResultUtils {
    public static PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setPage(page);
        pagedGridResult.setRows(list);
        pagedGridResult.setTotal(pageList.getPages());
        pagedGridResult.setRecords(pageList.getTotal());
        return pagedGridResult;
    }

//    public static <T> PagedGridResult setterPagedGrid(List<?> list, Integer page,Class<T> clazz) {
//
//        PageInfo<?> pageList = new PageInfo<>(list);
//        PagedGridResult pagedGridResult = new PagedGridResult();
//        pagedGridResult.setPage(page);
//        pagedGridResult.setRows(list);
//        pagedGridResult.setTotal(pageList.getPages());
//        pagedGridResult.setRecords(pageList.getTotal());
//        return pagedGridResult;
//    }
}
