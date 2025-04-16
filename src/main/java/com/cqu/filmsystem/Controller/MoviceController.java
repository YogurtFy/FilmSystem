package com.cqu.filmsystem.Controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Service.Impl.*;
import com.cqu.filmsystem.Service.MoviceService;
import com.cqu.filmsystem.Service.SysLogService;
import com.cqu.filmsystem.Service.TagService;
import com.cqu.filmsystem.pojo.*;
import com.cqu.filmsystem.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/movice")
public class MoviceController {

    @Autowired
    @Qualifier("moviceServiceImpl")
    MoviceService moviceService= new MoviceServiceImpl();

    @Autowired
    @Qualifier("typeServiceImpl")
    TypeServiceImpl typeService = new TypeServiceImpl();

    @Autowired
    @Qualifier("regionServiceImpl")
    RegionServiceImpl regionService = new RegionServiceImpl();

    @Autowired
    @Qualifier("actorServiceImpl")
    ActorServiceImpl actorService = new ActorServiceImpl();

    @Autowired
    @Qualifier("ratingServiceImpl")
    RatingServiceImpl ratingService = new RatingServiceImpl();

    @Autowired
    @Qualifier("remarksService")
    RemarksServiceImpl remarksService = new RemarksServiceImpl();

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    @Qualifier("recommendServiceImpl")
    RecommendServiceImpl recommendService = new RecommendServiceImpl();

    @Autowired
    @Qualifier("userServiceImpl")
    UserServiceImpl userService = new UserServiceImpl();

    @Autowired
    @Qualifier("tagServiceImpl")
    TagService tagService= new TagServiceImpl();


    @RequestMapping("/history")
    public String selectUserHistory( Model model , HttpServletRequest request)
    {
        try{
            //查询用户
            HttpSession session = request.getSession(false);
            UserInfo user = (UserInfo) session.getAttribute("user");
            //查询用户访问记录
            List<Syslog> syslogs = sysLogService.selectUserSyslog(user.getNickname());
            model.addAttribute("syslogs",syslogs);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "timeAxis";

    }


    @PostMapping("/log-time")
    public ResponseEntity<?> logTimeSpent(@RequestBody String timeData) {
        try {
            // 使用Jackson库来解析JSON数据
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(timeData);
            JsonNode timeSpentNode = rootNode.path("timeSpent");
            JsonNode movieIdNode = rootNode.path("movieId");

            // 检查是否存在timeSpent节点
            if (!timeSpentNode.isMissingNode() && !movieIdNode.isMissingNode())  {
                // 获取timeSpent的字符串值，并清除多余的双引号
                String timeSpentStr = timeSpentNode.asText().replace("\"", "");
                String moviceIdStr = movieIdNode.asText().replace("\"", "");

                // 将字符串转换成浮点数 和整数
                double timeSpentMins = Double.parseDouble(timeSpentStr);
                int moviceId = Integer.parseInt(moviceIdStr);
                System.out.println("Time spent on page: " + timeSpentMins + " minutes");

                Syslog syslog = new Syslog();
                syslog.setViewTime(timeSpentMins);
                // 获取需要更新的id
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    HttpSession session = request.getSession(false);
                    Long sysLogId = (Long) session.getAttribute("sysLogId");
                    syslog.setId(sysLogId);
                }
                // 根据id 更新浏览时间
                sysLogService.update(syslog);
            }
            // 返回成功响应
            return ResponseEntity.ok("Time logged successfully");

        } catch (Exception e) {
            System.out.println("Error processing the time data: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error processing time data");
        }
    }



    @RequestMapping("/showAll")
    public String select(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                         Model model , HttpServletRequest request) throws IOException {

        PageInfo<Movice> select = moviceService.select(pageNum,pageSize);
        model.addAttribute("pageInfo",select);

        //---------------------------------------------0.热门推荐-----------------------------------------
        PageInfo<Movice> movicePageInfo = recommendService.popularRecommendations(pageNum, pageSize);


        //---------------------------------------------1.个性推荐-----------------------------------------
        try{
            //获取用户信息
            HttpSession session = request.getSession(false);
            UserInfo userInfo = new UserInfo();
            userInfo = (UserInfo) session.getAttribute("user");
            PageInfo<Movice> movicePageInfo1 = recommendService.userPersonalizedRecommendations(userInfo.getNickname(),userInfo.getId(),pageNum,pageSize);
            if(movicePageInfo1.getList().size()<5 && pageNum==1 )
            {
                movicePageInfo1=movicePageInfo;
                movicePageInfo1.setTotal(5);
            }
            model.addAttribute("pageInfo",movicePageInfo1);
        }catch (Exception e){
            e.printStackTrace();
        }

        //---------------------------------------------2.查询收藏夹信息-------------------------------------
        try {
            //1.获取用户信息
            HttpSession session = request.getSession(false);
            UserInfo userInfo = null;
            userInfo = (UserInfo) session.getAttribute("user");
            model.addAttribute("user", userInfo);
            //查询用户的收藏夹
            List<Movice> movices = moviceService.selectFavorites(Math.toIntExact(userInfo.getId()));
            model.addAttribute("userFavorites",movices);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //---------------------------------------------3.查询所有分类---------------------------------------
        List<Type> types = typeService.selectAllType();
        if (types.size() > 8) {
            types = types.subList(0, 8);
        }
        model.addAttribute("types",types);
        model.addAttribute("HotpageInfo",movicePageInfo);
        return "index";
    }

    @RequestMapping("/showAll/search")
    public String showAllAdd(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                             @RequestParam(name = "searchContent", required = false, defaultValue = "") String searchContent,
                             Model model, HttpServletRequest request) throws IOException {

        PageInfo<Movice> select = moviceService.select(pageNum,pageSize);
        model.addAttribute("pageInfo",select);

        //---------------------------------------------0.热门推荐-----------------------------------------
        PageInfo<Movice> movicePageInfo = recommendService.popularRecommendations(pageNum, pageSize);


        //---------------------------------------------1.个性推荐-----------------------------------------

        try{
            //获取用户信息
            HttpSession session = request.getSession(false);
            UserInfo userInfo = new UserInfo();
            userInfo = (UserInfo) session.getAttribute("user");
            PageInfo<Movice> movicePageInfo1 = recommendService.userPersonalizedRecommendations(userInfo.getNickname(),userInfo.getId(),pageNum,pageSize);
            if(movicePageInfo1.getList().size()<5 && pageNum==1)
            {
                movicePageInfo1=movicePageInfo;
            }
            model.addAttribute("pageInfo",movicePageInfo1);
        }catch (Exception e){
            e.printStackTrace();
        }

        //---------------------------------------------2.查询收藏夹信息-------------------------------------
        try {
            //1.获取用户信息
            HttpSession session = request.getSession(false);
            UserInfo userInfo = null;
            userInfo = (UserInfo) session.getAttribute("user");
            model.addAttribute("user", userInfo);
            //查询用户的收藏夹
            List<Movice> movices = moviceService.selectFavorites(Math.toIntExact(userInfo.getId()));
            model.addAttribute("userFavorites",movices);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //---------------------------------------------3.查询所有分类---------------------------------------
        List<Type> types = typeService.selectAllType();
        if (types.size() > 8) {
            types = types.subList(0, 8);
        }
        model.addAttribute("types",types);
        model.addAttribute("HotpageInfo",movicePageInfo);

        return "index:: blogList";
    }


    @Mylog(value = "浏览电影")
    @RequestMapping("/details")
    public String showAllSearch(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                                @RequestParam(name = "id", required = true, defaultValue = "1") Integer id,
                                @RequestParam(name = "tip", required = false,defaultValue = "") String tip,
                                Model model,HttpServletRequest request)  throws IOException {

        //1.获取用户信息
        HttpSession session = request.getSession(false);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(0L);

        String oldViewKey=null;
        String viewKey=null;
        try {
            userInfo = (UserInfo) session.getAttribute("user");
            viewKey = "view:" + userInfo.getId();
            oldViewKey = RedisUtils.get(viewKey);
            model.addAttribute("user", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (oldViewKey == null) {
            RedisUtils.setTTl(viewKey, userInfo.getNickname() + userInfo.getEmail(), 30);
            // 浏览量+1
            moviceService.addView(id);
        } else {
            System.out.println("30秒内防刷");
        }


        //2.查询某一条电影信息
        Movice movies = moviceService.selectByid(id);

        System.out.println(movies);
        model.addAttribute("movie", movies);


        //3.查询类型
        List<Type> types = typeService.selectByMoviceId(id);
        model.addAttribute("types",types);


        //4.获取演员信息
        List<Actor> actors = actorService.selectByMoviceId(id);
        model.addAttribute("actors",actors);

        //5.查询评论
        List<Comment> comments = remarksService.selectByBlogIdAndParentCommentIsNull (pageNum, pageSize, id);
        PageInfo<Comment> pageInfo = new PageInfo<> (comments,5);
        RemarksServiceImpl commentServiceImpl = (RemarksServiceImpl) remarksService;
        List<Comment> commentList = commentServiceImpl.getAll (pageInfo.getList ());  //获取后代评论
        pageInfo.setList (commentList); //封装好的顶级评论和后代评论设置给pageInfo的list
        model.addAttribute ("pageInfo",pageInfo);


        //6.查询用户是否搜藏
        Movice movice = moviceService.selectFavorite(Math.toIntExact(userInfo.getId()), id);
        model.addAttribute ("favorite",movice);

        //7.查询所有标签
        List<Tag> tags = tagService.selectAllTagsWithCount();
        model.addAttribute("tags",tags);

        //8.查询该电影的标签:
        List<Tag> tags1 = tagService.selectTagByUserId(Math.toIntExact(userInfo.getId()), id);
        List<Integer> selectedGenreIds = new ArrayList<>();
        for (Tag tag: tags1)
        {
            selectedGenreIds.add(tag.getId());
        }
        model.addAttribute("selectedGenreIds",selectedGenreIds);

        try
        {
            Rating select = ratingService.select(Math.toIntExact(userInfo.getId()), id);
            //9.获取用户对该电影的评分
            int ratingnumber=0;
            ratingnumber = select.getRating().intValue();
            model.addAttribute ("ratingnumber",ratingnumber);
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        model.addAttribute ("tip",tip);
        System.out.println(tip);
        return "details";

    }


    @RequestMapping("/details/search")
    public String showAllSearch1(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize ,
                                 @RequestParam(name = "id", required = true, defaultValue = "1") Integer id,
                                 @RequestParam(name = "tip", required = false,defaultValue = "") String tip,
                                 Model model,HttpServletRequest request)  throws IOException {

        //5.查询评论
        List<Comment> comments = remarksService.selectByBlogIdAndParentCommentIsNull (pageNum, pageSize, id);
        PageInfo<Comment> pageInfo = new PageInfo<> (comments,5);
        RemarksServiceImpl commentServiceImpl = (RemarksServiceImpl) remarksService;
        List<Comment> commentList = commentServiceImpl.getAll (pageInfo.getList ());  //获取后代评论
        pageInfo.setList (commentList); //封装好的顶级评论和后代评论设置给pageInfo的list
        model.addAttribute ("pageInfo",pageInfo);

        model.addAttribute ("tip",tip);
        System.out.println(tip);
        return "details ::commentList";

    }



    @RequestMapping("/favorite")
    public String favorite(@RequestParam(name = "movieId", required = false, defaultValue = "1") Integer movieId,
                           Model model,HttpServletRequest request)  throws IOException {

        //1.获取用户信息
        HttpSession session = request.getSession(false);
        UserInfo userInfo = null;
        try {
            userInfo = (UserInfo) session.getAttribute("user");
            model.addAttribute("user", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String tip ;
        try
        {
            int i = moviceService.insertFavorite(Math.toIntExact(userInfo.getId()), movieId);
            if (i>0)
            {
                tip="收藏成功!";
            }
            else
            {
                tip="收藏失败!";
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            tip="收藏失败!";
        }

        String encodedTip = URLEncoder.encode(tip, StandardCharsets.UTF_8.toString());
        String idParam = "id=" + movieId; // 假设id是一个不需要编码的字符串或数字

        return "redirect:http://localhost:8080/movice/details?tip=" + encodedTip + "&" + idParam;

    }


    @RequestMapping("/deleteFavorite")
    public String deleteFavorite(@RequestParam(name = "movieId", required = false, defaultValue = "1") Integer movieId,
                                 Model model,HttpServletRequest request)  throws IOException {

        //1.获取用户信息
        HttpSession session = request.getSession(false);
        UserInfo userInfo = null;
        try {
            userInfo = (UserInfo) session.getAttribute("user");
            model.addAttribute("user", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String tip ;
        try
        {
            int i = moviceService.deleteFavorite(Math.toIntExact(userInfo.getId()), movieId);
            if (i>0)
            {
                tip="取消成功!";
            }
            else
            {
                tip="取消失败!";
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            tip="取消失败!";
        }


        String encodedTip = URLEncoder.encode(tip, StandardCharsets.UTF_8.toString());
        String idParam = "id=" + movieId; // 假设id是一个不需要编码的字符串或数字

        return "redirect:http://localhost:8080/movice/details?tip=" + encodedTip + "&" + idParam;

    }


    @RequestMapping("/selectFavorites")
    public String selectFavorite(Model model,HttpServletRequest request)  throws IOException {

        //1.获取用户信息
        HttpSession session = request.getSession(false);
        UserInfo userInfo = null;
        try {
            userInfo = (UserInfo) session.getAttribute("user");
            model.addAttribute("user", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //查询用户的收藏夹
        List<Movice> movices = moviceService.selectFavorites(Math.toIntExact(userInfo.getId()));
        model.addAttribute("movices",movices);
        model.addAttribute("count",movices.size());
        return "favorite";

    }


    @RequestMapping("/addComment")
    public String addComment(@RequestParam(name = "content", required = false, defaultValue = "") String content,
                             @RequestParam(name = "blogId", required = false, defaultValue = "") Long blogId,
                             @RequestParam(name = "parentCommentId", required = false, defaultValue = "-1") Integer parentCommentId, HttpServletRequest request,
                             Model model) throws IOException {
        //1.获取用户信息
        HttpSession session = request.getSession(false);
        UserInfo userInfo = null;
        try {
            userInfo = (UserInfo) session.getAttribute("user");
            model.addAttribute("user", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setMovice(new Movice());
        comment.getMovice().setId(Math.toIntExact(blogId));
        comment.setParentComment(new Comment());
        comment.getParentComment().setId(parentCommentId);
        comment.setUserInfo(userInfo);
        comment.setCreateTime(new Date());

        if (comment.getParentComment().getId() == -1) {
            comment.getParentComment().setId(null);
        }
        remarksService.saveComment(comment);

        List<Comment> comments = remarksService.selectByBlogIdAndParentCommentIsNull (1, 5, Math.toIntExact(blogId));
        PageInfo<Comment> pageInfo = new PageInfo<> (comments,5);
        RemarksServiceImpl commentServiceImpl = (RemarksServiceImpl) remarksService;
        List<Comment> commentList = commentServiceImpl.getAll (pageInfo.getList ());  //获取后代评论
        pageInfo.setList (commentList); //封装好的顶级评论和后代评论设置给pageInfo的list
        model.addAttribute ("pageInfo",pageInfo);
        return "details:: commentList";
    }

    @RequestMapping("/addTag")
    public String addTags( @RequestParam(name = "movieId" , required = false) int movieId,
                           @RequestParam(name = "tagIds", required = false) List<Integer> tagIds,Model model ,HttpServletRequest request)
            throws UnsupportedEncodingException {

        String tip ="" ;
        //1.获取用户信息
        HttpSession session = request.getSession(false);
        UserInfo userInfo = null;
        try {
            userInfo = (UserInfo) session.getAttribute("user");
            int userId= Math.toIntExact(userInfo.getId());

            //8.查询该电影的标签:
            List<Tag> tags1 = tagService.selectTagByUserId(Math.toIntExact(userInfo.getId()), movieId);
            List<Integer> selectedGenreIds = new ArrayList<>();   //为已存在数据的标签id
            for (Tag tag: tags1)
            {
                selectedGenreIds.add(tag.getId());
            }
            //先遍历之前的电影标签 如果不存在tagIds中就删掉
            for (Integer i: selectedGenreIds) {
                if (!tagIds.contains(i))
                {
                    tagService.deleteByThreeID(movieId,i,userId);
                }
            }
            //后遍历tagIds标签 如果不存在selectedGenreIds中就添加进去
            for (Integer i: tagIds)
            {
                if (!selectedGenreIds.contains(i)){
                    tagService.UserAddMovieTag(movieId,i,userId);
                }
            }
            tip="标签成功";
        } catch (Exception e) {
            e.printStackTrace();
            tip="标签失败";
        }
        String encodedTip = URLEncoder.encode(tip, StandardCharsets.UTF_8.toString());
        String idParam = "id=" + movieId; // 假设id是一个不需要编码的字符串或数字
        return "redirect:http://localhost:8080/movice/details?tip=" + encodedTip + "&" + idParam;
    }

}
