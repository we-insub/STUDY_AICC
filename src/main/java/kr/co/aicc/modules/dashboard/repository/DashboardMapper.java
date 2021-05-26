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
 * @package kr.co.aicc.modules.dashboard.repository
 * @file DashboardMapper.java
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
package kr.co.aicc.modules.dashboard.repository;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.dashboard.domain.Channel;
import kr.co.aicc.modules.dashboard.domain.DashBoard;
import kr.co.aicc.modules.dashboard.domain.DashBoardTotal;
import kr.co.aicc.modules.dashboard.domain.DashBoardType;
import kr.co.aicc.modules.dashboard.dto.DashBoardForm;
import kr.co.aicc.modules.dashboard.dto.WorkStatusForm;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DashboardMapper {

    public int insertChannel(Channel channel);

    List<Channel> selectChannel(WorkStatusForm workStatusForm);

    List<DashBoard> selectDashBoardChnlList(DashBoard dashBoard);

    DashBoardTotal selectDashBoardTotal(DashBoard dashBoard);

    List<DashBoardType> selectDashBoardType(DashBoard dashBoard);
}
