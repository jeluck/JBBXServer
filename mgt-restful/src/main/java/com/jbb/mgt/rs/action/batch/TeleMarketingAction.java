package com.jbb.mgt.rs.action.batch;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.TeleMarketing;
import com.jbb.mgt.core.domain.TeleMarketingDetail;
import com.jbb.mgt.core.domain.TeleMarketingInit;
import com.jbb.mgt.core.service.TeleMarketingDetailService;
import com.jbb.mgt.core.service.TeleMarketingService;
import com.jbb.mgt.rs.action.channel.ChannelAction;
import com.jbb.mgt.rs.action.channel.ChannelAction.Response;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.RsTeleMarketing;
import com.jbb.server.common.exception.WrongParameterValueException;

/**
 * 
 * 批次查询Action
 * 
 * @author wyq
 * @date 2018/04/29
 */
@Service(TeleMarketingAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeleMarketingAction extends BasicAction {

    public static final String ACTION_NAME = "TeleMarketingAction";

    private static Logger logger = LoggerFactory.getLogger(TeleMarketingAction.class);

    @Autowired
    private TeleMarketingService marketingService;

    @Autowired
    private TeleMarketingDetailService service;

    private Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void insertTeleInits(Request req) {
        logger.debug(">insertTeleInits");
        Integer batchId = req.betchId;
        Integer cnt = req.cnt;
        List<Integer> initAccounts = req.initAccounts;
        if (batchId == null || batchId <= 0) {
            throw new WrongParameterValueException("jbb.error.exception.wrong.paramvalue", "zh", "batchId");
        }
        if (cnt == null || cnt == 0 || cnt < -1) {
            throw new WrongParameterValueException("jbb.error.exception.wrong.paramvalue", "zh", "cnt");
        }
        if (initAccounts == null || initAccounts.size() == 0) {
            throw new WrongParameterValueException("jbb.error.exception.wrong.paramvalue", "zh", "initAccounts");
        }
        int accountId = this.account.getAccountId();
        List<TeleMarketingDetail> listDetails = service.selectTeleMarketingDetailsNotInit(batchId, true);
        if (listDetails.size() == 0 || null == listDetails) {
            throw new WrongParameterValueException("jbb.mgt.exception.telemarketingDetail.empty");
        }
        int size = listDetails.size();
        int total;
        if (cnt != -1) {
            total = cnt / initAccounts.size();
            if(cnt % initAccounts.size() != 0){
                total += 1;
            }
        } else {
            total = size / initAccounts.size();
            if(size % initAccounts.size() != 0){
                total += 1;
            }
        }
        if (cnt < size && cnt != -1) {
            size = cnt;
        }
        List<TeleMarketingInit> list = new ArrayList<TeleMarketingInit>(size);
        for (int i = 0; i < initAccounts.size(); i++) {
            for (int j = 0; j < total && size > (i * total + j); j++) {
                TeleMarketingInit teleMarketingInit = new TeleMarketingInit();
                teleMarketingInit.setId(listDetails.get(i * total + j).getId());
                teleMarketingInit.setAssignAccountId(accountId);
                teleMarketingInit.setInitAccountId(initAccounts.get(i));
                if (listDetails.get(i * total + j).getStatus() == 1){
                    list.add(teleMarketingInit);
                }
            }
        }
        if (list.size() > 0) {
            marketingService.insertTeleInits(list);
            List<TeleMarketingDetail> list2 = service.selectTeleMarketingDetailsNotInit(batchId, true);
            if (null == list2 || list2.size() == 0){
                TeleMarketing teleMarketing = marketingService.selectTeleMarkByBatchId(batchId, false);
                teleMarketing.setStatus(4);
                marketingService.updateTeleMarketing(teleMarketing);
            }
        }
        logger.debug("<insertTeleInits");
    }

    public void getTeleMarketings() {
        logger.debug(">getTeleMarketings");
        int accountId = this.account.getAccountId();
        List<TeleMarketing> list = marketingService.selectTeleMarketings(accountId);
        if (null == list || list.size() == 0) {
            this.response.batchs = null;
        }else{
            this.response.batchs = new ArrayList<RsTeleMarketing>(list.size());
            for (TeleMarketing teleMarketing : list) {
                RsTeleMarketing rsTeleMarketing = new RsTeleMarketing(teleMarketing);
                this.response.batchs.add(rsTeleMarketing);
            }
        }
        logger.debug("<getTeleMarketings");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private List<RsTeleMarketing> batchs;

        public List<RsTeleMarketing> getBatchs() {
            return batchs;
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        public Integer betchId;
        public Integer cnt;
        public List<Integer> initAccounts;
    }
}
