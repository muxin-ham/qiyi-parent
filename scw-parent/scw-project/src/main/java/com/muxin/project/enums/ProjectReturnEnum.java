package com.muxin.project.enums;

public enum ProjectReturnEnum {

    VIRTUAL((byte) 0, "虚拟物品"),
    REAL((byte) 1, "实物回报");

    private byte code;
    private String type;

    private ProjectReturnEnum(byte code, String type) {
        this.code = code;
        this.type = type;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
