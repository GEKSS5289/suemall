package com.sue.pojo.vo;

import com.sue.pojo.Items;
import com.sue.pojo.ItemsImg;
import com.sue.pojo.ItemsParam;
import com.sue.pojo.ItemsSpec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 17:56
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoVO {
    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;
}
