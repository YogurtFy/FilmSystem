package com.cqu.filmsystem.Mapper;

import com.cqu.filmsystem.pojo.Rating;
import com.cqu.filmsystem.pojo.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;



@Mapper
public interface RatingMapper {



    int  insert(@Param("userId") int userId,
                @Param("moviceId") int moviceId,
                @Param("rating") double rating);

    Rating select(@Param("userId") int userId,
                  @Param("moviceId") int moviceId);

    int update(@Param("userId") int userId,
               @Param("moviceId") int moviceId,
               @Param("rating") double rating);


    //基于内容的协同过滤
    List<Rating> getUserRatings(@Param("userId") Long userId);

    List<Rating> getAllUserRatings();





}
