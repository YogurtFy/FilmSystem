package com.cqu.filmsystem.Service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Mapper.TypeMapper;
import com.cqu.filmsystem.Service.TypeService;
import com.cqu.filmsystem.pojo.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeMapper typeMapper;

    @Override
    public List<Type> selectAllType() throws IOException {
        return typeMapper.selectAllType();
    }

    @Override
    public List<Type> selectByMovieId(int id) throws IOException {
        return typeMapper.selectByMovieId(id);
    }

    @Override
    public int insertMovieType(Integer movieId, Integer categoryId) {
        return typeMapper.insertMovieType(movieId, categoryId);
    }

    @Override
    public int updateMovieType(Integer movieId, Integer categoryId) {
        return typeMapper.updateMovieType(movieId, categoryId);
    }

    @Override
    public Type selectById(int id) {
        return typeMapper.selectById(id);
    }

    @Override
    public PageInfo<Type> selectAll(Integer pageNum, Integer pageSize, String title) {
        title= "%"+title+"%";
        PageHelper.startPage (pageNum, pageSize);
        List<Type> types = typeMapper.selectAll(title);
        PageInfo<Type> pageInfo = new PageInfo<> (types, 5);
        return  pageInfo;

    }


    @Override
    public int add(Type type) {
        return typeMapper.add(type);
    }

    @Override
    public int update(Type type) {
        return typeMapper.update(type);
    }

    @Override
    public int delete(int id) {
        return typeMapper.delete(id);
    }

    @Override
    public int deleteByMovieId(int id) {
        return typeMapper.deleteByMovieId(id);
    }

    @Override
    public int addByMovieIdTypeId(int id1, int id2) {
        return typeMapper.addByMovieIdTypeId(id1,id2);
    }


}
