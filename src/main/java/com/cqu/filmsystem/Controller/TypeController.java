package com.cqu.filmsystem.Controller;
import com.cqu.filmsystem.pojo.Movie;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Service.Impl.MovieServiceImpl;
import com.cqu.filmsystem.Service.Impl.RegionServiceImpl;
import com.cqu.filmsystem.Service.Impl.TypeServiceImpl;
import com.cqu.filmsystem.Service.MovieService;
import com.cqu.filmsystem.pojo.Region;
import com.cqu.filmsystem.pojo.Type;
import com.cqu.filmsystem.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/type")
public class TypeController {


    @Autowired
    @Qualifier("movieServiceImpl")
    MovieService movieService = new MovieServiceImpl();

    @Autowired
    @Qualifier("typeServiceImpl")
    TypeServiceImpl typeService = new TypeServiceImpl();

    @Autowired
    @Qualifier("regionServiceImpl")
    RegionServiceImpl regionService = new RegionServiceImpl();


    private static final Map<String, String[]> DECADES_MAP = new HashMap<>();
    static {
        DECADES_MAP.put("2025", new String[]{"2025-01-01", "2025-12-31"});
        DECADES_MAP.put("2024", new String[]{"2024-01-01", "2024-12-31"});
        DECADES_MAP.put("2023", new String[]{"2023-01-01", "2023-12-31"});
        DECADES_MAP.put("2022", new String[]{"2022-01-01", "2022-12-31"});
        DECADES_MAP.put("2021", new String[]{"2021-01-01", "2021-12-31"});
        DECADES_MAP.put("2020", new String[]{"2020-01-01", "2020-12-31"});
        DECADES_MAP.put("10年代", new String[]{"2010-01-01", "2019-12-31"});
        DECADES_MAP.put("00年代", new String[]{"2000-01-01", "2009-12-31"});
        DECADES_MAP.put("90年代", new String[]{"1990-01-01", "1999-12-31"});
        DECADES_MAP.put("80年代", new String[]{"1980-01-01", "1989-12-31"});
        DECADES_MAP.put("70年代", new String[]{"1970-01-01", "1979-12-31"});
        DECADES_MAP.put("60年代", new String[]{"1960-01-01", "1969-12-31"});
        DECADES_MAP.put("50年代", new String[]{"1950-01-01", "1959-12-31"});
        DECADES_MAP.put("更早的", new String[]{"0001-01-01", "1949-12-31"});
        DECADES_MAP.put("全部", new String[]{"0001-01-01","2030-12-31"});
    }



    @RequestMapping("/showAll")
    public String showAll( @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                           @RequestParam(name = "categoryId", required = false, defaultValue = "0") Integer categoryId,
                           @RequestParam(name = "year", required = false, defaultValue = "全部") String year,
                           @RequestParam(name = "regionId", required = false, defaultValue = "0") Integer regionId,
                           @RequestParam(name = "searchContent", required = false, defaultValue = "") String searchContent,
                           Model model, HttpServletRequest request)  throws IOException {


        //1.查询电影的所有分类
        List<Type> types = typeService.selectAllType();

        //2.查找电影的地区
        List<Region> regions = regionService.selectAllRegion();

        //3.对年代进行处理  确定开始和结束日期  dates[0] 开始日期   dates[1]结束日期
        String[] dates = DECADES_MAP.get(year);

        //4. 查询用户信息
        HttpSession session = request.getSession(false);
        try {
            UserInfo user = (UserInfo) session.getAttribute("user");
            model.addAttribute("user",user);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        PageInfo<Movie> PageInfo;
        if( categoryId ==0 && year.equals("全部") && regionId==0 && searchContent.equals("")) {

            //默认查询电影全部信息:
            PageInfo = movieService.select(pageNum,pageSize);

        }else
        {
            //根据条件查询电影信息
            PageInfo = movieService.selectByCondition(pageNum,pageSize, categoryId,regionId,dates[0],dates[1],searchContent);
        }
        model.addAttribute("pageInfo",PageInfo);
        model.addAttribute("types",types);
        model.addAttribute("regions",regions);
        model.addAttribute("searchContent",searchContent);

        return "types";

    }


    @RequestMapping("/showAll/search")
    public String showAllAdd( @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                              @RequestParam(name = "categoryId", required = false, defaultValue = "0") Integer categoryId,
                              @RequestParam(name = "year", required = false, defaultValue = "全部") String year,
                              @RequestParam(name = "regionId", required = false, defaultValue = "0") Integer regionId,
                              @RequestParam(name = "searchContent", required = false, defaultValue = "") String searchContent,
                              Model model)  throws IOException {

        //3.对年代进行处理  确定开始和结束日期  dates[0] 开始日期   dates[1]结束日期
        String[] dates = DECADES_MAP.get(year);

        PageInfo<Movie> PageInfo;
        if( categoryId ==0 && year.equals("全部") && regionId==0 && searchContent.equals("")) {

            //默认查询电影全部信息:
            PageInfo = movieService.select(pageNum,pageSize);

        }else
        {
            //根据条件查询电影信息
            PageInfo = movieService.selectByCondition(pageNum,pageSize, categoryId,regionId,dates[0],dates[1],searchContent);
        }

        model.addAttribute("pageInfo",PageInfo);

        return "types:: blogList";


    }







}
