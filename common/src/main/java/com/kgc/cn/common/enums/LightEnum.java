package com.kgc.cn.common.enums;

public enum LightEnum {
    RED("red"),
    GREEN("green"),
    YELLOW("yellow"),
    NULL("null");

    private String msg;

    LightEnum(String msg) {
        this.msg = msg;
    }

    /**
     * 匹配操作码
     */
    public static LightEnum matchCode(String opCodeStr){
        for (LightEnum opCode : LightEnum.values()) {
            if (opCode.name().equalsIgnoreCase(opCodeStr)) {
                return opCode;
            }
        }
        return LightEnum.NULL;
    }
}
