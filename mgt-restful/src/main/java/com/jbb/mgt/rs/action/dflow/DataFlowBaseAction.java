package com.jbb.mgt.rs.action.dflow;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.DataFlowBase;
import com.jbb.mgt.core.service.DataFlowBaseService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.RsDataFlowBase;

/**
 * 流量基本信息Action
 * 
 * @author wyq
 * @date 2018/04/28
 */
@Service(DataFlowBaseAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DataFlowBaseAction extends BasicAction {

    public static final String ACTION_NAME = "DataFlowConfigAction";

    private static Logger logger = LoggerFactory.getLogger(DataFlowBaseAction.class);

    private Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    @Autowired
    private DataFlowBaseService dataFlowBaseService;

    public void getDataFlowConfig() {
        logger.debug(">getDataFlowConfig()");
        
        List<DataFlowBase> list = dataFlowBaseService.getDataFlowBases();
        this.response.dataFlowBaseList = new ArrayList<RsDataFlowBase>(list.size());
        for (DataFlowBase base : list) {
            this.response.dataFlowBaseList.add(new RsDataFlowBase(base));
        }
        logger.debug("<getDataFlowConfig()");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private List<RsDataFlowBase> dataFlowBaseList;

        public List<RsDataFlowBase> getDataFlowBaseList() {
            return dataFlowBaseList;
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        public int dataFlowId;
    }
}
