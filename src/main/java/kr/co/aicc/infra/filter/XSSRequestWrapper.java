/*
 * AICC (AI Caption Center) version 1.0
 *
 *  Copyright ⓒ 2020 sorizava corp. All rights reserved.
 *
 *  This is a proprietary software of sorizava corp, and you may not use this file except in
 *  compliance with license agreement with sorizava corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of sorizava corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *
 * @package kr.co.aicc.infra.filter
 * @file XSSRequestWrapper.java
 * @author developer
 * @since 2020. 06. 08
 * @version 1.0
 * @title AICC
 * @description
 *
 *
 * << 개정이력 (Modification Information) >>
 *
 *   수정일		수정자	수정내용
 * -------------------------------------------------------------------------------
 * 2020. 06. 08	developer		 최초생성
 * -------------------------------------------------------------------------------
 */
package kr.co.aicc.infra.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.regex.Pattern;

public class XSSRequestWrapper extends HttpServletRequestWrapper {
	
	private static Pattern[] patterns = new Pattern[]{
		Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        //추가된 필터랑 문자열
        Pattern.compile("applet", Pattern.CASE_INSENSITIVE),Pattern.compile("meta", Pattern.CASE_INSENSITIVE),Pattern.compile("xml", Pattern.CASE_INSENSITIVE),
        Pattern.compile("blink", Pattern.CASE_INSENSITIVE),Pattern.compile("link", Pattern.CASE_INSENSITIVE),Pattern.compile("style", Pattern.CASE_INSENSITIVE),
        Pattern.compile("embed", Pattern.CASE_INSENSITIVE),Pattern.compile("object", Pattern.CASE_INSENSITIVE),Pattern.compile("iframe", Pattern.CASE_INSENSITIVE),
        Pattern.compile("frame", Pattern.CASE_INSENSITIVE),Pattern.compile("frameset", Pattern.CASE_INSENSITIVE),Pattern.compile("ilayer", Pattern.CASE_INSENSITIVE),
        Pattern.compile("layer", Pattern.CASE_INSENSITIVE),Pattern.compile("bgsound", Pattern.CASE_INSENSITIVE),Pattern.compile("title", Pattern.CASE_INSENSITIVE),
        Pattern.compile("base", Pattern.CASE_INSENSITIVE),Pattern.compile("innerHTML", Pattern.CASE_INSENSITIVE),Pattern.compile("charset", Pattern.CASE_INSENSITIVE),
        Pattern.compile("document", Pattern.CASE_INSENSITIVE),Pattern.compile("string", Pattern.CASE_INSENSITIVE),Pattern.compile("create", Pattern.CASE_INSENSITIVE),
        Pattern.compile("append", Pattern.CASE_INSENSITIVE),Pattern.compile("binding", Pattern.CASE_INSENSITIVE),Pattern.compile("alert", Pattern.CASE_INSENSITIVE),
        Pattern.compile("msgbox", Pattern.CASE_INSENSITIVE),Pattern.compile("msgbox", Pattern.CASE_INSENSITIVE),Pattern.compile("void", Pattern.CASE_INSENSITIVE),
        Pattern.compile("href", Pattern.CASE_INSENSITIVE),Pattern.compile("onabort", Pattern.CASE_INSENSITIVE),Pattern.compile("onactivae", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onafterprint", Pattern.CASE_INSENSITIVE),Pattern.compile("onbeforepaste", Pattern.CASE_INSENSITIVE),Pattern.compile("onbefore", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onbeforecopy", Pattern.CASE_INSENSITIVE),Pattern.compile("onbeforecut", Pattern.CASE_INSENSITIVE),Pattern.compile("onbeforedeactivate", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onbeforeeditfocus", Pattern.CASE_INSENSITIVE),Pattern.compile("onbeforepaste", Pattern.CASE_INSENSITIVE),Pattern.compile("onbeforeprint", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onbeforeunload", Pattern.CASE_INSENSITIVE),Pattern.compile("onbeforeupdate", Pattern.CASE_INSENSITIVE),Pattern.compile("onblur", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onbounce", Pattern.CASE_INSENSITIVE),Pattern.compile("oncellchange", Pattern.CASE_INSENSITIVE),Pattern.compile("onchange", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onclick", Pattern.CASE_INSENSITIVE),Pattern.compile("oncontextmenu", Pattern.CASE_INSENSITIVE),Pattern.compile("oncontrolselect", Pattern.CASE_INSENSITIVE),
        Pattern.compile("oncopy", Pattern.CASE_INSENSITIVE),Pattern.compile("oncut", Pattern.CASE_INSENSITIVE),Pattern.compile("ondataavailable", Pattern.CASE_INSENSITIVE),
        Pattern.compile("ondatasetchanged", Pattern.CASE_INSENSITIVE),Pattern.compile("ondatasetcomplete", Pattern.CASE_INSENSITIVE),Pattern.compile("ondblclick", Pattern.CASE_INSENSITIVE),
        Pattern.compile("ondeactivate", Pattern.CASE_INSENSITIVE),Pattern.compile("ondrag", Pattern.CASE_INSENSITIVE),Pattern.compile("ondragend", Pattern.CASE_INSENSITIVE),
        Pattern.compile("ondragenter", Pattern.CASE_INSENSITIVE),Pattern.compile("ondragleave", Pattern.CASE_INSENSITIVE),Pattern.compile("ondragover", Pattern.CASE_INSENSITIVE),
        Pattern.compile("ondragstart", Pattern.CASE_INSENSITIVE),Pattern.compile("ondrop", Pattern.CASE_INSENSITIVE),Pattern.compile("onerror", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onerrorupdate", Pattern.CASE_INSENSITIVE),Pattern.compile("onfilterchange", Pattern.CASE_INSENSITIVE),Pattern.compile("onfinish", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onfocus", Pattern.CASE_INSENSITIVE),Pattern.compile("onfocusin", Pattern.CASE_INSENSITIVE),Pattern.compile("onfocusout", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onhelp", Pattern.CASE_INSENSITIVE),Pattern.compile("onkeydown", Pattern.CASE_INSENSITIVE),Pattern.compile("onkeypress", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onkeyup", Pattern.CASE_INSENSITIVE),Pattern.compile("onlayoutcomplete", Pattern.CASE_INSENSITIVE),Pattern.compile("onload", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onlosecapture", Pattern.CASE_INSENSITIVE),Pattern.compile("onmousedown", Pattern.CASE_INSENSITIVE),Pattern.compile("onmouseenter", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onmouseleave", Pattern.CASE_INSENSITIVE),Pattern.compile("onmousemove", Pattern.CASE_INSENSITIVE),Pattern.compile("onmouseout", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onmouseover", Pattern.CASE_INSENSITIVE),Pattern.compile("onmouseup", Pattern.CASE_INSENSITIVE),Pattern.compile("onmousewheel", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onmove", Pattern.CASE_INSENSITIVE),Pattern.compile("onmoveend", Pattern.CASE_INSENSITIVE),Pattern.compile("onmovestart", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onpaste", Pattern.CASE_INSENSITIVE),Pattern.compile("onpropertychange", Pattern.CASE_INSENSITIVE),Pattern.compile("onreadystatechange", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onreset", Pattern.CASE_INSENSITIVE),Pattern.compile("onresize", Pattern.CASE_INSENSITIVE),Pattern.compile("onresizeend", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onresizestart", Pattern.CASE_INSENSITIVE),Pattern.compile("onrowenter", Pattern.CASE_INSENSITIVE),Pattern.compile("onrowexit", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onrowsdelete", Pattern.CASE_INSENSITIVE),Pattern.compile("onrowsinserted", Pattern.CASE_INSENSITIVE),Pattern.compile("onscroll", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onselect", Pattern.CASE_INSENSITIVE),Pattern.compile("onselectionchange", Pattern.CASE_INSENSITIVE),Pattern.compile("onselectstart", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onstart", Pattern.CASE_INSENSITIVE),Pattern.compile("onstop", Pattern.CASE_INSENSITIVE),Pattern.compile("onsubmit", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onunload", Pattern.CASE_INSENSITIVE),
	};

	public XSSRequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}

	@Override
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);

		if (values == null) {
			return null;
		}

		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = stripXSS(values[i]);
		}

		return encodedValues;
	}

	@Override
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);

		return stripXSS(value);
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		return stripXSS(value);
	}

	private String stripXSS(String valueStr) {
		String value=valueStr;
		if (value != null) {
			value = value.replaceAll("\0", "");

			for(Pattern scriptPattern : patterns){
				if(scriptPattern.matcher(value).find()){
					value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("#", "&#35")
							      .replaceAll("\"", "&quot").replaceAll("\\(", "&#40").replaceAll("\\)", "&#41")
							      .replaceAll("&", "&#38").replaceAll("'", "&apos;");
//					.replaceAll("\\\\", "&#92;").replaceAll("%", "&#37;").replaceAll("/", "&#47;"); // XSS필터에 사용하지 않는 문자
				}
			}
		}

		return value;
	}
}
