 package com.jbb.server.common.util;

import org.junit.Test;

public class DesensitizedUtilTest {

     @Test
     public void getName() {
       String name =   DesensitizedUtil.chineseName("王麻子");
       System.out.println(name);
     }
     
     @Test
     public void getNum() {
       String name =   DesensitizedUtil.mobilePhone("12312312311");
       System.out.println(name);
     }
}
