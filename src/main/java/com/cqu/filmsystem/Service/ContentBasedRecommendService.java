package com.cqu.filmsystem.Service;

import com.cqu.filmsystem.pojo.Movice;
import java.util.List;

public interface ContentBasedRecommendService {
    /**
     * 基于电影内容特征进行推荐
     * @param targetMovie 目标电影
     * @param numRecommendations 推荐数量
     * @return 推荐的电影列表
     */
    List<Movice> recommendBasedOnContent(Movice targetMovie, int numRecommendations);
} 