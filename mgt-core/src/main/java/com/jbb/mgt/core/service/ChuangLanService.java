package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.ChuangLanPhoneNumberRsp;
import com.jbb.mgt.core.domain.ChuangLanWoolCheckRsp;

public interface ChuangLanService {

    ChuangLanWoolCheckRsp woolCheck(String mobile, String ipAddress);

    ChuangLanPhoneNumberRsp validateMobile(String mobile);

    void sendMsgCode(String phoneNumber, String channelCode, String signName);
    
}
