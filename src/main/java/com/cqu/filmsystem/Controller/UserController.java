package com.cqu.filmsystem.Controller;
import com.cqu.filmsystem.Service.Impl.UserServiceImpl;
import com.cqu.filmsystem.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    UserServiceImpl userService = new UserServiceImpl();

    @RequestMapping({"/login"})
    public void login(@RequestParam(name="email" ,required = false)  String email,
                      @RequestParam(name="password", required = false ) String password ,
                      HttpServletResponse response, HttpServletRequest request,
                      Model model) throws IOException, ServletException {

        //1.判断用户是否存在
        UserInfo login = userService.login(email);
        String tip="" ;
        if (login!=null)
        {
            //2. 判断密码是否正确
            if(login.getPassword().equals(password))
            {
                System.out.println("登录成功");
                model.addAttribute("tip",tip);
                // 将用户信息存入session
                HttpSession session = request.getSession(true);
                session.setAttribute("user",login);

                //将数据放到servletContext中
                ServletContext servletContext = request.getServletContext();
                servletContext.setAttribute("user", login);

                request.getRequestDispatcher("/movie/showAll").forward(request,response);
                return;
            }
            else
            {
                tip="账号或者密码错误";
                model.addAttribute("tip",tip);
                System.out.println("账号或者密码错误");
                request.setAttribute("tip",tip);
                request.getRequestDispatcher("/user/login.html").forward(request,response);
                return;
            }
        }
        else
        {
            tip="抱歉,该账号还未注册";
            model.addAttribute("tip",tip);
            System.out.println("抱歉,该账号还未注册");
            request.setAttribute("tip",tip);
            request.getRequestDispatcher("/user/login.html").forward(request,response);
            return;
        }

    }


    @RequestMapping("/register")
    public void register(@RequestParam(name="email" ,required = false)  String email,
                         @RequestParam(name="password", required = false ) String password,
                         @RequestParam(name="passwordTow", required = false ) String passwordTow,
                         Model model,HttpServletRequest request ,HttpServletResponse response
    ) throws IOException, ServletException {

        // 1. 首先判断用户是否存在
        UserInfo login = userService.login(email);

        String tip;
        if (login==null)
        {
            //2. 判断两次的密码是否正确
            if(password.equals(passwordTow) && password!="")
            {
                Integer register = userService.register(email, password);

                if (register!=0)
                {
                    tip="注册成功";
                    System.out.println("注册成功");
                    request.setAttribute("tip",tip);
                    request.getRequestDispatcher("/user/login.html").forward(request,response);
                    return;
                }
                else
                {
                    System.out.println("系统故障,请稍后再试");
                    tip="注册成功";
                    request.setAttribute("tip",tip);
                    request.getRequestDispatcher("/user/register.html").forward(request,response);
                    return;
                }
            }
            else
            {

                if (password=="")
                {
                    tip="注册失败，密码不能为空";
                    System.out.println("注册失败，密码不能为空");
                }
                else
                {
                    tip="注册失败,两次密码不一致";
                    System.out.println("注册失败,两次密码不一致");
                }
                request.setAttribute("tip",tip);
                request.getRequestDispatcher("/user/register.html").forward(request,response);
                return;
            }
        }
        else
        {
            tip= "抱歉亲~账号已经存在";
            System.out.println("注册失败,抱歉亲~账号已经存在");
            request.setAttribute("tip",tip);
            request.getRequestDispatcher("/user/register.html").forward(request,response);
            return;
        }

    }



    @RequestMapping("/login.html")
    public String index(@RequestParam(name="tip" ,required = false )  String tip,
                        Model model ,HttpServletRequest request)
    {
        String tip1 = (String) request.getAttribute("tip");
        model.addAttribute("tip",tip1);

        return "login";

    }

    @RequestMapping("/register.html")
    public String index1( Model model ,HttpServletRequest request)
    {
        String tip1 = (String) request.getAttribute("tip");
        model.addAttribute("tip",tip1);
        return "register";
    }




    @RequestMapping("/exit")
    public String exit(HttpServletRequest request)  {


        ServletContext servletContext = request.getServletContext();
        HttpSession session = request.getSession(false);
        Boolean flag=true;
        UserInfo user = (UserInfo)servletContext.getAttribute("user");
//        RedisUtils.del("currentUser");
//
//        if (user.getRoles().get(0).getName().equals("ROLE_ADMIN"))
//        {
//            flag=true;
//        }
//        else
//        {
//            flag=false;
//        }


        //上下文中删除user
        servletContext.removeAttribute("user");
        //session中删除user
        session.removeAttribute("user");


        if (flag){
            return "redirect:http://localhost:8080/movie/showAll";
        }
        else
        {
            return "redirect:http://localhost:8080/movie/showAll";
        }

    }


    @RequestMapping("/toupdateName")
    public String toupdateName(@RequestParam(name = "message", required = false, defaultValue = "") String message,
                               Model model, HttpServletRequest request){

        //查找用户信息
        HttpSession session = request.getSession(false);
        UserInfo user = (UserInfo) session.getAttribute("user");

        model.addAttribute("user",user);
        if(message.equals(""))
        {

        }
        else {
            model.addAttribute("message",message);
        }
        return "update-nickname";

    }

    @RequestMapping("/updateName")
    public String updateName(@RequestParam(name = "name", required = false) String name, Model model, HttpServletRequest request)
    {

        //从session中获取用户信息
        String message;
        ServletContext servletContext = request.getServletContext();
        UserInfo user = (UserInfo) servletContext.getAttribute("user");
        try {
            userService.updateNickname(user.getId(),name);

            user.setNickname(name);
            //更新servletContext域下的user
            servletContext.setAttribute("user", user);
            //更新session里的user
            HttpSession session = request.getSession(false);
            session.setAttribute("user",user);

            message="修改成功!";

        } catch (Exception e) {
            message="修改失败！服务器异常";
            e.printStackTrace ();
        }

        model.addAttribute("message",message);
        model.addAttribute("user",user);
        return "update-nickname";
    }



    @RequestMapping("/toupdatePassword")
    public String toupdatePassword(@RequestParam(name = "message", required = false, defaultValue = "") String message,
                                   Model model, HttpServletRequest request)
    {


        //从context中获取用户信息
        ServletContext servletContext = request.getServletContext();
        UserInfo user = (UserInfo) servletContext.getAttribute("user");
        model.addAttribute("user",user);
        if(message.equals(""))
        {

        }
        else {
            model.addAttribute("message",message);
        }
        return "update-password";
    }



    @RequestMapping("/updatePassword")
    public String updatePassword(@RequestParam(name = "password", required = false) String password,
                                 @RequestParam(name = "passwords", required = false) String passwords,
                                 Model model, HttpServletRequest request)
    {

        ServletContext servletContext = request.getServletContext();
        UserInfo user = (UserInfo) servletContext.getAttribute("user");
        model.addAttribute("user",user);

        String message;
        String redirectUrl;

        if (!(password.equals(passwords)))
        {
            message="修改失败!两次密码不一致";

            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/user/toupdatePassword")
                    .queryParam("message", message)
                    .toUriString();

        }
        else
        {

            try {
                userService.updatePassword(user.getId(),password);
                message="修改成功!";
            }catch (Exception e)
            {
                message="修改失败!服务器异常";
            }



            if (message.equals("修改成功!"))
            {
                redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/user/exit")
                        .queryParam("message", message)
                        .toUriString();
            }
            else
            {
                redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/user/toupdatePassword")
                        .queryParam("message", message)
                        .toUriString();
            }

        }

        return "redirect:" + redirectUrl;
    }


    @RequestMapping("/toupdateImage")
    public String toupdateImage(@RequestParam(name = "message", required = false, defaultValue = "") String message,
                                Model model, HttpServletRequest request)
    {
        //查找用户信息
        ServletContext servletContext = request.getServletContext();
        UserInfo user = (UserInfo) servletContext.getAttribute("user");
        model.addAttribute("user",user);
        if(message.equals(""))
        {

        }
        else {
            model.addAttribute("message",message);
        }
        return "update-image";
    }


    @RequestMapping("/updateImage")
    public String updateImage(@RequestParam(name = "url", required = false) String url, Model model, HttpServletRequest request)
    {

        String message;

        ServletContext servletContext = request.getServletContext();
        UserInfo user = (UserInfo) servletContext.getAttribute("user");
        try {
            userService.updateImage(user.getId(), url);
            message="修改成功!";
        }catch (Exception e)
        {
            e.printStackTrace();
            message="修改失败!服务器异常";
        }

        model.addAttribute("message",message);

        String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/movie/showAll")
                .queryParam("message", message)
                .toUriString();
        return "redirect:" + redirectUrl;

    }




}
