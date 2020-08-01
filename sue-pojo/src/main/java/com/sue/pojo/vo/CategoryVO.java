package com.sue.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 15:11
 */

@Data
public class CategoryVO {
   private Integer id;
   private String name;
   private String type;
   private Integer fatherId;

   //三级分类vo list
   private List<SubCategoryVO> subCatList;
}
