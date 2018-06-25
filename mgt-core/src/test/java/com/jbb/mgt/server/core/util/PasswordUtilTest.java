 package com.jbb.mgt.server.core.util;

import org.junit.Test;

public class PasswordUtilTest {
     
     @Test
     public void testGeneratePw(){
         
         System.out.println(PasswordUtil.passwordHash("...JbbEmgt123."));
     }

}
