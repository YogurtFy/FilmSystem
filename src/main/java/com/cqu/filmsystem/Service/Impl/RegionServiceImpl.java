package com.cqu.filmsystem.Service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Mapper.RegionMapper;
import com.cqu.filmsystem.Service.RegionService;
import com.cqu.filmsystem.pojo.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    RegionMapper regionMapper;

    @Override
    public List<Region> selectAllRegion() throws IOException {
        List<Region> Regions = regionMapper.selectAllRegion();
        return Regions;
    }


    @Override
    public Region selectById(int id) {
        return regionMapper.selectById(id);
    }

    @Override
    public PageInfo<Region> selectAll(Integer pageNum, Integer pageSize, String title) {
        title= "%"+title+"%";
        PageHelper.startPage (pageNum, pageSize);
        List<Region> regions = regionMapper.selectAll(title);
        PageInfo<Region> pageInfo = new PageInfo<> (regions, 5);
        return  pageInfo;
    }

    @Override
    public List<Region> selectAllRegion(String title) {
        title= "%"+title+"%";
        List<Region> regions = regionMapper.selectAll(title);
        return  regions;
    }

    @Override
    public int add(Region region) {
        return regionMapper.add(region);
    }

    @Override
    public int update(Region region) {
        return regionMapper.update(region);
    }

    @Override
    public int delete(int id) {
        return regionMapper.delete(id);
    }
}
