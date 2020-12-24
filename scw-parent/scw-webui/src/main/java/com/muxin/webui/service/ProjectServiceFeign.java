package com.muxin.webui.service;

import com.muxin.common.response.AppResponse;
import com.muxin.webui.config.FeignConfig;
import com.muxin.webui.service.impl.ProjectServiceFeignException;
import com.muxin.webui.vo.resp.ProjectDetailVo;
import com.muxin.webui.vo.resp.ProjectVo;
import com.muxin.webui.vo.resp.ReturnPayConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "SCW-PROJECT",configuration = FeignConfig.class,fallback = ProjectServiceFeignException.class)
public interface ProjectServiceFeign {

    @RequestMapping(value = "/project/all",method = RequestMethod.GET)
    public AppResponse<List<ProjectVo>> allProject();

    @RequestMapping(value = "/project/detail/info/{projectId}",method = RequestMethod.GET)
    public AppResponse<ProjectDetailVo> projectInfo(@PathVariable("projectId")Integer projectId);

    @RequestMapping(value = "/project/returns/info/{returnId}",method = RequestMethod.GET)
    public AppResponse<ReturnPayConfirmVo> getTReturn(@PathVariable("returnId") Integer returnId);
}
