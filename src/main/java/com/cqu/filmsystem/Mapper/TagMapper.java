package com.cqu.filmsystem.Mapper;



import com.cqu.filmsystem.pojo.MovieTag;
import com.cqu.filmsystem.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {


     List<Tag> selectAll();

     List<Tag> selectTag(String name);


     Tag selectTagByid(Integer id);

     List<Tag> selectAllWithCount();

     List<Tag> selectByOther(int count); //指定博客的标签不足10个时，获取count个其它没有指定博客的标签

     List<Tag> selectByOtherTags(); //获取其它没有指定博客的所有标签


     //用户对电影打标签
     int UserAddMovieTag(@Param("moviceId") int moviceId,
                         @Param("tagId") int tagId,
                         @Param("userId") int userId);

     List<MovieTag> selectUserMovieTagLive(@Param("moviceId") int moviceId,
                                           @Param("tagId") int tagId,
                                           @Param("userId") int userId);

     List<Tag> selectTagByUserId( @Param("userId") int userId,@Param("moviceId") int moviceId);

     int  deleteByThreeID(@Param("moviceId") int moviceId,
                          @Param("tagId") int tagId,
                          @Param("userId") int userId);

     //根据名字查重
     Tag selectByName(String name);

     //管理页面-添加
     int insert(Tag record);

     //管理页面-查重
     Tag selectByPrimaryKey(Integer id);

     //管理页面-修改
     int updateByPrimaryKeySelective(Tag record);

     //管理-删除
     int deleteByPrimaryKey(Integer id);

     //管理-删除(查询该标签是否有博客)
     int selectCountByTagIdForBlogAndTag(Integer id);




}
