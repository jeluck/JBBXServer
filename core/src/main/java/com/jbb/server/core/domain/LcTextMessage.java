package com.jbb.server.core.domain;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LcTextMessage {
    @JsonProperty("from_peer")
    private String from;
    @JsonProperty("to_peers")
    private String[] to;
    private String message;
    @JsonProperty("conv_id")
    private String convId;
    @JsonProperty("transient")
    private boolean transientFlag;
    @JsonProperty("no_sync")
    private boolean noSync;
    @JsonProperty("msg_id")
    private String msgId;
    @JsonProperty("timestamp")
    private long timestamp;
    private Boolean recall;
    
    
    public LcTextMessage() {
        this.noSync = true;
    }
    
    

    public LcTextMessage(String from, String message, String convId, String msgId, long timestamp, Boolean recall) {
        super();
        this.from = from;
        this.message = message;
        this.convId = convId;
        this.msgId = msgId;
        this.timestamp = timestamp;
        this.recall = recall;
    }



    public LcTextMessage(String from, String[] to, String message, String convId, boolean transientFlag) {
        super();
        this.noSync = false;
        this.from = from;
        this.to = to;
        this.message = message;
        this.convId = convId;
        this.transientFlag = transientFlag;
    }
    
    public LcTextMessage(String from, int to, String message, String convId, boolean transientFlag) {
        super();
        this.noSync = false;
        this.from = from;
        this.to = new String[1];
        this.to[0] = String.valueOf(to);
        this.message = message;
        this.convId = convId;
        this.transientFlag = transientFlag;
    }
    
    public LcTextMessage(int from, int to, String message, String convId, boolean transientFlag) {
        super();
        this.noSync = false;
        this.from = String.valueOf(from);
        this.to = new String[1];
        this.to[0] = String.valueOf(to);
        this.message = message;
        this.convId = convId;
        this.transientFlag = transientFlag;
    }
   
   
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getConvId() {
        return convId;
    }

    public void setConvId(String convId) {
        this.convId = convId;
    }

    public boolean isTransientFlag() {
        return transientFlag;
    }

    public void setTransientFlag(boolean transientFlag) {
        this.transientFlag = transientFlag;
    }
    
    

    public boolean isNoSync() {
        return noSync;
    }

    public void setNoSync(boolean noSync) {
        this.noSync = noSync;
    }
    
    

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getRecall() {
        return recall;
    }

    public void setRecall(Boolean recall) {
        this.recall = recall;
    }

    @Override
    public String toString() {
        return "LcTextMessage [from=" + from + ", to=" + Arrays.toString(to) + ", message=" + message + ", convId="
            + convId + ", transientFlag=" + transientFlag + ", noSync=" + noSync + ", msgId=" + msgId + ", timestamp="
            + timestamp + ", recall=" + recall + "]";
    }

    
}
