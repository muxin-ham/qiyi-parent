package com.muxin.project.service.impl;

import com.muxin.project.mapper.TReturnMapper;
import com.muxin.project.po.TReturn;
import com.muxin.project.po.TReturnExample;
import com.muxin.project.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {
    @Autowired
    private TReturnMapper tReturnMapper;

    @Override
    public List<TReturn> getProjectReturns(Integer projectId) {
        TReturnExample exp = new TReturnExample();
        exp.createCriteria().andProjectidEqualTo(projectId);
        return tReturnMapper.selectByExample(exp);
    }
}
