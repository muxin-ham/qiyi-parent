package com.muxin.project.service;

import com.muxin.project.po.TReturn;

import java.util.List;

public interface ProjectInfoService {
    List<TReturn> getProjectReturns(Integer projectId);
}
