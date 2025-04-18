package com.cqu.filmsystem.Service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Mapper.ActorMapper;
import com.cqu.filmsystem.Service.ActorService;
import com.cqu.filmsystem.pojo.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class ActorServiceImpl implements ActorService {

    @Autowired
    ActorMapper actorMapper;


    @Override
    public List<Actor> selectByMovieId(int id) throws IOException {
        return actorMapper.selectByMovieId(id);
    }


    @Override
    public Actor selectById(int id) {
        return actorMapper.selectById(id);
    }

    @Override
    public PageInfo<Actor> selectAll(Integer pageNum, Integer pageSize, String title) {
        title= "%"+title+"%";
        PageHelper.startPage (pageNum, pageSize);
        List<Actor> types = actorMapper.selectAll(title);
        PageInfo<Actor> pageInfo = new PageInfo<> (types, 5);
        return  pageInfo;
    }

    @Override
    public List<Actor> selectAllActor(String title) {
        title= "%"+title+"%";
        List<Actor> actors = actorMapper.selectAll(title);
        return  actors;
    }

    @Override
    public int add(Actor actor) {
        return actorMapper.add(actor);
    }

    @Override
    public int update(Actor actor) {
        return actorMapper.update(actor);
    }

    @Override
    public int delete(int id) {
        return actorMapper.delete(id);
    }


}
