package kr.co.aicc.modules.dashboard.service;

import kr.co.aicc.infra.config.AppProperties;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.dashboard.domain.*;
import kr.co.aicc.modules.dashboard.dto.ChannelForm;
import kr.co.aicc.modules.dashboard.dto.DashBoardForm;
import kr.co.aicc.modules.dashboard.dto.WorkStatusForm;
import kr.co.aicc.modules.dashboard.repository.DashboardDao;
import kr.co.aicc.modules.schedule.repository.ScheduleDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardDao dashboardDao;
    private final ScheduleDao scheduleDao;
    private final AppProperties appProperties;
    @Override
    public List<Channel> workStatus(WorkStatusForm workStatusForm, Account account) {
        workStatusForm.setAccount(account);
        List<Channel> list = dashboardDao.selectChannel(workStatusForm);
        for (Channel channel : list) {
            channel.setThumbSysFileNm(channel.getFilePath().replace(appProperties.getUploadDirChannel(), "/file/image/channel") +
                    "/" + channel.getThumbSysFileNm()
            );
        }
        return list;
    }

    @Override
    public void createChnl(ChannelForm channelForm) throws Exception {
        Channel channel = Channel
                .builder()
                .chnlNm(channelForm.getChnlNm())
                .chnlDesc(channelForm.getChnlDesc())
                .linkUrl(channelForm.getLinkUrl())
                .build();
        dashboardDao.insertChannel(channel);
    }

    @Override
    public HashMap<String, Object> getDashBoardData(DashBoardForm dashBoardForm) {
        log.debug("getDashBoardData start", dashBoardForm);
        HashMap<String, Object> resMap = new HashMap<String, Object>();
        DashBoard dashBoard = DashBoard.builder().build();
        if (! ObjectUtils.isEmpty(dashBoardForm.getChnl())) {
            dashBoard.setPChnlNo(new ArrayList<String>(dashBoardForm.getChnl()));
        }
        if (! ObjectUtils.isEmpty(dashBoardForm.getGroupCode())) {
            dashBoard.setPGroupCode(new ArrayList<String>(dashBoardForm.getGroupCode()));
        }
        if (! ObjectUtils.isEmpty(dashBoardForm.getStartTime())) {
            dashBoard.setStartTime(LocalDateTime.parse(dashBoardForm.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        if (! ObjectUtils.isEmpty(dashBoardForm.getEndTime())) {
            dashBoard.setEndTime(LocalDateTime.parse(dashBoardForm.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        if (! ObjectUtils.isEmpty(dashBoardForm.getDashBoardType())) {
            dashBoard.setDashBoardType(dashBoardForm.getDashBoardType());
        }
        /* 그룹, 채널 코드 가져오기 */
        List<DashBoardType> dashBoardTypes = dashboardDao.selectDashBoardType(dashBoard);
        resMap.put("dashBoardTypes",dashBoardTypes);
        /* 채널 리스트 가져오기 */
        Account account = null;
        List<Channel> channelList = scheduleDao.selectChannelAllList();
        resMap.put("channelList",channelList);
        /* 대시보드 전체합계 구하기 */
        DashBoardTotal dashBoardTotal = dashboardDao.selectDashBoardTotal(dashBoard);
        resMap.put("dashBoardTotal", dashBoardTotal);
        /* 대시보드 리스트 가져오기 */
        List<DashBoard> dashBoardList = dashboardDao.selectDashBoardChnlList(dashBoard);
        resMap.put("dashBoardList", dashBoardList);
        resMap.put("dashBoardForm", dashBoardForm);



        return resMap;
    }

}
