package com.jbb.mgt.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import net.sf.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        JSONObject xyrs = new JSONObject();
        xyrs.element("logo", "https://jiebangbang.cn/config/logo/iou-logo7.png");
        xyrs.element("url", "http://xyrsapi1.fengyjf.com/h5/invite.jsp?invitationCode=null&channelCode=jiebangbang1");
        xyrs.element("name", "信用人生");
        xyrs.element("shortName", "xyrs");
        xyrs.element("desc1", "无视黑白");
        xyrs.element("desc2", "2分钟认证");
        xyrs.element("desc3", "10秒到账");

        JSONObject xnqb = new JSONObject();
        xnqb.element("logo", "https://jiebangbang.cn/config/logo/iou-logo8.png");
        xnqb.element("url", "https://api.9maibei.com/Mall/public/user/promotion-register.html?channel=jiebangbang");
        xnqb.element("name", "犀牛钱包");
        xnqb.element("shortName", "xiniuqianbao");
        xnqb.element("desc1", "额度：最高5000");
        xnqb.element("desc2", "有身份证就能申请下款");
        xnqb.element("desc3", "");

        /// Users/VincentTang/ws/jbb/jbb-h5-singup/config
        File file = new File("/Users/VincentTang/Desktop/version2.csv");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        int index = 0;
        while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
            System.out.println(s);
            if (index++ == 0)
                continue;

            String[] arr = s.split(",");

            File dir = new File("/Users/VincentTang/ws/jbb/config/0" + arr[0]);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File configFile = new File("/Users/VincentTang/ws/jbb/config/0" + arr[0] + "/config.json");
            configFile.createNewFile();
            FileOutputStream os = new FileOutputStream(configFile);
            
            JSONObject configO = new JSONObject();
            configO.element("sourceId", "0"+arr[0]);
            configO.element("name", arr[1]);
            configO.element("redirectUrl", "https://jiebangbang.cn/"+arr[4]+"/?sourceId=0"+arr[0]);

            if("xiniuqianbao".equals(arr[5])){
                configO.element("h5", JSONObject.toBean(xnqb));
            }else if("xyrs".equals(arr[5])){
                configO.element("h5", JSONObject.toBean(xyrs));
            }
          
            os.write(configO.toString(4).getBytes());
            os.close();

        }
        br.close();;

    }
}
