package kr.co.aicc.modules.settings.service;

import java.util.List;

import kr.co.aicc.modules.settings.dto.ChnlTeamDto;
import kr.co.aicc.modules.settings.dto.MemGrpDto;

public interface TeamService {
    
    List<MemGrpDto> findMemGrpList(MemGrpDto memGrpDto);
    int findMemGrpListCnt(MemGrpDto memGrpDto);
    List<MemGrpDto> findMemList(MemGrpDto memGrpDto);
    int findMemListCnt(MemGrpDto memGrpDto);
    List<MemGrpDto> getGrpMemInfo(MemGrpDto memGrpDto);
    int createGrpMem(MemGrpDto memGrpDto);
    
    List<ChnlTeamDto> findChnlTeamList(ChnlTeamDto chnlTeamDto);
    int findChnlTeamListCnt(ChnlTeamDto chnlTeamDto);
    int createChnlTeam(ChnlTeamDto chnlTeamDto);
    
 
}
