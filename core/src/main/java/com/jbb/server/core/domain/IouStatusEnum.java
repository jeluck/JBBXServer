 package com.jbb.server.core.domain;

 public enum IouStatusEnum {
     
     PUBLISH("发布",0), 
     EFFECTIVE("生效", 1), 
     MODIFY("修改申请", 2), 
     MODIFYAPPROVED("出借人确认修改", 3),
     MODIFYRJECT("出借人拒绝修改", 4), 
     EXTENSION("申请延期", 5), 
     EXTENSIONAPPROVED("出借人确认延期", 6), 
     EXTENSIONREJECT("出借人拒绝延期", 7),
     RETURN("申请已还", 8), 
     RETURNAPPROVED("出借人确认已还", 9), 
     RETURNREJECT("出借人拒绝已还", 10);
     
     
     private String desc;
     private int status;

     // 构造方法
     private IouStatusEnum(String desc, int status) {
         this.desc = desc;
         this.status = status;
     }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public static IouStatusEnum getIouStatus(int status) {
        for (IouStatusEnum item : IouStatusEnum.values()) {
            if (item.getStatus() == status) {
                return item;
            }
        }
        return null;
    }
  
     
     
}
