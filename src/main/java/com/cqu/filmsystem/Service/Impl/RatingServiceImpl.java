package com.cqu.filmsystem.Service.Impl;

import com.cqu.filmsystem.Mapper.RatingMapper;
import com.cqu.filmsystem.Service.RatingService;
import com.cqu.filmsystem.pojo.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    RatingMapper ratingMapper;


    @Override
    public   Map<Long, List<Rating>> getAllUserRatings() {

        List<Rating> ratings = ratingMapper.getAllUserRatings();
        //ratings列表按电影ID进行分组，每组的键是电影ID，值是该电影ID对应的所有评分对象的列表
        return ratings.stream().collect(Collectors.groupingBy(Rating::getUserId));

    }

    @Override
    public int insert(int userId, int movieId, double rating) {
        return ratingMapper.insert(userId, movieId,rating);
    }

    @Override
    public Rating select(int userId, int movieId) {
        return ratingMapper.select(userId, movieId);
    }

    @Override
    public int update(int userId, int movieId, double rating) {
        return ratingMapper.update(userId, movieId,rating);
    }
}
