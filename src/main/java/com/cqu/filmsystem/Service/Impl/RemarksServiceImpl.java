package com.cqu.filmsystem.Service.Impl;

import com.github.pagehelper.PageHelper;
import com.cqu.filmsystem.Mapper.RemarksMapper;
import com.cqu.filmsystem.Service.RemarksService;

import com.cqu.filmsystem.pojo.Comment;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


@Service("remarksService")
public class RemarksServiceImpl  implements RemarksService {
    @Autowired
    RemarksMapper commentMapper;

    @Override
    public List<Comment> selectByBlogIdAndParentCommentIsNull(Integer pageNum, Integer pageSize, Integer id) {

        //分页
        //获取顶级评论的集合（也就是parent_comment_id 字段为 null的）
        PageHelper.startPage (pageNum, pageSize);
        List<Comment> comments = commentMapper.selectByBlogIdAndParentCommentIsNull (id);
//        comments = getAll (comments);  //获取所有，封装后代评论  要想分页，不能再这操作，得操作pageInfo.getList
        return comments;
    }

    @Override
    public List<Comment> selectByBlogIdAndParentCommentIsNull(Integer id) {
        return null;
    }

    private List<Comment> replyList = new ArrayList<>(); //定义用于临时存储每个顶级评论下的所有后代评论的replyList，存储完一个之后记得清空

    //获取所有评论，把顶级评论下的评论封装到顶级评论的replyList中
    public List<Comment> getAll(List<Comment> comments) throws IOException {

        List<Comment> commentList = new ArrayList<> ();  //用于保存封装完的顶级评论（给顶级评论封装后代评论）
        for (Comment comment : comments) {
            //根据顶级评论的id查找所有属于他下面的后代评论
            getReplyList(comment.getId (),comment);  //这个方法会封装后代评论到replyList中
            comment.setReplyList (replyList); //设置这个顶级评论的后代评论
            commentList.add (comment);  //把封装好的这个顶级评论加到commentList中
            replyList = new ArrayList<> (); //清空已经给这个顶级评论封装后代评论，便于下个存储
        }
        return commentList;
    }

    //用于递归获取顶级评论为id下的所有后代评论
    private void getReplyList(Integer id, Comment parentComment) throws IOException {

        //读取mybatis-config.xml文件内容到reader对象中
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        //初始化mybatis数据库,创建SqlSessionFactory类的实例
        SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
        //创建SqlSession实例
        SqlSession session = sqlMapper.openSession(true);
        //为测试接口，所以需获取接口的对象，因当前接口不存在实现类，所以获取的是该接口的动态代理对象
        RemarksMapper commentMapper = session.getMapper(RemarksMapper.class);


        List<Comment> commentList = commentMapper.selectByParentId (id); //根据parent_comment_id获取Comment

        if (commentList == null){  //为空，下面已经没有后代评论
            return;  //返回上一层
        }
        //获取后代评论
        for (Comment comment : commentList) {
            comment.setParentComment (parentComment); //设置该评论的父评论

            comment.setParentCommentUserInfoId(parentComment.getUserInfo().getId());
            comment.setParentCommentUserInfoNickname(parentComment.getUserInfo().getNickname());
//            System.out.println(comment.getParentCommentUserInfoId());
//            System.out.println(comment.getParentCommentUserInfoNickname());

            replyList.add (comment); //将每个后代评论放进对于顶级评论的replyList中
            getReplyList(comment.getId (),comment); //递归继续获取后代的后代下面所有的评论
        }

    }






    //    -----------------------------------------------------管理台--------------------------------------------

    @Override
    public List<Comment> selectAllByLikeName(Integer pageNum, Integer pageSize, String name) {
        return null;
    }


    @Override
    public void saveComment(Comment comment) {
        commentMapper.insert (comment);
    }


    @Override
    public List<Comment> selectAllComment(Integer pageNum, Integer pageSize, String content, String email) {


        content= "%"+content+"%";
        email= "%"+email+"%";
        PageHelper.startPage (pageNum, pageSize);
        List<Comment> comments = commentMapper.selectAll(content, email);
        return  comments;

    }


    @Override
    public int deleteById(Integer id) {
    return  commentMapper.deleteByPrimaryKey(id);
    }







}
