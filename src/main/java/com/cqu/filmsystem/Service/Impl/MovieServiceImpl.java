package com.cqu.filmsystem.Service.Impl;

import com.cqu.filmsystem.Mapper.TypeMapper;
import com.cqu.filmsystem.pojo.Movie;
import com.cqu.filmsystem.pojo.Type;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Mapper.MovieMapper;
import com.cqu.filmsystem.Mapper.RatingMapper;
import com.cqu.filmsystem.Service.MovieService;
import com.cqu.filmsystem.pojo.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieMapper movieMapper;

    @Autowired
    TypeMapper typeMapper;

    @Autowired
     RatingMapper ratingMapper;

    //    ------------------------------------------基于内容的协同过滤-----------------------------------------------------------------------
    @Override
    public List<Movie> getAllMovies() {
        return movieMapper.getAllMovies();
    }

    @Override
    public Map<Integer, List<Rating>> getMovieRatings() {
        List<Rating> ratings = movieMapper.getMovieRatings();
        //ratings列表按电影ID进行分组，每组的键是电影ID，值是该电影ID对应的所有评分对象的列表
        return ratings.stream().collect(Collectors.groupingBy(Rating::getMovieId));
    }

    @Override
    public double calculateAdjustedCosineSimilarity(
            Movie movie1,
            Movie movie2,
            Map<Integer, List<Rating>> movieRatingsMap,
            Map<Long, List<Rating>> userRatingsMap) {

        // 获取电影1和电影2的评分列表
        List<Rating> ratings1 = movieRatingsMap.get(movie1.getId());
        List<Rating> ratings2 = movieRatingsMap.get(movie2.getId());

        if (ratings1 == null || ratings2 == null) {
//            System.out.println("One of the movies has no ratings.");
            return 0.0;
        }

        // 共同用户的评分
        Map<Long, Double> userRatingMap1 = ratings1.stream()
                .collect(Collectors.toMap(Rating::getUserId, Rating::getRating));
        Map<Long, Double> userRatingMap2 = ratings2.stream()
                .collect(Collectors.toMap(Rating::getUserId, Rating::getRating));

        // 找到共同的用户
        Set<Long> commonUsers = new HashSet<>(userRatingMap1.keySet());
        commonUsers.retainAll(userRatingMap2.keySet());

        if (commonUsers.isEmpty()) {
//            System.out.println("No common users found.");
            return 0.0;
        }

        // 计算每个共同用户的平均评分
        Map<Long, Double> userAverageRatings = new HashMap<>();
        for (Long user : commonUsers) {
            List<Rating> userRatings = userRatingsMap.get(user);
            double avgRating = userRatings.stream()
                    .mapToDouble(Rating::getRating)
                    .average()
                    .orElse(0.0);
            userAverageRatings.put(user, avgRating);
        }

        // 调试信息：打印共同用户及其平均评分
        for (Long user : commonUsers) {
//            System.out.printf("User %d avg rating: %f\n", user, userAverageRatings.get(user));
        }

        // 计算分子和分母
        double numerator = 0.0;
        double denominator1 = 0.0;
        double denominator2 = 0.0;

        for (Long user : commonUsers) {
            double adjustedRating1 = userRatingMap1.get(user) - userAverageRatings.get(user);
            double adjustedRating2 = userRatingMap2.get(user) - userAverageRatings.get(user);

            // 调试信息：打印调整后的评分
            System.out.printf("User %d adjusted ratings: movie1=%f, movie2=%f\n", user, adjustedRating1, adjustedRating2);

            numerator += adjustedRating1 * adjustedRating2;
            denominator1 += adjustedRating1 * adjustedRating1;
            denominator2 += adjustedRating2 * adjustedRating2;
        }

        // 调试信息：打印分子和分母
//        System.out.printf("Numerator: %f\n", numerator);
//        System.out.printf("Denominator1: %f, Denominator2: %f\n", denominator1, denominator2);

        // 检查分母是否为零
        if (denominator1 == 0.0 || denominator2 == 0.0) {
//            System.out.println("Denominator is zero.");
            return 0.0;
        }

        // 计算调整后的余弦相似度
        double similarity = numerator / (Math.sqrt(denominator1) * Math.sqrt(denominator2));

        // 输出调试信息
//        System.out.printf("Similarity between movie %d and movie %d: %f\n", movie1.getId(), movie2.getId(), similarity);
        return similarity;
    }



    //    ------------------------------------------基于内容的协同过滤-----------------------------------------------------------------------

    @Override
    public PageInfo<Movie> select(Integer pageNum, Integer pageSize) throws IOException {


        PageHelper.startPage (pageNum, pageSize);
        List<Movie> select = movieMapper.select();

        // 对每部电影，查询它的类别，并设置进 movie 对象中
        for (Movie movie : select) {

            List<Type> typeList = typeMapper.selectByMovieId(movie.getId());
            movie.setTypeList(typeList);
        }

        PageInfo<Movie> pageInfo = new PageInfo<>(select, 5);
        return pageInfo;
    }

    @Override
    public PageInfo<Movie> selectByCondition(Integer pageNum, Integer pageSize, Integer categoryId, Integer regionId,
                                             String startDate, String endDate, String searchContent) throws IOException {
        PageHelper.startPage (pageNum, pageSize);
        searchContent= "%"+searchContent+"%";
        List<Movie> select = movieMapper.selectByCondition(pageNum,pageSize, categoryId,regionId,startDate,endDate,searchContent);
        PageInfo<Movie> pageInfo = new PageInfo<> (select, 5);
        return pageInfo;
    }

    @Override
    public int addView(int id) {
        return movieMapper.addView(id);
    }

    @Override
    public Movie selectByid(int id) throws IOException {
        return movieMapper.selectByid(id);
    }

    @Override
    public Movie selectFavorite(int userId, int movieId) {
        return movieMapper.selectFavorite(userId,movieId);
    }

    @Override
    public int insertFavorite(int userId, int movieId) {
        return movieMapper.insertFavorite(userId,movieId);
    }

    @Override
    public int deleteFavorite(int userId, int movieId) {
        return movieMapper.deleteFavorite(userId,movieId);
    }

    @Override
    public List<Movie> selectFavorites(Integer userId) {
      return  movieMapper.selectFavorites(userId);
    }

    @Override
    public List<Movie> selectDirector() {
        return movieMapper.selectDirector();
    }

    @Override
    public PageInfo<Movie> selectAll(Integer pageNum, Integer pageSize, String title, String director) {


        title= "%"+title+"%";
        director= "%"+director+"%";

        PageHelper.startPage (pageNum, pageSize);
        List<Movie> select = movieMapper.selectAll(title,director);
        PageInfo<Movie> pageInfo = new PageInfo<> (select, 5);

        return  pageInfo;
    }

    @Override
    public int add(Movie movie) {
        return movieMapper.add(movie);
    }


    @Override
    public int update(Movie blog) {
        return movieMapper.update(blog);
    }


    //根据标签查询电影
    @Override
    public PageInfo<Movie> selectByTagId(Integer pageNum, Integer pageSize, Integer tagId) {
        PageHelper.startPage (pageNum, pageSize);
        List<Movie> select = movieMapper.selectByTagId(tagId);
        PageInfo<Movie> pageInfo = new PageInfo<> (select, 5);
        return pageInfo;
    }

    @Override
    public int deleteMovieById(Long id) {
        int res = movieMapper.deleteById(id);
        return res;
    }
}
