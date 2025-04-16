package com.cqu.filmsystem.Service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Mapper.MoviceMapper;
import com.cqu.filmsystem.Mapper.RatingMapper;
import com.cqu.filmsystem.Service.MoviceService;
import com.cqu.filmsystem.pojo.Movice;
import com.cqu.filmsystem.pojo.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class MoviceServiceImpl implements MoviceService {

    @Autowired
     MoviceMapper moviceMapper;

    @Autowired
     RatingMapper ratingMapper;

    //    ------------------------------------------基于内容的协同过滤-----------------------------------------------------------------------
    @Override
    public List<Movice> getAllMovies() {
        return moviceMapper.getAllMovies();
    }

    @Override
    public Map<Integer, List<Rating>> getMovieRatings() {
        List<Rating> ratings = moviceMapper.getMovieRatings();
        //ratings列表按电影ID进行分组，每组的键是电影ID，值是该电影ID对应的所有评分对象的列表
        return ratings.stream().collect(Collectors.groupingBy(Rating::getMovieId));
    }

    @Override
    public double calculateAdjustedCosineSimilarity(
            Movice movie1,
            Movice movie2,
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
    public PageInfo<Movice> select(Integer pageNum, Integer pageSize) throws IOException {


        PageHelper.startPage (pageNum, pageSize);
        List<Movice> select = moviceMapper.select();
        PageInfo<Movice> pageInfo = new PageInfo<> (select, 5);
        return pageInfo;
    }

    @Override
    public PageInfo<Movice> selectByCondition(Integer pageNum, Integer pageSize, Integer genreId, Integer regionId,
                                              String startDate, String endDate,String searchContent) throws IOException {
        PageHelper.startPage (pageNum, pageSize);
        searchContent= "%"+searchContent+"%";
        List<Movice> select = moviceMapper.selectByCondition(pageNum,pageSize,genreId,regionId,startDate,endDate,searchContent);
        PageInfo<Movice> pageInfo = new PageInfo<> (select, 5);
        return pageInfo;
    }

    @Override
    public int addView(int id) {
        return moviceMapper.addView(id);
    }

    @Override
    public Movice selectByid(int id) throws IOException {
        return moviceMapper.selectByid(id);
    }

    @Override
    public Movice selectFavorite(int userId, int movieId) {
        return moviceMapper.selectFavorite(userId,movieId);
    }

    @Override
    public int insertFavorite(int userId, int movieId) {
        return moviceMapper.insertFavorite(userId,movieId);
    }

    @Override
    public int deleteFavorite(int userId, int movieId) {
        return moviceMapper.deleteFavorite(userId,movieId);
    }

    @Override
    public List<Movice> selectFavorites(Integer userId) {
      return  moviceMapper.selectFavorites(userId);
    }

    @Override
    public List<Movice> selectDirector() {
        return moviceMapper.selectDirector();
    }

    @Override
    public PageInfo<Movice> selectAll(Integer pageNum, Integer pageSize,String title, String director) {


        title= "%"+title+"%";
        director= "%"+director+"%";

        PageHelper.startPage (pageNum, pageSize);
        List<Movice> select = moviceMapper.selectAll(title,director);
        PageInfo<Movice> pageInfo = new PageInfo<> (select, 5);

        return  pageInfo;
    }

    @Override
    public int add(Movice movice) {
        return moviceMapper.add(movice);
    }



    @Override
    public int delete(int id) {
        return 0;
    }

    @Override
    public int update(Movice blog) {
        return moviceMapper.update(blog);
    }


    //根据标签查询电影
    @Override
    public PageInfo<Movice> selectByTagId(Integer pageNum, Integer pageSize, Integer tagId) {
        PageHelper.startPage (pageNum, pageSize);
        List<Movice> select = moviceMapper.selectByTagId(tagId);
        PageInfo<Movice> pageInfo = new PageInfo<> (select, 5);
        return pageInfo;
    }
}
