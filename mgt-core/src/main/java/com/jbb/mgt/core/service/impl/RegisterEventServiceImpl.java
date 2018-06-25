package com.jbb.mgt.core.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jgroups.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.github.pagehelper.util.StringUtil;
import com.jbb.domain.LoanPlatformPolicy;
import com.jbb.domain.RecommandCntPolicy;
import com.jbb.mgt.core.dao.ChannelDao;
import com.jbb.mgt.core.dao.UserApplyRecordDao;
import com.jbb.mgt.core.dao.UserDao;
import com.jbb.mgt.core.domain.Channel;
import com.jbb.mgt.core.domain.DataFlowBase;
import com.jbb.mgt.core.domain.DataFlowSetting;
import com.jbb.mgt.core.domain.OrgRecharges;
import com.jbb.mgt.core.domain.Property;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.mgt.core.domain.mapper.Mapper;
import com.jbb.mgt.core.domain.mq.RegisterEvent;
import com.jbb.mgt.core.service.OrgRechargeDetailService;
import com.jbb.mgt.core.service.OrgRechargesService;
import com.jbb.mgt.core.service.RegisterEventService;
import com.jbb.mgt.server.core.util.JexlUtil;
import com.jbb.mgt.server.core.util.OrgDataFlowSettingsUtil;
import com.jbb.mgt.server.core.util.RegisterEventUtil;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.mq.MqClient;
import com.jbb.server.mq.Queues;
import com.jbb.server.shared.map.StringMapper;

@Service("RegisterEventService")
public class RegisterEventServiceImpl implements RegisterEventService {

    private static int DELAY_STEP = 1;
    private static int DEFAULT_RECOMMAND_CNT = 3;

    private static DefaultTransactionDefinition NEW_TX_DEFINITION =
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    private static Map<Integer, Integer> userCntMap = new HashMap<Integer, Integer>();

    private static Logger logger = LoggerFactory.getLogger(RegisterEventServiceImpl.class);

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserApplyRecordDao userApplyRecordDao;
    @Autowired
    private OrgRechargesService orgRechargesService;
    @Autowired
    private OrgRechargeDetailService orgRechargeDetailService;

    @Autowired
    private PlatformTransactionManager txManager;

    private void rollbackTransaction(TransactionStatus txStatus) {
        if (txStatus == null) {
            return;
        }

        try {
            txManager.rollback(txStatus);
        } catch (Exception er) {
            logger.warn("Cannot rollback transaction", er);
        }
    }

    @Override
    public void publishRegisterEvent(RegisterEvent event) {
        try {
            final long msgExpiry = PropertyManager.getLongProperty("ppc.registeruser.message.expiry", 300L) * 1000L;
            MqClient.send(Queues.JBB_MGT_USER_REGISTER_QUEUE_ADDR, Mapper.toBytes(event), msgExpiry);
        } catch (Exception e) {
            logger.error("Exception in sending register user event", e);
        }

    }

    @Override
    public void publishRegisterEvent(Integer step, Boolean test, int userId, String channelCode) {
        if (step == null) {
            return;
        }
        boolean testF = (test != null && test == true);
        if (testF) {
            logger.info("test user, userId =" + userId + " , channelCode=" + channelCode);
            return;
        }

        RegisterEvent event = new RegisterEvent(step, userId, channelCode);
        this.publishRegisterEvent(event);
    }

    /**
     * 处理消息
     */
    @Override
    public void processMessage(RegisterEvent event) {
        logger.debug("> processMessage(), event= " + event);
        if (event == null) {
            logger.debug("< processMessage(), event is null");
            return;
        }

        if (StringUtil.isEmpty(event.getChannelCode()) || event.getUserId() == 0 || event.getStep() == 0) {
            // 来源不明渠道，或者用户ID为空，或者step为空。 不做推荐
            logger.debug("< processMessage(), chnanelCode is empty, or user id is not set, or step is not set");
            return;
        }

        int delay = PropertyManager.getIntProperty("jbb.user.register.step.delay", 600);
        // 如果是第一步，delay，稍后处理
        if (event.getStep() == DELAY_STEP) {
            logger.debug("delay to process, event= " + event);
            RegisterEventUtil.addRegisterEventWithDelay(event, delay);
        } else {
            // 其他步骤，马上处理
            logger.debug("processing, event = " + event);
            RegisterEventUtil.removeSendTask(event);
            processEvent(event);
        }
        logger.debug("< processMessage()");
    }

    @Override
    public void processSpecApply(User user) {
        logger.debug("> processSpecApply(), user= " + user);
        // TODO 将特殊的用户(目前指 580以下)，分配给特别的账户查看
        
        logger.debug("< processSpecApply()");

    }

    @Override
    public void processEventToSelfOrgs(RegisterEvent event) {
        logger.debug("> processEventToSelfOrgs(), event= " + event);
        Channel channel = channelDao.selectChannelByCode(event.getChannelCode());
        User user = userDao.selectUserById(event.getUserId());

        if (channel.isJbbChannel()) {
            // 非JBB渠道，不做推荐
            return;
        }

        if (user == null) {
            // 用户不存在，不做推荐
            return;
        }

        // 特殊处理, 580以下的客户，分配给特别的账户查询
        if (user.getZhimaScore() < 580) {
            this.processSpecApply(user);
            return;
        }
        
        // 从系统配置加载 第一档的组织配置
        String[] selfOrgs = PropertyManager.getProperties("sys.policy.self.orgs");

        if (selfOrgs == null || selfOrgs.length == 0) {
            return;
        }

        int index = (int)(Math.random() * selfOrgs.length);

        int orgId = Integer.valueOf(selfOrgs[index]);
        applyToOrg(user.getUserId(), orgId);

        logger.debug("< processEventToSelfOrgs()");

    }

    /**
     * 处理RegisterEvent
     */
    @Override
    public void processEvent(RegisterEvent event) {
        logger.debug("> processEvent(), event= " + event);
        Channel channel = channelDao.selectChannelByCode(event.getChannelCode());
        User user = userDao.selectUserById(event.getUserId());

        if (!channel.isJbbChannel()) {
            // 非JBB渠道，不做推荐
            logger.debug("< processEvent(), channel is not JBB Channel, channel code = " + channel.getChannelCode());
            return;
        }

        if (user == null) {
            // 用户不存在，不做推荐
            logger.debug("< processEvent(), user not found");
            return;
        }

        // Start 检查用户是否处理过
        int userId = user.getUserId();
        if (OrgDataFlowSettingsUtil.APPLY_USER_SET.contains(userId)) {
            logger.warn("duplicate message, userId=" + userId);
            return;
        }
        // END

        // 继续推荐流程
        // OrgDataFlowSettingsUtil.APPLY_USER_SET.add(userId);

        applyToOrgs(channel, user);
        logger.debug("< processEvent()");
    }

    private void applyToOrgs(Channel channel, User user) {

        logger.info(">applyToOrgs(), channel= " + channel + " , user =" + user);

        int userId = user.getUserId();

        // 检查用户是否在N天内申请过，如果申请过，直接返回
        int days = PropertyManager.getIntProperty("jbb.user.applied.in.days", 30);
        if (isAppliedInXDays(userId, days)) {
            logger.info("user {} applied in {} days", userId, days);
            return;
        }

        // 特殊处理, 580以下的客户，分配给特别的账户查询
        if (user.getZhimaScore() < 580) {
            this.processSpecApply(user);
            return;
        }

        // 加载 系统策略
        LoanPlatformPolicy policy = loadLoanPlatformPolicy();

        // 获取 默认推荐数
        int recommandMaxCnt = countRecommandCnt(policy, user);
        logger.info("recommandMaxCnt : " + recommandMaxCnt);

        if (recommandMaxCnt == 0) {
            logger.warn("because of recommandMaxCnt is zero, user not recommand to any organization, user = {}", user);
            return;
        }

        // 获取满足条件的出借组织
        Set<Integer> toChooseOrgs = new HashSet<Integer>();

        OrgDataFlowSettingsUtil.ORG_SETTINGS.forEach((orgId, orgSetting) -> {
            if (orgSetting.isClosed()) {
                return;
            }

            // 检查此用户是否存在于此组织，如果存在，则不再推荐
            if (userDao.checkUserExistInOrg(orgId, user.getPhoneNumber(), null, null)) {
                return;
            }

            // 检查用户是否还有导流余额
            OrgRecharges o = orgRechargesService.selectOrgRechargesForUpdate(orgId);
            if (o.getDataBudget() < 0) {
                logger.info(" org's data budget < 0 , orgId = " + orgId);
                return;
            }

            // 检查是否符合组织条件，检查分两部分， 一是org setting
            int dfId = orgSetting.getDataFlowId();
            DataFlowBase dfBase = OrgDataFlowSettingsUtil.DF_SETTINGS_BASE.get(dfId);
            boolean orgFlag = JexlUtil.executeJexl(dfBase.getJexlScript(), user);

            if (!orgFlag)
                return;

            // 获取当天推荐数，检查是否超过最大推荐数限制
            long startTs = DateUtil.getTodayStartTs();
            long endTs = startTs + DateUtil.DAY_MILLSECONDES;
            int cnt = userApplyRecordDao.countUserApply(orgId, new Timestamp(startTs), new Timestamp(endTs));
            if (cnt >= orgSetting.getMaxAvlue()) {
                return;
            }
            // 获取系统 对组织配置的策略
            String script = PropertyManager.getProperty("sys.policy.org.script." + orgId);
            boolean sFlag = JexlUtil.executeJexl(script, user);

            if (!sFlag)
                return;

            logger.info(" orgId = " + orgId + " , orgFlag =" + orgFlag + " , sFlag =" + sFlag);
            if (orgFlag && sFlag) {
                toChooseOrgs.add(orgId);
            }
        });

        // 执行互斥策略，相同组的组织不同时推荐
        Set<Integer> toRecommandOrgs = filterOrgsByGroupPolicy(policy, toChooseOrgs);

        if (toRecommandOrgs.size() != 0 && toRecommandOrgs.size() <= recommandMaxCnt) {
            // 待筛选出借人数少于需要推荐的人数， 直接返回
            applyToOrgs(user, toRecommandOrgs);
            return;
        }

        // 按组织需求量确认权重，推荐出借组织
        Set<Integer> recommandOrgs = filterOrgsBySettings(recommandMaxCnt, toRecommandOrgs);

        // 推荐给筛选出来的出借组织
        applyToOrgs(user, recommandOrgs);

        logger.info(">applyToOrgs()");

    }

    private Set<Integer> filterOrgsBySettings(int recommandCnt, Set<Integer> orgs) {

        // 如果为空，直接返回
        if (orgs == null) {
            return new HashSet<Integer>();
        }
        // 如果数据量少于推荐量，直接返回
        if (orgs.size() <= recommandCnt) {
            return orgs;
        }

        // 按权重筛选推荐列表
        int size = orgs.size();
        double totalP = 0.0;
        int[] users = new int[size];
        double[] userPs = new double[size];
        int index = 0;

        for (Integer orgId : orgs) {
            // int weight = PropertyManager.getIntProperty("sys.org.weight." + orgId, 1);
            DataFlowSetting setting = OrgDataFlowSettingsUtil.ORG_SETTINGS.get(orgId);
            int weight = setting.getMaxAvlue();
            totalP += weight;
            users[index] = orgId;
            userPs[index] = totalP;
            index++;
        } ;

        Set<Integer> recommandOrgs = new HashSet<Integer>();
        int cnt = 0;
        for (int i = 0; i < recommandCnt;) {
            double p = Math.random() * totalP;
            int userIndex = 0;
            for (int j = 0; j < userPs.length; j++) {
                if (userPs[j] >= p) {
                    userIndex = j;
                    break;
                }
            }
            if (!recommandOrgs.contains(users[userIndex])) {
                recommandOrgs.add(users[userIndex]);
                cnt++;
            }

            if (cnt >= recommandCnt) {
                break;
            }
        }

        return recommandOrgs;

    }

    /**
     * 插入申请表，推荐给组织
     * 
     * @param user
     * @param orgs
     */

    private void applyToOrgs(User user, Set<Integer> orgs) {
        logger.debug("> applyToOrgs(), user= " + user + ", orgs=" + Util.print(orgs));
        orgs.forEach(orgId -> {

            TransactionStatus txStatus = null;
            try {
                txStatus = txManager.getTransaction(NEW_TX_DEFINITION);

                DataFlowSetting setting = OrgDataFlowSettingsUtil.ORG_SETTINGS.get(orgId);
                int dFlowId = setting.getDataFlowId();
                DataFlowBase dfBase = OrgDataFlowSettingsUtil.DF_SETTINGS_BASE.get(dFlowId);
                int price = dfBase.getPrice();
                OrgRecharges o = orgRechargesService.selectOrgRechargesForUpdate(orgId);
                o.setTotalDataExpense(o.getTotalDataExpense() + price);
                orgRechargesService.updateOrgRecharges(o);
                orgRechargeDetailService.handleConsumeData(orgId, user.getUserId(), price);

                applyToOrg(user.getUserId(), orgId);

                txManager.commit(txStatus);
                txStatus = null;

            } finally {

                rollbackTransaction(txStatus);
            }

        });
        logger.debug("< applyToOrgs()");
    }

    private void applyToOrg(int userId, int orgId) {

        logger.debug("> applyToOrg(), userId= " + userId + ", orgId=" + orgId);
        UserApplyRecord record = new UserApplyRecord(userId, orgId);
        record.setStatus(1);
        userApplyRecordDao.insertUserApplyRecord(record);
        logger.debug("> applyToOrg()");
    }

    private Set<Integer> filterOrgsByGroupPolicy(LoanPlatformPolicy policy, Set<Integer> toChooseOrgs) {

        Map<String, Set<Integer>> map = new HashMap<String, Set<Integer>>();
        if (policy == null) {
            return toChooseOrgs;
        }

        String[] groups = policy.getGroups();
        if (groups == null || groups.length == 0) {
            return toChooseOrgs;
        }

        for (int i = 0; i < groups.length; i++) {
            if (StringUtil.isEmpty(groups[i])) {
                continue;
            }
            String[] orgs = groups[i].split("\\|");
            Set<Integer> groupSet = new HashSet<Integer>();
            for (String orgIdStr : orgs) {
                try {
                    int orgId = Integer.valueOf(orgIdStr);
                    if (toChooseOrgs.contains(orgId)) {
                        groupSet.add(orgId);
                    }
                } catch (Exception e) {
                    logger.warn("parse org group to int error: group=" + orgs[i]);
                }
            }
            if (groupSet.size() > 0) {
                map.put(groups[i], groupSet);
            }
        }

        if (map.size() == 0) {
            return toChooseOrgs;
        }

        Set<Integer> setA = new HashSet<Integer>();
        Set<Integer> setD = new HashSet<Integer>();
        map.forEach((key, setC) -> {
            // 差值
            Set<Integer> setE = new HashSet<Integer>();
            setE.addAll(setC);
            setE.removeAll(setD);

            if (setE.size() == 0) {
                return;
            }

            // 先把一个用户U
            Integer u = getRandomElement(setE);

            setA.removeAll(setE);
            setA.add(u);

            setD.addAll(setE);
            setD.remove(u);

        });
        return setA;
    }

    /**
     * 从set中随机取得一个元素
     * 
     * @param set
     * @return
     */
    public static <E> E getRandomElement(Set<E> set) {
        int index = (int)(Math.random() * set.size());
        int i = 0;
        for (E e : set) {
            if (i == index) {
                return e;
            }
            i++;
        }
        return null;
    }

    private int countRecommandCnt(LoanPlatformPolicy policy, User user) {
        int recommandMaxCnt = PropertyManager.getIntProperty("jbb.recommand.cnt", DEFAULT_RECOMMAND_CNT);
        if (policy == null) {
            return recommandMaxCnt;
        }
        // 获取推荐数策略
        RecommandCntPolicy[] recommandCntPolicy = policy.getRecommandCntPolicy();
        if (!CommonUtil.isNullOrEmpty(recommandCntPolicy)) {
            for (RecommandCntPolicy p : recommandCntPolicy) {
                if (recommandMaxCnt <= p.getRecommandCnt()) {
                    logger.debug(" recommand policy -> continue, policy limit {} >= {} ", p.getRecommandCnt(),
                        recommandMaxCnt);
                    continue;
                }

                logger.debug(" recommand policy -> " + p);
                boolean flag = JexlUtil.executeJexl(p.getJexlScript(), user);
                if (flag) {
                    recommandMaxCnt = (p.getRecommandCnt() < recommandMaxCnt ? p.getRecommandCnt() : recommandMaxCnt);
                }
            }
        }
        return recommandMaxCnt;
    }

    private LoanPlatformPolicy loadLoanPlatformPolicy() {
        LoanPlatformPolicy policy = null;
        // 获取policy文件
        String policyContent = PropertyManager.getProperty(Property.SYS_LOAN_POLICY);

        if (policyContent == null) {
            logger.error("not found policy.");
            return null;
        }
        try {
            policy = StringMapper.readPolicy(policyContent);
        } catch (Exception e) {
            logger.error("load policy error =" + e.getMessage());
        }
        return policy;
    }

    // 检查在天内是否申请过，并且有推荐过给相应出借方
    private boolean isAppliedInXDays(int userId, int days) {
        long todayStart = DateUtil.getStartTsOfDay(DateUtil.getCurrentTime());
        Timestamp start = new Timestamp(todayStart - days * DateUtil.DAY_MILLSECONDES);
        return userDao.checkUserApplied(userId, start);
    }

}
