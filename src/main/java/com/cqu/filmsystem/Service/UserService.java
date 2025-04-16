package com.cqu.filmsystem.Service;

import com.github.pagehelper.PageInfo;
import com.cqu.filmsystem.pojo.Type;
import com.cqu.filmsystem.pojo.UserInfo;
import com.cqu.filmsystem.test.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


public interface UserService {
    UserInfo login(String email ) throws IOException;

    Integer register(String email, String password) throws IOException;

    void updateNickname(Long id, String nickname);

    void updatePassword(Long id, String password);

    void updateImage(Long id, String url);

    List<UserInfo> selectAllUser();


//                         管理员

    UserInfo selectById(int id );

    PageInfo<UserInfo> selectAll(Integer pageNum, Integer pageSize, String title);

    int update(UserInfo userInfo);










}
