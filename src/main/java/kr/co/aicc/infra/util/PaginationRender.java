package kr.co.aicc.infra.util;

import java.text.MessageFormat;

public class PaginationRender {
    public String renderPagination(PaginationInfo paginationInfo, String contextPath, String jsFunction) {

        StringBuffer strBuff = new StringBuffer();

        int firstPageNo = paginationInfo.getFirstPageNo();
        int firstPageNoOnPageList = paginationInfo.getFirstPageNoOnPageList();
        int totalPageCount = paginationInfo.getTotalPageCount();
        int pageSize = paginationInfo.getPageSize();
        int lastPageNoOnPageList = paginationInfo.getLastPageNoOnPageList();
        int currentPageNo = paginationInfo.getCurrentPageNo();
        int lastPageNo = paginationInfo.getLastPageNo();

        String firstPageLabel = "<li class=\"page-item\"><a class=\"page-link\" href=\"#\" onclick=\"{0}({1}); return false;\">❮❮</a></li>";
        String previousPageLabel = "<li class=\"page-item\"><a class=\"page-link\" href=\"#\" onclick=\"{0}({1}); return false;\">❮</a></li>";
        String currentPageLabel = "<li class=\"page-item active\"><a class=\"page-link\" href=\"#\">{0}</a></li>";
        String otherPageLabel = "<li class=\"page-item\"> <a class=\"page-link\" href=\"#\" onclick=\"{0}({1}); return false;\">{2}</a></li>";

        String nextPageLabel = "<li class=\"page-item\"><a class=\"page-link\" href=\"#\" onclick=\"{0}({1}); return false;\">❯</a></li>";
        String lastPageLabel = "<li class=\"page-item\"> <a class=\"page-link\" href=\"#\" onclick=\"{0}({1}); return false;\">❯❯</a></li>";

        if (totalPageCount > pageSize) {
            if (firstPageNoOnPageList > pageSize) {
                strBuff.append(MessageFormat.format(firstPageLabel,
                        new Object[] { jsFunction, Integer.toString(firstPageNo) }));
                strBuff.append(MessageFormat.format(previousPageLabel,
                        new Object[] { jsFunction, Integer.toString(firstPageNoOnPageList - 1) }));
            } else {
                strBuff.append(MessageFormat.format(firstPageLabel,
                        new Object[] { jsFunction, Integer.toString(firstPageNo) }));
                strBuff.append(MessageFormat.format(previousPageLabel,
                        new Object[] { jsFunction, Integer.toString(firstPageNo) }));
            }
        }

        for (int i = firstPageNoOnPageList; i <= lastPageNoOnPageList; i++) {
            if (i == currentPageNo) {
                strBuff.append(MessageFormat.format(currentPageLabel, new Object[] { Integer.toString(i) }));
            } else {
                strBuff.append(MessageFormat.format(otherPageLabel,
                        new Object[] { jsFunction, Integer.toString(i), Integer.toString(i) }));
            }
        }

        if (totalPageCount > pageSize) {
            if (lastPageNoOnPageList < totalPageCount) {
                strBuff.append(MessageFormat.format(nextPageLabel,
                        new Object[] { jsFunction, Integer.toString(firstPageNoOnPageList + pageSize) }));
                strBuff.append(
                        MessageFormat.format(lastPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
            } else {
                strBuff.append(
                        MessageFormat.format(nextPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
                strBuff.append(
                        MessageFormat.format(lastPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
            }
        }
        return strBuff.toString();
    }
}