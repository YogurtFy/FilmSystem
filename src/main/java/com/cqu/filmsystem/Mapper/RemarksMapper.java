package com.cqu.filmsystem.Mapper;



import com.cqu.filmsystem.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;




@Mapper
public interface RemarksMapper {

    List<Comment> selectComment();

    List<Comment> selectByBlogIdAndParentCommentIsNull(Integer id);

    Comment selectById(int id);

    List<Comment> selectByParentId(Integer parentId);

    int insert(Comment record);


    List<Comment> selectAll(@Param ("content") String content, @Param ("email") String email);

    int deleteByPrimaryKey(Integer id);


}
