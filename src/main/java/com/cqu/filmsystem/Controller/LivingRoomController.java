package com.cqu.filmsystem.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LivingRoomController {
    /**
     * 放映室页面
     */
    @GetMapping("/living/room")
    public String showLivingRoom(Model model) {
        // 用于前端导航栏高亮“放映室”
        model.addAttribute("n", 6);

        // 你可以在这里添加放映室数据，比如电影列表、观众人数等
        // model.addAttribute("rooms", yourRoomService.getRoomList());

        // 返回模板路径：templates/theater-room.html
        return "living-room";
    }

    /**
     * 放映厅详情页面
     */
    @GetMapping("/living-room-detail")
    public String showLivingRoomDetail(Model model) {
        // 你可以在这里添加放映厅的具体数据，比如详细信息、电影播放等
        // model.addAttribute("details", yourDetailsService.getRoomDetails());

        return "living-room-detail"; // 返回放映厅详情页面模板
    }
}
