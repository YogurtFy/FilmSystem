package com.cqu.filmsystem.Controller.admin;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Service.Impl.MovieServiceImpl;
import com.cqu.filmsystem.Service.Impl.TypeServiceImpl;
import com.cqu.filmsystem.Service.Impl.UserServiceImpl;
import com.cqu.filmsystem.Service.MovieService;
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
import java.util.List;



@Controller
@RequestMapping("/admin/user")
public class UserAdminController {


    @Autowired
    @Qualifier("movieServiceImpl")
    MovieService movieService = new MovieServiceImpl();

    @Autowired
    @Qualifier("typeServiceImpl")
    TypeServiceImpl typeService = new TypeServiceImpl();

    @Autowired
    @Qualifier("userServiceImpl")
    UserServiceImpl userService = new UserServiceImpl();


    @RequestMapping("/showAll")
    public String select(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                         @RequestParam(name = "title", required = false, defaultValue = "") String title,
                         @RequestParam(name = "message", required = false, defaultValue = "") String message,
                         Model model , HttpServletRequest request) throws IOException {

        //查询所有用户
        PageInfo<UserInfo> select = userService.selectAll(pageNum,pageSize,title);

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
        return "user-admin";
    }


    @RequestMapping("/showAll/search")
    public String showAllAdd(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                                   @RequestParam(name = "title", required = false, defaultValue = "") String title,
                                   @RequestParam(name = "message", required = false, defaultValue = "") String message,
                                   Model model , HttpServletRequest request) throws IOException {


        //查询所有电影
        PageInfo<UserInfo> select = userService.selectAll(pageNum,pageSize,title);
        model.addAttribute("pageInfo",select);
        model.addAttribute("title",title);
        model.addAttribute("message",message);
        return "user-admin:: blogList";
    }


    // 修改页面数据查询
    @RequestMapping("/toUpdate")
    public String toUpdate(@RequestParam(name = "id") Long id, Model model) throws IOException {

        //查用用户信息
        UserInfo userInfo = userService.selectById(Math.toIntExact(id));
        model.addAttribute("user",userInfo);

        return "user-admin-update";
    }





    @RequestMapping("/update")
    public String update(@RequestParam(name = "id") int id,UserInfo userInfo, @RequestParam(name = "tagIds", required = false) List<Integer> tagIds, RedirectAttributes attributes) {


        String message=null;
        String redirectUrl;
        try{

            //对电影信息进行修改
            userInfo.setId((long) id);
            int update = userService.update(userInfo);


            message="修改成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/user/showAll")
                    .queryParam("message", message)
                    .toUriString();

        }catch (Exception e)
        {
            e.printStackTrace();
            message="修改失败!";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/user/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }

        return "redirect:" + redirectUrl;
    }




}


