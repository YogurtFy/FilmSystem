package com.cqu.filmsystem.Service.Impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.Mapper.UserMapper;
import com.cqu.filmsystem.Service.UserService;
import com.cqu.filmsystem.pojo.UserInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper  userMapper;

    @Override
    public UserInfo login(String email) throws IOException {
        //读取mybatis-config.xml文件内容到reader对象中
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        //初始化mybatis数据库,创建SqlSessionFactory类的实例
        SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
        //创建SqlSession实例
        SqlSession session = sqlMapper.openSession(true);
        //为测试接口，所以需获取接口的对象，因当前接口不存在实现类，所以获取的是该接口的动态代理对象
        UserMapper mapper = session.getMapper(UserMapper.class);
        return mapper.login(email);
    }

    @Override
    public Integer register(String email, String password) throws IOException {
        //读取mybatis-config.xml文件内容到reader对象中
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        //初始化mybatis数据库,创建SqlSessionFactory类的实例
        SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
        //创建SqlSession实例
        SqlSession session = sqlMapper.openSession(true);
        //为测试接口，所以需获取接口的对象，因当前接口不存在实现类，所以获取的是该接口的动态代理对象
        UserMapper mapper = session.getMapper(UserMapper.class);
        Map<String,Object> user = new HashMap<>();
        user.put("email",email);
        user.put("password",password);
        return mapper.register(user);
    }


    @Override
    public void updateNickname(Long id, String nickname) {
        userMapper.updateNickname(id, nickname);
    }

    @Override
    public void updatePassword(Long id, String password) {
        userMapper.updatePassword(id, password);

    }

    @Override
    public void updateImage(Long id, String url) {
        userMapper.updateImage(id, url);
    }

    @Override
    public List<UserInfo> selectAllUser() {
        return userMapper.selectAllUser();
    }

    @Override
    public UserInfo selectById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public PageInfo<UserInfo> selectAll(Integer pageNum, Integer pageSize, String title) {
        title= "%"+title+"%";
        PageHelper.startPage (pageNum, pageSize);
        List<UserInfo> userInfos = userMapper.selectAll(title);
        PageInfo<UserInfo> pageInfo = new PageInfo<> (userInfos, 5);
        return  pageInfo;
    }

    @Override
    public int update(UserInfo userInfo) {
        return userMapper.update(userInfo);
    }


}
