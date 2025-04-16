package com.cqu.filmsystem.Mapper;

import com.cqu.filmsystem.pojo.Type;
import com.cqu.filmsystem.pojo.UserInfo;
import com.cqu.filmsystem.pojo.UserRecommend;
import com.cqu.filmsystem.test.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface UserMapper {

     UserInfo login(String email);

     Integer register(Map<String,Object> user);

     List<UserInfo> selectAllUser();


     void updateNickname(@Param ("id") Long id, @Param ("nickname") String nickname);

     void updatePassword(@Param ("id") Long id, @Param ("password") String password);

     void updateImage(@Param ("id") Long id, @Param ("url") String url);



     UserInfo selectById(int id );

     List<UserInfo> selectAll(@Param("title") String title);

     int update(UserInfo userInfo);




}
