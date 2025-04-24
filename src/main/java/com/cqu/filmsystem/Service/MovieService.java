package com.cqu.filmsystem.Service;

import com.cqu.filmsystem.pojo.Movie;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.pojo.Rating;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MovieService {


    //基于内容的电影推荐
    List<Movie> getAllMovies();
    Map<Integer, List<Rating>> getMovieRatings();
    double calculateAdjustedCosineSimilarity(Movie movie1, Movie movie2, Map<Integer, List<Rating>> movieRatingsMap, Map<Long, List<Rating>> userRatingsMap);


    PageInfo<Movie> select(Integer pageNum, Integer pageSize) throws IOException;

    PageInfo<Movie> selectByCondition(Integer pageNum, Integer pageSize, Integer categoryId, Integer regionId, String startDate, String endDate, String searchContent) throws IOException;

    public int addView(int id);

    public Movie selectByid(int id) throws IOException;


    //查询某个用户是否收藏该电影
    Movie selectFavorite(int userId , int movieId);

    //加入搜藏夹
    int insertFavorite(int userId , int movieId);

    //取消搜藏夹
    int deleteFavorite(int userId , int movieId);

    List <Movie> selectFavorites(Integer userId);

    //查询导演
    List<Movie> selectDirector();

    PageInfo<Movie> selectAll(Integer pageNum, Integer pageSize, String title, String director);

    int add(Movie movie);

    int update(Movie blog);

    PageInfo<Movie> selectByTagId(Integer pageNum, Integer pageSize, Integer tagId);

    int deleteMovieById(Long id);
}
