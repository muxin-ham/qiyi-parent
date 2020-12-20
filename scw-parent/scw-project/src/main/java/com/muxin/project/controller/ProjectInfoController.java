package com.muxin.project.controller;

import com.muxin.common.response.AppResponse;
import com.muxin.project.po.TReturn;
import com.muxin.project.service.ProjectInfoService;
import com.muxin.utils.OSSTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
@Api(tags = "项目基本功能模块（文件上传、项目信息获取等）")
@Slf4j
public class ProjectInfoController {

    @Autowired
    private ProjectInfoService projectInfoService;

    @Autowired
    private OSSTemplate ossTemplate;

    @ApiOperation("文件上传功能")
    @RequestMapping(value = "/upload",method = RequestMethod.POST,headers = "content-type=multipart/form-data")
    public AppResponse upload(@RequestParam("file")MultipartFile[] files){
        if (files!=null&&files.length>0){
            /*map用于存储相应信息，文件的urls*/
            Map<String,Object> urls = new HashMap<>();
            List<String> list = new ArrayList<String>();
            for (MultipartFile file : files) {
                try {
                    if (!file.isEmpty()){
                        String url = ossTemplate.upload(file.getInputStream(), file.getOriginalFilename());
                        list.add(url);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            urls.put("urls",list);
            log.debug("ossTemplate信息：{},文件上传成功访问路径{}",ossTemplate,list);
            return AppResponse.success(urls);
        }
        AppResponse appResponse = AppResponse.fail(null);
        appResponse.setMsg("请选择需要上传的文件");
        return appResponse;
    }

    @ApiOperation("获取项目回报列表")
    @RequestMapping(value = "/details/returns/{projectId}",method = RequestMethod.GET)
    public AppResponse<List<TReturn>> detailsReturn(@PathVariable("projectId") Integer projectId){
        List<TReturn> projectReturns = projectInfoService.getProjectReturns(projectId);
        return AppResponse.success(projectReturns);
    }
}
