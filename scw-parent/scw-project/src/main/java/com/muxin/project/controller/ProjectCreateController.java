package com.muxin.project.controller;

import com.alibaba.fastjson.JSON;
import com.muxin.common.enums.ProjectStatusEnum;
import com.muxin.common.response.AppResponse;
import com.muxin.project.constants.ProjectConstant;
import com.muxin.project.po.TReturn;
import com.muxin.project.service.ProjectCreateService;
import com.muxin.project.vo.req.ProjectBaseInfoVo;
import com.muxin.project.vo.req.ProjectRedisStorageVo;
import com.muxin.project.vo.req.ProjectReturnVo;
import com.muxin.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "项目基本功能模块（创建、保存、项目信息获取、文件上传等）")
public class ProjectCreateController {

    @Autowired
    private ProjectCreateService projectCreateService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation("项目发起第一步：阅读同意协议")
    @RequestMapping(value = "/init",method = RequestMethod.GET)
    public AppResponse init(BaseVo baseVo){
        String accessToken = baseVo.getAccessToken();
        /*通过登录令牌获取用户id*/
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.isEmpty(memberId)){
            return AppResponse.fail("无此权限，请先登录");
        }
        Integer id = Integer.valueOf(memberId);
        //在业务层将初始化项目放入redis
        String token = projectCreateService.initCreateProject(id);
        return AppResponse.success(token);
    }

    @ApiOperation("项目发起第2步-保存项目的基本信息")
    @RequestMapping(value = "/saveBaseInfo",method = RequestMethod.POST)
    public AppResponse saveBaseInfo(ProjectBaseInfoVo projectBaseInfoVo){
        /*1、从redis获取projectRedisStorageVo*/
        String projectToken = projectBaseInfoVo.getProjectToken();
        String s = redisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken);
        if(StringUtils.isEmpty(s)){
            return AppResponse.fail("");
        }
        ProjectRedisStorageVo storageVo = JSON.parseObject(s, ProjectRedisStorageVo.class);
        /*2、将项目基础信息存入storageVo*/
        BeanUtils.copyProperties(projectBaseInfoVo,storageVo);
        /*3、将storageVo再次存入redis*/
        String jsonString = JSON.toJSONString(storageVo);
        redisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken,jsonString);
        return AppResponse.success(projectToken);
    }

    @ApiOperation("项目发起第3步-项目保存项目回报信息")
    @RequestMapping(value = "/saveReturn",method = RequestMethod.POST)
    public AppResponse saveReturnInfo(@RequestBody List<ProjectReturnVo> returnVos){
        if(returnVos.size()>0){
            /*1、获取projectToken*/
            ProjectReturnVo projectReturnVo = returnVos.get(0);
            String projectToken = projectReturnVo.getProjectToken();
            /*2、从redis中获取storageVo*/
            String s = redisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken);
            ProjectRedisStorageVo storageVo = JSON.parseObject(s, ProjectRedisStorageVo.class);
            /*3、将returnVos存入storageVO*/
            List<TReturn> tReturns = new ArrayList<>();
            for (ProjectReturnVo returnVo : returnVos) {
                TReturn tReturn = new TReturn();
                BeanUtils.copyProperties(returnVo,tReturn);
                tReturns.add(tReturn);
            }
            storageVo.setProjectReturns(tReturns);
            /*4、将storageVo重新转为json字符串*/
            String jsonString = JSON.toJSONString(storageVo);
            /*5、将storageVo存入redis*/
            redisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken,jsonString);
            return AppResponse.success("ok");
        }
        return AppResponse.fail("无回报信息");
    }

    @ApiOperation("项目发起第4步-项目保存项目回报信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken",value = "用户令牌",required = true),
            @ApiImplicitParam(name = "projectToken",value="项目标识",required = true),
            @ApiImplicitParam(name="ops",value="用户操作类型 0-保存草稿 1-提交审核",required = true)})
    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    public AppResponse submit(String accessToken,String projectToken,String ops){
        /*1、用户令牌校验*/
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.isEmpty(memberId)){
            return AppResponse.fail("无权限，请先登录");
        }
        /*2、获取storageVo信息*/
        String s = redisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken);
        /*json字符串转为vo*/
        ProjectRedisStorageVo storageVo = JSON.parseObject(s, ProjectRedisStorageVo.class);
        /*3、判断操作类型*/
        if(StringUtils.isEmpty(ops)){
            AppResponse.fail("请选择操作类型");
        }
        ProjectStatusEnum projectStatusEnum = null;
        if("0".equals(ops)){
            //获取项目 草稿状态
            projectStatusEnum = ProjectStatusEnum.DRAFT;
        } else if("1".equals(ops)){
            //获取项目 草稿状态
            projectStatusEnum = ProjectStatusEnum.SUBMIT_AUTH;
        } else{
            return AppResponse.fail("不支持此操作");
        }
        //保存项目
        projectCreateService.saveProjectInfo(projectStatusEnum,storageVo);
        return AppResponse.success(null);
    }
}
