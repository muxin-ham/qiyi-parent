package com.muxin.user.mapper;

import com.muxin.user.po.TMemberCert;
import com.muxin.user.po.TMemberCertExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TMemberCertMapper {
    long countByExample(TMemberCertExample example);

    int deleteByExample(TMemberCertExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TMemberCert record);

    int insertSelective(TMemberCert record);

    List<TMemberCert> selectByExample(TMemberCertExample example);

    TMemberCert selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TMemberCert record, @Param("example") TMemberCertExample example);

    int updateByExample(@Param("record") TMemberCert record, @Param("example") TMemberCertExample example);

    int updateByPrimaryKeySelective(TMemberCert record);

    int updateByPrimaryKey(TMemberCert record);
}