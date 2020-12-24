package com.muxin.webui.vo.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddressVo implements Serializable {
    //地址id
    private Integer id = 1;

    //会员地址:默认值
    private String address = "朝阳门外大街31号";

}
