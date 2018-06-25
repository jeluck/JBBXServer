package com.jbb.server.core.domain;

import java.util.List;

public class SourceCounts {
    private String sourceId;

    private List<UserCounts> dateCounts;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public List<UserCounts> getDateCounts() {
        return dateCounts;
    }

    public void setDateCounts(List<UserCounts> dateCounts) {
        this.dateCounts = dateCounts;
    }

    @Override
    public String toString() {
        return "SourceCounts [sourceId=" + sourceId + ", dateCounts=" + dateCounts + "]";
    }

}
