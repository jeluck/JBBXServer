package com.jbb.mgt.core.domain;

public class IPAddressInfo {

    public static String SUCCESS_STATUS = "1";

    private String status;
    private String info;
    private String infocode;
    private String province;
    private String city;
    private String adcode;
    private String rectangle;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getRectangle() {
        return rectangle;
    }

    public void setRectangle(String rectangle) {
        this.rectangle = rectangle;
    }

    public String getIpArea() {
        String str = this.getProvince() != null ? this.getProvince() : "";
        str += this.getCity() != null ? this.getCity() : "";
        return str;

    }

    @Override
    public String toString() {
        return "IPAddressInfo [status=" + status + ", info=" + info + ", infocode=" + infocode + ", province="
            + province + ", city=" + city + ", adcode=" + adcode + ", rectangle=" + rectangle + "]";
    }

}
