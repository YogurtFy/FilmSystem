package com.cqu.filmsystem.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/region")
public class RegionController {


//    @RequestMapping("/showAll")
//    public String showAll( @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
//                           @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
//                           @RequestParam(name = "typeId", required = false, defaultValue = "0") Integer typeId,
//                           @RequestParam(name = "searchContent", required = false, defaultValue = "") String searchContent,
//                           Model model)  throws IOException {
//
//        //1.查询电影的所有分类
//
//
//        //2.查找电影的地区
//
//
//        PageInfo<Movie> blogPageInfo;
//        if( typeId ==0) {
//            //查询博客信息:
//            blogPageInfo = blogService.selectAll(pageNum, pageSize,searchContent);
//
//        }else
//        {
//            //更具TypeId     查询下一页
//            blogPageInfo = blogService.selectByTypeId(pageNum,pageSize,typeId);
//        }
//
//
//        model.addAttribute("pageInfo",blogPageInfo);
//
//        //查询标签：
//        List<Type> types = typeService.selectAllTypesWithCount();
//        model.addAttribute("types",types);
//
//        model.addAttribute("typeId",typeId);
//
//        UserInfo userInfo = userFeign.sessionUser_homepage();
//        model.addAttribute("user",userInfo);
//        return "types";
//
//    }
//
//
//    @RequestMapping("/showAll/search")
//    public String showAllAdd( @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
//                           @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
//                           @RequestParam(name = "typeId", required = false, defaultValue = "0") Integer typeId,
//                              @RequestParam(name = "searchContent", required = false, defaultValue = "") String searchContent,
//
//                           Model model)  throws IOException {
//
//
//        PageInfo<Blog> blogPageInfo;
//        if(typeId==0)
//        {
//            //查询博客信息:   主页的下一页
//            blogPageInfo = blogService.selectAll(pageNum, pageSize,searchContent);
//        }
//        else
//        {
//            //更具TypeId     查询下一页
//            blogPageInfo = blogService.selectByTypeId(pageNum,pageSize,typeId);
//        }
//
//
//        model.addAttribute("pageInfo",blogPageInfo);
//        System.out.println(blogPageInfo);
//        model.addAttribute("typeId",typeId);
//
//        return "types:: blogList";
//    }
//






}
