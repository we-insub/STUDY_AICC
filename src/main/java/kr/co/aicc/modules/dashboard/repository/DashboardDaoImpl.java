package kr.co.aicc.modules.dashboard.repository;

import kr.co.aicc.modules.dashboard.domain.Channel;
import kr.co.aicc.modules.dashboard.domain.DashBoard;
import kr.co.aicc.modules.dashboard.domain.DashBoardTotal;
import kr.co.aicc.modules.dashboard.domain.DashBoardType;
import kr.co.aicc.modules.dashboard.dto.DashBoardForm;
import kr.co.aicc.modules.dashboard.dto.WorkStatusForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DashboardDaoImpl implements DashboardDao {

    private final DashboardMapper dashboardMapper;

    @Override
    public void insertChannel(Channel channel) {
        dashboardMapper.insertChannel(channel);
    }

    @Override
    public List<Channel> selectChannel(WorkStatusForm workStatusForm) {
        return dashboardMapper.selectChannel(workStatusForm);
    }

    @Override
    public List<Channel> selectChannel() {
        return dashboardMapper.selectChannel(new WorkStatusForm());
    }

    @Override
    public List<DashBoard> selectDashBoardChnlList(DashBoard dashBoard) {
        return dashboardMapper.selectDashBoardChnlList(dashBoard);
    }

    @Override
    public DashBoardTotal selectDashBoardTotal(DashBoard dashBoard) {
        return dashboardMapper.selectDashBoardTotal(dashBoard);
    }

    @Override
    public List<DashBoardType> selectDashBoardType(DashBoard dashBoard) {
        return dashboardMapper.selectDashBoardType(dashBoard);
    }
}
