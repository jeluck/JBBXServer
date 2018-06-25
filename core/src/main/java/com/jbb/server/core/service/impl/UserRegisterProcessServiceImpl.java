package com.jbb.server.core.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlScript;
import org.apache.commons.jexl3.MapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.domain.LoanPlatformPolicy;
import com.jbb.domain.SourcePolicy;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.IDCardUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.dao.AccountDao;
import com.jbb.server.core.domain.Property;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserProperty;
import com.jbb.server.core.service.AccountService;
import com.jbb.server.core.service.PushUserService;
import com.jbb.server.core.service.UserRegisterProcessService;
import com.jbb.server.core.util.SpringUtil;
import com.jbb.server.shared.map.StringMapper;

@Service("userRegisterProcessService")
public class UserRegisterProcessServiceImpl implements UserRegisterProcessService {

    private static Logger logger = LoggerFactory.getLogger(UserRegisterProcessServiceImpl.class);

    private static int COUNT = 500;

    private static int DEFAULT_RECOMMAND_CNT = 3;

    private static Map<Integer, Integer> userCntMap = new HashMap<Integer, Integer>();

    private static final JexlEngine JEXL = new JexlBuilder().cache(512).strict(true).silent(false).create();

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountDao accountDao;

    @Override
    public void applyToLendUser(int userId) {

        User user = accountService.getUser(userId);

        if (user == null) {
            logger.warn("not found user, userId=" + userId);
            return;
        }
        // 检查用户是否在N天内申请过，如果申请过，直接返回
        int days = PropertyManager.getIntProperty("jbb.user.applied.in.days", 3);

        if (isAppliedInXDays(userId, days)) {
            logger.warn("user {} applied in {} days", userId, days);
            return;
        }

        String policyContent = PropertyManager.getProperty(Property.SYS_LOAN_POLICY);

        if (policyContent == null) {
            logger.error("not found policy.");
            return;
        }
        LoanPlatformPolicy policy = null;
        try {
            policy = StringMapper.readPolicy(policyContent);
        } catch (Exception e) {
            logger.error("load policy error =" + e.getMessage());
            e.printStackTrace();
        }

        // 按策略获取出借人列表
        int[] targetUserIds = getLoanUsersByPolicy(policy, user);

        // 用户申请
        if (targetUserIds != null && targetUserIds.length > 0) {
            saveApplayUser(userId, targetUserIds);
            // 接口推送
            pushDataByInterface(user, targetUserIds);
        }

    }

    // 检查在天内是否申请过，并且有推荐过给相应出借方
    private boolean isAppliedInXDays(int userId, int days) {
        long todayStart = DateUtil.getStartTsOfDay(DateUtil.getCurrentTime());
        Timestamp start = new Timestamp(todayStart - days * DateUtil.DAY_MILLSECONDES);
        return accountDao.checkUserApplied(userId, start);
    }

    private void pushDataByInterface(User user, int[] targetUserIds) {
        logger.debug(">pushDataByInterface, userId =" + user.getUserId());
        for (int i = 0; i < targetUserIds.length; i++) {
            int targetUserId = targetUserIds[i];
            UserProperty pBeanName =
                accountService.searchUserPropertiesByUserIdAndName(targetUserId, UserProperty.P_BEAN_CLASS);
            UserProperty pServerPath =
                accountService.searchUserPropertiesByUserIdAndName(targetUserId, UserProperty.P_SERVER_PATH);
            if (pBeanName != null && pServerPath != null && !StringUtil.isEmpty(pBeanName.getValue())
                && !StringUtil.isEmpty(pServerPath.getValue())) {
                logger.info(" post to userId=  " + targetUserId + " , beanName =" + pBeanName.getValue()
                    + " , serverPath =" + pServerPath.getValue());
                PushUserService pushUserService = SpringUtil.getBean(pBeanName.getValue(), PushUserService.class);
                pushUserService.postUserData(user, pServerPath.getValue());
            }
        }
        logger.debug("<pushDataByInterface");
    }

    private boolean isMatchLender(User user, int lendUserId) {
        UserProperty p = accountDao.selectUserPropertyByUserIdAndName(lendUserId, Constants.FILTER_EXPRESSION);
        if (p != null && !StringUtil.isEmpty(p.getValue())) {
            // 执行JEXL语句
            try {
                String script = p.getValue();
                JexlScript jexlS = JEXL.createScript(script);

                // populate the context
                JexlContext context = new MapContext();

                String idCard = user.getIdCardNo();

                // 用户芝麻分
                UserProperty up =
                    accountService.searchUserPropertiesByUserIdAndName(user.getUserId(), Constants.SESAME_CREDIT_SCORE);
                int score = 0;
                if (up != null && !StringUtil.isEmpty(up.getValue())) {
                    try {
                        score = Integer.valueOf(up.getValue());
                    } catch (NumberFormatException e) {
                        // nothing to do
                    }
                }
                context.set("score", score);
                UserProperty upPlatform =
                    accountService.searchUserPropertiesByUserIdAndName(user.getUserId(), Constants.SIGNIN_PLATFROM);

                // 注册手机号所属系统：ios或者Android
                String platform = "";
                if (upPlatform != null) {
                    platform = upPlatform.getValue();
                }
                context.set("platform", platform);

                // 证件里的年龄，性别，地区
                if (IDCardUtil.validate(idCard)) {
                    context.set("age", IDCardUtil.calculateAge(idCard));
                    context.set("sex", IDCardUtil.calculateSex(idCard));
                    context.set("area", IDCardUtil.getProvinceCode(idCard));
                }else{
                    context.set("age", 0);
                    context.set("area", null);
                    context.set("sex", null);
                }
                // execute
                boolean result = (Boolean)jexlS.execute(context);

                logger.debug("jexl execute, resule={}, jexlS={} , score={}, platform={}, user={}, lenderId={} ", result, script, score, platform, user, lendUserId);
                return result;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private int[] getLoanUsersByPolicy(LoanPlatformPolicy policy, User user) {
        String sourceId = user.getSourceId();
        int recommandCnt = PropertyManager.getIntProperty("jbb.lender.limit", DEFAULT_RECOMMAND_CNT);

        List<SourcePolicy> resoucePolicy = getProbabilityBySourceId(policy, sourceId);

        List<SourcePolicy> sPolicy = new ArrayList<SourcePolicy>();
        // 按申请人的条件过滤待推荐的出借人列表
        for (SourcePolicy p : resoucePolicy) {
            if (isMatchLender(user, p.getUserId())) {
                sPolicy.add(p);
            }
        }
        // END 过滤完成

        if (sPolicy == null || sPolicy.size() == 0) {
            return null;
        }
        double pTotal = 0;
        int[] users = new int[sPolicy.size()];
        double[] ps = new double[sPolicy.size()];
        int index = 0;
        for (SourcePolicy sp : sPolicy) {
            users[index] = sp.getUserId();
            ps[index] = sp.getP();
            index++;
            pTotal += sp.getP();
        }

        // 如果推荐列表少于建议的用户数，直接返回
        if (users.length <= recommandCnt) {
            return users;
        }

        int[] userIndexs = getRecommand(recommandCnt, ps, pTotal);

        int[] rUsers = new int[userIndexs.length];
        for (int i = 0; i < userIndexs.length; i++) {
            rUsers[i] = users[userIndexs[i] - 1];
        }
        return rUsers;
    }

    private List<SourcePolicy> getProbabilityBySourceId(LoanPlatformPolicy policy, String sourceId) {
        List<SourcePolicy> sPolicy = new ArrayList<SourcePolicy>();
        SourcePolicy[] allPolicy = policy.getSourcePolicy();
        if (allPolicy == null || allPolicy.length == 0) {
            return null;
        }

        if (sourceId == null) {
            return null;
        }

        for (int i = 0; i < allPolicy.length; i++) {
            if (sourceId.equals(allPolicy[i].getSourceId())) {
                sPolicy.add(allPolicy[i]);
            }
        }
        return sPolicy;
    }

    private void saveApplayUser(int userId, int[] targetUserIds) {
        try {
            logger.info("userId = " + userId);
            for (int i = 0; i < targetUserIds.length; i++) {
                logger.info("------->" + targetUserIds[i]);
            }
            accountService.saveUserApplyRecords(userId, targetUserIds);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void printCnt() {
        for (Entry<Integer, Integer> e : userCntMap.entrySet()) {
            logger.info(e.getKey() + "\t" + e.getValue());
        }
    }

    // 以下由概率计算推荐的用户， 返回下标
    private int[] getRecommand(int recommandCnt, double[] p, double totalP) {
        // 推荐用户数

        // 初始化数据, 初始500个数据
        int length = COUNT;
        int[] x = new int[length];
        int begin = 0;
        double pt = 0;
        for (int j = 0; j < p.length; j++) {

            pt += p[j];
            int end = (int)(pt * length / totalP);
            for (int i = begin; i < end; i++) {

                x[i] = j + 1;
            }
            begin = end;
        }

        // 随机交换
        for (int i = 0; i < length / 2; i++) {
            swap(x, x.length);
        }

        // 计算推荐
        int[] r = new int[recommandCnt];
        int index = 0;

        for (int i = 0; i < x.length; i++) {
            if (index >= recommandCnt) {
                break;
            }
            int s = x[(int)(Math.random() * length)];
            while (isInArray(s, r)) {
                s = x[(int)(Math.random() * length)];
            }
            r[index++] = s;

        }
        return r;
    }

    private boolean isInArray(int val, int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == val) {
                return true;
            }
        }
        return false;
    }

    private void swap(int[] x, int len) {
        int i = (int)(Math.random() * len);
        int j = (int)(Math.random() * len);

        int temp = x[i];
        x[i] = x[j];
        x[j] = temp;
    }

}
