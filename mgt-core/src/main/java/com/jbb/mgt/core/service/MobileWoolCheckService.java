 package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.ChuangLanWoolCheck;

public interface MobileWoolCheckService {


     void insertWoolCheckResult(ChuangLanWoolCheck data);

     /**
      * 获取最近一次
      * 
      * @param mobile
      */
     ChuangLanWoolCheck getWoolCheckResult(String mobile);
}
