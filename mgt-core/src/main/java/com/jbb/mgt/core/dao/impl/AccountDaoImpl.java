package com.jbb.mgt.core.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbb.mgt.core.dao.AccountDao;
import com.jbb.mgt.core.dao.mapper.AccountMapper;
import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.AccountKey;
import com.jbb.mgt.core.domain.IPAddressInfo;
import com.jbb.mgt.core.domain.LoginLog;
import com.jbb.mgt.core.domain.Roles;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.DateUtil;

@Repository("AccountDao")
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private AccountMapper mapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertAccount(Account account) {
        mapper.insertAccount(account);
        int accountId = account.getAccountId();
        List<Roles> r = account.getRoles();
        if (r.size() != 0 || r != null) {
            for (int i = 0; i < r.size(); i++) {
                Roles ro = r.get(i);
                int roleId = ro.getRoleId();
                int[] upAccount = ro.getUpAccounts();
                int[] downAccount = ro.getDownAccounts();
                mapper.insertAccountRole(accountId, roleId);
                if (!CommonUtil.isNullOrEmpty(upAccount)) {
                    for (int upAccountsId : upAccount) {
                        mapper.insertupAccounts(accountId, upAccountsId, roleId);
                    }
                }
                if (!CommonUtil.isNullOrEmpty(downAccount)) {
                    for (int downAccountsId : downAccount) {
                        mapper.insertdownAccounts(accountId, downAccountsId, roleId);
                    }
                }
            }
        }
    }

    @Override
    public List<Account> selectAccounts(int orgId) {
        return mapper.selectAccounts(orgId);
    }

    @Override
    public Account selectAccountById(int accountId, Integer orgId) {
        return mapper.selectAccountById(accountId, orgId);
    }

    @Override
    public Account selectAccountByJbbUserId(int jbbUserId) {
        return mapper.selectAccountByJbbUserId(jbbUserId);
    }

    @Override
    public Account selectAccountByUsername(String username) {
        return mapper.selectAccountByUsername(username);
    }

    @Override
    public int[] selectAccountRoles(int accountId) {
        List<Integer> perms = mapper.selectAccountRoles(accountId);

        int[] res = null;
        if (perms != null) {
            res = new int[perms.size()];
            for (int i = 0; i < res.length; i++) {
                res[i] = perms.get(i);
            }
        }

        return res;

    }

    @Override
    public void insertAccountRole(int accountId, int roleId) {
        mapper.insertAccountRole(accountId, roleId);
    }

    @Override
    public AccountKey getAccountKey(int userId) {
        return mapper.selectUserKey(userId);
    }

    /**
     * Insert a new user key record and return true. If the key exists and valid, get it properties and return false. If
     * the key exists, but deleted, update it and return true.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean insertAccountKey(AccountKey userKey) {
        boolean res = false;

        try {
            mapper.insertUserKey(userKey);
            res = true;
        } catch (DuplicateKeyException e) {
            // key exists
            AccountKey key = mapper.selectUserKey(userKey.getAccoountId());

            if (key != null) {
                if (key.isDeleted()) {
                    mapper.updateUserKey(userKey);
                    res = true;
                } else {
                    // existing key found
                    userKey.setExpiry(key.getExpiry());
                    userKey.setUserKey(key.getUserKey());
                }
            } else {
                throw e;
            }
        }

        return res;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAccountKey(AccountKey userKey) {
        mapper.updateUserKey(userKey);
    }

    @Override
    // @CacheEvict(cacheNames=CACHE_USERS_BY_KEY)
    public boolean deleteAccountKey(String userKey) {
        return mapper.deleteUserKey(userKey) > 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAccount(Account account) {
        mapper.updateAccount(account);
        int accountId = account.getAccountId();
        mapper.deleteAccountRoles(accountId);
        mapper.deleteUpAccounts(accountId);
        mapper.deleteDownAccounts(accountId);
        mapper.deleteDUpAccounts(accountId);
        mapper.deleteUDownAccounts(accountId);
        List<Roles> r = account.getRoles();
        for (int i = 0; i < r.size(); i++) {
            Roles ro = r.get(i);
            int roleId = ro.getRoleId();
            int[] upAccount = ro.getUpAccounts();
            int[] downAccount = ro.getDownAccounts();
            mapper.insertAccountRole(accountId, roleId);
            if (!CommonUtil.isNullOrEmpty(upAccount)) {
                for (int upAccountsId : upAccount) {
                    mapper.insertupAccounts(accountId, upAccountsId, roleId);
                }
            }
            if (!CommonUtil.isNullOrEmpty(downAccount)) {
                for (int downAccountsId : downAccount) {
                    mapper.insertdownAccounts(accountId, downAccountsId, roleId);
                }
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deleteAccount(int accountId) {
        mapper.deleteAccountRoles(accountId);
        return mapper.deleteAccount(accountId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean updateAccountRoles(int accountId, int[] roles) {
        mapper.deleteAccountRoles(accountId);
        for (int i = 0; i < roles.length; i++) {
            mapper.insertAccountRole(accountId, roles[i]);
        }
        return true;
    }

    @Override
    public void deleteAccountRoles(int accountId) {
        mapper.deleteAccountRoles(accountId);

    }

    @Override
    public void deleteUserKeyByAccountId(int accountId) {
        mapper.deleteUserKeyByAccountId(accountId);
    }

    @Override
    public Account authenticate(String userKey) {
        return mapper.selectAccountByKey(userKey, DateUtil.getCurrentTimeStamp());
    }

    @Override
    public int[] getAccountPermissions(int accountId) {
        return mapper.selectAccountPermissions(accountId);
    }

    @Override
    public void updatePassword(int accountId, String newPassword) {
        mapper.updatePassword(accountId, newPassword);
    }

    @Override
    public void updateAccountstates(int accountId) {
        mapper.updateAccountstates(accountId);
    }

    @Override
    public boolean checkUsernameExist(String username) {
        return mapper.checkUsernameExist(username) > 0;
    }

    @Override
    public void insertLoginLog(LoginLog loginLog) {
        mapper.insertLoginLog(loginLog);

    }

    @Override
    public List<LoginLog> selectLoginLogs(Integer orgId, Integer accountId) {
        return mapper.selectLoginLogs(orgId, accountId);
    }

    @Override
    public void insertupAccounts(int accountId, int upAccountsId, int roleId) {
        mapper.insertupAccounts(accountId, upAccountsId, roleId);
    }

    @Override
    public void insertdownAccounts(int accountId, int downAccountsId, int roleId) {
        mapper.insertdownAccounts(accountId, downAccountsId, roleId);
    }

    @Override
    public int[] selectupAccounts(int accountId) {
        List<Integer> perms = mapper.selectupAccounts(accountId);

        int[] res = null;
        if (perms != null) {
            res = new int[perms.size()];
            for (int i = 0; i < res.length; i++) {
                res[i] = perms.get(i);
            }
        }

        return res;
    }

    @Override
    public int[] selectdownAccounts(int accountId) {
        List<Integer> perms = mapper.selectdownAccounts(accountId);
        int[] res = null;
        if (perms != null) {
            res = new int[perms.size()];
            for (int i = 0; i < res.length; i++) {
                res[i] = perms.get(i);
            }
        }

        return res;
    }

    @Override
    public void deleteUpAccounts(int accountId) {
        mapper.deleteUpAccounts(accountId);
    }

    @Override
    public void deleteDownAccounts(int accountId) {
        mapper.deleteDownAccounts(accountId);
    }

    @Override
    public void deleteDUpAccounts(int accountId) {
        mapper.deleteDUpAccounts(accountId);
    }

    @Override
    public void deleteUDownAccounts(int accountId) {
        mapper.deleteUDownAccounts(accountId);
    }

    @Override
    public List<Roles> selectAccountRole(int accountId) {
        List<Integer> r = mapper.selectAccountRoles(accountId);
        List<Roles> s = new ArrayList<Roles>(r.size());
        for (int i = 0; i < r.size(); i++) {
            s.add(new Roles());
            Roles roles = s.get(i);
            List<Integer> perms = mapper.select(accountId, r.get(i), 1);
            List<Integer> permss = mapper.select(accountId, r.get(i), 2);
            int[] res = null;
            int[] ress = null;
            if (perms != null) {
                res = new int[perms.size()];
                for (int j = 0; j < res.length; j++) {
                    res[j] = perms.get(j);
                }
                roles.setUpAccounts(res);
            }
            if (permss != null) {
                ress = new int[permss.size()];
                for (int k = 0; k < ress.length; k++) {
                    ress[k] = permss.get(k);
                }
                roles.setDownAccounts(ress);
            }

            roles.setRoleId(r.get(i));
            roles.setDescription(mapper.selectDescprition(r.get(i)));

        }
        return s;
    }

    @Override
    public List<Account> selectAccountByRoleId(int orgId, int roleId) {
        return mapper.selectAccountByRoleId(orgId, roleId);
    }

    @Override
    public List<Account> selectDownAccountByRoleId(int accountId, int roleId) {
        return mapper.selectDownAccountByRoleId(accountId, roleId);
    }

    @Override
    public void freezeAccount(Integer accountId) {
        mapper.freezeAccount(accountId);
    }

    @Override
    public void thawAccount(Integer accountId) {
        mapper.thawAccount(accountId);
    }

    @Override
    public Integer getAccountCount(Integer orgId) {
        return mapper.getAccountCount(orgId);
    }

    @Override
    public boolean checkJbbIdExist(Integer jbbUserId) {
        return mapper.checkJbbIdExist(jbbUserId) > 0;
    }

    @Override
    public Account selectOrgAdminAccount(int orgId) {
         return mapper.selectOrgAdminAccount(orgId);
    }

    
}
