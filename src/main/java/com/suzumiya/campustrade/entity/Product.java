package com.suzumiya.campustrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "商品标题不能为空")
    private String title;// 必填

    private String description;// 选填

    @NotBlank(message = "商品类别不能为空")
    private String category;// 必填

    @NotNull(message = "售价不能为空")
    @DecimalMin(value = "0.01", message = "售价必须大于0")
    private BigDecimal price;// 必填

    @JsonProperty("original_price")
    private BigDecimal originalPrice;// 选填

    private String images; // 选填

    @JsonProperty("condition_type")
    @NotBlank(message = "成色不能为空")
    private String conditionType;// 必填

    @JsonProperty("trade_type")
    private String tradeType;// 选填

    @JsonProperty("seller_name")
    private String sellerName;// 选填

    @JsonProperty("seller_phone")
    private String sellerPhone;// 选填

    private String status;// 选填

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
