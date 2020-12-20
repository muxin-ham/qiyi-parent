package com.muxin.project.controller;

import com.muxin.common.response.AppResponse;
import com.muxin.project.enums.ProjectImageTypeEnum;
import com.muxin.project.po.*;
import com.muxin.project.service.ProjectInfoService;
import com.muxin.project.vo.resp.ProjectDetailVo;
import com.muxin.project.vo.resp.ProjectVo;
import com.muxin.utils.OSSTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @ApiOperation("获取项目列表")
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public AppResponse<List<ProjectVo>> allProject(){
        /*1、获取项目列表*/
        List<TProject> allProjects = projectInfoService.getAllProjects();
        /*2、创建projectVo集合*/
        List<ProjectVo> projectVos = new ArrayList<>();
        for (TProject project : allProjects) {
            ProjectVo projectVo = new ProjectVo();
            /*3、vo转po*/
            BeanUtils.copyProperties(project,projectVo);
            /*4、根据projectId获取图片列表*/
            List<TProjectImages> projectImages = projectInfoService.getProjectImages(project.getId());
            /*5、获取头图*/
            for (TProjectImages projectImage : projectImages) {
                if(projectImage.getImgtype()== ProjectImageTypeEnum.HEADER.getCode()){
                    projectVo.setHeaderImage(projectImage.getImgurl());
                }
            }
            /*6、将项目添加入列表*/
            projectVos.add(projectVo);
        }
        return AppResponse.success(projectVos);
    }

    @ApiOperation("获取项目详细信息")
    @RequestMapping(value = "/detail/info/{projectId}",method = RequestMethod.GET)
    public AppResponse<ProjectDetailVo> projectInfo(@PathVariable("projectId")Integer projectId){
        /*1、创建resp vo对象*/
        ProjectDetailVo projectDetailVo = new ProjectDetailVo();
        /*2、查询详细信息*/
        TProject project = projectInfoService.projectInfo(projectId);
        /*3、po转vo*/
        BeanUtils.copyProperties(project,projectDetailVo);
        /*4、根据projectId获取图片列表*/
        List<TProjectImages> projectImages = projectInfoService.getProjectImages(project.getId());
        /*5、头图详情图放入vo*/
        List<String> detailsImage = projectDetailVo.getDetailsImage();
        if(detailsImage==null){
            detailsImage=new ArrayList<>();
        }
        for (TProjectImages projectImage : projectImages) {
            if(projectImage.getImgtype()==ProjectImageTypeEnum.HEADER.getCode()){
                projectDetailVo.setHeaderImage(projectImage.getImgurl());
            }else{
                detailsImage.add(projectImage.getImgurl());
            }
        }
        projectDetailVo.setDetailsImage(detailsImage);
        /*6、查询回报信息，放入vo*/
        List<TReturn> returns = projectInfoService.getProjectReturns(projectId);
        projectDetailVo.setProjectReturns(returns);
        return AppResponse.success(projectDetailVo);
    }

    @ApiOperation("获取系统所有的项目标签")
    @RequestMapping(value = "/tags",method = RequestMethod.GET)
    public AppResponse<List<TTag>> tags() {
        List<TTag> tags = projectInfoService.getAllProjectTags();
        return AppResponse.success(tags);
    }

    @ApiOperation("获取系统所有的项目分类")
    @RequestMapping(value = "/types",method = RequestMethod.GET)
    public AppResponse<List<TType>> types() {
        List<TType> types = projectInfoService.getProjectTypes();
        return AppResponse.success(types);
    }

    @ApiOperation("获取回报信息")
    @RequestMapping(value = "/returns/info/{returnId}",method = RequestMethod.GET)
    public AppResponse<TReturn> getTReturn(@PathVariable("returnId") Integer returnId){
        TReturn tReturn = projectInfoService.getReturnInfo(returnId);
        return AppResponse.success(tReturn);
    }

}
