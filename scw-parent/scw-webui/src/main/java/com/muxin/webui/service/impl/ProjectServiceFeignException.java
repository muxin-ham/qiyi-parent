package com.muxin.webui.service.impl;

import com.muxin.common.response.AppResponse;
import com.muxin.webui.service.ProjectServiceFeign;
import com.muxin.webui.vo.resp.ProjectDetailVo;
import com.muxin.webui.vo.resp.ProjectVo;
import com.muxin.webui.vo.resp.ReturnPayConfirmVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectServiceFeignException implements ProjectServiceFeign {
    @Override
    public AppResponse<List<ProjectVo>> allProject() {
        AppResponse<List<ProjectVo>> response = AppResponse.fail(null);
        response.setMsg("调用远程服务失败【项目列表】");
        return response;
    }

    @Override
    public AppResponse<ProjectDetailVo> projectInfo(Integer projectId) {
        AppResponse<ProjectDetailVo> response = AppResponse.fail(null);
        response.setMsg("调用远程服务失败【项目详情】");
        return response;
    }

    @Override
    public AppResponse<ReturnPayConfirmVo> getTReturn(Integer returnId) {
        AppResponse<ReturnPayConfirmVo> response = AppResponse.fail(null);
        response.setMsg("调用远程服务失败【回报信息】");
        return response;
    }
}
