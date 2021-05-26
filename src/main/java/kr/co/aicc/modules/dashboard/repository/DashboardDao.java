package kr.co.aicc.modules.dashboard.repository;

import kr.co.aicc.modules.dashboard.domain.Channel;
import kr.co.aicc.modules.dashboard.domain.DashBoard;
import kr.co.aicc.modules.dashboard.domain.DashBoardTotal;
import kr.co.aicc.modules.dashboard.domain.DashBoardType;
import kr.co.aicc.modules.dashboard.dto.DashBoardForm;
import kr.co.aicc.modules.dashboard.dto.WorkStatusForm;

import java.util.*;

public interface DashboardDao {

    /**
     * 채널 인서트
     * @param channel
     */
    public void insertChannel(Channel channel);

    /**
     * 채널 전체 검색
     */
    public List<Channel> selectChannel(WorkStatusForm workStatusForm);

    public List<Channel> selectChannel();

    List<DashBoard> selectDashBoardChnlList(DashBoard dashBoard);

    DashBoardTotal selectDashBoardTotal(DashBoard dashBoard);

    List<DashBoardType> selectDashBoardType(DashBoard dashBoard);
}
