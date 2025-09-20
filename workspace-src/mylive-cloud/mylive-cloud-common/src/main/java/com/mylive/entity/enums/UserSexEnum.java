package com.mylive.entity.enums;

public enum UserSexEnum {
    WOMAN(0, "女"), MAN(1, "男"), SECRECY(2, "未知");
    private Integer type;
    private String desc;

    UserSexEnum(Integer status, String desc) {
        this.type = status;
        this.desc = desc;
    }

    public static UserSexEnum getByType(Integer type) {
        for (UserSexEnum item : UserSexEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
