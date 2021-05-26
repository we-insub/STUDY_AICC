package kr.co.aicc.modules.settings.service;

import kr.co.aicc.modules.settings.dto.MemberForm;

import java.util.*;

public interface MemberService {    
	
    List<MemberForm> selectMemList(MemberForm memberForm);
    
    int selectMemListCnt(MemberForm memberForm);
    
    int updateMember(MemberForm memberForm);
    
    int deleteMember(MemberForm memberForm);
}
