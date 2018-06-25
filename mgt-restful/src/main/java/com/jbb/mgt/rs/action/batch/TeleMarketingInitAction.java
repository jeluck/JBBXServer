package com.jbb.mgt.rs.action.batch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.TeleMarketingDetail;
import com.jbb.mgt.core.domain.TeleMarketingInit;
import com.jbb.mgt.core.service.TeleMarketingService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.InitOp;
import com.jbb.mgt.server.rs.pojo.RsTeleMarketingDetail;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 批次initAction类
 *
 * @author wyq
 * @date 2018/5/18 18:01
 */
@Service(TeleMarketingInitAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeleMarketingInitAction extends BasicAction {
    public static final String ACTION_NAME = "TeleMarketingInitAction";

    private static Logger logger = LoggerFactory.getLogger(TeleMarketingInitAction.class);

    private TeleMarketingInitAction.Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new TeleMarketingInitAction.Response();
    }

    @Autowired
    private TeleMarketingService teleService;

    public void selectTeleInitsByAccountId() {
        logger.debug(">selectTeleInitsByAccountId");
        List<TeleMarketingInit> list = teleService.selectMobilesByAccountId(this.account.getAccountId());
        this.response.mobiles = new ArrayList<InitOp>(list.size());
        for (TeleMarketingInit teleMarketingInit : list) {
            InitOp initOp = new InitOp(teleMarketingInit);
            this.response.mobiles.add(initOp);
        }
        logger.debug("<selectTeleInitsByAccountId");
    }

    public void updateTeleMarketingInitByIdAndOpCommet(Request req) {
        logger.debug(">updateTeleMarketingInitByIdAndOpCommet,id=" + req.id);
        TeleMarketingInit init = teleService.selectTeleMarketingInitById(req.id);
        if (null == req.id || null == init) {
            throw new WrongParameterValueException("jbb.mgt.error.exception.wrong.paramvalue", "zh", "id");
        }
        if (StringUtil.isEmpty(req.opCommet)) {
            throw new WrongParameterValueException("jbb.mgt.error.exception.wrong.paramvalue", "zh", "opCommet");
        }
        teleService.updateTeleMarketingInitByIdAndOpCommet(req.id, req.opCommet);
        logger.debug("<updateTeleMarketingInitByIdAndOpCommet");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private List<InitOp> mobiles;

        public List<InitOp> getMobiles() {
            return mobiles;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        public Integer id;
        public String opCommet;
    }
}
