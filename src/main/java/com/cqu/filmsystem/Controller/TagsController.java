package com.cqu.filmsystem.Controller;

import com.cqu.filmsystem.Mapper.TypeMapper;
import com.cqu.filmsystem.Service.Impl.TypeServiceImpl;
import com.cqu.filmsystem.pojo.Movie;
import com.cqu.filmsystem.pojo.Type;
import com.github.pagehelper.PageInfo;

import com.cqu.filmsystem.Service.Impl.MovieServiceImpl;
import com.cqu.filmsystem.Service.Impl.TagServiceImpl;
import com.cqu.filmsystem.Service.MovieService;
import com.cqu.filmsystem.Service.TagService;
import com.cqu.filmsystem.pojo.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/tag")
public class TagsController {


    @Autowired
    @Qualifier("movieServiceImpl")
    MovieService movieService = new MovieServiceImpl();

    @Autowired
    @Qualifier("typeServiceImpl")
    TypeServiceImpl typeService = new TypeServiceImpl();

    @Autowired
    @Qualifier("tagServiceImpl")
    TagService tagService= new TagServiceImpl();



    @RequestMapping("/showAll")
    public String showAll( @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                           @RequestParam(name = "tagId", required = false, defaultValue = "0") Integer tagId,
                           @RequestParam(name = "searchContent", required = false, defaultValue = "") String searchContent,
                           Model model)  throws IOException {
        PageInfo<Movie> PageInfo;
        //查询博客信息:
        if(tagId==0)
        {
            // 查询博客 首页
            PageInfo = movieService.select(pageNum, pageSize);
        }
        else
        {
            // 根据tagId查询  首页
            PageInfo = movieService.selectByTagId(pageNum,pageSize,tagId);
        }

        model.addAttribute("pageInfo",PageInfo);

        Map<Integer, List<Type>> movieTypesMap = new HashMap<>();
        for (Movie movie : PageInfo.getList()) {
            List<Type> typeList = typeService.selectByMovieId(movie.getId()); // 查询该电影的所有分类
            movieTypesMap.put(movie.getId(), typeList);
        }
        model.addAttribute("movieTypesMap", movieTypesMap); // 前端用这个展示标签

        //查询标签：
        List<Tag> tags = tagService.selectAllTagsWithCount();
        model.addAttribute("tags",tags);
        model.addAttribute("tagId",tagId);
        return "tags";

    }


    @RequestMapping("/showAll/search")
    public String showAllAdd( @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                           @RequestParam(name = "tagId", required = false, defaultValue = "0") Integer tagId,
                           Model model)  throws IOException {



        PageInfo<Movie> PageInfo;
        //查询博客信息:
        //查询博客信息:
        if(tagId==0)
        {
            // 查询博客 首页
            PageInfo = movieService.select(pageNum, pageSize);
        }
        else
        {
            // 根据tagId查询  首页
            PageInfo = movieService.selectByTagId(pageNum,pageSize,tagId);
        }
        Map<Integer, List<Type>> movieTypesMap = new HashMap<>();
        for (Movie movie : PageInfo.getList()) {
            List<Type> typeList = typeService.selectByMovieId(movie.getId()); // 查询该电影的所有分类
            movieTypesMap.put(movie.getId(), typeList);
        }
        model.addAttribute("movieTypesMap", movieTypesMap); // 前端用这个展示标签

        model.addAttribute("pageInfo",PageInfo);
        model.addAttribute("tagId",tagId);
        return "tags:: blogList";
    }

}
