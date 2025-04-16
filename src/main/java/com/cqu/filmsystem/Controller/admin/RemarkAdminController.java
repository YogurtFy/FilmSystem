package com.cqu.filmsystem.Controller.admin;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Service.Impl.RemarksServiceImpl;
import com.cqu.filmsystem.pojo.Comment;
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
@RequestMapping("/admin/remark")
public class RemarkAdminController {


    @Autowired
    @Qualifier("remarksService")
    RemarksServiceImpl remarksService = new RemarksServiceImpl();


    @RequestMapping("/showAll")
    public String select(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                         @RequestParam(name = "content", required = false, defaultValue = "") String content,
                         @RequestParam(name = "nickname", required = false, defaultValue = "") String nickname,
                         @RequestParam(name = "message", required = false, defaultValue = "") String message,
                         Model model , HttpServletRequest request) throws IOException {

        //查询所有评论
        List<Comment> select = remarksService.selectAllComment(pageNum,pageSize,content,nickname);
        PageInfo<Comment> pageInfo = new PageInfo<> (select,5);
        RemarksServiceImpl commentServiceImpl = (RemarksServiceImpl) remarksService;
        List<Comment> commentList = commentServiceImpl.getAll (pageInfo.getList ());  //获取后代评论
        pageInfo.setList (commentList); //封装好的顶级评论和后代评论设置给pageInfo的list

        //查询用户信息
        HttpSession session = request.getSession(false);
        try {
            UserInfo user = (UserInfo) session.getAttribute("user");
            model.addAttribute("user",user);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("content",content);
        model.addAttribute("nickname",nickname);
        model.addAttribute("message",message);
        return "remark-admin";
    }


    @RequestMapping("/showAll/search")
    public String showAllAdd(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                                   @RequestParam(name = "content", required = false, defaultValue = "") String content,
                                   @RequestParam(name = "nickname", required = false, defaultValue = "") String nickname,
                                   @RequestParam(name = "message", required = false, defaultValue = "") String message,
                                   Model model , HttpServletRequest request) throws IOException {

        //查询所有评论
        List<Comment> select = remarksService.selectAllComment(pageNum,pageSize,content,nickname);
        PageInfo<Comment> pageInfo = new PageInfo<> (select,5);
        RemarksServiceImpl commentServiceImpl = (RemarksServiceImpl) remarksService;
        List<Comment> commentList = commentServiceImpl.getAll (pageInfo.getList ());  //获取后代评论
        pageInfo.setList (commentList); //封装好的顶级评论和后代评论设置给pageInfo的list


        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("content",content);
        model.addAttribute("nickname",nickname);
        model.addAttribute("message",message);
        return "remark-admin:: blogList";
    }


    @RequestMapping("/deleteById")
    public String deleteById(Integer id, RedirectAttributes attributes) {

        String message=null;
        String redirectUrl;
        try
        {
            //删除
            int delete = remarksService.deleteById(id);
            message="删除成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/remark/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }catch (Exception e)
        {
            e.printStackTrace();
            message="删除失败!";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/remark/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }
        return "redirect:" + redirectUrl;
    }



}


