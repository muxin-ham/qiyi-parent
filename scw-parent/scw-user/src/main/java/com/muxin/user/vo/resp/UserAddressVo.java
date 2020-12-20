package com.muxin.user.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户地址")
public class UserAddressVo {
    @ApiModelProperty("地址id")
    private Integer id ;

    @ApiModelProperty("会员地址")
    private String address ;
}
