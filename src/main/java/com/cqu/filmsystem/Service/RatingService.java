package com.cqu.filmsystem.Service;

import com.cqu.filmsystem.pojo.Movice;
import com.cqu.filmsystem.pojo.Rating;
import com.cqu.filmsystem.pojo.Type;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface RatingService {


    //用于基于内容的电影推荐
    Map<Long, List<Rating>> getAllUserRatings();

    int  insert(int userId, int moviceId, double rating);

    Rating select(int userId, int moviceId);

    int  update(int userId, int moviceId, double rating);


}
