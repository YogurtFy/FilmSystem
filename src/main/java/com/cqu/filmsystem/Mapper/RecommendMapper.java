package com.cqu.filmsystem.Mapper;

import com.cqu.filmsystem.pojo.UserRecommend;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper    //这个注解表示这个是mybatis的mapeper
@Repository
public interface RecommendMapper {


    List<UserRecommend>  userBrowsingHistory(String username);


}