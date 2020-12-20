package com.muxin.project.vo.resp;

import com.muxin.project.po.TReturn;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel("商品详细信息")
@Data
public class ProjectDetailVo implements Serializable {
    /*项目基本信息*/
    private Integer id;//项目id
    private Integer memberid;//发起人id
    private String name;//项目名称
    private String remark;//项目简介
    private Long money;//筹资金额
    private Integer day;//筹资天数
    private byte status;//项目状态 '0 - 即将开始， 1 - 众筹中， 2 - 众筹成功， 3 - 众筹失败'
    private String deploydate;//发布日期
    private Long supportmoney = 0L;//支持金额
    private Integer supporter = 0;//支持者数量
    private Integer completion = 0;//完成度
    private Integer follower = 100;//关注者数量
    /*项目图片信息*/
    private String headerImage;//项目头图片
    private List<String> detailsImage;//详情图（多）

    /*项目标签*/
    //private List<Integer> tagids;
    /*项目分类*/
    //private List<Integer> typeids;
    /*项目回报信息*/
    private List<TReturn> projectReturns;

}
