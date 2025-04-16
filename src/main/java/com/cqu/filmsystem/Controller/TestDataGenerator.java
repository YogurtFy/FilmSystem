package com.cqu.filmsystem.Controller;
import com.github.javafaker.Faker;
import com.cqu.filmsystem.Service.Impl.MoviceServiceImpl;
import com.cqu.filmsystem.Service.MoviceService;
import com.cqu.filmsystem.pojo.Movice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


@Controller
public class TestDataGenerator {


    @Autowired
    @Qualifier("moviceServiceImpl")
    MoviceService moviceService = new MoviceServiceImpl();


    @RequestMapping("/testData/movice/add")
    public void testDataMoviceAdd() {
        for (int i = 1; i < 50; i++) {
            Faker faker = new Faker();
            // 创建一个Movice对象
            Movice movice = new Movice();
            // 生成电影名
            movice.setTitle(faker.name().fullName());

            // 生成导演名
            movice.setDirector(faker.options().option("杰森·莫玛","安珀·赫德","威廉·达福","帕特里克·威尔森","妮可·基德曼","杜夫·龙格尔","陈伟霆","周润发","刘诗诗","王志文","张译","陈宝国","陈道明","张颂文","肖战","吴京","陈晓","梁朝伟","刘德华","胡歌","成龙","李连杰","刘德华","张曼玉","巩俐","章子怡","赵薇","黄晓明","范冰冰","吴京","徐峥","王宝强","邓超","孙俪","胡歌","刘亦菲","陈坤","周迅","李冰冰","黄渤","沈腾","马丽","吴亦凡","鹿晗","杨洋","迪丽热巴","赵丽颖","杨幂","唐嫣"));

            // 生成发行日期（假设是未来的某个日期）
            Date date = new Date();
            movice.setReleaseDate(new Date(date.getTime() - (long) (Math.random() * 1000L * 60L * 60L * 24L * 365L)));


            // 生成时长（假设是随机的分钟数）
            movice.setRuntime(faker.number().numberBetween(60, 240));

            // 生成语言
            movice.setLanguage(faker.options().option("English","Spanish", "French", "German", "Chinese", "Japanese"));

            // 生成简介（大约50个字符）
            movice.setDescription("这两位宛如神衹一般强大的对手于一场壮观的战争中相遇，彼时世界命运正悬于一线。为了找到真正的家园，金刚与他的保护者们踏上了一次艰难的旅程。与他们一道前行的还有一个年轻的孤儿女孩——吉雅，这个女孩与金刚之间存在着一种独特而强大的紧密联系。但意想不到的是，他们在前行的航道上与愤怒的哥斯拉狭路相逢，也由此在全球引起了一系列破坏。一股无形的力量造成了这两只巨兽之间的巨大冲突，深藏在地心深处的奥秘也由此揭开。");

            // 平均评分默认为0
            movice.setAverageRating("0");

            // 电影海报链接和电影视频链接为空
            String picture="https://picsum.photos/id/"+i+"/220/125";
            movice.setPicture(picture);
            movice.setVideo("https://pyy-filmsystem.oss-cn-chengdu.aliyuncs.com/video/movie.mp4");

            movice.setRegionId(faker.number().numberBetween(1, 7));

            // 浏览量、评论数、收藏数、观看时间默认为0
            movice.setPageView(0);
            movice.setCommentTime(0);
            movice.setFavritesTime(0);
            movice.setViewTime(0);

            // 用于推荐的字段
            movice.setRating(0.0);
            moviceService.add(movice);

        }
    }

}