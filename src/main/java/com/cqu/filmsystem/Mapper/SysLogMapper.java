package com.cqu.filmsystem.Mapper;

import com.cqu.filmsystem.pojo.Rating;
import com.cqu.filmsystem.pojo.Syslog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper    //这个注解表示这个是mybatis的mapeper
@Repository
public interface SysLogMapper {

    List<Syslog> selectUserSyslog (String username);


    List<Syslog> selectIsLive(Syslog syslog);

    //写入日志
    int addLog(Syslog syslog);

    int update(Syslog syslog);

    int updateTime(Syslog syslog);
}