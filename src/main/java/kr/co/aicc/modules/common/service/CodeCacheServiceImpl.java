/*
 * AICC (AI Caption Center) version 1.0
 *
 *  Copyright ⓒ 2020 sorizava corp. All rights reserved.
 *
 *  This is a proprietary software of sorizava corp, and you may not use this file except in
 *  compliance with license agreement with sorizava corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of sorizava corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *
 * @package kr.co.aicc.modules.common.service
 * @file CommonServiceImpl.java
 * @author developer
 * @since 2020. 06. 08
 * @version 1.0
 * @title AICC
 * @description
 *
 *
 * << 개정이력 (Modification Information) >>
 *
 *   수정일		수정자	수정내용
 * -------------------------------------------------------------------------------
 * 2020. 06. 08	developer		 최초생성
 * -------------------------------------------------------------------------------
 */
package kr.co.aicc.modules.common.service;

import kr.co.aicc.infra.enums.CommonEnum;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.domain.Role;
import kr.co.aicc.modules.common.repository.CommonDao;
import kr.co.aicc.modules.settings.domain.Code;
import kr.co.aicc.modules.settings.dto.ResourceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CodeCacheServiceImpl implements CodeCacheService {
    private final CommonDao commonDao;

    @Override
    public List<Code> getCodeList() {
        return commonDao.getCodeList();
    }

    @Override
    public List<Code> getCode(String grpCd) {
        if(grpCd == null) return null;

        List<Code> result = new ArrayList<Code>();
        List<Code> codeList = this.getCodeList();

        Iterator<Code> iterator = codeList.iterator();
        while (iterator.hasNext()) {
            Code code = (Code) iterator.next();
            if(grpCd.equals(code.getGrpCd())) {
                result.add(code);
            }
        }
        return result;
    }

	@Override
	public List<String> getYearList() {
    	Calendar yyyy = Calendar.getInstance();
    	int year = yyyy.get(Calendar.YEAR);
    	int num = 50;    	
    	
    	List<String> yearList = new ArrayList<>();
    	for (int i=0; i<num; i++) {
    		if (i==0) {
        		yearList.add(i,Integer.toString(year));    			
    		}
    		year = year - 1;
    		yearList.add(i,Integer.toString(year));
    	}
    	Collections.sort(yearList, Collections.reverseOrder());
    	
		return yearList;
	}

	@Override
	public List<String> getMonthList() {
    	List<String> monthList = new ArrayList<>();
    	int month = 0;
    	for (int i=0; i<12; i++) {
    		month = month + 1;
    		if (month < 10) {
        		monthList.add(i, "0"+Integer.toString(month));    			
    		} else {
        		monthList.add(i, Integer.toString(month));
    		}
    	}
    	Collections.sort(monthList);
    	
		return monthList;
	}

	@Override
	public List<String> getDayList() {
    	List<String> dayList = new ArrayList<>();
    	int day = 0;
    	for (int i=0; i<31; i++) {
    		day = day + 1;
    		if (day < 10) {
        		dayList.add(i, "0"+Integer.toString(day));    			
    		} else {
        		dayList.add(i, Integer.toString(day));
    		}
    	}
    	Collections.sort(dayList);
    	
		return dayList;
	}

	@Override
	public List<Role> findRoleList() {
		return commonDao.findRoleList();
	}

	@Override
	public List<ResourceDto> findResList() {
		List<ResourceDto> result = commonDao.findResList();

		for (int i=0; i<result.size(); i++) {
			if ("1".equals(result.get(i).getResLvl())) {
				if (result.get(i).getResNo() == 1) {
					result.get(i).setIcon("icon-dashboard");
				} else if (result.get(i).getResNo() == 2) {
					result.get(i).setIcon("icon-templates");
				} else if (result.get(i).getResNo() == 3) {
					result.get(i).setIcon("icon-settings");					
				} else {
					result.get(i).setIcon("icon-settings");					
				}				
			}
		}
		return result;
	}

	@Override
	public List<ResourceDto> findResList(Account account) {
		List<ResourceDto> result = commonDao.findResList(account);

		for (int i=0; i<result.size(); i++) {
			if ("1".equals(result.get(i).getResLvl())) {
				if ("대시보드".equals(result.get(i).getResNm())) {
					result.get(i).setIcon("icon-dashboard");
				} else if ("방송스케쥴".equals(result.get(i).getResNm())) {
					result.get(i).setIcon("icon-templates");
				} else if ("settings".equals(result.get(i).getResNm())) {
					result.get(i).setIcon("icon-settings");					
				} else {
					result.get(i).setIcon("icon-settings");					
				}				
			}
		}
		return result;
	}	

	@Override
	public ResourceDto findResSel(String requestUrl) {
		ResourceDto result = commonDao.findResSel(requestUrl);

		return result;
	}	
}
