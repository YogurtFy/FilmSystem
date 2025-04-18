package com.cqu.filmsystem.Service;

import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.pojo.Type;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.util.List;


public interface TypeService {

    //1. 插叙所有分类
    List<Type> selectAllType() throws IOException;


    //2.根据电影ID查询类型
    List<Type>  selectByMovieId(int id) throws  IOException;



    int insertMovieType(@Param("movieId") Integer movieId,
                        @Param("categoryId") Integer categoryId);


    int updateMovieType(@Param("movieId") Integer movieId,
                        @Param("categoryId") Integer categoryId);



//-----------------------------------------------管理员------------------------------------------------------------------
    Type selectById(int id );

    PageInfo<Type> selectAll(Integer pageNum, Integer pageSize, String title);

    int add(Type type);

    int update(Type type);

    int delete(int id);

    int deleteByMovieId(int id);

    int addByMovieIdTypeId(int id1, int id2);

}
