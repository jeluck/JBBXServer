package com.jbb.domain;

public class RecommandCntPolicy {
    private String jexlScript;
    private int recommandCnt;

    public String getJexlScript() {
        return jexlScript;
    }

    public void setJexlScript(String jexlScript) {
        this.jexlScript = jexlScript;
    }

    public int getRecommandCnt() {
        return recommandCnt;
    }

    public void setRecommandCnt(int recommandCnt) {
        this.recommandCnt = recommandCnt;
    }

    @Override
    public String toString() {
        return "RecommandCntPolicy [jexlScript=" + jexlScript + ", recommandCnt=" + recommandCnt + "]";
    }

}
