package com.hog.bigdata.enums;

public enum  OriginPurpEnum {


    SLAUGHTER(1,"屠宰"),
    FEEDING(2,"饲养"),
    SEED(3,"种用"),

    ;

    private int code;
    private String name;

    OriginPurpEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
