package com.muxin.project.service;

import com.muxin.project.po.*;

import java.util.List;

public interface ProjectInfoService {
    /*获取项目对应的回报信息*/
    List<TReturn> getProjectReturns(Integer projectId);

    /*获取系统中所有的项目*/
    List<TProject> getAllProjects();

    /*获取项目图片*/
    List<TProjectImages> getProjectImages(Integer id);

    /*获取项目详细信息*/
    public TProject projectInfo(Integer projectId);

    /*获得项目标签列表*/
    List<TTag> getAllProjectTags();

    /*获得项目分类列表*/
    List<TType> getProjectTypes();

    /*获取回报信息*/
    TReturn getReturnInfo(Integer returnId);
}
