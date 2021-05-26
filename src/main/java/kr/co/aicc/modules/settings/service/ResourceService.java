package kr.co.aicc.modules.settings.service;

import java.util.List;

import kr.co.aicc.modules.settings.dto.ResourceDto;

public interface ResourceService {

    List<ResourceDto> findResList(ResourceDto resourceDto);
    
    int resOrdUp(ResourceDto resourceDto);
    
    int resOrdDown(ResourceDto resourceDto);
    
    int createRes(ResourceDto resourceDto);
    
    int updateRes(ResourceDto resourceDto);
    
    int findDeleteResYn(ResourceDto resourceDto);
    
    int deleteRes(ResourceDto resourceDto);
}
