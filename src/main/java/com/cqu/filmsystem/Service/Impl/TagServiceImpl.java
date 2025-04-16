package com.cqu.filmsystem.Service.Impl;

import com.github.pagehelper.PageHelper;
import com.cqu.filmsystem.Mapper.MoviceMapper;
import com.cqu.filmsystem.Mapper.TagMapper;
import com.cqu.filmsystem.Service.TagService;
import com.cqu.filmsystem.pojo.Movice;
import com.cqu.filmsystem.pojo.MovieTag;
import com.cqu.filmsystem.pojo.Tag;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


@Service
public class TagServiceImpl implements TagService {


    @Autowired
    TagMapper tagMapper;

    @Autowired
    private MoviceMapper moviceMapper;

    @Override
    public List<Tag> selectAll() {
        return tagMapper.selectAll();
    }

    @Override
    public int UserAddMovieTag(int moviceId, int tagId, int userId) {
        return tagMapper.UserAddMovieTag(moviceId,tagId,userId);
    }

    @Override
    public List<MovieTag> selectUserMovieTagLive(int moviceId, int tagId, int userId) {
        return tagMapper.selectUserMovieTagLive(moviceId,tagId,userId);
    }

    @Override
    public List<Tag> selectTagByUserId(int userId, int moviceId) {
        return tagMapper.selectTagByUserId(userId,moviceId);
    }

    @Override
    public int deleteByThreeID(int moviceId, int tagId, int userId) {
        return tagMapper.deleteByThreeID(moviceId,tagId,userId);
    }

    @Override
    public List<Tag> selectTag() throws IOException {
        return tagMapper.selectTag(null);
    }

    @Override
    public Tag selectTagByid(Integer id) throws IOException {
        //读取mybatis-config.xml文件内容到reader对象中
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        //初始化mybatis数据库,创建SqlSessionFactory类的实例
        SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
        //创建SqlSession实例
        SqlSession session = sqlMapper.openSession(true);
        TagMapper mapper = session.getMapper(TagMapper.class);
        return mapper.selectTagByid(id);
    }

    @Cacheable
    @Override
    public List<Tag> selectAllTagsWithCount() {

        List<Tag> tags = tagMapper.selectAllWithCount();
        List<Tag> tagList = tagMapper.selectByOtherTags();
        if (tagList != null && tagList.size () > 0){
            for (Tag tag : tagList) {
                tags.add (tag);
            }
        }
        return tags;
    }

    @Override
    public List<Tag> selectAllByLikeName(Integer pageNum, Integer pageSize, String name) {
        return null;
    }

    @Cacheable
    @Override
    public List<Tag> selectAllWithCount() {

        List<Tag> tags = tagMapper.selectAllWithCount();

        if (tags.size () < 10){  //小于10个标签，获取没有指定博客的标签
            int count = 10 -tags.size ();
            List<Tag> tagList = tagMapper.selectByOther(count);
            for (Tag tag : tagList) {
                tags.add (tag);
            }
        }
        else if (tags.size () > 10) {  //大于10个标签，截取10个
            List<Tag> tagList = new ArrayList<>();
            for (int i = 0; i<10; i++){
                tagList.add (tags.get (i));
            }
            tags = tagList;
        }
        return tags;
    }


    //-------------------------------------------------管理员----------------------------------------------------
    @Override
    public List<Tag> selectAllTag(Integer pageNum, Integer pageSize, String name) {

        if(name.equals(""))
        {
            PageHelper.startPage (pageNum, pageSize);
            List<Tag> tags = tagMapper.selectTag(null);
            return tags;
        }

        String myname ="%"+name+"%";
        PageHelper.startPage (pageNum, pageSize);
        List<Tag> tags = tagMapper.selectTag(myname);
        return tags;
    }

    @Override
    public Tag selectByName(String name) {
        return tagMapper.selectByName(name);
    }

    //管理页面-增添操作
    @CacheEvict(allEntries = true)
    @Override
    public Boolean save(Tag tag) {
        boolean flag = true;
        try {
            tagMapper.insert (tag);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace ();
        }
        return flag;
    }

    @Override
    public Tag selectById(Integer id) {
        return tagMapper.selectByPrimaryKey(id);
    }


    @CacheEvict(allEntries = true)
    @Override
    public Boolean update(Tag tag) {
        boolean flag = true;
        try {
            tagMapper.updateByPrimaryKeySelective(tag);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace ();
        }
        return flag;
    }



    @CacheEvict(allEntries = true)
    @Override
    public Boolean deleteById(Integer id) {
        boolean flag = true;
        try {
            int count = tagMapper.selectCountByTagIdForBlogAndTag(id);
            if (count != 0){
                flag = false;
            } else {
                tagMapper.deleteByPrimaryKey (id);
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace ();
        }
        return flag;
    }


}
