package kr.co.aicc.modules.schedule.service;

import kr.co.aicc.infra.enums.SchedTypeEnum;
import kr.co.aicc.modules.dashboard.domain.Schedule;
import kr.co.aicc.modules.schedule.repository.ScheduleDao;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("TimelineXlsx")
public class TimelineExcelView extends AbstractXlsxView {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleDao scheduleDao;

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm"));

        String toDay = request.getParameter("toDay");
        String type = request.getParameter("type");
        String chnlNo = request.getParameter("chnlNo");
        String chnlNm = request.getParameter("chnlNm");
        Schedule schedule = Schedule.builder()
                .chnlNo(Integer.parseInt(chnlNo))
                .build();

        String excelname = "";
        List<Schedule> scheduleList = null;
        switch (type) {
            case "sample":
                excelname = "SampleExcel";
                scheduleList = new ArrayList<Schedule>();
                Schedule sched = Schedule.builder()
                        .schedType(SchedTypeEnum.LIVE)
                        .bgnTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now().plusMinutes(1))
                        .progNm("프로그램1")
                        .build();
                scheduleList.add(sched);
                break;
            case "date":
                excelname = chnlNm + "_Excel";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate curDate = LocalDate.parse(toDay, formatter);
                schedule.setCurDay(curDate);
                LocalDateTime curDayTime = curDate.atTime(8,0,0,0);
                schedule.setSearchStartTime(curDayTime);
                schedule.setSearchEndTime(curDayTime.plusDays(1));
                scheduleList = scheduleDao.selectScheduleList(schedule);
                break;

        }

        /* 엑셀 세팅 */
        response.setHeader("Content-Disposition","attachment; filename=\""+excelname+"_"+time+".xlsx\" ");
        CellStyle cellStyleText = workbook.createCellStyle();
        CellStyle cellStyleDate = workbook.createCellStyle();
        CellStyle cellStyleTime = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        CreationHelper creationHelper = workbook.getCreationHelper();
        cellStyleText.setDataFormat(dataFormat.getFormat("text"));
        cellStyleDate.setDataFormat(
                creationHelper.createDataFormat().getFormat("yyyy/mm/dd hh:mm")
        );
        Sheet sheet = workbook.createSheet(excelname+"_"+time);
        int row = 0;

        /* 타이틀 시작 */
        Row row0 = sheet.createRow(row++);
        Cell cell0 = row0.createCell(0);
        cell0.setCellValue("프로그램명");
        Cell cell1 = row0.createCell(1);
        cell1.setCellValue("방송타입");
        Cell cell2 = row0.createCell(2);
        cell2.setCellValue("시작시간");
        Cell cell3 = row0.createCell(3);
        cell3.setCellValue("종료시간");

        Row dataRow = null;
        Cell cell = null;
        if (! ObjectUtils.isEmpty(scheduleList)) {
            for (Schedule sched : scheduleList) {
                dataRow = sheet.createRow(row++);
                cell = dataRow.createCell(0);
                if (! ObjectUtils.isEmpty(sched.getProgNm())) {
                    cell.setCellValue(sched.getProgNm()); // 프로그램 명
                }
                cell = dataRow.createCell(1);
                cell.setCellStyle(cellStyleText);
                cell.setCellValue(sched.getSchedType().desc()); // 방송타입
                cell = dataRow.createCell(2);
                cell.setCellStyle(cellStyleText);
                cell.setCellValue(sched.getBgnTime().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일 HH시mm분"))); // 시작시간
                cell = dataRow.createCell(3);
                cell.setCellStyle(cellStyleText);
                cell.setCellValue(sched.getEndTime().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일 HH시mm분"))); // 종료시간
            }
        }

    }
}
