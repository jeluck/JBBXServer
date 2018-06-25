package com.jbb.server.core.domain;

public class Fee {

    private String name;
    private String body;
    private String desc;
    private int amount;
    
    public Fee(){
        super();
    }

    public Fee(String name, String body, String desc, int amount) {
        super();
        this.name = name;
        this.body = body;
        this.desc = desc;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public String getDesc() {
        return desc;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Fee [name=" + name + ", body=" + body + ", desc=" + desc + ", amount=" + amount + "]";
    }

}
