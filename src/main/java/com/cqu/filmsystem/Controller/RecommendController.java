package com.cqu.filmsystem.Controller;


import com.cqu.filmsystem.pojo.Movie;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Service.Impl.RecommendServiceImpl;
import com.cqu.filmsystem.Service.Impl.UserServiceImpl;
import com.cqu.filmsystem.pojo.UserInfo;
import com.cqu.filmsystem.pojo.UserRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/test")
public class RecommendController {

    @Autowired
    @Qualifier("recommendServiceImpl")
    RecommendServiceImpl recommendService = new RecommendServiceImpl();

    @Autowired
    @Qualifier("userServiceImpl")
    UserServiceImpl userService = new UserServiceImpl();



    @RequestMapping("/recommend")
    public void test() {
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

            //根据姓名查询其用户的使用历史
            List<UserRecommend> userRecommends = recommendService.userBrowsingHistory(name);

            List<Movie> movieList = new ArrayList<>();
            for (int j = 0; j < userRecommends.size(); j++) {
                movieList.add(userRecommends.get(j).getMovie());
            }
            userRecommend.setMovieList(movieList);
            userArrayList.add(userRecommend);
        }
        Recommend recommend = new Recommend();
        List<Movie> recommendationMovies = recommend.recommend("一有",userArrayList);
        System.out.println("-----------------------");
        System.out.println("推荐结果如下：");
        for (Movie movie : recommendationMovies) {
//            System.out.println("电影："+movie.getTitle()+" ,评分："+movie.getRating());
            System.out.println(movie);
        }
        for(int i=0 ; i<16 ; i++ )
        {
            recommendationMovies.add(recommendationMovies.get(0));
        }
        int pageNum = 1; // 第一页
        int pageSize = 5; // 每页5条
        List<Movie> pageContent = recommendationMovies.stream()
                .skip((pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
        PageInfo<Movie> pageInfo = new PageInfo<>(pageContent);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(recommendationMovies.size());

    }


    @RequestMapping("/recommend1")
    public void recommendMovies() {
        List<Movie> movieList = recommendService.recommendMoviesForUser(1L);
        for (Movie m: movieList) {
            System.out.println(m);
        }
    }


}
