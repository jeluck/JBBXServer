package com.jbb.server.rs.action;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.core.domain.LoanPlatform;
import com.jbb.server.core.service.LoanPlatformService;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.rs.pojo.RsLoanPlatform;

@Service(LoanPlatformAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LoanPlatformAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(LoanPlatformAction.class);
    public static final String ACTION_NAME = "LoanPlatformAction";
    @Autowired
    private LoanPlatformService loanPlatformService;
    private Response response;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void getLoanPlatforms(String loanType, boolean all, boolean simple) {
        logger.debug(">getLoanPlatforms, loanType = " + loanType + ", all = " + all + ", simple = " + simple);
        if (all) {
            // 获取所有的贷款平台信息，包手isDeleted为1的。
            getAllPlatforms(simple);
        } else {
            getPlatfromsByParams(loanType);
        }

        logger.debug("<getLoanPlatforms");
    }

    private void getAllPlatforms(boolean simple) {
        List<LoanPlatform> list = loanPlatformService.getAllLoanPlatforms();
        this.response.loanPlatforms = generateResponse(list, simple);
        this.response.pictureServerUrl = PropertyManager.getProperty("jbb.picture.server.address");
        this.response.path = PropertyManager.getProperty("jbb.picture.server.loanpic.path");
    }

    private void getPlatfromsByParams(String loanType) {
        Integer userId = null;
        if (this.user != null) {
            userId = this.user.getUserId();
            logger.debug(", userId" + userId);
        }
        this.response.loanPlatforms = generateResponse(loanPlatformService.getLoanPlatforms(userId, loanType), false);
        this.response.pictureServerUrl = PropertyManager.getProperty("jbb.picture.server.address");
        this.response.path = PropertyManager.getProperty("jbb.picture.server.loanpic.path");
    }

    private List<RsLoanPlatform> generateResponse(List<LoanPlatform> lps, boolean simple) {     
        List<RsLoanPlatform> rList = new ArrayList<RsLoanPlatform>(lps.size());
        for (LoanPlatform lp : lps) {
            if (simple) {
                rList.add(new RsLoanPlatform(lp.getPlatformId(), lp.getName(), lp.getDescription()));
            } else {
                rList.add(new RsLoanPlatform(lp));
            }
        }
        return rList;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private List<RsLoanPlatform> loanPlatforms;

        private String pictureServerUrl;
        private String path;

        @JsonProperty("loanPlatforms")
        public List<RsLoanPlatform> getLoanPlatforms() {
            return loanPlatforms;
        }

        public String getPictureServerUrl() {
            return pictureServerUrl;
        }

        public void setPictureServerUrl(String pictureServerUrl) {
            this.pictureServerUrl = pictureServerUrl;
        }

        public String getPath() {
            return path;
        }

    }
}
