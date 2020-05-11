package com.dili.dr.glossary;

/**
 * 可用状态
 */
public enum EnabledEnum {
    ENABLED(true, "启用"),
    DISABLED(false, "禁用");

    private String name;
    private Boolean code ;

    EnabledEnum(Boolean code, String name){
        this.code = code;
        this.name = name;
    }

    public static EnabledEnum getEnabledState(Boolean code) {
        for (EnabledEnum anEnum : EnabledEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public Boolean getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
