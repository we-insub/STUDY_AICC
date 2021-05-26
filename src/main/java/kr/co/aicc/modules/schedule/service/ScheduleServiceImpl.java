package kr.co.aicc.modules.schedule.service;

import kr.co.aicc.infra.config.AppProperties;
import kr.co.aicc.infra.enums.CommonEnum;
import kr.co.aicc.infra.enums.ResponseEnum;
import kr.co.aicc.infra.enums.SchedTypeEnum;
import kr.co.aicc.infra.exception.BizException;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.dashboard.domain.*;
import kr.co.aicc.modules.schedule.domain.ChnlTeam;
import kr.co.aicc.modules.schedule.domain.GroupTeam;
import kr.co.aicc.modules.schedule.domain.ScheduleFile;
import kr.co.aicc.modules.schedule.domain.TimeLine;
import kr.co.aicc.modules.schedule.dto.*;
import kr.co.aicc.modules.schedule.repository.ScheduleDao;
import kr.co.aicc.modules.webfos.dto.MemberInfo;
import kr.co.aicc.modules.webfos.dto.ProgramInfo;
import kr.co.aicc.modules.webfos.dto.SubtitleDetailInfo;
import kr.co.aicc.modules.webfos.service.WebfosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;
import org.apache.tika.Tika;
import org.apache.tomcat.jni.Local;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleDao scheduleDao;
    private final AppProperties appProperties;
    private final WebfosService webfosService;

    @Override
    public boolean createTestData(Account account) {

        /*if (programInfos.size() > 0) {
            Iterator<ProgramInfo> iterator = programInfos.iterator();
            while (iterator.hasNext()) {
                ProgramInfo programInfo = (ProgramInfo) iterator.next();
                List<MemberInfo> memberInfos = programInfo.getMemberInfoList();
                if (memberInfos.size() > 0) {
                    for (MemberInfo memberInfo : memberInfos) {
                        if (memberInfo.getMemNo().longValue() == account.getMemNo().longValue()) {
                            if (memberInfo.isParticipation()) {

                            }
                        }
                    }
                }
            }
        }*/


        //stream

    	/*LocalDateTime fromLocalDateTime = LocalDateTime.now();
        LocalDateTime toLocalDateTime = LocalDateTime.now().plusDays(1);
        int fromYear = fromLocalDateTime.getYear();
        int fromMonth = fromLocalDateTime.getMonthValue();
        int fromDay = fromLocalDateTime.getDayOfMonth();
        int toYear = toLocalDateTime.getYear();
        int toMonth = toLocalDateTime.getMonthValue();
        int toDay = toLocalDateTime.getDayOfMonth();
        log.debug(fromYear+"-"+fromMonth+"-"+fromDay+" 테스트 데이터 삽입 스따뜨~!!");



        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        LocalDateTime workStartDateTime = null;
        LocalDateTime workEndDateTime = null;
        int totalMinutes = 1020; // 17시간을 분으로 작성 하루 전체 분량
        String schedType = "";
        double randomValue = Math.random();
        long seed = System.currentTimeMillis();
        int schedNo = 0;
        Random random = new Random(seed);
        int chnlLen = 11;
        List<Channel> channelList = scheduleDao.selectChannelAllList();

        int programIdx = random.nextInt(107) +7;
        int totalWorkTime = 0; // 프로그램작업분량
        int totalWorkTimeBase = 0;
        int workTime = 0; // 개인작업분량
        ArrayList<MemberSchedule> memSchList = null;
        int schMemberflow = 0; // 0 : 디폴트 구조, 1 : 메인과 부사수 구조
        int restMemberFlow = 0; // 0 : 휴식 , 1 : 식사 , 2 : 작업

        for (Channel channel : channelList) {
            List<ChnlTeam> chnlTeam = channel.getChnlTeams();
            startDateTime = LocalDateTime.of(fromYear, fromMonth, fromDay, 8, 0); // 편성은 매일 오전 8시부터 시작
            endDateTime = LocalDateTime.of(toYear, toMonth, toDay, 7, 0); // 다음날 새벽 1시에 종료
            while (true){
                Schedule schedule = new Schedule();
                schedule.setChnlNo(channel.getChnlNo());
                programIdx = random.nextInt(100); // 프로그램 선택
                schedule.setProgNm("랜덤 프로그램" + programIdx);
                schedType = String.format("%02d", random.nextInt(4)+1); // 랜덤으로 스케줄 타입을 정함
                schedule.setSchedType(SchedTypeEnum.findValue(schedType));
                schedule.setBaseDate(String.format("%d%02d%02d",fromYear, fromMonth, fromDay));
                totalWorkTime = (random.nextInt(4) + 3) * 10; // 랜덤으로 편성 시간을 구한다 30~60분
                schedule.setBgnTime(startDateTime);
                workStartDateTime = startDateTime;
                startDateTime = startDateTime.plusMinutes(totalWorkTime);
                workEndDateTime = workStartDateTime.plusMinutes(totalWorkTime);
                schedule.setEndTime(startDateTime);
                schedule.setAccount(account);
                scheduleDao.insertSchedule(schedule);
                log.debug("schedule 세팅 완료" + schedule);
                if (startDateTime.isBefore(endDateTime)) {
                    // 작업자 세팅
                    log.debug("작업자 세팅 시작" + startDateTime.isBefore(endDateTime));
                    memSchList = new ArrayList<MemberSchedule>();
                    schMemberflow = random.nextInt(2);
                    restMemberFlow = random.nextInt(3);
                    int memSchIdx = 1;
                    while (true) {
                        MemberSchedule memSch = new MemberSchedule();
                        if (schMemberflow == 0 && memSchList.size() == 0) {
                            if (restMemberFlow == 0) {
                                // 휴식
                                memSch.setSchedGb("R");
                            } else if (restMemberFlow == 1) {
                                // 식사
                                memSch.setSchedGb("E");
                            } else if (restMemberFlow == 2) {
                                // 작업
                                memSch.setSchedGb("W");
                            }
                            memSch.setBgnTime(workStartDateTime);
                            memSch.setEndTime(workEndDateTime);
                            memSch.setWorkAmt(String.format("%d",totalWorkTime));
                        } else {
                            if ((totalWorkTime < 5)) {
                                break;
                            } else {
                                totalWorkTimeBase = (totalWorkTime >= 5) ? totalWorkTime/5:0;
                                workTime = (totalWorkTimeBase > 2) ? ((random.nextInt(totalWorkTimeBase - 1) + 1) * 5):5;
                                totalWorkTime = totalWorkTime - workTime;
                                memSch.setBgnTime(workStartDateTime);
                                workStartDateTime = workStartDateTime.plusMinutes(workTime);
                                memSch.setEndTime(workStartDateTime);
                                memSch.setWorkAmt(String.format("%d",workTime));
                                memSch.setSchedGb("W");
                            }
                        }

                        memSch.setBaseDate(String.format("%d%02d%02d",fromYear, fromMonth, fromDay));
                        if (chnlTeam.size() == 0) {
                            break;
                        }
                        memSch.setMemNo(chnlTeam.get(random.nextInt(chnlTeam.size())).getMemNo());
                        memSch.setOrd(memSchIdx);
                        memSch.setCretr(account.getMemNo());
                        // 작업일정에만 스케줄 정보를 입력
                        if (memSch.getSchedGb().equals("W")) {
                            memSch.setSchedNo(schedule.getSchedNo());
                        }
                        memSchList.add(memSch);
                        memSchIdx++;
                    }
                    // 작업일정 인서트
                    if (0 < memSchList.size()) {
                        scheduleDao.insertMemScheduleList(memSchList);
                    }
                    continue;
                } else { // 종료시간 오버됨
                    break;
                }
            }
        }*/
        return false;
    }

    @Override
    public HashMap<String, Object> getTimeLineData(TimeLineForm timeLineForm, @CurrentAccount Account account) throws BizException {
        log.debug("getTimeLineData : ", timeLineForm, account);
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        TimeLine pTimeLine = TimeLine.builder()
                .account(account)
                .chnlNos(timeLineForm.getChnlNos())
                .build();

        if (! ObjectUtils.isEmpty(timeLineForm.getCurday())) {
            pTimeLine.setCurDay(LocalDate.parse(timeLineForm.getCurday(), DateTimeFormatter.ofPattern("yyyyMMdd")));
            LocalDateTime curDayTime = LocalDate.parse(timeLineForm.getCurday(), DateTimeFormatter.ofPattern("yyyyMMdd")).atTime(8,0,0,0);
            pTimeLine.setSearchStartTime(curDayTime);
            pTimeLine.setSearchEndTime(curDayTime.plusDays(1));
        }

        // 편성표 데이터 가져오기
        TimeLine timeLine = scheduleDao.selectTimeLineData(pTimeLine);
        LocalDateTime nowTime = LocalDateTime.now();
        // timeLine.channelList 루프 chnlTeams
        // 채널팀

        // 웹포스 버튼 출력 여부 세팅
        boolean isWebposButton = false;
        // 프로필 이미지 경로
        String path = "";
        if (! ObjectUtils.isEmpty(timeLine)) {
            List<Channel> channelList = timeLine.getChannelList();
            for (Channel channel : channelList) {
                if (channel.getChnlNo() > 0) {
                    setWebfosButtonDisplay(channel.getScheduleList(), account, nowTime);
                }
                Iterator<Schedule> scheduleIterator = channel.getScheduleList().iterator();
                while (scheduleIterator.hasNext()) {
                    Schedule schedule = (Schedule) scheduleIterator.next();
                    Iterator<MemberSchedule> msi = schedule.getMemberSchedules().iterator();
                    while (msi.hasNext()) {
                        MemberSchedule memberSchedule = (MemberSchedule) msi.next();
                        if (! ObjectUtils.isEmpty(memberSchedule.getFilePath())) {
                            path = memberSchedule.getFilePath().replace(appProperties.getUploadDirProfile(), "/file/image/profile") +
                                    "/" + memberSchedule.getThumbSysFileNm();
                            memberSchedule.setThumbSysFileNm(path);
                        }
                    }
                }

                // 채널팀 순서세팅
                List<ChnlTeam> chnlTeams = channel.getChnlTeams();
                if (! ObjectUtils.isEmpty(chnlTeams)) {
                    chnlTeams.sort(
                        new Comparator<ChnlTeam>() {
                           @Override
                           public int compare(ChnlTeam arg0, ChnlTeam arg1) {
                               // TODO Auto-generated method stub
                               long age0 = arg0.getMemNo();
                               long age1 = arg1.getMemNo();
                               if(age0 == age1) return 0;
                               else if(age0 > age1) return 1;
                               else return -1;
                           }
                       }
                    );
                }
            }
        }
        List<ProgramInfo> programInfos = webfosService.getProgramInfoListOfWebfos();
        List<Channel> channelList = new ArrayList<Channel>();
        if (! ObjectUtils.isEmpty(timeLine)) {
            channelList = timeLine.getChannelList();
        }


        for (Channel channel : channelList) {
            channel.setWebfosButtonDisplay(programInfos, account);
        }

        hashMap.put("timeLine", timeLine);
        hashMap.put("programInfos", programInfos);
        return hashMap;
    }

    private void setWebfosButtonDisplay(List<Schedule> scheduleList, Account account, LocalDateTime nowTime) {
        log.debug("setWebfosButtonDisplay : ", scheduleList, account);
        boolean isWebposButton = false;
        LocalDateTime start = null;
        LocalDateTime end = null;
        for (Schedule schedule : scheduleList) {
            isWebposButton = false;
            if (schedule.getSchedNo() > 0) {
                for (MemberSchedule memberSchedule : schedule.getMemberSchedules()) {
                    if (memberSchedule.getMemNo() == account.getMemNo()) {
                        isWebposButton = true;
                    }
                }
                // 일정에 포함되어있음
                if (isWebposButton) {
                    // 5분 이전부터 종료까지
                    start = schedule.getBgnTime().minusMinutes(5);
                    end = schedule.getEndTime();
                    if (start.isBefore(nowTime) && end.isAfter(nowTime)) {
                        isWebposButton = true;
                    } else {
                        isWebposButton = false;
                    }
                }
                // 관리자나 매니저면 무조껀 보임
                if (account.hasRole(CommonEnum.Role.ROLE_ADMIN.value()) || account.hasRole(CommonEnum.Role.ROLE_MANAGER.value()) || account.hasRole(CommonEnum.Role.ROLE_WATCH.value())) {
                    isWebposButton = true;
                }
            }
            schedule.setWebposButton(isWebposButton);
        }
    }

    @Override
    public HashMap<String, Object> regSchedule(SchedForm schedForm, Account account) throws BizException {

        log.debug("regSchedule : ", schedForm, account);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", ResponseEnum.Status.SUCCESS);
        map.put("res", ResponseEnum.ErrorCode.AICC_0000_200);
        map.put("method", "add");

        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", ResponseEnum.ErrorCode.AICC_0000_401);
            return map;
        }

        int startM = Integer.parseInt(schedForm.getStartM());
        int endM = Integer.parseInt(schedForm.getEndM());
        int startH = Integer.parseInt(schedForm.getStartH());
        int endH = Integer.parseInt(schedForm.getEndH());

        LocalDate startDate = schedForm.getStartDay();
        LocalDate endDate = schedForm.getEndDay();
        // int diffDayCnt = diffDayCnt(endDate, startDate);
        LocalDateTime startTime = startDate.atTime(startH, startM, 0, 0);
        LocalDateTime endTime = endDate.atTime(endH, endM, 0, 0);

        if (! endTime.isAfter(startTime)) {
            throw new BizException("SchedForm.workTime.beforeAfter");
        }

        Schedule schedule = Schedule.builder()
                .chnlNo(schedForm.getChnlNo())
                .progNm(schedForm.getProgNm())
                .baseDate(startTime.getYear()+""+String.format("%02d", startTime.getMonthValue())+""+String.format("%02d", startTime.getDayOfMonth()))
                .bgnTime(startTime)
                .endTime(endTime)
                .schedType(SchedTypeEnum.findValue(schedForm.getSchedType()))
                .account(account)
                .build();

        if (0 == schedForm.getSchedNo()) {
            scheduleDao.insertSchedule(schedule);
        } else {
            schedule.setSchedNo(schedForm.getSchedNo());
            Schedule oldSchedule = scheduleDao.selectScheduleBySchedNo(schedForm.getSchedNo());
            int diffStart = diffSecondsCnt(schedule.getBgnTime(), oldSchedule.getBgnTime());
            schedForm.setDiffStart(diffStart);
            scheduleDao.updateSchedule(schedule);
        }

        map.put("schedule", schedule);
        map.put("schedForm", schedForm);
        return map;
    }

    @Override
    public HashMap<String, Object> deleteSchedule(SchedForm schedForm, Account account) {
        log.debug("deleteSchedule : ", schedForm, account);

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("status", ResponseEnum.Status.SUCCESS);
        map.put("res", ResponseEnum.ErrorCode.AICC_0000_200);
        map.put("method", "add");

        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", ResponseEnum.ErrorCode.AICC_0000_401);
            return map;
        }

        Schedule schedule = Schedule.builder()
                .chnlNo(schedForm.getChnlNo())
                .baseDate(String.format("%d%02d%02d", schedForm.getToDay().getYear(), schedForm.getToDay().getMonthValue(), schedForm.getToDay().getDayOfMonth()))
                .account(account)
                .build();
        LocalDateTime curDayTime = schedForm.getToDay().atTime(8,0,0,0);
        schedule.setSearchStartTime(curDayTime);
        schedule.setSearchEndTime(curDayTime.plusDays(1));

        if (! ObjectUtils.isEmpty(schedForm.getSchedNo())) {
            schedule.setSchedNo(schedForm.getSchedNo());
        }

        // 삭제 처리 프로그램, 일정, 작업자
        int memSchedDelCount = scheduleDao.updateDelMemSchedByChnlNo(schedule);
        int scheduleDelCount = scheduleDao.updateDelschedByChnlNo(schedule);
        log.debug("삭제 처리 수 = ","스케줄: " + memSchedDelCount + " 작업자: " + scheduleDelCount);

        return map;
    }

    @Override
    public HashMap<String, Object> scheduleExcelUpload(SchedExcelUpload schedExcelUpload, Account account ) {
        log.debug("scheduleExcelUpload start : ", schedExcelUpload);
        LocalDate toDate = LocalDate.parse(schedExcelUpload.getToDay(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<Schedule> scheduleList = new ArrayList<Schedule>();
        Schedule schedule = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        long chnlNo = Integer.parseInt(schedExcelUpload.getChnlNo());
        String oriFileName = "";
        String sysFileName = "";
        String dateStr = "";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", ResponseEnum.Status.SUCCESS);
        map.put("res", ResponseEnum.ErrorCode.AICC_0000_200);
        map.put("method", "add");
        LocalDate dateDir = LocalDate.now();
        String excelDir = appProperties.getUploadDirSchedule()+"/"+dateDir.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", ResponseEnum.ErrorCode.AICC_0000_401);
            return map;
        }
        File file = new File(excelDir);
        if (! file.exists()) {
            log.debug("파일 디렉토리가 없음");
            if (file.mkdirs()) {
                log.debug("디렉토리 생성 성공");
            }
        }

        MultipartFile excelFile = schedExcelUpload.getSchedExcel();
        if (excelFile.isEmpty()) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", "파일을 첨부해 주세요.");
            return map;
        }
        log.debug("getOriginalFilename : " + excelFile.getOriginalFilename());
        log.debug("getSize : "+excelFile.getSize());
        log.debug("getName : "+excelFile.getName());
        log.debug("getContentType : "+excelFile.getContentType());
        log.debug("getResource : "+excelFile.getResource());

        try {
            oriFileName = excelFile.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            sysFileName = uuid.toString()+""+excelFile.getOriginalFilename();
            byte[] data = excelFile.getBytes();
            // utf-8 pom 제거
            if (data[0] == -17 && data[1] == -69 && data[2] == -65) {
                for (int b = 3; b < data.length - 3; b++) {
                    data[b-3] = data[b];
                }
            }
            FileOutputStream fos = new FileOutputStream(excelDir +"/"+ sysFileName);
            fos.write(data);
            fos.close();

            Tika tika = new Tika();
            String mimeType = tika.detect(excelDir +"/"+ sysFileName);
            log.debug("mimeType : " + mimeType);
            if (! (mimeType.indexOf("spreadsheet") > -1)) {
                map.put("status", ResponseEnum.Status.ERROR);
                map.put("res", "알 수 없는 형식의 파일입니다.");
                return map;
            }

            OPCPackage opcPackage = OPCPackage.open(excelFile.getInputStream());
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
            XSSFSheet sheet = workbook.getSheetAt(0);
            // 엑셀 내부 체크
            for (int i = 1, sheetLen = sheet.getLastRowNum() + 1; i < sheetLen; i++) {
                row = sheet.getRow(i);
                cell = row.getCell(0);// 프로그램 명
                if (ObjectUtils.isEmpty(cell)) {
                    continue;
                }
                if (! ObjectUtils.isEmpty(cell)) {
                    if (ObjectUtils.isEmpty(cell.getStringCellValue())) {
                        map.put("status", ResponseEnum.Status.ERROR);
                        map.put("res", (i+1)+"행 프로그램명이 없습니다.");
                        return map;
                    }
                }
                cell = row.getCell(1);// 방송타입
                if (! ObjectUtils.isEmpty(cell)) {
                    if (ObjectUtils.isEmpty(cell.getStringCellValue())) {
                        map.put("status", ResponseEnum.Status.ERROR);
                        map.put("res", (i+1)+"행 방송타입이 없습니다.");
                        return map;
                    }
                    SchedTypeEnum schedTypeEnum = SchedTypeEnum.findDesc(cell.getStringCellValue());
                    if (ObjectUtils.isEmpty(schedTypeEnum)) {
                        map.put("status", ResponseEnum.Status.ERROR);
                        map.put("res", (i+1)+"행 잘못된 방송타입 입니다.");
                        return map;
                    }
                }
                cell = row.getCell(2);// 시작 시간
                if (! ObjectUtils.isEmpty(cell)) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if (ObjectUtils.isEmpty(cell.getStringCellValue())) {
                        map.put("status", ResponseEnum.Status.ERROR);
                        map.put("res", (i+1)+"행 시작시간이 없습니다.");
                        return map;
                    }
                    dateStr = cell.getStringCellValue().replace("'", "");
                    if (! (dateStr.matches("\\d{4}년\\d{2}월\\d{2}일 \\d{2}시\\d{2}분"))) {
                        map.put("status", ResponseEnum.Status.ERROR);
                        map.put("res", (i+1)+"행 시작시간타입의 입력 형식 오류");
                        return map;
                    }
                }

                LocalDateTime startDateTime = LocalDateTime.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy년MM월dd일 HH시mm분"));
                if (startDateTime.isBefore(toDate.atTime(8, 0, 0))) {
                    map.put("status", ResponseEnum.Status.ERROR);
                    map.put("res", (i+1)+"행 시작시간의 최소 시간은 당일 오전 8시 까지입니다.");
                    return map;
                }

                cell = row.getCell(3);// 종료 시간
                if (! ObjectUtils.isEmpty(cell)) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if (ObjectUtils.isEmpty(cell.getStringCellValue())) {
                        map.put("status", ResponseEnum.Status.ERROR);
                        map.put("res", (i+1)+"행 종료시간이 없습니다.");
                        return map;
                    }
                    dateStr = cell.getStringCellValue().replace("'", "");
                    if (! (dateStr.matches("\\d{4}년\\d{2}월\\d{2}일 \\d{2}시\\d{2}분"))) {
                        map.put("status", ResponseEnum.Status.ERROR);
                        map.put("res", (i+1)+"행 종료시간타입의 입력 형식 오류");
                        return map;
                    }
                }
                LocalDateTime endDateTime = LocalDateTime.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy년MM월dd일 HH시mm분"));

                int diffDayCnt = diffDayCnt(endDateTime.toLocalDate(), toDate);
                if (diffDayCnt > 0) {
                    if (endDateTime.isAfter(toDate.plusDays(1).atTime(8,0,0))) {
                        map.put("status", ResponseEnum.Status.ERROR);
                        map.put("res", (i+1)+"행 종료시간의 최대 시간은 다음날 오전 8시 까지입니다.");
                        return map;
                    }
                }

                if (! endDateTime.isAfter(startDateTime)) {
                    map.put("status", ResponseEnum.Status.ERROR);
                    map.put("res", (i+1)+"행 시작시간과 종료시간을 확인해 주세요.");
                    return map;
                }


            }


            for (int i = 1, sheetLen = sheet.getLastRowNum() + 1; i < sheetLen; i++) {
                row = sheet.getRow(i);
                if (ObjectUtils.isEmpty(row)) {
                    continue;
                }
                schedule = new Schedule();
                cell = row.getCell(0);// 프로그램 명
                if (! ObjectUtils.isEmpty(cell)) {
                    schedule.setProgNm(cell.getStringCellValue());
                }
                cell = row.getCell(1);// 일정타입
                if (! ObjectUtils.isEmpty(cell)) {
                    SchedTypeEnum schedTypeEnum = SchedTypeEnum.findDesc(cell.getStringCellValue());
                    schedule.setSchedType(schedTypeEnum);
                }
                cell = row.getCell(2);// 시작 시간
                if (! ObjectUtils.isEmpty(cell)) {
                    schedule.setBgnTime(LocalDateTime.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy년MM월dd일 HH시mm분")));
                }
                cell = row.getCell(3);// 종료 시간
                if (! ObjectUtils.isEmpty(cell)) {
                    schedule.setEndTime(LocalDateTime.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy년MM월dd일 HH시mm분")));
                }
                schedule.setChnlNo(chnlNo);
                if (!ObjectUtils.isEmpty(schedule)) {
                    schedule.setBaseDate(schedule.getBgnTime().getYear()+""+String.format("%02d",schedule.getBgnTime().getMonthValue())+""+String.format("%02d", schedule.getBgnTime().getDayOfMonth()));
                }
                schedule.setAccount(account);
                scheduleList.add(schedule);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (0 < scheduleList.size()) {
            // 일정 데이터 삽입
            scheduleDao.insertScheduleAsList(scheduleList);
            // 파일업로드 이력 삽입
            ScheduleFile scheduleFile = ScheduleFile.builder()
                    .fileGb("SCHED")
                    .refNo(chnlNo)
                    .fileNm(oriFileName)
                    .sysFileNm(sysFileName)
                    .filePath(excelDir)
                    .fileSize(String.format("%d", excelFile.getSize()))
                    .build();
            scheduleFile.setCretr(account.getMemNo());
            scheduleDao.insertScheduleFileHistory(scheduleFile);
            schedExcelUpload.setSchedExcel(null);
            map.put("scheduleList", scheduleList);
            map.put("schedExcelUpload", schedExcelUpload);
        } else {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", "데이터를 입력해주세요.");
            return map;
        }

        return map;
    }

    @Override
    public HashMap<String, Object> getUploadHistoryData(SchedExcelUploadHistory schedExcelUploadHistory, Account account) {
        log.debug("getUploadHistoryData start : ", schedExcelUploadHistory);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", ResponseEnum.Status.SUCCESS);
        map.put("res", ResponseEnum.ErrorCode.AICC_0000_200);
        map.put("method", "add");

        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", ResponseEnum.ErrorCode.AICC_0000_401);
            return map;
        }

        ScheduleFile scheduleFile = ScheduleFile.builder()
                .refNo(schedExcelUploadHistory.getChnlNo())
                .build();
        List<ScheduleFile> scheduleFiles = scheduleDao.selectFileHistory(scheduleFile);
        map.put("scheduleFiles", scheduleFiles);
        return map;
    }

    @Override
    public ResponseEntity<Resource> downloadHistoryExcel(SchedExcelFile schedExcelFile, Account account) {
        log.debug("downloadHistoryExcel start : ", schedExcelFile);
        List<Schedule> scheduleList = new ArrayList<Schedule>();

        long fileNo = schedExcelFile.getFileNo();
        ScheduleFile scheduleFile = scheduleDao.selectScheduleFile(fileNo);
        String filePath = scheduleFile.getFilePath()+"/"+scheduleFile.getSysFileNm();

        File target = new File(filePath);
        HttpHeaders header = new HttpHeaders();
        Resource rs = null;
        if(target.exists()) {
            try {
                String mimeType = Files.probeContentType(Paths.get(target.getAbsolutePath()));
                if(mimeType == null) {
                    mimeType = "octet-stream";
                }
                rs = new UrlResource(target.toURI());
                header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ scheduleFile.getFileNm() +"\"");
                header.setCacheControl("no-cache");
                header.setContentType(MediaType.parseMediaType(mimeType));
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<Resource>(rs, header, HttpStatus.OK);
    }

    @Override
    public HashMap<String, Object> copySchedule(CopySchedule copySchedule, Account account) {
        log.debug("copySchedule : ", copySchedule, account);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", ResponseEnum.Status.SUCCESS);
        map.put("res", ResponseEnum.ErrorCode.AICC_0000_200);
        map.put("method", "add");

        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", ResponseEnum.ErrorCode.AICC_0000_401);
            return map;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String toDay = copySchedule.getToDay();
        LocalDate curDate = LocalDate.parse(toDay, formatter);
        String chooseDay = copySchedule.getChooseDay();
        LocalDate chooseDate = LocalDate.parse(chooseDay, formatter);

        Schedule schedule = Schedule.builder()
                .chnlNo(copySchedule.getChnlNo())
                .curDay(chooseDate)
                .build();
        MemberSchedule memberSchedule = MemberSchedule.builder().build();
        LocalDateTime curDayTime = chooseDate.atTime(8,0,0,0);
        schedule.setSearchStartTime(curDayTime);
        schedule.setSearchEndTime(curDayTime.plusDays(1));
        // 스케줄 조회
        List<Schedule> scheduleList = scheduleDao.selectScheduleList(schedule);
        int diffDayCnt = diffDayCnt(curDate, chooseDate);
        for (Schedule sched : scheduleList) {
            sched.setPrntSchedNo(sched.getSchedNo());
            sched.setBgnTime(sched.getBgnTime().plusDays(diffDayCnt));
            sched.setEndTime(sched.getEndTime().plusDays(diffDayCnt));
            sched.setBaseDate(sched.getBgnTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            sched.setAccount(account);
            scheduleDao.insertSchedule(sched);
            if (! ObjectUtils.isEmpty(copySchedule.getIsMem())) {
                for (MemberSchedule memberSched : sched.getMemberSchedules()) {
                    memberSched.setSchedNo(sched.getSchedNo());
                    memberSched.setBgnTime(memberSched.getBgnTime().plusDays(diffDayCnt));
                    memberSched.setEndTime(memberSched.getEndTime().plusDays(diffDayCnt));
                    memberSched.setBaseDate(memberSched.getBgnTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    memberSched.setCretr(account.getMemNo());
                }

                if (sched.getMemberSchedules().size() > 0) {
                    scheduleDao.insertMemScheduleList(sched.getMemberSchedules());
                }
            } else {
                sched.getMemberSchedules().clear();
            }
        }

        map.put("scheduleList", scheduleList);
        map.put("copySchedule", copySchedule);
        return map;
    }

    @Override
    public HashMap<String, Object> getGroupTeam(Account account) {
        log.debug("getGroupTeam : ", account);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", ResponseEnum.Status.SUCCESS);
        map.put("res", ResponseEnum.ErrorCode.AICC_0000_200);
        map.put("method", "add");

        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", ResponseEnum.ErrorCode.AICC_0000_401);
            return map;
        }
        List<GroupTeam> groupTeams = scheduleDao.selectGroupTeamListAll();
        //List<ChnlTeam> chnlTeams = scheduleDao.selectChnlTeamListAll();
        map.put("groupTeams", groupTeams);
        return map;
    }

    @Override
    public HashMap<String, Object> getChnlTeamMem(ChnlTeamMemForm chnlTeamMemForm, Account account) {
        log.debug("getChnlTeamMem : ", chnlTeamMemForm, account);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", ResponseEnum.Status.SUCCESS);
        map.put("res", ResponseEnum.ErrorCode.AICC_0000_200);
        map.put("method", "add");

        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", ResponseEnum.ErrorCode.AICC_0000_401);
            return map;
        }
        List<ChnlTeam> chnlTeams = null;
        if(! ObjectUtils.isEmpty(chnlTeamMemForm.getChnlNo())) {
            ChnlTeam chnlTeam = ChnlTeam.builder()
                    .chnlNo(chnlTeamMemForm.getChnlNo())
                    .build();
            chnlTeams = scheduleDao.selectChnlTeamMemList(chnlTeam);
        }
        map.put("chnlTeams", chnlTeams);
        return map;
    }

    @Override
    public HashMap<String, Object> regMemSched(MemSchedForm memSchedForm, Account account) {
        log.debug("regMemSched : ", memSchedForm, account);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", ResponseEnum.Status.SUCCESS);
        map.put("res", ResponseEnum.ErrorCode.AICC_0000_200);

        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", ResponseEnum.ErrorCode.AICC_0000_401);
            return map;
        }

        String memSchedNo = memSchedForm.getMemSchedNo();
        String startM = memSchedForm.getStartM();
        String endM = memSchedForm.getEndM();
        String startH = memSchedForm.getStartH();
        String endH = memSchedForm.getEndH();

        LocalDateTime startTime = LocalDateTime.of(memSchedForm.getStartDay().getYear(), memSchedForm.getStartDay().getMonthValue(), memSchedForm.getStartDay().getDayOfMonth(), Integer.parseInt(startH), Integer.parseInt(startM), 0, 0);
        LocalDateTime endTime = LocalDateTime.of(memSchedForm.getEndDay().getYear(), memSchedForm.getEndDay().getMonthValue(), memSchedForm.getEndDay().getDayOfMonth(), Integer.parseInt(endH), Integer.parseInt(endM), 0,0 );

        if (! endTime.isAfter(startTime)) {
            throw new BizException("SchedForm.workTime.beforeAfter");
        }

        MemberSchedule memberSchedule = MemberSchedule.builder()
                .memNo(Integer.parseInt(memSchedForm.getMemNo()))
                .bgnTime(startTime)
                .endTime(endTime)
                .baseDate(startTime.getYear()+""+String.format("%02d", startTime.getMonthValue())+""+String.format("%02d", startTime.getDayOfMonth()))
                .schedGb(memSchedForm.getSchedGb())
                .workAmt(String.format("%d", diffSecondsCnt(startTime, endTime)))
                .build();
        if (! ObjectUtils.isEmpty(memSchedForm.getChnlTeam())) {
            memberSchedule.setChnlTeam(ChnlTeam.builder().chnlNo(memSchedForm.getChnlTeam()).build());
        }
        if (! ObjectUtils.isEmpty(memSchedForm.getOrd())) {
            memberSchedule.setOrd(memSchedForm.getOrd());
        }
        if (! ObjectUtils.isEmpty(memSchedForm.getSchedNo())) {
            memberSchedule.setSchedNo(Integer.parseInt(memSchedForm.getSchedNo()));
        }
        if (! ObjectUtils.isEmpty(memSchedForm.getStartMemAmt())) {
            memberSchedule.setStartAmt(memSchedForm.getStartMemAmt());
        }
        if (ObjectUtils.isEmpty(memSchedNo)) {
            memberSchedule.setCretr(account.getMemNo());
            scheduleDao.insertMemberSchedule(memberSchedule);
        } else {
            memberSchedule.setChgr(account.getMemNo());
            memberSchedule.setMemSchedNo(Integer.parseInt(memSchedNo));
            scheduleDao.updateMemberSchedule(memberSchedule);
        }
        //
        memberSchedule = scheduleDao.selectMemberSchedule(memberSchedule);
        String path = (! ObjectUtils.isEmpty(memberSchedule.getFilePath())) ? memberSchedule.getFilePath().replace(appProperties.getUploadDirProfile(), "/file/image/profile") +
                "/" + memberSchedule.getThumbSysFileNm():"";
        memberSchedule.setThumbSysFileNm(path);
        map.put("memberSchedule", memberSchedule);
        map.put("memSchedForm", memSchedForm);
        return map;
    }

    @Override
    public HashMap<String, Object> copyNoWorkSchedule(CopyNoWorkSchedule copyNoWorkSchedule, Account account) {
        log.debug("copyNoWorkSchedule : ", copyNoWorkSchedule, account);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", ResponseEnum.Status.SUCCESS);
        map.put("res", ResponseEnum.ErrorCode.AICC_0000_200);
        map.put("method", "add");

        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", ResponseEnum.ErrorCode.AICC_0000_401);
            return map;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String toDay = copyNoWorkSchedule.getToDay();
        LocalDate curDate = LocalDate.parse(toDay, formatter);
        String chooseDay = copyNoWorkSchedule.getChooseDay();
        LocalDate chooseDate = LocalDate.parse(chooseDay, formatter);

        MemberSchedule memberSchedule = MemberSchedule.builder()
                .schedNo(0)
                .baseDate(chooseDate.getYear()+String.format("%02d", chooseDate.getMonthValue())+String.format("%02d", chooseDate.getDayOfMonth()))
                .build();
        LocalDateTime curDayTime = chooseDate.atTime(8,0,0,0);
        memberSchedule.setSearchStartTime(curDayTime);
        memberSchedule.setSearchEndTime(curDayTime.plusDays(1));

        List<MemberSchedule> memberSchedules = scheduleDao.selectMemberScheduleList(memberSchedule);
        int diffDayCnt = diffDayCnt(curDate, chooseDate);
        for (MemberSchedule memberSched : memberSchedules) {

            memberSched.setBgnTime(memberSched.getBgnTime().plusDays(diffDayCnt));
            memberSched.setEndTime(memberSched.getEndTime().plusDays(diffDayCnt));
            memberSched.setBaseDate(memberSched.getBgnTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            memberSched.setCretr(account.getMemNo());
            //scheduleDao.insertMemberSchedule(memberSched);
        }
        if (memberSchedules.size() > 0) {
            scheduleDao.insertMemScheduleList(memberSchedules);
        }

        map.put("noWorkmemberScheduleList", memberSchedules);
        return map;
    }

    @Override
    public HashMap<String, Object> deleteMemberSchedule(MemSchedForm memSchedForm, Account account) {
        log.debug("deleteMemberSchedule : ", memSchedForm, account);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", ResponseEnum.Status.SUCCESS);
        map.put("res", ResponseEnum.ErrorCode.AICC_0000_200);
        map.put("method", "add");

        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", ResponseEnum.ErrorCode.AICC_0000_401);
            return map;
        }
        scheduleDao.updateDelMemSchedByMemSchedNo(Integer.parseInt(memSchedForm.getMemSchedNo()), account.getMemNo());
        return map;
    }

    @Override
    public void scheduleCheck(SchedForm schedForm, Account account) throws BizException {
        log.debug("scheduleCheck : ", schedForm, account);
        // 수정될 일정이 수정전 일정보다 짧게 수정될수는 없다.
        int startM = Integer.parseInt(schedForm.getStartM());
        int endM = Integer.parseInt(schedForm.getEndM());
        int startH = Integer.parseInt(schedForm.getStartH());
        int endH = Integer.parseInt(schedForm.getEndH());
        if (! ObjectUtils.isEmpty(schedForm.getSchedNo())) {
            Schedule schedule = scheduleDao.selectScheduleBySchedNo(schedForm.getSchedNo());
            if (! ObjectUtils.isEmpty(schedule)) {
                if (0 < schedule.getMemberSchedules().size()) {
                    // 작업자 최대 시간
                    LocalDateTime maxMemTime = null;
                    // 작업자 최소 시간
                    LocalDateTime minMemTime = null;
                    for (MemberSchedule memberSchedule : schedule.getMemberSchedules()) {
                        if (ObjectUtils.isEmpty(minMemTime)) {
                            minMemTime = memberSchedule.getBgnTime();
                            continue;
                        }
                        minMemTime = (minMemTime.isBefore(memberSchedule.getBgnTime())) ? minMemTime:memberSchedule.getBgnTime();
                    }
                    for (MemberSchedule memberSchedule : schedule.getMemberSchedules()) {
                        if (ObjectUtils.isEmpty(maxMemTime)) {
                            maxMemTime = memberSchedule.getEndTime();
                            continue;
                        }
                        maxMemTime = (maxMemTime.isAfter(memberSchedule.getEndTime())) ? maxMemTime:memberSchedule.getEndTime();
                    }
                    // 새로운 최소 시간
                    LocalDateTime newMinMemTime = LocalDateTime.of(schedule.getBgnTime().getYear(), schedule.getBgnTime().getMonthValue(), schedule.getBgnTime().getDayOfMonth(), startH, startM);
                    // 새로운 최대 시간
                    LocalDateTime newMaxMemTime = LocalDateTime.of(schedule.getBgnTime().getYear(), schedule.getBgnTime().getMonthValue(), schedule.getBgnTime().getDayOfMonth(), endH, endM);
                    if (minMemTime.isBefore(newMinMemTime)) {
                        throw new BizException("SchedForm.workTime.Scope");
                    }
                    if (maxMemTime.isAfter(newMaxMemTime)) {
                        throw new BizException("SchedForm.workTime.Scope");
                    }
                }
            }
        }


        LocalDate startDate = schedForm.getStartDay();
        LocalDate endDate = schedForm.getEndDay();
        LocalDateTime toTime = LocalDateTime.now();
        int diffDayCnt = diffDayCnt(endDate, schedForm.getToDay());
        // 스케줄 등록시 시작시간의 최소 시간은 당일 오전 8시까지
        LocalDateTime startTime = startDate.atTime(startH,startM ,0);
        LocalDateTime baseTime = schedForm.getToDay().atTime(8, 0, 0, 0);
        if (startTime.isBefore(baseTime)) {
            throw new BizException("SchedForm.workDayStart.Scope");
        }
        // 스케줄 등록시 종료시간의 최대 시간은 다음날 오전 8시 까지
        if (diffDayCnt > 0) {
            LocalDateTime endTime = endDate.atTime(endH, endM, 0, 0);
            baseTime = schedForm.getToDay().plusDays(1).atTime(8, 0, 0, 0);
            if (endTime.isAfter(baseTime)) {
                throw new BizException("SchedForm.workDayEnd.Scope");
            }
        }

    }

    @Override
    public HashMap<String, Object> isWebposButtonExposure(TimeLineForm timeLineForm, Account account) {
        log.debug("isWebposButtonExposure : ", timeLineForm, account);
        HashMap<String, Object> map = new HashMap<String, Object>();
        Schedule schedule = Schedule.builder()
                .curDay(LocalDate.parse(timeLineForm.getCurday(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                .account(account)
                .build();
        LocalDateTime curDayTime = LocalDate.parse(timeLineForm.getCurday(), DateTimeFormatter.ofPattern("yyyyMMdd")).atTime(8,0,0,0);
        schedule.setSearchStartTime(curDayTime);
        schedule.setSearchEndTime(curDayTime.plusDays(1));
        List<Schedule> wbeList = scheduleDao.selectWebfosButtonScheduleList(schedule);
        setWebfosButtonDisplay(wbeList, account, LocalDateTime.now());
        List<ProgramInfo> programInfos = webfosService.getProgramInfoListOfWebfos();

        for (Schedule sched : wbeList) {
            sched.setWebfosButtonDisplayByProgramInfos(programInfos, account);
        }

        map.put("wbeList", wbeList);
        return map;
    }

    @Override
    public List<Channel> getTotalChnlList(Account account) {

        return scheduleDao.selectTotalChnlList(account);
    }

    @Override
    public HashMap<String, Object> getGroupTeamMem(GroupTeamMemForm groupTeamMemForm, Account account) {
        log.debug("getGroupTeamMem : ", groupTeamMemForm, account);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", ResponseEnum.Status.SUCCESS);
        map.put("res", ResponseEnum.ErrorCode.AICC_0000_200);
        map.put("method", "add");

        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", ResponseEnum.ErrorCode.AICC_0000_401);
            return map;
        }
        List<ChnlTeam> groupTeams = null;
        if(! ObjectUtils.isEmpty(groupTeamMemForm.getGrpType())) {
            GroupTeam groupTeam = GroupTeam.builder()
                    .grpType(groupTeamMemForm.getGrpType())
                    .build();
            groupTeams = scheduleDao.selectGroupTeamMemList(groupTeam);
        }
        map.put("groupTeams", groupTeams);
        return map;
    }

    @Override
    public HashMap<String, Object> getWorkDetails(WorkDetail workDetail, Account account) {
        log.debug("getWorkDetails : ", workDetail, account);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", ResponseEnum.Status.SUCCESS);
        map.put("res", ResponseEnum.ErrorCode.AICC_0000_200);
        map.put("method", "add");

        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            map.put("status", ResponseEnum.Status.ERROR);
            map.put("res", ResponseEnum.ErrorCode.AICC_0000_401);
            return map;
        }
        DashBoard dashBoard = DashBoard.builder()
                .chnlNo(workDetail.getChnlNo())
                .startTime(workDetail.getToDay().atTime(8,0,0,0))
                .endTime(workDetail.getToDay().plusDays(1).atTime(8,0,0,0))
                .build();
        List<DashBoardInfo> dashBoardList = scheduleDao.selectWorkDetailList(dashBoard);
        if (dashBoardList.size() > 0) {
            map.put("dashBoardList", dashBoardList);
            return map;
        } else {
            map.put("status", ResponseEnum.Status.ERROR);
            return map;
        }
    }

    @Override
    public void textDownload(HttpServletResponse response, long schedNo) throws Exception {

        SubtitleDetailInfo subtitleDetailInfo = scheduleDao.selectSubDtlInfo(schedNo);
        String fileName = "";
        String subDesc = "";
        if (ObjectUtils.isEmpty(subtitleDetailInfo)) {
            fileName = schedNo + ".txt";
            subDesc = "";
        } else {
            fileName = subtitleDetailInfo.getCretDt()+"_"+subtitleDetailInfo.getProgNm()+".txt";
            subDesc = subtitleDetailInfo.getSubDesc();
        }

        // 1. 다운로드 페이지 설정
        String docName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + docName + ";");
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter txtPrinter = response.getWriter();
        // 2. 소스코드 출력
        String snippet = URLDecoder.decode(subDesc, "UTF-8");
        txtPrinter.print(snippet);
        response.flushBuffer();
    }

    private int diffDayCnt (LocalDate end, LocalDate start) {
        int diffDay = 0;
        Period period = Period.between(start, end);
        diffDay = period.getDays();
        return diffDay;
    }
    private int diffSecondsCnt(LocalDateTime today, LocalDateTime now) {
        Duration duration = Duration.between(today, now);
        long seconds = duration.getSeconds();
        return (int) (seconds / 60);
    }
}
