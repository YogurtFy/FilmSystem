package com.cqu.filmsystem.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WelcomeController {
    @GetMapping("/")
    public String welcome(Model model) {
        model.addAttribute("message", "Hello, Thymeleaf!");
        return "welcome"; // 返回模板文件的名称（不包括.html扩展名）
    }

    @GetMapping("/index")
    public String index(Model model) {

        return "index"; // 返回模板文件的名称（不包括.html扩展名）
    }
}
