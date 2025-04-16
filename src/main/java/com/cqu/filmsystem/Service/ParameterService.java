package com.cqu.filmsystem.Service;

import com.cqu.filmsystem.pojo.Parameter;

public interface ParameterService {


    Parameter select();

    int  update(Parameter parameter);

}
