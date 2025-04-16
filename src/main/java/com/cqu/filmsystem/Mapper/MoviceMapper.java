package com.cqu.filmsystem.Mapper;

import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.pojo.Movice;
import com.cqu.filmsystem.pojo.Rating;
import com.cqu.filmsystem.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface MoviceMapper {

    List<Movice> select();

    List<Movice> selectByCondition(
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize,
            @Param("genreId") Integer genreId,
            @Param("regionId") Integer regionId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("searchContent") String searchContent);


    // 浏览量添加1
    public int addView(int id);

    //电影播放详情页
    Movice selectByid(int id);

    //查询某个用户是否收藏该电影
    Movice selectFavorite(   @Param("userId") Integer userId,
                             @Param("movieId") Integer movieId);

    //加入搜藏夹
    int insertFavorite(
     @Param("userId") Integer userId,
     @Param("movieId") Integer movieId);

    //取消搜藏夹
    int deleteFavorite(@Param("userId") Integer userId,
                       @Param("movieId") Integer movieId);

    List <Movice> selectFavorites(@Param("userId") Integer userId);


    List<Movice> selectDirector();

    List<Movice> selectAll(@Param("title") String title,
                           @Param("director") String director);

    int add(Movice movice);


    int update(Movice movice);

    int delete(int id);


    List<Movice> selectByTagId(Integer tagId);

    //基于内容的协同过滤
    //查询所有电影
    List<Movice> getAllMovies();
    List<Rating> getMovieRatings();






}
