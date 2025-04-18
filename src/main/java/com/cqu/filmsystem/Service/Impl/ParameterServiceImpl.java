package com.cqu.filmsystem.Service.Impl;

import com.cqu.filmsystem.Mapper.ParameterMapper;
import com.cqu.filmsystem.Service.ParameterService;
import com.cqu.filmsystem.pojo.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    ParameterMapper parameterMapper;

    @Override
    public Parameter select() {
        return parameterMapper.select();
    }

    @Override
    public int update(Parameter parameter) {
        return parameterMapper.update(parameter);
    }
}
