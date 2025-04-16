package com.cqu.filmsystem.Service;

import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.pojo.Actor;
import com.cqu.filmsystem.pojo.Movice;
import com.cqu.filmsystem.pojo.UserRecommend;

import java.io.IOException;
import java.util.List;

public interface RecommendService {


    //基于用户的协同过滤
    List<UserRecommend>  userBrowsingHistory(String username);

    PageInfo<Movice> userPersonalizedRecommendations(String username,Long userId,int pageNum,int pageSize);

    PageInfo<Movice> popularRecommendations(int pageNum,int pageSize);


    //基于内容的协同过滤
    List<Movice> recommendMoviesForUser(Long UserId);


}
