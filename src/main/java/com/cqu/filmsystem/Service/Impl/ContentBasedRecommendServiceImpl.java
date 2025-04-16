package com.cqu.filmsystem.Service.Impl;

import com.cqu.filmsystem.Service.ContentBasedRecommendService;
import com.cqu.filmsystem.pojo.Movice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContentBasedRecommendServiceImpl implements ContentBasedRecommendService {

    @Autowired
    private MoviceServiceImpl moviceService;

    @Override
    public List<Movice> recommendBasedOnContent(Movice targetMovie, int numRecommendations) {
        // 获取所有电影
        List<Movice> allMovies = moviceService.getAllMovies();
        
        // 计算目标电影与其他所有电影的相似度
        Map<Movice, Double> similarityScores = new HashMap<>();
        
        for (Movice movie : allMovies) {
            if (movie.getId() != targetMovie.getId()) {
                double similarity = calculateContentSimilarity(targetMovie, movie);
                similarityScores.put(movie, similarity);
            }
        }
        
        // 按相似度排序并返回前N个推荐
        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.<Movice, Double>comparingByValue().reversed())
                .limit(numRecommendations)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private double calculateContentSimilarity(Movice movie1, Movice movie2) {
        double similarity = 0.0;
        int featureCount = 0;
        
        // 1. 类型相似度
        if (movie1.getGenre() != null && movie2.getGenre() != null) {
            Set<String> genres1 = new HashSet<>(Arrays.asList(movie1.getGenre().split(",")));
            Set<String> genres2 = new HashSet<>(Arrays.asList(movie2.getGenre().split(",")));
            similarity += calculateJaccardSimilarity(genres1, genres2);
            featureCount++;
        }
        
        // 2. 导演相似度
        if (movie1.getDirector() != null && movie2.getDirector() != null) {
            if (movie1.getDirector().equals(movie2.getDirector())) {
                similarity += 1.0;
            }
            featureCount++;
        }
        
        // 3. 语言相似度
        if (movie1.getLanguage() != null && movie2.getLanguage() != null) {
            if (movie1.getLanguage().equals(movie2.getLanguage())) {
                similarity += 1.0;
            }
            featureCount++;
        }
        
        // 4. 时长相似度（归一化处理）
        if (movie1.getRuntime() > 0 && movie2.getRuntime() > 0) {
            double runtimeDiff = Math.abs(movie1.getRuntime() - movie2.getRuntime());
            double maxRuntime = Math.max(movie1.getRuntime(), movie2.getRuntime());
            similarity += 1.0 - (runtimeDiff / maxRuntime);
            featureCount++;
        }
        
        // 5. 地区相似度
        if (movie1.getRegionId() == movie2.getRegionId()) {
            similarity += 1.0;
            featureCount++;
        }
        
        // 返回平均相似度
        return featureCount > 0 ? similarity / featureCount : 0.0;
    }

    private double calculateJaccardSimilarity(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
} 