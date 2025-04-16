package com.cqu.filmsystem.Service;

import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.pojo.Actor;
import com.cqu.filmsystem.pojo.Type;

import java.io.IOException;
import java.util.List;


public interface ActorService {



    List<Actor>  selectByMoviceId(int id) throws  IOException;


    //-----------------------------------------------管理员------------------------------------------------------------------
    Actor selectById(int id );

    PageInfo<Actor> selectAll(Integer pageNum, Integer pageSize, String title);

    List<Actor> selectAllActor(String title);



    int add(Actor actor);

    int update(Actor actor);

    int delete(int id);

}
