package com.muxin.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.muxin.common.enums.ProjectStatusEnum;
import com.muxin.project.constants.ProjectConstant;
import com.muxin.project.enums.ProjectImageTypeEnum;
import com.muxin.project.mapper.*;
import com.muxin.project.po.*;
import com.muxin.project.service.ProjectCreateService;
import com.muxin.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TProjectTagMapper projectTagMapper;

    @Autowired
    private TProjectTypeMapper projectTypeMapper;

    @Autowired
    private TReturnMapper returnMapper;

    @Override
    public String initCreateProject(Integer id) {
        /*生成项目临时token*/
        String projectToken = UUID.randomUUID().toString().replace("-", "");
        /*创建项目vo*/
        ProjectRedisStorageVo projectVo = new ProjectRedisStorageVo();
        projectVo.setMemeberid(id);
        projectVo.setProjectToken(projectToken);
        String jsonString = JSON.toJSONString(projectVo);
        /*存取redis*/
        redisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken,jsonString);
        return projectToken;
    }

    @Override
    public void saveProjectInfo(ProjectStatusEnum status, ProjectRedisStorageVo storageVo) {
        /*1、保存项目基本信息*/
        TProject tProject = new TProject();
        BeanUtils.copyProperties(storageVo, tProject);
        /*设置项目状态*/
        tProject.setStatus(status.getCode() + "");
        /*设置项目创建时间*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tProject.setCreatedate(simpleDateFormat.format(new Date()));
        /*插入基本信息，主要通过selectKey返回主键*/
        projectMapper.insertSelective(tProject);
        Integer projectId = tProject.getId();
        /*2.1、保存图片信息，头图*/
        String headerImage = storageVo.getHeaderImage();
        TProjectImages hImage = new TProjectImages(null, projectId, headerImage, ProjectImageTypeEnum.HEADER.getCode());
        projectImagesMapper.insertSelective(hImage);
        /*2.2、保存详细图*/
        List<String> detailsImage = storageVo.getDetailsImage();
        for (String img : detailsImage) {
            TProjectImages dImage = new TProjectImages(null, projectId, img, ProjectImageTypeEnum.DETAILS.getCode());
            projectImagesMapper.insertSelective(dImage);
        }
        /*3、保存项目的标签信息*/
        List<Integer> tagIds = storageVo.getTagids();
        for (Integer tagId : tagIds) {
            TProjectTag tProjectTag = new TProjectTag(null, projectId, tagId);
            projectTagMapper.insertSelective(tProjectTag);
        }
        /*4、保存项目类型信息*/
        List<Integer> typeIds = storageVo.getTypeids();
        for (Integer typeId : typeIds) {
            TProjectType tProjectType = new TProjectType(null, projectId, typeId);
            projectTypeMapper.insertSelective(tProjectType);
        }
        /*5、保存回报信息*/
        List<TReturn> projectReturns = storageVo.getProjectReturns();
        for (TReturn projectReturn : projectReturns) {
            projectReturn.setProjectid(projectId);
            returnMapper.insertSelective(projectReturn);
        }
        /*6、删除redis中的临时数据storageVo*/
        redisTemplate.delete(ProjectConstant.TEMP_PROJECT_PREFIX+storageVo.getProjectToken());
    }
}
