package com.jbb.mgt.core.dao;

import java.util.List;

import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.AccountKey;
import com.jbb.mgt.core.domain.LoginLog;
import com.jbb.mgt.core.domain.Roles;

public interface AccountDao {
    // 插入账户
    void insertAccount(Account account);

    // 获取账户列表
    List<Account> selectAccounts(int orgId);
    
    // 获取组织管理员账户
    Account selectOrgAdminAccount(int orgId);

    // 通过主账户ID，获取各子账户列表
    Account selectAccountById(int accountId, Integer orgId);

    //通过jbbUserId获取account对象
    Account selectAccountByJbbUserId(int jbbUserId);

    // 更新账户信息
    void updateAccount(Account account);

    // 删除账户信息
    boolean deleteAccount(int accountId);

    // 更新账户角色
    boolean updateAccountRoles(int accountId, int[] roles);

    // 获取账户角色列表
    int[] selectAccountRoles(int accountId);

    // 删除账户所有角色
    void deleteAccountRoles(int accountId);

    // 增加账户角色
    void insertAccountRole(int accountId, int roleId);

    // 通过账号获取信息
    Account selectAccountByUsername(String username);

    AccountKey getAccountKey(int userId);

    boolean insertAccountKey(AccountKey userKey);

    void updateAccountKey(AccountKey userKey);

    boolean deleteAccountKey(String userKey);

    void deleteUserKeyByAccountId(int accountId);

    Account authenticate(String userKey);

    void updateAccountstates(int accountId);

    int[] getAccountPermissions(int accountId);

    void updatePassword(int accountId, String newPassword);

    boolean checkUsernameExist(String username);

    // 管理员通过orgId获取登录日志，其他子账户通过accountId获取登录日志。
    List<LoginLog> selectLoginLogs(Integer orgId, Integer accountId);

    // 插入账户登录日志
    void insertLoginLog(LoginLog loginLog);

    // 插入上级
    void insertupAccounts(int accountId, int upAccountsId,int roleId);

    // 插入下级
    void insertdownAccounts(int accountId, int downAccountsId,int roleId);

    // 获取上级
    int[] selectupAccounts(int accountId);

    // 获取下级
    int[] selectdownAccounts(int accountId);

    void deleteUpAccounts(int accountId);

    void deleteDownAccounts(int accountId);

    void deleteDUpAccounts(int accountId);

    void deleteUDownAccounts(int accountId);
    
    List<Roles> selectAccountRole(int accountId);
    
    List<Account> selectAccountByRoleId(int orgId,int roleId);
    
    List<Account> selectDownAccountByRoleId(int accountId,int roleId);

    void freezeAccount(Integer accountId);

    void thawAccount(Integer accountId);
    
    Integer getAccountCount(Integer orgId);

    boolean checkJbbIdExist(Integer nickname);
}
