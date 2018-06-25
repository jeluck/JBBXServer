package com.jbb.mgt.rs.action.login;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.AccountKey;
import com.jbb.mgt.core.domain.LoginLog;
import com.jbb.mgt.core.domain.Organization;
import com.jbb.mgt.core.service.AliyunService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;

@Service(LoginAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LoginAction extends BasicAction {
    public static final String ACTION_NAME = "LoginAction";

    private static Logger logger = LoggerFactory.getLogger(LoginAction.class);

    private Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    @Autowired
    private AliyunService aliyunService;

    public void loginByPassword(String username, String password, String sessionId, String sig, String token,
        String scene) {
        logger.debug(">loginByPassword() phoneNumber={}", username);
        if (StringUtil.isEmpty(username)) {
            throw new WrongParameterValueException("empty username");
        }
        boolean codeVerifyEnable = PropertyManager.getBooleanProperty("jbb.aliyun.code.enable", false);

        if (codeVerifyEnable) {
            verifyCode(sessionId, sig, token, scene);
            aliyunService.afsCheck(sessionId, sig, token, scene, this.getRemoteAddress());
        }

        Account account = this.coreAccountService.loginByPassword(username, password, this.getRemoteAddress());
        createData(account, Constants.ONE_DAY_MILLIS);

        logger.debug("<loginByPassword");
    }

    private void verifyCode(String sessionId, String sig, String token, String scene) {
        if (StringUtil.isEmpty(sessionId)) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "sessionId");
        }
        if (StringUtil.isEmpty(sig)) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "sig");
        }
        if (StringUtil.isEmpty(token)) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "token");
        }
        if (StringUtil.isEmpty(scene)) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "scene");
        }

    }

    private void createData(Account account, long expiry) {
        int accountId = account.getAccountId();

        AccountKey userKey = account.getKey();
        boolean createNewKey = (userKey == null);

        if (!createNewKey) {
            Timestamp keyExpiry = userKey.getExpiry();
            createNewKey
                = (keyExpiry == null) || (keyExpiry.getTime() < System.currentTimeMillis() + Constants.ONE_DAY_MILLIS);
        }

        if (createNewKey) {
            // create new user key
            userKey = coreAccountService.createUserKey(accountId, expiry, true);
        }

        response.key = userKey.getUserKey();
        Timestamp keyExpiry = userKey.getExpiry();
        response.keyExpire = StringUtil.printDateTime(keyExpiry, DateUtil.getCurrentCalendar());
        response.keyExpireMs = keyExpiry.getTime();
        response.organization = account.getOrganization();
    }

    public void getLoginLogs(int pageNo, int pageSize, Integer orgId) {
        List<LoginLog> logs = null;

        PageHelper.startPage(pageNo, pageSize);

        if (orgId != null && isOrgAdmin()) {
            logs = this.coreAccountService.getLoginLogsByOrgId(this.account.getOrgId());
        } else {
            logs = this.coreAccountService.getLoginLogsByAccountId(this.account.getAccountId());
        }
        if (CommonUtil.isNullOrEmpty(logs)) {
            return;
        }

        PageInfo<LoginLog> pageInfo = new PageInfo<LoginLog>(logs);

        this.response.pageInfo = pageInfo;

        PageHelper.clearPage();
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private String key;
        private String keyExpire;
        private long keyExpireMs;
        private Organization organization;

        public Organization getOrganization() {
            return organization;
        }

        private PageInfo<LoginLog> pageInfo;

        public String getKey() {
            return key;
        }

        public String getKeyExpire() {
            return keyExpire;
        }

        public long getKeyExpireMs() {
            return keyExpireMs;
        }

        public PageInfo<LoginLog> getPageInfo() {
            return pageInfo;
        }

    }

}
