package com.muxin.project.service.impl;

import com.muxin.project.mapper.*;
import com.muxin.project.po.*;
import com.muxin.project.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {
    @Autowired
    private TReturnMapper tReturnMapper;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TTagMapper tagMapper;

    @Autowired
    private TTypeMapper typeMapper;

    @Override
    public List<TReturn> getProjectReturns(Integer projectId) {
        TReturnExample exp = new TReturnExample();
        exp.createCriteria().andProjectidEqualTo(projectId);
        return tReturnMapper.selectByExample(exp);
    }

    @Override
    public List<TProject> getAllProjects() {
        return projectMapper.selectByExample(null);
    }

    @Override
    public List<TProjectImages> getProjectImages(Integer id) {
        TProjectImagesExample exp = new TProjectImagesExample();
        exp.createCriteria().andProjectidEqualTo(id);
        return projectImagesMapper.selectByExample(exp);
    }

    @Override
    public TProject projectInfo(Integer projectId) {
        return projectMapper.selectByPrimaryKey(projectId);
    }

    @Override
    public List<TTag> getAllProjectTags() {
        return tagMapper.selectByExample(null);
    }

    @Override
    public List<TType> getProjectTypes() {
        return typeMapper.selectByExample(null);
    }

    @Override
    public TReturn getReturnInfo(Integer returnId) {
        return tReturnMapper.selectByPrimaryKey(returnId);
    }
}
