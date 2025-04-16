package com.cqu.filmsystem.Service;

import com.cqu.filmsystem.pojo.Syslog;
import com.cqu.filmsystem.pojo.UserRecommend;

import java.util.List;


public interface SysLogService {

    List<Syslog> selectUserSyslog (String username);


    List<Syslog>  selectIsLive(Syslog syslog);

    int addLog(Syslog syslog);

    int update(Syslog syslog);

    int updateTime(Syslog syslog);




}
