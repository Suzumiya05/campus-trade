package com.suzumiya.campustrade.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/* 用户可能只修改昵称和手机号，所以如果直接用 User 实体加 @Valid，会要求 username、password 等字段也满足校验，导致失败 */
@Data
public class UpdateUserRequest {

    @NotBlank
    private String nickname;

    //电话号暂不做校验
    private String phone;
}
