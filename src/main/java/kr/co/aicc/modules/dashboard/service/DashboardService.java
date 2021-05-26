package kr.co.aicc.modules.dashboard.service;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.dashboard.domain.Channel;
import kr.co.aicc.modules.dashboard.domain.DashboardRow;
import kr.co.aicc.modules.dashboard.dto.ChannelForm;
import kr.co.aicc.modules.dashboard.dto.DashBoardForm;
import kr.co.aicc.modules.dashboard.dto.WorkStatusForm;

import java.util.*;

public interface DashboardService {

    public List<Channel> workStatus(WorkStatusForm workStatusForm, Account account);
    public void createChnl(ChannelForm channelForm) throws Exception;
    public HashMap<String, Object> getDashBoardData(DashBoardForm dashBoardForm);
}
