package com.jbb.mgt.rs.action.account;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.Roles;
import com.jbb.mgt.core.service.JbbService;
import com.jbb.mgt.core.service.OrganizationService;
import com.jbb.mgt.server.core.util.PasswordUtil;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.RsAccount;
import com.jbb.server.common.Constants;
import com.jbb.server.common.exception.AccessDeniedException;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;

@Service(AccountAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountAction extends BasicAction {
    public static final String ACTION_NAME = "AccountAction";
    private static Logger logger = LoggerFactory.getLogger(AccountAction.class);
    private static DefaultTransactionDefinition NEW_TX_DEFINITION
        = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    private Response response;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private JbbService jbbService;
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
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void insertAccount(Request request) {
        logger.debug("> insertAccount(), request = {}", request);
        TransactionStatus txStatus = null;
        Integer count = coreAccountService.getAccountCount(this.account.getOrgId());
        Integer count2 = organizationService.getOrganizationById(this.account.getOrgId()).getCount();
        if (count >= count2) {
            throw new WrongParameterValueException("jbb.mgt.exception.maximumAccounts");
        }
        validateRequest(request, false);
        Integer Code = jbbService.check(request.jbbUserId, request.nickname);
        if (Code == -1||Code==10) {
            throw new WrongParameterValueException("jbb.mgt.exception.system.Maintain");
        } else if (Code != 0) {
            throw new WrongParameterValueException("jbb.mgt.exception.MismatchUsernameAndJbbId");
        }
        Account newAccount = generateAccount(request);
        try {
            txStatus = txManager.getTransaction(NEW_TX_DEFINITION);
            coreAccountService.createAccount(newAccount);
            Integer Code2 = jbbService.checkSend(newAccount.getAccountId(), newAccount.getJbbUserId());
            if (Code2 != 0) {
                throw new WrongParameterValueException("jbb.mgt.exception.system.Maintain");
            }
            txManager.commit(txStatus);
            txStatus = null;
        } finally {
            rollbackTransaction(txStatus);
        }
        this.response.accountId = newAccount.getAccountId();
        logger.debug("< insertAccount()");
    }

    public void getAccountlById(Integer accountId) {
        logger.debug("> getAccountlById(), accountId = " + accountId);
        if (accountId == null) {
            accountId = this.account.getAccountId();
        }
        Account accountR = coreAccountService.getAccountById(accountId, this.account.getOrgId(), true);
        this.response.account = new RsAccount(accountR);
        logger.debug("< getAccountlById()");

    }

    public void getAccounts(Integer roleId, Boolean isGetDownAccount) {
        logger.debug("> getAccounts(), roleId = " + roleId);
        Boolean GetDownAccount = isGetDownAccount == null ? false : isGetDownAccount;
        if (this.isOrgAdmin()) {
            GetDownAccount = false;
        }
        List<Account> account1 = coreAccountService.selectAccountByRoleId(this.account.getOrgId(), 2);
        List<Account> accounts = new ArrayList<>();
        if (roleId == null || roleId == 0) {
            int[] ps = {Constants.MGT_P_ORGADMIN};
            validateUserAccess(ps);
            accounts = coreAccountService.getAccount(this.account.getOrgId(), true);
        } else if (GetDownAccount == false) {
            accounts = coreAccountService.selectAccountByRoleId(this.account.getOrgId(), roleId);
            accounts.removeAll(account1);
            accounts.addAll(account1);

        } else if (GetDownAccount == true) {
            accounts = coreAccountService.selectDownAccountByRoleId(this.account.getAccountId(), roleId);
            accounts.removeAll(account1);
            accounts.addAll(account1);

        }
        if (!CommonUtil.isNullOrEmpty(accounts)) {
            this.response.accounts = new ArrayList<RsAccount>(accounts.size());
            for (Account account : accounts) {
                this.response.accounts.add(new RsAccount(account));
            }
        }

    }

    public void updateAccountlById(Integer accountId, Request request) {
        logger.debug("> updateAccountlById(), accountId ={}, request = {}", accountId, request);

        if (accountId == null) {
            accountId = this.account.getAccountId();
        }

        validateRequest(request, true);

        Account accountEdit = coreAccountService.getAccountById(accountId, this.account.getOrgId(), true);
        if (accountId == this.account.getAccountId() && this.isOrgAdmin()) {
            // 组织管理员不能修改自己的组强管理权限
            request.roles = accountEdit.getRoles();
        }

        // 检查权限
        validateOpRight(accountEdit);

        generateAccountForEdit(accountEdit, request);

        coreAccountService.updateAccount(accountEdit);
        logger.debug("> updateAccountlById()");
    }

    public void deleteAccountById(Integer accountId) {

        if (accountId == null) {
            accountId = this.account.getAccountId();
        }

        if (accountId == this.account.getAccountId() && this.isOrgAdmin()) {
            // 组织管理员不能删除自己
            throw new WrongParameterValueException("jbb.mgt.exception.error.admin");
        }

        logger.debug("> deleteAccountById(), accountId ={}", accountId);
        Account accountDeleted = coreAccountService.getAccountById(accountId, this.account.getOrgId(), false);
        // 检查权限
        validateOpRight(accountDeleted);
        if (CommonUtil.inArray(Constants.MGT_P_ORGADMIN, accountDeleted.getPermissions())) {
            throw new WrongParameterValueException("jbb.mgt.exception.error.admin");
        }

        coreAccountService.updateAccountStates(accountId);
        logger.debug("< deleteAccountById()");
    }

    public void freezeAccountById(Integer accountId) {
        logger.debug("> freezeAccount(), accountId ={}", accountId);

        if (accountId == this.account.getAccountId() && this.isOrgAdmin()) {
            // 组织管理员不能冻结自己
            throw new WrongParameterValueException("jbb.mgt.exception.error.admin");
        }

        Account freezeAccount = coreAccountService.getAccountById(accountId, this.account.getOrgId(), false);
        // 检查权限
        validateOpRight(freezeAccount);
        if (CommonUtil.inArray(Constants.MGT_P_ORGADMIN, freezeAccount.getPermissions())) {
            throw new WrongParameterValueException("jbb.mgt.exception.error.admin");
        }

        coreAccountService.freezeAccount(accountId);
        logger.debug("< freezeAccount()");
    }

    public void thawAccountById(int accountId) {
        logger.debug("> thawAccountById(), accountId ={}", accountId);

        if (accountId == this.account.getAccountId() && this.isOrgAdmin()) {
            // 组织管理员不能解冻自己
            return;
        }

        logger.debug("> thawAccountById(), accountId ={}", accountId);
        Account thawAccount = coreAccountService.getAccountById(accountId, this.account.getOrgId(), false);
        // 检查权限
        validateOpRight(thawAccount);

        coreAccountService.thawAccount(accountId);
        logger.debug("< thawAccountById()");

    }

    private void validateOpRight(Account accountO) {

        if (accountO == null) {
            throw new ObjectNotFoundException("jbb.mgt.exception.accountNotFound");
        }

        if (!(accountO.getCreator() == this.account.getCreator() || this.isOrgAdmin())) {
            throw new AccessDeniedException("jbb.mgt.exception.accountAccessDenied");
        }
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private Integer accountId;
        private RsAccount account;
        private List<RsAccount> accounts;

        public Integer getAccountId() {
            return accountId;
        }

        public RsAccount getAccount() {
            return account;
        }

        public List<RsAccount> getAccounts() {
            return accounts;
        }
    }

    private void validateRequest(Request req, boolean isEdit) {
        if (!isEdit && (StringUtil.isEmpty(req.password) || req.password.length() < 8)) {
            throw new WrongParameterValueException("jbb.mgt.exception.passwordError");
        }

        if (!isEdit && (StringUtil.isEmpty(req.username) || coreAccountService.checkUsernameExist(req.username))) {
            throw new WrongParameterValueException("jbb.mgt.exception.usernameDuplicate");
        }
        if (!isEdit && (req.jbbUserId == null || coreAccountService.checkJbbIdExist(req.jbbUserId))) {
            throw new WrongParameterValueException("jbb.mgt.exception.jbbUserIdDuplicate");
        }
    }

    private Account generateAccount(Request req) {
        Account newAccount = new Account();
        newAccount.setJbbUserId(req.jbbUserId);
        newAccount.setPassword(PasswordUtil.passwordHash(req.password));
        newAccount.setOrgId(account.getOrgId());
        newAccount.setUsername(req.username);
        newAccount.setCreator(account.getAccountId());
        newAccount.setNickname(req.nickname);
        newAccount.setPhoneNumber(req.phoneNumber);
        newAccount.setRoles(req.roles);
        newAccount.setCreationDate(DateUtil.getCurrentTimeStamp());
        return newAccount;
    }

    private void generateAccountForEdit(Account accountEdit, Request req) {
        // 修改时，仅允许修改如下字段
        if (!StringUtil.isEmpty(req.password)) {
            accountEdit.setPassword(PasswordUtil.passwordHash(req.password));
        }
        accountEdit.setNickname(req.nickname);
        accountEdit.setPhoneNumber(req.phoneNumber);
        // 只有管理员可以修改账户上下游信息
        if (this.isOrgAdmin() && !CommonUtil.inArray(Constants.MGT_P_ORGADMIN, accountEdit.getPermissions())) {
            accountEdit.setRoles(req.roles);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        public Integer jbbUserId;
        public String password;
        public String username;
        public String nickname;
        public String phoneNumber;
        public List<Roles> roles;
    }

}
