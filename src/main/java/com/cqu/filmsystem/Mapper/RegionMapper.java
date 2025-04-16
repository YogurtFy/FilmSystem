package com.cqu.filmsystem.Mapper;

import com.cqu.filmsystem.pojo.Region;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface RegionMapper {

    List<Region> selectAllRegion();

    //------------------------------------------------管理员-----------------------------------------------------------------
    Region selectById(int id );

    List<Region> selectAll(@Param("title") String title);

    int add(Region region);

    int update(Region region);

    int delete(int id);



}
