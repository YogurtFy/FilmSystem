package com.cqu.filmsystem.Controller.admin;

import com.cqu.filmsystem.Service.Impl.ParameterServiceImpl;
import com.cqu.filmsystem.Service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import com.cqu.filmsystem.pojo.Parameter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;


@Controller
@RequestMapping("/admin/parameter")
public class ParameterAdminController {

    @Autowired
    @Qualifier("parameterServiceImpl")
    ParameterService parameterService= new ParameterServiceImpl();


    @RequestMapping("/showAll")
    public String select(@RequestParam(name = "message", required = false, defaultValue = "") String message,
                         Model model , HttpServletRequest request)
    {
        //查询参数
        Parameter select = parameterService.select();

        model.addAttribute("parameter",select);
        model.addAttribute("message",message);
        return "parameter-admin";
    }


    //修改界面
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(Integer id){
        ModelAndView mv = new ModelAndView ();

        Parameter select = parameterService.select();
        mv.addObject ("parameter",select);
        mv.setViewName ("parameter-admin-update");
        return mv;
    }


    //修改
    @RequestMapping("/update")
    public String update(Parameter parameter, BindingResult result, RedirectAttributes attributes){

        String redirectUrl;
        String message;

        int update = parameterService.update(parameter);
        if (update>0){
            attributes.addFlashAttribute ("message","修改成功！");
            message="修改成功！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/parameter/showAll")
                    .queryParam("message", message)
                    .toUriString();
        } else {
            attributes.addFlashAttribute ("message","修改失败！");
            message="修改失败！";
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/admin/parameter/showAll")
                    .queryParam("message", message)
                    .toUriString();
        }

        return "redirect:" +redirectUrl;
    }



}
