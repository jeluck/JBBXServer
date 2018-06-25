package com.jbb.mgt.server.core.util;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlScript;
import org.apache.commons.jexl3.MapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.mgt.core.domain.User;
import com.jbb.server.common.util.IDCardUtil;
import com.jbb.server.common.util.StringUtil;

public class JexlUtil {
    private static Logger logger = LoggerFactory.getLogger(JexlUtil.class);
    private static final JexlEngine JEXL = new JexlBuilder().cache(512).strict(true).silent(false).create();

    public static boolean executeJexl(String script, User user) {
        if (StringUtil.isEmpty(script)) {
            return true;
        }

        if (user == null) {
            return false;
        }

        // 执行JEXL语句
        try {

            JexlScript jexlS = JEXL.createScript(script);
            // populate the context
            JexlContext context = new MapContext();
            String idCard = user.getIdCard();
            context.set("score", user.getZhimaScore());
            context.set("platform", user.getPlatform());
            context.set("channelCode", user.getChannelCode());
            // 证件里的年龄，性别，地区
            if (IDCardUtil.validate(idCard)) {
                context.set("age", IDCardUtil.calculateAge(idCard));
                context.set("sex", IDCardUtil.calculateSex(idCard));
                context.set("area", IDCardUtil.getProvinceCode(idCard));
            } else {
                context.set("age", 0);
                context.set("area", null);
                context.set("sex", null);
            }
            // execute
            boolean result = (Boolean)jexlS.execute(context);
            logger.debug("jexl execute, resule={}, jexlS={} ,userId={} ", result, script, user.getUserId());
            return result;
        } catch (Exception e) {
            logger.debug("jexl execute with error, error" + e.getMessage());
        }
        return false;
    };

}
