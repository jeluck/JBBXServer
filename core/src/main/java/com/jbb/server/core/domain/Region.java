package com.jbb.server.core.domain;

public class Region {

    private String code;
    private String area;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Region [code=" + code + ", area=" + area + "]";
    }

   

}
