package com.suzumiya.campustrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String description;
    private String category;
    private BigDecimal price;

    @JsonProperty("original_price")
    private BigDecimal originalPrice;

    private String images;

    @JsonProperty("condition_type")
    private String conditionType;

    @JsonProperty("trade_type")
    private String tradeType;

    @JsonProperty("seller_name")
    private String sellerName;

    @JsonProperty("seller_phone")
    private String sellerPhone;

    private String status;

    @JsonProperty("view_count")
    private Integer viewCount;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("create_time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @JsonProperty("update_time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
