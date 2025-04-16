package com.cqu.filmsystem.Service;



import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.pojo.Comment;

import java.util.List;

public interface RemarksService {

    List<Comment> selectByBlogIdAndParentCommentIsNull(Integer pageNum, Integer pageSize, Integer id);

    List<Comment> selectByBlogIdAndParentCommentIsNull(Integer id);



    //管理-查询名称模糊查询所有
     List<Comment> selectAllByLikeName(Integer pageNum, Integer pageSize, String name);

     void saveComment(Comment comment);


    //管理页面-查询
    List<Comment> selectAllComment(Integer pageNum, Integer pageSize, String content, String email);


    //管理页面-删除
     int deleteById(Integer id);



}
