package com.cqu.filmsystem.Controller.admin;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Service.Impl.MovieServiceImpl;
import com.cqu.filmsystem.Service.Impl.RegionServiceImpl;
import com.cqu.filmsystem.Service.Impl.TypeServiceImpl;
import com.cqu.filmsystem.Service.MovieService;
import com.cqu.filmsystem.pojo.Region;
import com.cqu.filmsystem.pojo.UserInfo;
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



@Controller
@RequestMapping("/admin/region")
public class RegionAdminController {


    @Autowired
    @Qualifier("movieServiceImpl")
    MovieService movieService = new MovieServiceImpl();

    @Autowired
    @Qualifier("typeServiceImpl")
    TypeServiceImpl typeService = new TypeServiceImpl();

    @Autowired
    @Qualifier("regionServiceImpl")
    RegionServiceImpl regionService= new RegionServiceImpl();


    @RequestMapping("/showAll")
    public String select(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                         @RequestParam(name = "title", required = false, defaultValue = "") String title,
                         @RequestParam(name = "message", required = false, defaultValue = "") String message,
                         Model model , HttpServletRequest request) throws IOException {

        //查询所有地区
        PageInfo<Region> select = regionService.selectAll(pageNum, pageSize, title);

        //查询用户信息
        HttpSession session = request.getSession(false);
        try {
            UserInfo user = (UserInfo) session.getAttribute("user");
            model.addAttribute("user",user);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        model.addAttribute("pageInfo",select);
        model.addAttribute("title",title);
        model.addAttribute("message",message);
        return "region-admin";
    }


    @RequestMapping("/showAll/search")
    public String showAllAdd(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                                   @RequestParam(name = "title", required = false, defaultValue = "") String title,
                                   @RequestParam(name = "message", required = false, defaultValue = "") String message,
                                   Model model , HttpServletRequest request) throws IOException {

        //查询所有标签
        PageInfo<Region> select = regionService.selectAll(pageNum,pageSize,title);

        model.addAttribute("pageInfo",select);
        model.addAttribute("title",title);
        model.addAttribute("message",message);
        return "region-admin:: blogList";
    }


    // 修改页面数据查询
    @RequestMapping("/toUpdate")
    public String toUpdate(@RequestParam(name = "id") int id, Model model) throws IOException {


        //查询分类信息
        Region region = regionService.selectById(id);
        model.addAttribute("region",region);

        return "region-admin-update";
    }


    // 添加页面数据查询
    @RequestMapping("/toAdd")
    public String toAdd( Model model) throws IOException {


        return "region-admin-input";
    }

    @RequestMapping("/add")
    public String add(Region region, RedirectAttributes attributes,Model model) {


        String message=null;
        String redirectUrl;

        try
        {
            //添加电影信息
            int add = regionService.add(region);

            message="添加成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/region/showAll")
                    .queryParam("message", message)
                    .toUriString();

        }catch (Exception e)
        {
            e.printStackTrace();
            message="插入失败!";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/region/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }

        return "redirect:" + redirectUrl;
    }








    @RequestMapping("/update")
    public String update(@RequestParam(name = "id") int id,Region region, RedirectAttributes attributes) {


        String message=null;
        String redirectUrl;
        try{

            //对分类信息进行修改
            region.setRegionId(id);
            int update = regionService.update(region);

            message="修改成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/region/showAll")
                    .queryParam("message", message)
                    .toUriString();

        }catch (Exception e)
        {
            e.printStackTrace();
            message="修改失败!";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/region/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }

        return "redirect:" + redirectUrl;
    }


    @RequestMapping("/deleteById")
    public String deleteById(Long id, RedirectAttributes attributes) {

        String message=null;
        String redirectUrl;

        try
        {
            //删除
            int delete = regionService.delete(Math.toIntExact(id));
            message="删除成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/region/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }catch (Exception e)
        {
            e.printStackTrace();
            message="删除失败!";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/region/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }
        return "redirect:" + redirectUrl;
    }



}


