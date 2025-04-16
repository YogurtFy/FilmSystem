package com.cqu.filmsystem.Service;

import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.pojo.Movice;
import com.cqu.filmsystem.pojo.Rating;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MoviceService {


    //基于内容的电影推荐
    List<Movice> getAllMovies();
    Map<Integer, List<Rating>> getMovieRatings();
    double calculateAdjustedCosineSimilarity(Movice movie1, Movice movie2, Map<Integer, List<Rating>> movieRatingsMap,Map<Long, List<Rating>> userRatingsMap);


    PageInfo<Movice> select(Integer pageNum, Integer pageSize) throws IOException;

    PageInfo<Movice> selectByCondition(Integer pageNum, Integer pageSize,Integer genreId , Integer regionId,String startDate,String endDate,String searchContent) throws IOException;

    public int addView(int id);

    public Movice selectByid(int id) throws IOException;


    //查询某个用户是否收藏该电影
    Movice selectFavorite(int userId , int movieId);

    //加入搜藏夹
    int insertFavorite(int userId , int movieId);

    //取消搜藏夹
    int deleteFavorite(int userId , int movieId);

    List <Movice> selectFavorites( Integer userId);

    //查询导演
    List<Movice> selectDirector();



    PageInfo<Movice> selectAll( Integer pageNum, Integer pageSize,String title, String director);

    int add(Movice movice);



    int delete(int id);


    int update(Movice blog);

    PageInfo<Movice> selectByTagId(Integer pageNum, Integer pageSize, Integer tagId);
}
