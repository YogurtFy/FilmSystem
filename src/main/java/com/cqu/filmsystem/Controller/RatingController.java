package com.cqu.filmsystem.Controller;

import com.cqu.filmsystem.Service.Impl.RatingServiceImpl;
import com.cqu.filmsystem.pojo.Rating;
import com.cqu.filmsystem.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    @Qualifier("ratingServiceImpl")
    RatingServiceImpl ratingService = new RatingServiceImpl();


    @RequestMapping("/submit")
    public ResponseEntity<?> showAllAdd(@RequestParam(name = "movieId", required = false, defaultValue = "0") Integer moviceId ,
                                        @RequestParam(name = "rating", required = false, defaultValue = "0") double rating,
                                        Model model, HttpServletRequest request) throws IOException{

        //1.查询用户信息
        UserInfo user = new UserInfo();
        HttpSession session = request.getSession(false);
        try {
             user = (UserInfo) session.getAttribute("user");
            model.addAttribute("user",user);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            //2.应该先判断是否有评分 有的话就更新 没有的话就应该新插入
            Rating select = ratingService.select(Math.toIntExact(user.getId()), moviceId);
            if(select == null)
            {
                int insert = ratingService.insert(Math.toIntExact(user.getId()), moviceId, rating);
            }
            else
            {
                int update = ratingService.update(Math.toIntExact(user.getId()),moviceId,rating);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Rating submitted successfully\"}");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"" + e.getMessage() + "\"}");
        }


        //跳转到详情界面
//        return "redirect:http://localhost:8080/movice/details?id="+moviceId;

    }
}
