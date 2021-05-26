package kr.co.aicc.modules.dashboard.service;

import kr.co.aicc.modules.dashboard.domain.DashBoard;
import kr.co.aicc.modules.dashboard.domain.DashBoardInfo;
import kr.co.aicc.modules.dashboard.dto.DashBoardForm;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component("DashBoardXlsx")
public class DashBoardExcelView extends AbstractXlsxView {

    @Autowired
    private DashboardService dashboardService;

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm"));

        /* 데이터 가져오기 */
        DashBoardForm dashBoardForm = new DashBoardForm();
        String dashBoardType = (ObjectUtils.isEmpty(request.getParameter("dashBoardType")))?"chnl":request.getParameter("dashBoardType"); // group or chnl
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        dashBoardForm.setDashBoardType(dashBoardType);
        if (! ObjectUtils.isEmpty(request.getParameterValues("chnl"))) {
            dashBoardForm.setChnl(new HashSet<String>(Arrays.asList(request.getParameterValues("chnl"))));
        }
        if (! ObjectUtils.isEmpty(request.getParameterValues("groupCode"))) {
            dashBoardForm.setGroupCode(new HashSet<String>(Arrays.asList(request.getParameterValues("groupCode"))));
        }
        dashBoardForm.setStartTime(startTime);
        dashBoardForm.setEndTime(endTime);
        HashMap<String, Object> map = dashboardService.getDashBoardData(dashBoardForm);

        /* 엑셀 세팅 */
        response.setHeader("Content-Disposition","attachment; filename=\"DashBoard_"+time+".xlsx\" ");
        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("#,##O"));
        Sheet sheet = workbook.createSheet(time+" 작업 내역 상세");
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));
        sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));
        sheet.addMergedRegion(new CellRangeAddress(0,0,2,6));
        if (dashBoardType.equals("group")) {
            sheet.addMergedRegion(new CellRangeAddress(0,0,7,9));
        }
        int row = 0;

        /* 타이틀 시작 */
        Row row0 = sheet.createRow(row++);
        Cell cell0 = row0.createCell(0);
        if (dashBoardType.equals("group")) {
            cell0.setCellValue("채널명");
        } else {
            cell0.setCellValue("그룹팀명");
        }
        Cell cell1 = row0.createCell(1);
        cell1.setCellValue("담당자명");
        Cell cell2 = row0.createCell(2);
        cell2.setCellValue("작업분량");
        if (dashBoardType.equals("group")) {
            Cell cell3 = row0.createCell(7);
            cell3.setCellValue("작업외분량");
        }
        Row row1 = sheet.createRow(row++);
        Cell cell4 = row1.createCell(2);
        cell4.setCellValue("생방송");
        Cell cell5 = row1.createCell(3);
        cell5.setCellValue("재방송");
        Cell cell6 = row1.createCell(4);
        cell6.setCellValue("관리자지원");
        Cell cell7 = row1.createCell(5);
        cell7.setCellValue("무자막재방");
        Cell cell8 = row1.createCell(6);
        cell8.setCellValue("합계");
        if (dashBoardType.equals("group")) {
            Cell cell9 = row1.createCell(7);
            cell9.setCellValue("휴식");
            Cell cell10 = row1.createCell(8);
            cell10.setCellValue("식사");
            Cell cell11 = row1.createCell(9);
            cell11.setCellValue("합계");
        }

        /* 타이틀 끝 */

        List<DashBoard> dashBoardList = (List<DashBoard>) map.get("dashBoardList");
        Row dataRow = null;
        Cell cell = null;
        String title = "";
        int col = 0;
        int infoIdx = 0;
        for (DashBoard dashBoard : dashBoardList) {
            col = 0;
            sheet.addMergedRegion(new CellRangeAddress(row,row + (dashBoard.getDashBoardInfoList().size()-2),0,0));
            dataRow = sheet.createRow(row++);
            title = (dashBoardType.equals("chnl"))?dashBoard.getCnmlNm():dashBoard.getGroupCodeName();
            cell = dataRow.createCell(col++);
            cell.setCellValue(title);
            infoIdx = 0;
            for (DashBoardInfo dashBoardInfo : dashBoard.getDashBoardInfoList()) {
                col = 0;
                dataRow = (infoIdx > 0)?sheet.createRow(row++):dataRow;
                if (dashBoardInfo.getMemNo() == 0) {
                    sheet.addMergedRegion(new CellRangeAddress(row-1,row-1,0,1));
                    cell = dataRow.createCell(col++);
                    cell.setCellValue("합계");
                    col++;
                } else {
                    col++;
                    cell = dataRow.createCell(col++);
                    cell.setCellValue(dashBoardInfo.getMemNm()+" ("+dashBoardInfo.getMemId()+")");
                }
                cell = dataRow.createCell(col++);
                cell.setCellValue(dashBoardInfo.getLiveWork());
                cell = dataRow.createCell(col++);
                cell.setCellValue(dashBoardInfo.getReWork());
                cell = dataRow.createCell(col++);
                cell.setCellValue(dashBoardInfo.getAdminWork());
                cell = dataRow.createCell(col++);
                cell.setCellValue(dashBoardInfo.getNosupotWork());
                cell = dataRow.createCell(col++);
                cell.setCellValue(dashBoardInfo.getWorkTotal());
                if (dashBoardType.equals("group")) {
                    cell = dataRow.createCell(col++);
                    cell.setCellValue(dashBoardInfo.getRestWork());
                    cell = dataRow.createCell(col++);
                    cell.setCellValue(dashBoardInfo.getEWork());
                    cell = dataRow.createCell(col++);
                    cell.setCellValue(dashBoardInfo.getRestTotal());
                }
                infoIdx++;
            }
        }
    }
}
