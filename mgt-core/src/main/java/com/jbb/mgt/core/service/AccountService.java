package com.jbb.mgt.core.service;

import java.util.List;

import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.AccountKey;
import com.jbb.mgt.core.domain.LoginLog;
import com.jbb.server.common.exception.WrongUserKeyException;
import org.apache.ibatis.annotations.Param;

public interface AccountService {
    // 创建子账户
    void createAccount(Account account);

    // 通过账号Id获取账号信息
    Account getAccountById(int accountId, Integer orgId, boolean detail);

    //通过jbbUsrId获取account对象
    Account selectAccountByJbbUserId(int jbbUserId);

    // 通过主账号获取子账号信息
    List<Account> getAccount(int orgId, boolean detail);

    // 更新账号信息
    void updateAccount(Account account);

    // 通过账号id 删除账号
    void deleteAccount(int accountId);

    // 通过我账号名获取账号信息
    Account getAccountByUsername(String username);

    // 通过账号密码登陆
    Account loginByPassword(String username, String password, String ipAddress);

    // 账户通过userKey登录
    Account login(String userKey) throws WrongUserKeyException;

    void updateAccountStates(int accountId);

    AccountKey createUserKey(int accountId, long expiry, boolean b);

    Account logoutAccount(String userKey);

    void resetPassword(int accountId, String newPassword);

    boolean checkUsernameExist(String username);
    
    boolean checkJbbIdExist(Integer jbbUserId);

    void insertLoginLog(LoginLog log);

    List<LoginLog> getLoginLogsByOrgId(int orgId);

    List<LoginLog> getLoginLogsByAccountId(int accountId);

    /**
     * 验证accountId是否正确
     * 
     * @param accountId
     * @param orgId
     * @return
     */
    boolean verifyAccountId(Integer accountId, Integer orgId);

    int[] selectdownAccounts(int accountId);

    List<Account> selectAccountByRoleId(int orgId, int roleId);

    List<Account> selectDownAccountByRoleId(int accountId, int roleId);

    void freezeAccount(Integer accountId);

    void thawAccount(Integer accountId);

    Integer getAccountCount(Integer orgId);

}
