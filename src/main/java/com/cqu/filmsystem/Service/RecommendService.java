package com.cqu.filmsystem.Service;

import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.pojo.Movie;
import com.cqu.filmsystem.pojo.UserRecommend;

import java.util.List;

public interface RecommendService {


    //基于用户的协同过滤
    List<UserRecommend>  userBrowsingHistory(String username);

    PageInfo<Movie> userPersonalizedRecommendations(String username, Long userId, int pageNum, int pageSize);

    PageInfo<Movie> popularRecommendations(int pageNum, int pageSize);


    //基于内容的协同过滤
    List<Movie> recommendMoviesForUser(Long UserId);


}
