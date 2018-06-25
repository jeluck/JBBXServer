package com.jbb.mgt.rs.action.index;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.jbb.mgt.core.domain.OrgRecharges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Bill;
import com.jbb.mgt.core.domain.StatisticsMoney;
import com.jbb.mgt.core.service.OrgRechargesService;
import com.jbb.mgt.core.service.UserApplyRecordService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.shared.rs.Util;

@Service(IndexCountAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IndexCountAction extends BasicAction {

    public static final String ACTION_NAME = "IndexCountAction";

    private static Logger logger = LoggerFactory.getLogger(IndexCountAction.class);

    private Response response;

    @Autowired
    @Qualifier("UserApplyRecordService")
    private UserApplyRecordService userApplyRecordService;

    @Autowired
    @Qualifier("OrgRechargesService")
    private OrgRechargesService orgRechargesService;

    @Override
    protected ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    /**
     * 首页统计数据
     *
     * @param startDate
     * @param endDate
     */
    public void indexCountData(String startDate, String endDate) {
        logger.debug(">indexCountData startDate={}, endDate{}", startDate, endDate);
        Timestamp tsStartDate = Util.parseTimestamp(startDate, TimeZone.getDefault());
        Timestamp tsEndDate = Util.parseTimestamp(endDate, TimeZone.getDefault());
        this.response.statisticsMoney
            = userApplyRecordService.selectIndexCountData(tsStartDate, tsEndDate, this.account.getOrgId());
        logger.debug("<indexCountData");
    }

    /**
     * 机构余额数据
     */
    public void orgMarginData() {
        logger.debug(">orgMarginData");
        OrgRecharges orgRecharges = orgRechargesService.selectOrgRecharges(this.account.getOrgId());
        if (null != orgRecharges) {
            Bill bill = new Bill();
            bill.setType("短信");
            bill.setTotalAmount(orgRecharges.getTotalSmsAmount());
            bill.setExpense(orgRecharges.getTotalSmsExpense());
            bill.setBuget(bill.getTotalAmount() - bill.getExpense());
            Bill bill2 = new Bill();
            bill2.setType("流量");
            bill2.setTotalAmount(orgRecharges.getTotalDataAmount());
            bill2.setExpense(orgRecharges.getTotalDataExpense());
            bill2.setBuget(bill2.getTotalAmount() - bill2.getExpense());
            this.response.bills = new ArrayList<>();
            this.response.bills.add(bill);
            this.response.bills.add(bill2);
        }
        logger.debug("<orgMarginData");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        /**
         * 统计金额
         */
        public StatisticsMoney statisticsMoney;

        public List<Bill> bills;

        public List<Bill> getBills() {
            return bills;
        }

        public StatisticsMoney getStatisticsMoney() {
            return statisticsMoney;
        }

    }
}
