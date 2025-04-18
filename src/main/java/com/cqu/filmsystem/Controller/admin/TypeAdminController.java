package com.cqu.filmsystem.Controller.admin;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Service.Impl.MovieServiceImpl;
import com.cqu.filmsystem.Service.Impl.TypeServiceImpl;
import com.cqu.filmsystem.Service.MovieService;
import com.cqu.filmsystem.pojo.Type;
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
@RequestMapping("/admin/type")
public class TypeAdminController {


    @Autowired
    @Qualifier("movieServiceImpl")
    MovieService movieService = new MovieServiceImpl();

    @Autowired
    @Qualifier("typeServiceImpl")
    TypeServiceImpl typeService = new TypeServiceImpl();


    @RequestMapping("/showAll")
    public String select(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                         @RequestParam(name = "title", required = false, defaultValue = "") String title,
                         @RequestParam(name = "message", required = false, defaultValue = "") String message,
                         Model model , HttpServletRequest request) throws IOException {

        //查询所有标签
        PageInfo<Type> select = typeService.selectAll(pageNum, pageSize, title);
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
        return "type-admin";
    }


    @RequestMapping("/showAll/search")
    public String showAllAdd(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                                   @RequestParam(name = "title", required = false, defaultValue = "") String title,
                                   @RequestParam(name = "message", required = false, defaultValue = "") String message,
                                   Model model , HttpServletRequest request) throws IOException {

        //查询所有标签
        PageInfo<Type> select = typeService.selectAll(pageNum,pageSize,title);

        model.addAttribute("pageInfo",select);
        model.addAttribute("title",title);
        model.addAttribute("message",message);
        return "type-admin:: blogList";
    }


    // 修改页面数据查询
    @RequestMapping("/toUpdate")
    public String toUpdate(@RequestParam(name = "id") int id, Model model) throws IOException {


        //查询分类信息
        Type type = typeService.selectById(id);
        model.addAttribute("type",type);



        return "type-admin-update";
    }


    // 添加页面数据查询
    @RequestMapping("/toAdd")
    public String toAdd( Model model) throws IOException {


        return "type-admin-input";
    }

    @RequestMapping("/add")
    public String add(Type type, RedirectAttributes attributes,Model model) {


        String message=null;
        String redirectUrl;

        try
        {
            //添加电影信息
            int add = typeService.add(type);

            message="添加成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/type/showAll")
                    .queryParam("message", message)
                    .toUriString();

        }catch (Exception e)
        {
            e.printStackTrace();
            message="插入失败!";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/type/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }

        return "redirect:" + redirectUrl;
    }








    @RequestMapping("/update")
    public String update(@RequestParam(name = "id") int id,Type type, RedirectAttributes attributes) {


        String message=null;
        String redirectUrl;
        try{

            //对分类信息进行修改
            type.setCategoryId(id);
            int update = typeService.update(type);

            message="修改成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/type/showAll")
                    .queryParam("message", message)
                    .toUriString();

        }catch (Exception e)
        {
            e.printStackTrace();
            message="修改失败!";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/type/showAll")
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
            int delete = typeService.delete(Math.toIntExact(id));
            message="删除成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/type/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }catch (Exception e)
        {
            e.printStackTrace();
            message="删除失败!";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/type/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }
        return "redirect:" + redirectUrl;
    }



}


