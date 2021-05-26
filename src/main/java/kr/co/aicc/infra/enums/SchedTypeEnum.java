package kr.co.aicc.infra.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import kr.co.aicc.infra.handler.ValueEnum;
import org.apache.commons.lang3.StringUtils;

public enum SchedTypeEnum implements ValueEnum {
    LIVE("01","생방송","bg_pink", "live"),
    RERUN("02","재방송","bg_blue", "re"),
    SUPPOT("03","관리자지원","bg_purple", "admin"),
    NOSUPPOT("04","미지원재방","bg_indigo", "unsupport"),
    FILLER("05","필러","", "")
    ;

    final private String value;
    final private String desc;
    final private String style;
    final private String css;

    SchedTypeEnum(String value, String desc, String style, String css) {
        this.value = value;
        this.desc = desc;
        this.style = style;
        this.css = css;
    }

    public String value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    public String style() {
        return style;
    }

    public String css() { return css; }

    public boolean equals(String param) {
        return StringUtils.equalsIgnoreCase(this.value, param);
    }

    public static SchedTypeEnum findValue(final String v) {
        SchedTypeEnum find = null;
        for(SchedTypeEnum val: values()) {
            if(v.equals(val.value())){
                find = val;
                break;
            }
        }
        return find;
    }

    public static SchedTypeEnum findDesc(final String d) {
        SchedTypeEnum find = null;
        for(SchedTypeEnum val: values()) {
            if(d.equals(val.desc())){
                find = val;
                break;
            }
        }
        return find;
    }

    @Override
    @JsonValue
    public String getValue() {
        return value;
    }
}
