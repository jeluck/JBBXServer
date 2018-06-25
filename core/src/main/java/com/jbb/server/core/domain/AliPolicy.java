package com.jbb.server.core.domain;

public class AliPolicy {

    private String accessid;
    private String policy;
    private String signature;
    private String dir;
    private String host;
    private long expire;
    
    public AliPolicy(){
        
    }

    public AliPolicy(String accessid, String policy, String signature, String dir, String host, long expire) {
        super();
        this.accessid = accessid;
        this.policy = policy;
        this.signature = signature;
        this.dir = dir;
        this.host = host;
        this.expire = expire;
    }

    public String getAccessid() {
        return accessid;
    }

    public void setAccessid(String accessid) {
        this.accessid = accessid;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "AliPolicy [accessid=" + accessid + ", policy=" + policy + ", signature=" + signature + ", dir=" + dir
            + ", host=" + host + ", expire=" + expire + "]";
    }

}
