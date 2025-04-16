package com.cqu.filmsystem.Mapper;

import com.cqu.filmsystem.pojo.Actor;
import com.cqu.filmsystem.pojo.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface ActorMapper {

    List<Actor>  selectByMoviceId(@Param("id") Integer id) ;


    List<Actor> selectAllActor();

    //------------------------------------------------管理员-----------------------------------------------------------------
    Actor selectById(int id );

    List<Actor> selectAll(@Param("title") String title);

    int add(Actor actor);

    int update(Actor actor);

    int delete(int id);

}
