package com.jbb.mgt.core.domain;

public class Statictic {
    private Integer TheDayBeforeYesterday;
    private Integer yesterday;
    private Integer Today;

    public Integer getTheDayBeforeYesterday() {
        return TheDayBeforeYesterday;
    }

    public void setTheDayBeforeYesterday(Integer theDayBeforeYesterday) {
        TheDayBeforeYesterday = theDayBeforeYesterday;
    }

    public Integer getYesterday() {
        return yesterday;
    }

    public void setYesterday(Integer yesterday) {
        this.yesterday = yesterday;
    }

    public Integer getToday() {
        return Today;
    }

    public void setToday(Integer today) {
        Today = today;
    }

    @Override
    public String toString() {
        return "Statictic [TheDayBeforeYesterday=" + TheDayBeforeYesterday + ", yesterday=" + yesterday + ", Today="
            + Today + "]";
    }

}
