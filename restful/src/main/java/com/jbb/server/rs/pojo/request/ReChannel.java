package com.jbb.server.rs.pojo.request;

public class ReChannel {

    private String filterExpression;
    private String sources;
    private int weight;
    private int maxCnt;

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMaxCnt() {
        return maxCnt;
    }

    public void setMaxCnt(int maxCnt) {
        this.maxCnt = maxCnt;
    }

    public String getFilterExpression() {
        return filterExpression;
    }

    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }

    @Override
    public String toString() {
        return "ReChannel [filterExpression=" + filterExpression + ", sources=" + sources + ", weight=" + weight
            + ", maxCnt=" + maxCnt + "]";
    }

}
