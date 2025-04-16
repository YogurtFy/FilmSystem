package com.cqu.filmsystem.Service;

import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.pojo.Region;
import com.cqu.filmsystem.pojo.Type;

import java.io.IOException;
import java.util.List;


public interface RegionService {

    //1. 插叙所有地区
    List<Region> selectAllRegion() throws IOException;


    //-----------------------------------------------管理员------------------------------------------------------------------
    Region selectById(int id );

    PageInfo<Region> selectAll(Integer pageNum, Integer pageSize, String title);
    List<Region> selectAllRegion(String title);

    int add(Region region);

    int update(Region region);

    int delete(int id);



}
