package com.sue.pojo;

import javax.persistence.*;

public class Category {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 父id
     */
    private String parent;

    /**
     * 名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 获取id
     *
     * @return id - id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取父id
     *
     * @return parent - 父id
     */
    public String getParent() {
        return parent;
    }

    /**
     * 设置父id
     *
     * @param parent 父id
     */
    public void setParent(String parent) {
        this.parent = parent;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取顺序
     *
     * @return sort - 顺序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置顺序
     *
     * @param sort 顺序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}