package com.muxin.project.vo.req;

import com.muxin.project.po.TReturn;
import com.muxin.vo.BaseVo;
import lombok.Data;

import java.util.List;
/**
 * 组合实体类：project，标签-多，分类-多
 */
@Data
public class ProjectRedisStorageVo extends BaseVo {
    /*1、项目临时token*/
    private String projectToken;//项目临时token（前三步从redis存取的凭证）

    /*2、项目基本信息*/
    private Integer memeberid;//发起人
    private String name;//项目名称
    private String remark;//项目简介
    private Integer money;//筹资金额
    private Integer day;//筹资天数
    private String headerImage;//头图
    private List<String> detailsImage;//详情图（多）
    private List<Integer> tagids;//项目的标签id
    private List<Integer> typeids;//项目的分类id

    /*3、项目回报信息*/
    private List<TReturn> projectReturns;//项目回报
}
