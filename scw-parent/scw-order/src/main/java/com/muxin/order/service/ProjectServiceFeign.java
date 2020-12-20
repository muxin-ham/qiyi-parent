package com.muxin.order.service;

import com.muxin.common.response.AppResponse;
import com.muxin.order.service.impl.ProjectServiceFeignException;
import com.muxin.order.vo.resp.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "SCW-PROJECT",fallback = ProjectServiceFeignException.class)
public interface ProjectServiceFeign {
    @RequestMapping(value = "/project/details/returns/{projectId}",method = RequestMethod.GET)
    public AppResponse<List<TReturn>> returnInfo(@PathVariable("projectId") Integer projectId);
}
