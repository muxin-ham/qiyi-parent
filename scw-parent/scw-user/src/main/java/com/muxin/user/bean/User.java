package com.muxin.user.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("测试实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @ApiModelProperty("主键")
    private int id;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("邮箱")
    private String email;
}
