package com.jbb.server;

import java.util.List;


 public class RegionEntry {
     private String code;
     private String name;
     
     private List<RegionEntry> sub;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<RegionEntry> getSub() {
        return sub;
    }
    public void setSub(List<RegionEntry> sub) {
        this.sub = sub;
    }
    
    
     
}
