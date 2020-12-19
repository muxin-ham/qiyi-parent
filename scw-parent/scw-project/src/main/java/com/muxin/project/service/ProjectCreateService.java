package com.muxin.project.service;

import com.muxin.common.enums.ProjectStatusEnum;
import com.muxin.project.vo.req.ProjectRedisStorageVo;

public interface ProjectCreateService {
    public String initCreateProject(Integer id);

    public void saveProjectInfo(ProjectStatusEnum status, ProjectRedisStorageVo storageVo);
}
