package com.jbb.server.core.domain;

public class LiveDetectForeResult {

    private int sim;
    private boolean valid;
    private String liveMsg;
    private String compareMsg;

    public LiveDetectForeResult() {
        super();
    }

    public LiveDetectForeResult(int sim, boolean valid,  String compareMsg, String liveMsg) {
        super();
        this.sim = sim;
        this.valid = valid;
        this.liveMsg = liveMsg;
        this.compareMsg = compareMsg;
    }

    public int getSim() {
        return sim;
    }

    public void setSim(int sim) {
        this.sim = sim;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getLiveMsg() {
        return liveMsg;
    }

    public void setLiveMsg(String liveMsg) {
        this.liveMsg = liveMsg;
    }

    public String getCompareMsg() {
        return compareMsg;
    }

    public void setCompareMsg(String compareMsg) {
        this.compareMsg = compareMsg;
    }

    @Override
    public String toString() {
        return "LiveDetectForeResult [sim=" + sim + ", valid=" + valid + ", liveMsg=" + liveMsg + ", compareMsg="
            + compareMsg + "]";
    }

}
