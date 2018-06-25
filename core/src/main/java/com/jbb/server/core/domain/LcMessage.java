package com.jbb.server.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LcMessage {
    private static final int LC_TEXT_TYPE = -1;
    
    @JsonProperty("_lctype")
    private int lctype;
    @JsonProperty("_lctext")
    private String lctext;
    @JsonProperty("_lcattrs")
    private LcMessageAttrs lcattrs;

    public LcMessage() {}

    public LcMessage(String lctext) {
        this.lctype = LcMessage.LC_TEXT_TYPE;
        this.lctext = lctext;
    }

    public LcMessage(LcMessageAttrs lcattrs) {
        this.lctype = LcMessage.LC_TEXT_TYPE;
        this.lctext = null;
        this.lcattrs = lcattrs;
    }

    public LcMessage(String lctext, LcMessageAttrs lcattrs) {
        super();
        this.lctype = LcMessage.LC_TEXT_TYPE;
        this.lctext = lctext;
        this.lcattrs = lcattrs;
    }

    public int getLctype() {
        return lctype;
    }

    public void setLctype(int lctype) {
        this.lctype = lctype;
    }

    public String getLctext() {
        return lctext;
    }

    public void setLctext(String lctext) {
        this.lctext = lctext;
    }

    public LcMessageAttrs getLcattrs() {
        return lcattrs;
    }

    public void setLcattrs(LcMessageAttrs lcattrs) {
        this.lcattrs = lcattrs;
    }

    @Override
    public String toString() {
        return "LcMessage [lctype=" + lctype + ", lctext=" + lctext + ", lcattrs=" + lcattrs + "]";
    }

    
   
}
