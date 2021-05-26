package kr.co.aicc.infra.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

@Component
public class AiccUtilsDialect extends AbstractDialect implements IExpressionObjectDialect {

	protected AiccUtilsDialect() {
		super("aiccUtils");
	}

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {
            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton("aiccUtils");
            }

            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                return new AiccUtils();
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }
        };
    }

    public class AiccUtils {

    	public String addDays(int days , String dateFormat) {

    		DateFormat df = new SimpleDateFormat(StringUtils.defaultIfEmpty(dateFormat, "yyyy-MM-dd"));
    		Calendar cal =new GregorianCalendar(Locale.KOREA);
    		cal.setTime(new Date());
    		cal.add(Calendar.DATE,days);
    		System.out.println("df.format(cal.getTime() ::: " + df.format(cal.getTime()));

    		return df.format(cal.getTime());
    	}

    	public String drawPagination(PaginationInfo paginationInfo ,String contextPath, String jsFunction) {
    		PaginationRender render = new PaginationRender();
    		return render.renderPagination(paginationInfo, contextPath, jsFunction);
    	}

    	public String unEscapeHtml(String html) {
    		if (html != null && !html.equals("")) {
	    	String escapeHtml =  HtmlUtil.unEscapeHtml(html);
				return HtmlUtil.stripXSS(escapeHtml);
    		}else {
    			return "";
    		}
    	}
    }
}

