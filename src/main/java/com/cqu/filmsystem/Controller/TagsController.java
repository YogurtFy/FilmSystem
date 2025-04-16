package com.cqu.filmsystem.Controller;

import com.github.pagehelper.PageInfo;

import com.cqu.filmsystem.Service.Impl.MoviceServiceImpl;
import com.cqu.filmsystem.Service.Impl.TagServiceImpl;
import com.cqu.filmsystem.Service.MoviceService;
import com.cqu.filmsystem.Service.TagService;
import com.cqu.filmsystem.pojo.Movice;
import com.cqu.filmsystem.pojo.Tag;
import com.cqu.filmsystem.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;



@Controller
@RequestMapping("/tag")
public class TagsController {


    @Autowired
    @Qualifier("moviceServiceImpl")
    MoviceService moviceService= new MoviceServiceImpl();



    @Autowired
    @Qualifier("tagServiceImpl")
    TagService tagService= new TagServiceImpl();



    @RequestMapping("/showAll")
    public String showAll( @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                           @RequestParam(name = "tagId", required = false, defaultValue = "0") Integer tagId,
                           @RequestParam(name = "searchContent", required = false, defaultValue = "") String searchContent,
                           Model model)  throws IOException {
        PageInfo<Movice> PageInfo;
        //查询博客信息:
        if(tagId==0)
        {
            // 查询博客 首页
            PageInfo = moviceService.select(pageNum, pageSize);
        }
        else
        {
            // 根据tagId查询  首页
            PageInfo = moviceService.selectByTagId(pageNum,pageSize,tagId);
        }

        model.addAttribute("pageInfo",PageInfo);

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



        PageInfo<Movice> PageInfo;
        //查询博客信息:
        //查询博客信息:
        if(tagId==0)
        {
            // 查询博客 首页
            PageInfo = moviceService.select(pageNum, pageSize);
        }
        else
        {
            // 根据tagId查询  首页
            PageInfo = moviceService.selectByTagId(pageNum,pageSize,tagId);
        }
        model.addAttribute("pageInfo",PageInfo);
        model.addAttribute("tagId",tagId);
        return "tags:: blogList";
    }




}
