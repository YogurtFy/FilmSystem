package com.cqu.filmsystem.Service.Impl;

import com.cqu.filmsystem.Service.ContentBasedRecommendService;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Controller.Recommend;
import com.cqu.filmsystem.Mapper.MoviceMapper;
import com.cqu.filmsystem.Mapper.ParameterMapper;
import com.cqu.filmsystem.Mapper.RatingMapper;
import com.cqu.filmsystem.Mapper.RecommendMapper;
import com.cqu.filmsystem.Service.RecommendService;
import com.cqu.filmsystem.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    RecommendMapper recommendMapper;

    @Autowired
    MoviceMapper moviceMapper;

    @Autowired
    UserServiceImpl userService;


    @Autowired
    private RatingMapper ratingMapper;

    @Autowired
    MoviceServiceImpl moviceService;

    @Autowired
    ParameterServiceImpl parameterService;

    @Autowired
    @Qualifier("ratingServiceImpl")
    RatingServiceImpl ratingService = new RatingServiceImpl();

    @Autowired
    private ContentBasedRecommendService contentBasedRecommendService;

    //基于用户的协同过滤
    @Override
    public List<UserRecommend> userBrowsingHistory(String username) {
        return recommendMapper.userBrowsingHistory(username);
    }
    @Override
    public PageInfo<Movice> userPersonalizedRecommendations(String username,Long userId,int pageNum,int pageSize) {
        //----------------------------------------------1.个性推荐-------------------------------------------------------
        //查询所有用户
        List<UserInfo> users = userService.selectAllUser();
        ArrayList<UserRecommend> userArrayList = new ArrayList<>();
        //根据用户名查询每个用户的用户名查询他的浏览记录
        for (int i = 0; i < users.size(); i++) {
            UserRecommend userRecommend = new UserRecommend();
            UserInfo user = users.get(i);
            String name = user.getNickname();

            userRecommend.setUsername(name);
            userRecommend.setId(Math.toIntExact(user.getId()));
            List<UserRecommend> userRecommends = userBrowsingHistory(name);

            List<Movice> moviceList = new ArrayList<>();
            for (int j = 0; j < userRecommends.size(); j++) {
                moviceList.add(userRecommends.get(j).getMovice());
            }
            userRecommend.setMoviceList(moviceList);
            userArrayList.add(userRecommend);
        }
        Recommend recommend = new Recommend();

        // 定义三种推荐算法的权重
        double userBasedWeight = 0.4;    // 基于用户的协同过滤权重
        double contentBasedWeight = 0.3;  // 基于内容的协同过滤权重
        double contentWeight = 0.3;       // 基于内容的推荐权重

        // 存储所有电影的最终得分
        Map<Movice, Double> finalScores = new HashMap<>();

        //1.基于用户的协同过滤推荐
        List<Movice> userBasedMovies = recommend.recommend(username,userArrayList);
        // 保留前select.getU()个元素
        Parameter select = parameterService.select();
        if (userBasedMovies.size() > select.getU()) {
            userBasedMovies = userBasedMovies.subList(0, (int) select.getU());
        }
        // 添加基于用户的协同过滤得分
        for (Movice movie : userBasedMovies) {
            finalScores.put(movie, userBasedWeight);
        }

        //2.基于电影内容的协同过滤推荐
        List<Movice> contentBasedMovies = recommendMoviesForUser(userId);
        // 添加基于内容的协同过滤得分
        for (Movice movie : contentBasedMovies) {
            if (finalScores.containsKey(movie)) {
                finalScores.put(movie, finalScores.get(movie) + contentBasedWeight);
            } else {
                finalScores.put(movie, contentBasedWeight);
            }
        }

        //3.基于内容的推荐
        List<Movice> userHistory = userBrowsingHistory(username).stream()
                .map(UserRecommend::getMovice)
                .collect(Collectors.toList());
        
        if (!userHistory.isEmpty()) {
            // 使用用户最近看过的电影作为目标电影进行基于内容的推荐
            Movice targetMovie = userHistory.get(0);
            List<Movice> contentBasedRecommendations = contentBasedRecommendService.recommendBasedOnContent(targetMovie, 10);
            
            // 添加基于内容的推荐得分
            for (Movice movie : contentBasedRecommendations) {
                if (finalScores.containsKey(movie)) {
                    finalScores.put(movie, finalScores.get(movie) + contentWeight);
                } else {
                    finalScores.put(movie, contentWeight);
                }
            }
        }

        // 根据最终得分排序
        List<Movice> recommendationMovies = finalScores.entrySet().stream()
                .sorted(Map.Entry.<Movice, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        System.out.println("-----------------------");
        System.out.println("推荐结果如下：");
        for (Movice movie : recommendationMovies) {
            System.out.println("电影："+movie.getTitle()+" ,评分："+movie.getRating()+" ,最终得分："+finalScores.get(movie));
        }

        List<Movice> pageContent = recommendationMovies.stream()
                .skip((pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
        PageInfo<Movice> pageInfo = new PageInfo<>(pageContent);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(recommendationMovies.size());

        int totalPages = (int) Math.ceil((double) recommendationMovies.size() / pageSize);
        pageInfo.setPages(totalPages);
        return pageInfo;
    }

    @Override
    public PageInfo<Movice> popularRecommendations(int pageNum, int pageSize) {
        //查询所有电影
        List<Movice> select = moviceMapper.select();

        Parameter select1 = parameterService.select();
        double B1=select1.getB1();
        double B2=select1.getB2();
        double B3=select1.getB3();
        double B4= select1.getB4();

        //计算每个电影的热度值
        for (Movice m: select) {
            int w1=m.getPageView();
            int w2=m.getCommentTime();
            int w3=m.getFavritesTime();
            double w4= m.getViewTime();

            double sum=w1*B1+w2*B2+w3*B3+w4*B4;
            m.setSum((int) sum);
        }

        select.sort(new Comparator<Movice>() {
            @Override
            public int compare(Movice o1, Movice o2) {
                if (o1.getSum() > o2.getSum()) {
                    return -1;  // o1 比 o2 大，o1 排在前面
                } else if (o1.getSum() < o2.getSum()) {
                    return 1;   // o1 比 o2 小，o1 排在后面
                } else {
                    return 0;   // o1 和 o2 相等
                }
            }
        });

        List<Movice> pageContent = select.stream()
                .skip((pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
        PageInfo<Movice> pageInfo = new PageInfo<>(pageContent);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(select.size());
        return pageInfo;

    }


    //基于内容的协同过滤
    @Override
    public List<Movice> recommendMoviesForUser(Long userId) {
        // 获取所有的电影列表
        List<Movice> allMovies = moviceService.getAllMovies();

        // 获取所有电影的评分数据，返回一个以电影ID为键，评分列表为值的Map
        Map<Integer, List<Rating>> movieRatingsMap = moviceService.getMovieRatings();

        // 获取特定用户的所有评分数据
        List<Rating> userRatings = ratingMapper.getUserRatings(userId);

        // 创建一个Map来存储每部电影的相似度得分
        Map<Movice, Double> similarityMap = new HashMap<>();

        // 寻找所有用户的所有评分数据
        Map<Long, List<Rating>> allUserRatings = ratingService.getAllUserRatings();

        // 遍历用户的评分列表
        for (Rating userRating : userRatings) {
            // 查找用户评分对应的电影
            Movice ratedMovie = allMovies.stream()
                    .filter(movie -> movie.getId() == userRating.getMovieId())
                    .findFirst()
                    .orElse(null);

            if (ratedMovie != null) {  // 确保找到了电影
                // 遍历所有电影
                for (Movice movie : allMovies) {
                    // 排除用户已经评分过的电影
                    if (!movie.equals(ratedMovie)) {
                        // 计算用户评分过的电影与当前电影之间的相似度
                        double similarity = moviceService.calculateAdjustedCosineSimilarity(ratedMovie, movie, movieRatingsMap,allUserRatings);

                        // 将相似度和用户评分乘积累加到相似度Map中
                        similarityMap.put(movie, similarityMap.getOrDefault(movie, 0.0) + similarity * userRating.getRating());
                    }
                }
            }
        }

        // 将Map的条目放入一个List中
        List<Map.Entry<Movice, Double>> entryList = new ArrayList<>(similarityMap.entrySet());

        // 对List进行排序
        entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // 将排序结果放回一个新的LinkedHashMap中
        Map<Movice, Double> sortedByValue = new LinkedHashMap<>();
        for (Map.Entry<Movice, Double> entry : entryList) {
            sortedByValue.put(entry.getKey(), entry.getValue());
        }

        // 打印排序后的Map
        sortedByValue.forEach((movie, similarity) ->
                System.out.println("Movie: " + movie.getTitle() + ", Similarity: " + similarity));


        double c = parameterService.select().getC();
        //返回相似度大于c的电影
        return similarityMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() > c)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        // 对相似度Map按值进行降序排序，并取前10个相似度最高的电影
//        return similarityMap.entrySet().stream()
//                .sorted(Map.Entry.<Movice, Double>comparingByValue().reversed())
//                .limit(10)
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());
    }
}

