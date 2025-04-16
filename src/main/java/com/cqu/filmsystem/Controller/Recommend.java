package com.cqu.filmsystem.Controller;

import com.cqu.filmsystem.pojo.Movice;
import com.cqu.filmsystem.pojo.UserRecommend;

import java.util.*;
import java.util.stream.Collectors;

public class Recommend {

    // 创建一个自定义比较器，根据值的大小从大到小排序
    Comparator<Double> comparator = (Double key1, Double key2) -> {
        return key2.compareTo(key1);
    };

    private static Movice findMovieInList(List<Movice> movieList, String title) {
        for (Movice movie : movieList) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null; // 如果没有找到，返回null
    }

    /**
     * 在给定username的情况下，计算其他用户和它的距离并排序
     * @param username
     * @return
     */
    private Map<Double, String> computeNearestNeighbor(String username, List<UserRecommend> users) {
        Map<Double, String> distances = new TreeMap<>(comparator);
        UserRecommend u1 = new UserRecommend(username);
        for (UserRecommend user : users) {
            if (username.equals(user.getUsername())) {
                u1 = user;
            }
        }

        for (int i = 0; i < users.size(); i++) {
            UserRecommend u2 = users.get(i);
            String u2Username = u2.getUsername();

            // 空值判断，避免 NullPointerException
            if (u2Username != null && !u2Username.equals(username)) {
                double distance = pearson_dis(u2.getMoviceList(), u1.getMoviceList());
                distances.put(distance, u2Username);
            }
        }

        return distances;
    }

    /**
     * 计算2个打分序列间的pearson距离
     * @param user1Ratings
     * @param user2Ratings
     * @return
     */
    private double pearson_dis(List<Movice> user1Ratings, List<Movice> user2Ratings) {
        if (user1Ratings == null || user2Ratings == null || user1Ratings.isEmpty() || user2Ratings.isEmpty()) {
            return 0; // 处理空列表的情况
        }

        Map<String, Double> user2RatingsMap = user2Ratings.stream()
                .filter(movie -> movie.getTitle() != null)  // 过滤掉标题为 null 的电影
                .collect(Collectors.toMap(Movice::getTitle, Movice::getRating));

        List<Movice> commonMovies = user1Ratings.stream()
                .filter(movie1 -> user2RatingsMap.containsKey(movie1.getTitle()))
                .collect(Collectors.toList());

        if (commonMovies.isEmpty()) {
            return 0; // 如果没有共同的电影，返回0
        }

        double sumXY = commonMovies.stream()
                .mapToDouble(movie1 -> movie1.getRating() * user2RatingsMap.get(movie1.getTitle()))
                .sum();

        double sumX = commonMovies.stream()
                .mapToDouble(Movice::getRating)
                .sum();

        double sumY = commonMovies.stream()
                .mapToDouble(movie1 -> user2RatingsMap.get(movie1.getTitle()))
                .sum();

        double sumX2 = commonMovies.stream()
                .mapToDouble(movie1 -> Math.pow(movie1.getRating(), 2))
                .sum();

        double sumY2 = commonMovies.stream()
                .mapToDouble(movie1 -> Math.pow(user2RatingsMap.get(movie1.getTitle()), 2))
                .sum();

        int n = commonMovies.size();

        double denominator = Math.sqrt((sumX2 - Math.pow(sumX, 2) / n) * (sumY2 - Math.pow(sumY, 2) / n));
        if (denominator == 0) {
            return 0; // 如果分母为0，返回0
        }

        return (sumXY - (sumX * sumY) / n) / denominator;
    }

    public List<Movice> recommend(String username, List<UserRecommend> users) {
        // 找到最近邻
        Map<Double, String> distances = computeNearestNeighbor(username, users);
        String nearest = distances.values().iterator().next();
        String secondObject = "";
        Iterator<String> iterator = distances.values().iterator();
        if (iterator.hasNext()) {
            nearest = iterator.next();
        }
        if (iterator.hasNext()) {
            secondObject = iterator.next();
        }

        // 找到最近邻和第二邻近的用户
        UserRecommend neighborRatings1 = new UserRecommend();
        UserRecommend neighborRatings2 = new UserRecommend();
        for (UserRecommend user : users) {
            if (nearest.equals(user.getUsername())) {
                neighborRatings1 = user;
            }
            if (secondObject.equals(user.getUsername())) {
                neighborRatings2 = user;
            }
        }

        // 找到当前用户的电影
        UserRecommend userRatings = new UserRecommend();
        for (UserRecommend user : users) {
            if (username.equals(user.getUsername())) {
                userRatings = user;
            }
        }

        // 推荐电影列表和去重集合
        List<Movice> recommendationMovies = new ArrayList<>();
        Map<String, Movice> movieTitles = new HashMap<>();
        Set<String> recommendedMovieTitles = new HashSet<>();

        // 优先推荐最近邻用户看的电影
        for (Movice movie : neighborRatings1.getMoviceList()) {
            if (userRatings.find(movie.getTitle()) == null && !recommendedMovieTitles.contains(movie.getTitle())) {
                recommendationMovies.add(movie);
                recommendedMovieTitles.add(movie.getTitle());
            }
        }

        // 推荐第二邻居用户看的电影
        List<Movice> neighborRatings2Movies = neighborRatings2.getMoviceList();
        if (neighborRatings2Movies == null) {
            neighborRatings2Movies = new ArrayList<>(); // 如果为 null，则初始化为空列表
        }

        for (Movice movie : neighborRatings2Movies) {
            if (userRatings.find(movie.getTitle()) == null && !recommendedMovieTitles.contains(movie.getTitle())) {
                recommendationMovies.add(movie);
                recommendedMovieTitles.add(movie.getTitle());
            }
        }

        // 然后推荐其他没有在邻居电影中出现的电影
        for (UserRecommend user : users) {
            String uName = user.getUsername();

            if (uName != null && !uName.equals(username)) {
                List<Movice> movieList = user.getMoviceList();
                if (movieList != null) {
                    for (Movice movie : movieList) {
                        String title = movie.getTitle();

                        if (title != null &&
                                userRatings.find(title) == null &&
                                !recommendedMovieTitles.contains(title)) {

                            recommendationMovies.add(movie);
                            recommendedMovieTitles.add(title);
                        }
                    }
                }
            }
        }


        // 返回推荐的电影，已按优先级排序
        Collections.sort(recommendationMovies);
        return recommendationMovies;
    }
}
