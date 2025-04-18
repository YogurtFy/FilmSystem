package com.cqu.filmsystem.Mapper;

import com.cqu.filmsystem.pojo.Movie;
import com.cqu.filmsystem.pojo.Rating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface MovieMapper {

    List<Movie> select();

    List<Movie> selectByCondition(
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize,
            @Param("categoryId") Integer categoryId,
            @Param("regionId") Integer regionId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("searchContent") String searchContent);


    // 浏览量添加1
    public int addView(int id);

    //电影播放详情页
    Movie selectByid(int id);

    //查询某个用户是否收藏该电影
    Movie selectFavorite(@Param("userId") Integer userId,
                         @Param("movieId") Integer movieId);

    //加入搜藏夹
    int insertFavorite(
     @Param("userId") Integer userId,
     @Param("movieId") Integer movieId);

    //取消搜藏夹
    int deleteFavorite(@Param("userId") Integer userId,
                       @Param("movieId") Integer movieId);

    List <Movie> selectFavorites(@Param("userId") Integer userId);


    List<Movie> selectDirector();

    List<Movie> selectAll(@Param("title") String title,
                          @Param("director") String director);

    int add(Movie movie);


    int update(Movie movie);

    int delete(int id);


    List<Movie> selectByTagId(Integer tagId);

    //基于内容的协同过滤
    //查询所有电影
    List<Movie> getAllMovies();
    List<Rating> getMovieRatings();






}
