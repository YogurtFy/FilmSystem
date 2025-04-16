package com.cqu.filmsystem.Service.Impl;

import com.cqu.filmsystem.Mapper.RecommendMapper;
import com.cqu.filmsystem.Mapper.SysLogMapper;
import com.cqu.filmsystem.Service.SysLogService;
import com.cqu.filmsystem.pojo.Syslog;
import com.cqu.filmsystem.pojo.UserRecommend;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    SysLogMapper sysLogMapper;

    @Autowired
    RecommendMapper recommendMapper;

    @Override
    public List<Syslog> selectUserSyslog(String username) {
        return  sysLogMapper.selectUserSyslog(username);
    }

    @Override
    public List<Syslog> selectIsLive(Syslog syslog) {
        return sysLogMapper.selectIsLive(syslog);
    }

    //写入日志
    @Override
    public int addLog(Syslog syslog) {
        return sysLogMapper.addLog(syslog);
    }

    @Override
    public int update(Syslog syslog) {
        return sysLogMapper.update(syslog);
    }

    @Override
    public int updateTime(Syslog syslog) {
        return sysLogMapper.updateTime(syslog);
    }


}