package com.cqu.filmsystem.Mapper;

import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.pojo.Movice;
import com.cqu.filmsystem.pojo.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.util.List;


@Mapper
public interface TypeMapper {

    List<Type> selectAllType();

    List<Type>  selectByMoviceId(@Param("id") Integer id) ;


    int insertMoviceType(@Param("movieId") Integer movieId,
                   @Param("genresId") Integer genresId);

    int updateMoviceType(@Param("movieId") Integer movieId,
                         @Param("genresId") Integer genresId);


//------------------------------------------------管理员-----------------------------------------------------------------
    Type selectById(int id );

    List<Type> selectAll(@Param("title") String title);

    int add(Type type);

    int update(Type type);

    int delete(int id);

    int deleteByMoviceId(int id);

    int addByMoviceIdTypeId(@Param("id1") Integer id1,
                            @Param("id2") Integer id2);




}
