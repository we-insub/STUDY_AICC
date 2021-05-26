package kr.co.aicc.infra.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
	public static String unEscapeHtml(String html) {
		if( html != null && !html.equals("")) {
			Pattern p = Pattern.compile("/&lt;|&gt;|&#35|&quot|&#40|&#41|&#38|&apos;/g");
			Matcher m = p.matcher(html);
	        while(m.find()) {
	        	html =  html.replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&#35","#")
	        				.replaceAll("&quot","\"").replaceAll("&#40","\\(").replaceAll("&#41","\\)")
	        				.replaceAll("&#38","&").replaceAll("&apos;","'");
	        }
	        return html;
		} else {
	        return "";
		}

	}
	static String stripXSS(String value) {

		if (value != null && !value.equals("")) {
			value = value.replaceAll("", "");

			Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			/* 이미지 태그를 위해 일단 주석
			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");
			*/
			scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("form", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("object", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("embed", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("applet", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");
		}
		return value;
	}

}
