package com.cqu.filmsystem.Controller;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Service.Impl.RemarksServiceImpl;
import com.cqu.filmsystem.pojo.Comment;
import com.cqu.filmsystem.pojo.Movice;
import com.cqu.filmsystem.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/remark")
public class RemarksController {

    @Autowired
    RemarksServiceImpl remarksService = new RemarksServiceImpl();


    @RequestMapping("/select")
    public PageInfo<Comment> showAllCommentByBlogId(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                    @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                                                    @RequestParam(name = "id") Integer id, Model model) throws IOException {


        List<Comment> comments = remarksService.selectByBlogIdAndParentCommentIsNull (pageNum, pageSize, id);
        PageInfo<Comment> pageInfo = new PageInfo<> (comments,5);
        RemarksServiceImpl commentServiceImpl = (RemarksServiceImpl) remarksService;
        List<Comment> commentList = commentServiceImpl.getAll (pageInfo.getList ());  //获取后代评论
        pageInfo.setList (commentList); //封装好的顶级评论和后代评论设置给pageInfo的list
        model.addAttribute ("pageInfo",pageInfo);
        return pageInfo;
    }








}
