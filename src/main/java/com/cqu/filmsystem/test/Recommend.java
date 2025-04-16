package com.cqu.filmsystem.test;

import com.cqu.filmsystem.pojo.UserRecommend;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author junfeng.lin
 * @date 2021/3/18 13:42
 */
public class Recommend {
    /**
     * 在给定username的情况下，计算其他用户和它的距离并排序
     * @param username
     * @return
     */
    // 创建一个自定义比较器，根据值的大小从大到小排序
    Comparator<Double> comparator = (Double key1, Double key2) -> {
        // 返回负值表示 key1 应该排在 key2 的前面，正值表示 key2 应该排在 key1 的前面，0 表示相等
        return key2.compareTo(key1);
    };



    private Map<Double, String> computeNearestNeighbor(String username, List<User> users) {

        Map<Double, String> distances = new TreeMap<>(comparator);

        User u1 = new User(username);

        for (User user:users) {
            if (username.equals(user.username)) {
                u1 = user;
            }
        }

        for (int i = 0; i < users.size(); i++) {
            User u2 = users.get(i);

            if (!u2.username.equals(username)) {
                double distance = pearson_dis(u2.movieList, u1.movieList);
                distances.put(distance, u2.username);
            }
        }


        // 输出结果，按照值从大到小排序
        for (Map.Entry<Double, String> entry : distances.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("该用户与其他用户的皮尔森相关系数 -> " + distances);
        return distances;
    }


    /**
     * 计算2个打分序列间的pearson距离
     * 选择公式四进行计算
     * @param rating1
     * @param rating2
     * @return
     */
    private double pearson_dis(List<Movie> rating1, List<Movie> rating2) {
        int n=rating1.size();
        List<Integer> rating1ScoreCollect = rating1.stream().map(A -> A.score).collect(Collectors.toList());
        List<Integer> rating2ScoreCollect = rating2.stream().map(A -> A.score).collect(Collectors.toList());

        if(rating1ScoreCollect.size()>rating2ScoreCollect.size()){
            int i1 = rating1ScoreCollect.size() - rating2ScoreCollect.size();
            for (int i = 0; i < i1; i++) {
                rating2ScoreCollect.add(0);
            }
        }else if(rating1ScoreCollect.size()<rating2ScoreCollect.size()){
            int i1 = rating2ScoreCollect.size() - rating1ScoreCollect.size();
            for (int i = 0; i < i1; i++) {
                rating1ScoreCollect.add(0);
            }
        }

        n=rating2ScoreCollect.size();
        double Ex= rating1ScoreCollect.stream().mapToDouble(x->x).sum();
        double Ey= rating2ScoreCollect.stream().mapToDouble(y->y).sum();
        double Ex2=rating1ScoreCollect.stream().mapToDouble(x->Math.pow(x,2)).sum();
        double Ey2=rating2ScoreCollect.stream().mapToDouble(y->Math.pow(y,2)).sum();
        double Exy= IntStream.range(0,n).mapToDouble(i->rating1ScoreCollect.get(i)*rating2ScoreCollect.get(i)).sum();
        double numerator=Exy-Ex*Ey/n;
        double denominator=Math.sqrt((Ex2-Math.pow(Ex,2)/n)*(Ey2-Math.pow(Ey,2)/n));
        if (denominator==0){
            return 0.0;
        }
        return numerator/denominator;
    }


    public List<Movie> recommend(String username, List<User> users) {

        //找到最近邻
        Map<Double, String> distances = computeNearestNeighbor(username, users);
        String nearest = distances.values().iterator().next();
        System.out.println("最近邻 -> " + nearest);

        //找到最近邻看过，但是我们没看过的电影，计算推荐
        User neighborRatings = new User();
        for (User user:users) {
            if (nearest.equals(user.username)) {
                neighborRatings = user;
            }
        }
        System.out.println("最近邻看过的电影 -> " + neighborRatings.movieList);

        User userRatings = new User();
        for (User user:users) {
            if (username.equals(user.username)) {
                userRatings = user;
            }
        }
        System.out.println("用户看过的电影 -> " + userRatings.movieList);


        //根据自己和邻居的电影计算推荐的电影
        List<Movie> recommendationMovies = new ArrayList<>();
        for (Movie movie : neighborRatings.movieList) {
            if (userRatings.find(movie.movieName) == null) {
                recommendationMovies.add(movie);
            }
        }

        Collections.sort(recommendationMovies);
        return recommendationMovies;
    }


}

