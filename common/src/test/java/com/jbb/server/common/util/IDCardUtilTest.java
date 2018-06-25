package com.jbb.server.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.Assert;

public class IDCardUtilTest {

    @Test
    public void test() {
        String idcard = "430426198606056177";
        boolean f = IDCardUtil.validate(idcard);
        assertFalse(f);
        idcard = "430426198606056175";
        f = IDCardUtil.validate(idcard);
        assertTrue(f);
        
        idcard = "11010519491231002X";
        f = IDCardUtil.validate(idcard);
        assertTrue(f);
        
        idcard = "";
        f = IDCardUtil.validate(idcard);
        assertFalse(f);
        
        idcard = null;
        f = IDCardUtil.validate(idcard);
        assertFalse(f);
        
        idcard = "422822198209170069";
        f = IDCardUtil.validate(idcard);
        assertTrue(f);
        
        idcard = "130129199407100053";
        f = IDCardUtil.validate(idcard);
        assertTrue(f);
        
        idcard = "610330199701082213";
        f = IDCardUtil.validate(idcard);
        assertTrue(f);
        
        idcard = "111111111111111111";
        f = IDCardUtil.validate(idcard);
        assertFalse(f);
    }
    
    @Test
    public void testBelongProvince() {
        String[] provinces = {"41", "51", "52", "65", "54", "53", "45"};
        
        boolean flag = IDCardUtil.isBelongToProvince("422822198209170069", provinces);
        assertFalse(flag);
        flag = IDCardUtil.isBelongToProvince("412822198209170069", provinces);
        assertTrue(flag);
        flag = IDCardUtil.isBelongToProvince("522822198209170069", provinces);
        assertTrue(flag);
        flag = IDCardUtil.isBelongToProvince("532822198209170069", provinces);
        assertTrue(flag);
    }
}
