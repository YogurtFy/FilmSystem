package com.cqu.filmsystem.Controller.admin;

import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Service.Impl.TagServiceImpl;
import com.cqu.filmsystem.pojo.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;



@RequestMapping("/admin/tag")
@Controller
public class TagsAdminController {

    @Autowired
    TagServiceImpl tagService = new TagServiceImpl();


    //-------------------------------------------------管理台-----------------------------------------------------------


    @RequestMapping("/showAll")
    public String showAllTag(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                    @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                                    @RequestParam(name = "name", required = false, defaultValue = "") String name,
                                    @RequestParam(name = "message", required = false, defaultValue = "") String message,
                                    Model model)
    {
        List<Tag> Tags = tagService.selectAllTag(pageNum,pageSize,name);
        PageInfo<Tag> pageInfo = new PageInfo<> (Tags, 5);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("name",name);
        model.addAttribute("message",message);
        return "tags-admin";
    }

    //添加界面
    @RequestMapping("/toAdd")
    public String toAdd(Model model){
        model.addAttribute ("tag",new Tag());
        return "tags-admin-input";
    }

    //添加
    @RequestMapping("/add")
    public String add(Tag tag, BindingResult result,RedirectAttributes attributes){

        // 查询当前的标签是重复
        Tag Tag1 = tagService.selectByName(tag.getName());

        String redirectUrl;
        String message;

        if (Tag1 != null){
            result.rejectValue ("name","nameError","不能添加重复的类别名称");
        }

        if (result.hasErrors ()){
            return "tags-admin-input";
        }

        //调用service层保存类别
        Boolean save = tagService.save(tag);

        if (save){
            attributes.addFlashAttribute ("message","添加成功！");
            message="添加成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/tag/showAll")
                    .queryParam("message", message)
                    .toUriString();
        } else {
            attributes.addFlashAttribute ("message","添加失败！");
            message="添加失败！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/tag/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }

        return "redirect:" +redirectUrl;
    }

    //修改界面
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(Integer id){
        ModelAndView mv = new ModelAndView ();


        Tag Tag = tagService.selectById(id);

        mv.addObject ("tag",Tag);
        mv.setViewName ("tags-admin-update");
        return mv;
    }


    //修改
    @RequestMapping("/update")
    public String update( Tag Tag, BindingResult result, RedirectAttributes attributes){

        String redirectUrl;
        String message;
        Tag Tag1 = tagService.selectByName(Tag.getName());
        if (Tag1 != null){
            result.rejectValue ("name","nameError","该类别名称已存在，不能修改！");
        }

        if (result.hasErrors ()){
            return "tags-update";
        }

        Boolean update = tagService.update (Tag);
        if (update){
            attributes.addFlashAttribute ("message","修改成功！");
            message="修改成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/tag/showAll")
                    .queryParam("message", message)
                    .toUriString();
        } else {
            attributes.addFlashAttribute ("message","修改失败！");
            message="修改失败！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/tag/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }

        return "redirect:" +redirectUrl;
    }

    //删除
    @RequestMapping("/deleteById")
    public String deleteById(Integer id, RedirectAttributes attributes){

        //调用service层删除类别
        Boolean aBoolean = tagService.deleteById (id);

        String redirectUrl;
        String message;

        if (aBoolean){
            attributes.addFlashAttribute ("message","删除成功！");
            message="删除成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/tag/showAll")
                    .queryParam("message", message)
                    .toUriString();
        } else {
            attributes.addFlashAttribute ("message","删除失败，有博客是该标签！");
            message="删除失败！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/tag/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }

        return "redirect:" +redirectUrl;
    }


}
