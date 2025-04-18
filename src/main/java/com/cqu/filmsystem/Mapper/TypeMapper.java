package com.cqu.filmsystem.Mapper;

import com.cqu.filmsystem.pojo.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TypeMapper {

    List<Type> selectAllType();

    List<Type>  selectByMovieId(@Param("id") Integer id) ;


    int insertMovieType(@Param("movieId") Integer movieId,
                        @Param("categoryId") Integer categoryId);

    int updateMovieType(@Param("movieId") Integer movieId,
                        @Param("categoryId") Integer categoryId);


//------------------------------------------------管理员-----------------------------------------------------------------
    Type selectById(int id );

    List<Type> selectAll(@Param("title") String title);

    int add(Type type);

    int update(Type type);

    int delete(int id);

    int deleteByMovieId(int id);

    int addByMovieIdTypeId(@Param("id1") Integer id1,
                           @Param("id2") Integer id2);
}
