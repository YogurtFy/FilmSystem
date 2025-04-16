package com.cqu.filmsystem.Service;


import com.cqu.filmsystem.pojo.MovieTag;
import com.cqu.filmsystem.pojo.Tag;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.util.List;


public interface TagService {


    //查询所有分类
    List<Tag> selectAll();


    //用户对电影打标签
    int UserAddMovieTag( int moviceId, int tagId, int userId);

    List<MovieTag> selectUserMovieTagLive( int moviceId, int tagId, int userId);

    List<Tag> selectTagByUserId(  int userId,int moviceId);

    int  deleteByThreeID(int moviceId, int tagId, int userId);


    List<Tag> selectTag() throws IOException;

    Tag selectTagByid(Integer id) throws IOException;

    //获取前10条标签博客排行
    List<Tag> selectAllWithCount();

    //获取所有标签博客排行
    List<Tag> selectAllTagsWithCount();



    //管理-查询名称模糊查询所有
    public List<Tag> selectAllByLikeName(Integer pageNum, Integer pageSize, String name);

    //管理页面-查询
    public  List<Tag> selectAllTag(Integer pageNum, Integer pageSize, String name);

    //管理页面-查询的类型是否重复
    Tag selectByName(String name);

    //管理页面-添加类型
    public Boolean save(Tag tag);

    //管理页面-进入添加修改页  查询当前类型
    Tag selectById(Integer id);

    //管理页面-修改
    public Boolean update(Tag tag);

    //管理页面-删除
    public Boolean deleteById(Integer id);
}
