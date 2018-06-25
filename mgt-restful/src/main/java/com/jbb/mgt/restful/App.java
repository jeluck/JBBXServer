package com.jbb.mgt.restful;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jbb.server.common.util.MD5;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        String host = "https://yx.juxinli.com";
        String orgId = "jiebangbang";
        String apiKey = "3f326d1ee4d04ee6954372cf045701a3";
        Date now = new Date();

        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeMark = df.format(now);

        String website = "jiedaibao";
        String taskId = "JBB001";
        String jumpUrl = "http://jiebangbang.cn";
        // md5 32(orgId + timeMark + website + taskId +apiKey)
        String sign = MD5.MD5Encode(orgId + timeMark + website + taskId + apiKey);
        // ${host}/apiui/{orgId}/{sign}/{timeMark}/{website}/{taskId}?jumpUrl={jumpUrl}
        System.out.println(host + "/apiui/" + orgId + "/" + sign + "/" + timeMark + "/" + website + "/" + taskId
            + "?jumpUrl=" + jumpUrl);
    }
}
