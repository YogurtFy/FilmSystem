package com.cqu.filmsystem.Mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cqu.filmsystem.pojo.Parameter;


@Mapper
public interface ParameterMapper {

    Parameter select();

    int update(Parameter parameter);


}
