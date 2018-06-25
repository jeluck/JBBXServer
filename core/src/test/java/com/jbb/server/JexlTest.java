package com.jbb.server;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlScript;
import org.apache.commons.jexl3.MapContext;

import com.jbb.server.common.util.IDCardUtil;

public class JexlTest {

    private static final JexlEngine JEXL = new JexlBuilder().cache(512).strict(true).silent(false).create();

    public static void main(String[] args) throws InterruptedException {
        //年龄为0||[23,35], 且芝麻分>580
//        String script = "if (((age>=21 && age<=38) || age==0) && score >=580 && area!='65' && area!='15'){return true} else {return false}";
        
        String script = "if (score  >=600 && sourceId > '019' ){return true} else {return false}";

        JexlScript e = JEXL.createScript(script);
        
        String idCard = "";

        // populate the context
        JexlContext context = new MapContext();
        context.set("sourceId", "039");
        context.set("age", 0);
        context.set("score", 660);
        context.set("area", null);
        boolean result = (Boolean)e.execute(context);
        System.out.println(result);
        context.set("sourceId", "012");
        context.set("age", 20);
        context.set("score", 600);
        result = (Boolean)e.execute(context);
        System.out.println(result);
        
        
        context.set("sourceId", "011");
        context.set("age", 0);
        context.set("score", 450);
        result = (Boolean)e.execute(context);
        System.out.println(result);
        
        context.set("age", 32);
        context.set("score", 630);
        result = (Boolean)e.execute(context);
        System.out.println(result);
        
        
        context.set("score", 630);
        if (IDCardUtil.validate(idCard)) {
            context.set("age", IDCardUtil.calculateAge(idCard));
            context.set("sex", IDCardUtil.calculateSex(idCard));
            context.set("area", IDCardUtil.getProvinceCode(idCard));
        }else{
            context.set("age", 0);
            context.set("area", null);
            context.set("sex", null);
        }
        result = (Boolean)e.execute(context);
        System.out.println(result);
        
//        BeeperControl bc = new BeeperControl();
   
        
        
//        
//        for(int i=0;i<10;i++){
//            new Thread(new Runnable(){
//                public void run(){
//                    System.out.println("xxx");
//                    bc.sendUserWithDelay(111, 10);
//                }
//            }).start();
//            Thread.sleep(1000);
//            bc.sendUserWithDelay(i, 10);
//            if(i>5){
//                bc.removeSendTask(i);
//            }
//        }
//        Thread.sleep(20000);
//        bc.destory();
    }
    
    
    
    
}


