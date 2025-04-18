package com.cqu.filmsystem.Controller.admin;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Service.Impl.ActorServiceImpl;
import com.cqu.filmsystem.Service.Impl.MovieServiceImpl;
import com.cqu.filmsystem.Service.Impl.RegionServiceImpl;
import com.cqu.filmsystem.Service.Impl.TypeServiceImpl;
import com.cqu.filmsystem.Service.MovieService;
import com.cqu.filmsystem.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Controller
@RequestMapping("/admin/movie")
public class MovieAdminController {

    @Autowired
    @Qualifier("movieServiceImpl")
    MovieService movieService = new MovieServiceImpl();

    @Autowired
    @Qualifier("typeServiceImpl")
    TypeServiceImpl typeService = new TypeServiceImpl();

    @Autowired
    @Qualifier("regionServiceImpl")
    RegionServiceImpl regionService = new RegionServiceImpl();

    @Autowired
    @Qualifier("actorServiceImpl")
    ActorServiceImpl actorService = new ActorServiceImpl();



    @RequestMapping("/showAll")
    public String select(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                         @RequestParam(name = "title", required = false, defaultValue = "") String title,
                         @RequestParam(name = "director", required = false, defaultValue = "") String director,
                         @RequestParam(name = "message", required = false, defaultValue = "") String message,
                         Model model , HttpServletRequest request) throws IOException {

        //查询所有电影
        PageInfo<Movie> select = movieService.selectAll(pageNum,pageSize,title,director);

        //查询用户信息
        HttpSession session = request.getSession(false);
        try {
            UserInfo user = (UserInfo) session.getAttribute("user");
            model.addAttribute("user",user);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        //查询所有导演
        List<Movie> directorList = movieService.selectDirector();

        model.addAttribute("pageInfo",select);
        model.addAttribute("title",title);
        model.addAttribute("director",director);
        model.addAttribute("message",message);
        model.addAttribute("directorList",directorList);
        return "movie-admin";
    }


    @RequestMapping("/showAll/search")
    public String showAllAdd(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                                   @RequestParam(name = "title", required = false, defaultValue = "") String title,
                                   @RequestParam(name = "director", required = false, defaultValue = "") String director,
                                   @RequestParam(name = "message", required = false, defaultValue = "") String message,
                                   Model model , HttpServletRequest request) throws IOException {


        //查询所有电影
        PageInfo<Movie> select = movieService.selectAll(pageNum,pageSize,title,director);
        model.addAttribute("pageInfo",select);
        model.addAttribute("title",title);
        model.addAttribute("director",director);
        model.addAttribute("message",message);
        return "movie-admin:: blogList";
    }


    // 修改页面数据查询
    @RequestMapping("/toUpdate")
    public String toUpdate(@RequestParam(name = "id") int id, Model model) throws IOException {

        //查询电影信息
        Movie movie = movieService.selectByid(id);
        model.addAttribute("movie", movie);

        //查询所有分类的
        List<Type> types = typeService.selectAllType();
        model.addAttribute("types",types);

        //查询该电影的分类
        List<Type> types1 = typeService.selectByMovieId(id);
        model.addAttribute("types1",types1);
        model.addAttribute("id",id);

        //查询所有导演
        List<Actor> actors = actorService.selectAllActor("");

        //查询所有地区
        List<Region> regions = regionService.selectAllRegion("");
        model.addAttribute("actors",actors);
        model.addAttribute("regions",regions);

        //获取电影的分类：
        List<Type> types2 = typeService.selectByMovieId(id);
        List<Integer> selectedCategoryIds = new ArrayList<>();
        for (Type type: types2)
        {
            selectedCategoryIds.add(type.getCategoryId());
        }
        model.addAttribute("selectedCategoryIds", selectedCategoryIds);
        return "movie-admin-update";
    }


    // 添加页面数据查询
    @RequestMapping("/toAdd")
    public String toAdd( Model model) throws IOException {

        //查询所有分类的
        List<Type> types = typeService.selectAllType();
        //查询所有导演
        List<Actor> actors = actorService.selectAllActor("");
        //查询所有地区
        List<Region> regions = regionService.selectAllRegion("");

        model.addAttribute("types",types);
        model.addAttribute("actors",actors);
        model.addAttribute("regions",regions);
        return "movie-admin-input";
    }

    @RequestMapping("/add")
    public String add(Movie blog, @RequestParam(name = "tagIds", required = false) List<Integer> tagIds, RedirectAttributes attributes, Model model) {


        //设置电影信息
        blog.setAverageRating(String.valueOf(0));
        blog.setReleaseDate(new Date());
        blog.setPageView(0);

        String message=null;
        String redirectUrl;

        try
        {
            //添加电影信息
            int add = movieService.add(blog);

            //查询电影id
            PageInfo<Movie> moviePageInfo = movieService.selectAll(1, 5, blog.getTitle(), blog.getDirector());


            int movieId = moviePageInfo.getList().get(0).getId();

            //插入类型
            for (int i=0 ; i<tagIds.size(); i++)
            {
                Integer categoryId = tagIds.get(i);
                typeService.insertMovieType(movieId, categoryId);
            }

            message="添加成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/movie/showAll")
                    .queryParam("message", message)
                    .toUriString();


        }catch (Exception e)
        {
            e.printStackTrace();
            message="插入失败!";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/movie/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }

        return "redirect:" + redirectUrl;
    }



    @RequestMapping("/update")
    public String update(@RequestParam(name = "id") int id, Movie blog, @RequestParam(name = "tagIds", required = false) List<Integer> tagIds, RedirectAttributes attributes) {

        String message=null;
        String redirectUrl;
        try{

            //对电影信息进行修改
            blog.setId(id);
            int update = movieService.update(blog);


            //删除全部分类
            int i1 = typeService.deleteByMovieId(id);


            //对分类进行修改
            for (int i=0 ; i<tagIds.size(); i++)
            {
                Integer categoryId = tagIds.get(i);
                typeService.addByMovieIdTypeId(id, categoryId);
            }

            message="修改成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/movie/showAll")
                    .queryParam("message", message)
                    .toUriString();

        }catch (Exception e)
        {
            e.printStackTrace();
            message="修改失败!";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/movie/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }

        return "redirect:" + redirectUrl;
    }


    @RequestMapping("/deleteById")
    public String deleteById(Long id, RedirectAttributes attributes) {

        return "redirect:";
    }


    @RequestMapping("/")
    public String toadmin() {

        return "index-admin";
    }


}


