package com.cqu.filmsystem.Service;

import com.cqu.filmsystem.pojo.Rating;

import java.util.List;
import java.util.Map;


public interface RatingService {


    //用于基于内容的电影推荐
    Map<Long, List<Rating>> getAllUserRatings();

    int  insert(int userId, int movieId, double rating);

    Rating select(int userId, int movieId);

    int  update(int userId, int movieId, double rating);

}
