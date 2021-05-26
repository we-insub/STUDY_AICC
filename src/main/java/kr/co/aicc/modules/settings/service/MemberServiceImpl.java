package kr.co.aicc.modules.settings.service;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.domain.Role;
import kr.co.aicc.modules.settings.dto.MemberForm;
import kr.co.aicc.modules.settings.repository.SettingsDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final SettingsDao settingsDao;
    private final ModelMapper modelMapper;

	@Override
	public List<MemberForm> selectMemList(MemberForm memberForm) {
		
		List<MemberForm> memberList = settingsDao.selectMemList(memberForm);

		for (int i=0; i<memberList.size(); i++) {
			
			Iterator<Role> itr = memberList.get(i).getMemRoles().iterator();
			String memRoles = "";
			while (itr.hasNext()) {
				if (memRoles == "") {
					memRoles = itr.next().getRoleNm();
				} else {
					memRoles = memRoles + ", " + itr.next().getRoleNm();
				}				
			}
			memberList.get(i).setMemRolesString(memRoles);
			
			if ("".equals(memberList.get(i).getBrthday()) || memberList.get(i).getBrthday() == null) {
				memberList.get(i).setAge(0);
			} else {
				memberList.get(i).setAge(getAge(memberList.get(i).getBrthday()));				
			}
			
			String zipNo = memberList.get(i).getZipNo();
			String baseAddr = memberList.get(i).getBaseAddr();
			String dtlAddr = memberList.get(i).getDtlAddr();			
			String fulladdr = "";
			
			if ("".equals(zipNo) || zipNo == null) {
				fulladdr = "";
			} else {
				fulladdr = "(" + zipNo + ") " + baseAddr;
//				fulladdr = "(" + zipNo + ") " + baseAddr + " " + dtlAddr;
			}
			memberList.get(i).setFullAddr(fulladdr);
			
			String brthday = memberList.get(i).getBrthday();
			memberList.get(i).setBrthday(brthday.substring(0,4) + "-" + brthday.substring(4,6) + "-" + brthday.substring(6,8));
		}
		
		return memberList;
	}

	public int getAge(String pBrthDay) {
		int brthYear = Integer.parseInt(pBrthDay.substring(0,4));
		int brthMonth = Integer.parseInt(pBrthDay.substring(4,6));
		int brthDay = Integer.parseInt(pBrthDay.substring(6,8));
		
        Calendar current = Calendar.getInstance();
        int currentYear  = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay   = current.get(Calendar.DAY_OF_MONTH);
       
        int age = currentYear - brthYear;
        // 생일 안 지난 경우 -1
        if (brthMonth * 100 + brthDay > currentMonth * 100 + currentDay) {
        	age--;
        }            
       
        return age;
	}
	
	@Override
	public int updateMember(MemberForm memberForm) {
        Account account = modelMapper.map(memberForm, Account.class);
        
        String[] memRoles = memberForm.getRole().split(",");
        		
        settingsDao.deleteRoleByMemNo(memberForm.getMemNo());
        
        for (int i=0; i<memRoles.length; i++) {
            Role role = new Role(); 
            role.setRoleNo(Long.parseLong(memRoles[i]));
            role.setMemNo(memberForm.getMemNo());
            role.setCretr(memberForm.getCretr());
            role.setChgr(memberForm.getChgr());
            settingsDao.createRoleByMemNo(role);
        }
        account.setBrthday(account.getBrthday().replaceAll("-", ""));

        log.debug(" settingsDao.updateMember   account: {}", account);
        
        if ("03".equals(account.getStat())) {
        	account.setWdlRsnCd("99"); //기타
        }
        
		return settingsDao.updateMember(account);
	}

	@Override
	public int selectMemListCnt(MemberForm memberForm) {
		return settingsDao.selectMemListCnt(memberForm);
	}

	@Override
	public int deleteMember(MemberForm memberForm) {
		memberForm.setStat("03");	//탈퇴
		memberForm.setWdlRsnCd("99"); //기타
		return settingsDao.deleteMember(memberForm);
	}
}
