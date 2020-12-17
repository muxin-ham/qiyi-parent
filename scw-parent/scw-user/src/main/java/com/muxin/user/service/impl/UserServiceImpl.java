package com.muxin.user.service.impl;

import com.muxin.user.enums.UserExceptionEnum;
import com.muxin.user.exception.UserException;
import com.muxin.user.mapper.TMemberMapper;
import com.muxin.user.po.TMember;
import com.muxin.user.po.TMemberExample;
import com.muxin.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TMemberMapper memberMapper;

    @Override
    public void register(TMember member) {
        //1、检查账号是否已被注册
        TMemberExample exp = new TMemberExample();
        exp.createCriteria().andLoginacctEqualTo(member.getLoginacct());
        long l = memberMapper.countByExample(exp);
        //System.out.println("l:"+l);
        if(l>0){
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }
        //2、账号未被注册，设置参数
        /*密码加密*/
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(member.getUserpswd());
        member.setUserpswd(encode);
        member.setUsername(member.getLoginacct());
        /*实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证*/
        member.setAuthstatus("0");
        /*用户类型: 0 - 个人， 1 - 企业*/
        member.setUsertype("0");
        /*账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府*/
        member.setAccttype("2");
        System.out.println("插入数据:"+member.getLoginacct());
        memberMapper.insertSelective(member);
    }

    @Override
    public TMember login(String username, String password) {
        /*1、查询用户是否存在*/
        TMemberExample exp = new TMemberExample();
        TMemberExample.Criteria criteria = exp.createCriteria();
        criteria.andLoginacctEqualTo(username);
        List<TMember> tMembers = memberMapper.selectByExample(exp);
        if(tMembers!=null&&tMembers.size()>0){
            /*2、检验密码是否正确*/
            TMember tMember = tMembers.get(0);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean b = encoder.matches(password, tMember.getUserpswd());
            return b?tMember:null;
        }
        return null;
    }

    @Override
    public TMember findTMemberById(Integer id) {
        /*1、查询*/
        return memberMapper.selectByPrimaryKey(id);
    }


}
