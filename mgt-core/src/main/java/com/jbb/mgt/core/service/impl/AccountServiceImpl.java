package com.jbb.mgt.core.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbb.mgt.core.dao.AccountDao;
import com.jbb.mgt.core.dao.OrganizationDao;
import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.AccountKey;
import com.jbb.mgt.core.domain.IPAddressInfo;
import com.jbb.mgt.core.domain.LoginLog;
import com.jbb.mgt.core.domain.Organization;
import com.jbb.mgt.core.service.AccountService;
import com.jbb.mgt.core.service.AliyunService;
import com.jbb.mgt.server.core.util.PasswordUtil;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ExecutionException;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.exception.WrongUserKeyException;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;

@Service("AccountService")
public class AccountServiceImpl implements AccountService {
    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private static final int ACCOUNT_KEY_SIZE = 64;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AliyunService aliyunService;

    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public Account loginByPassword(String username, String password, String ipAddress) {
        Account account = accountDao.selectAccountByUsername(username);
        if (account == null || account.isDeleted()) {
            throw new ObjectNotFoundException("jbb.mgt.exception.accountNotFound");
        }
        if (!PasswordUtil.verifyPassword(password, account.getPassword())) {
            throw new WrongParameterValueException("jbb.error.exception.passwordError");
        }
        if (account.isFreeze()) {
            throw new WrongParameterValueException("jbb.mgt.exception.userFreeze");
        }
        Organization org = organizationDao.selectOrganizationById(account.getOrgId());
        if (org.isDeleted()) {
            throw new WrongParameterValueException("jbb.mgt.exception.orgFreeze");
        }

        // 插入日志
        IPAddressInfo ipAddressInfo = aliyunService.getIPAddressInfo(ipAddress);
        LoginLog log = new LoginLog(account.getAccountId(), ipAddress);
        if (ipAddressInfo != null) {
            log.setProvince(ipAddressInfo.getProvince());
            log.setCity(ipAddressInfo.getCity());
        }
        accountDao.insertLoginLog(log);
        account.setOrganization(org);
        return account;
    }

    @Override
    public void createAccount(Account account) {
        accountDao.insertAccount(account);
    }

    @Override
    public Account getAccountById(int accountId, Integer orgId, boolean detail) {
        Account account = accountDao.selectAccountById(accountId, orgId);
        if (detail && account != null) {
            account.setRoles(accountDao.selectAccountRole(accountId));
        }
        return account;
    }

    @Override
    public Account selectAccountByJbbUserId(int jbbUserId) {
        return accountDao.selectAccountByJbbUserId(jbbUserId);
    }

    @Override
    public void updateAccount(Account account) {
        accountDao.updateAccount(account);
    }

    @Override
    public List<Account> getAccount(int orgId, boolean detail) {
        List<Account> accounts = accountDao.selectAccounts(orgId);
        if (detail && !CommonUtil.isNullOrEmpty(accounts)) {
            for (Account account : accounts) {
                account.setRoles(accountDao.selectAccountRole(account.getAccountId()));
            }
        }
        return accounts;
    }

    @Override
    public void deleteAccount(int accountId) {
        accountDao.deleteAccount(accountId);
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountDao.selectAccountByUsername(username);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Account login(String userKey) throws WrongUserKeyException {
        if (userKey == null)
            throw new WrongUserKeyException();

        Account account = accountDao.authenticate(userKey);

        if (account == null) {
            throw new WrongUserKeyException();
        }

        loginProcess(account);

        return account;
    }

    private void loginProcess(Account account) {
        // 获取用户权限
        account.setPermissions(accountDao.getAccountPermissions(account.getAccountId()));
    }

    @Override
    public AccountKey createUserKey(int accountId, long expiry, boolean extend) {
        return createUserKey(accountId, expiry, ACCOUNT_KEY_SIZE, extend);
    }

    /**
     * Generate a new user key for authenticated user
     *
     * @param userId user ID
     * @param expiry key expiry period in milliseconds (-1 means it will not be expired)
     * @param keySize new key size in bytes
     * @param extend extend key expiry date, if it is less than requested date
     * @return user key
     */
    private AccountKey createUserKey(int accountId, long expiry, int keySize, boolean extend) {
        if (logger.isDebugEnabled()) {
            logger.debug("|>createUserKey() accountId=" + accountId + ", expiry=" + expiry + ", keySize=" + keySize
                + ", extend=" + extend);
        }

        long maxKeyExpiry = PropertyManager.getIntProperty("jbb.core.userKey.maxExpiry", -1) * Constants.ONE_DAY_MILLIS;
        if ((maxKeyExpiry > 0) && ((expiry <= 0) || (expiry > maxKeyExpiry))) {
            expiry = maxKeyExpiry;
        }

        long currentTime = DateUtil.getCurrentTime();
        long expiryTime = expiry <= 0 ? Constants.LAST_SYSTEM_MILLIS : currentTime + expiry;

        boolean success = false;
        Exception ex = null;
        AccountKey userKey = null;

        for (int i = 0; !success && (i < 10); i++) {
            boolean createNew = true;
            boolean update = false;

            userKey = accountDao.getAccountKey(accountId);

            if (userKey != null) {
                update = true;

                long keyExpiry = userKey.getExpiry().getTime();
                if (!userKey.isDeleted() && (keyExpiry > currentTime)) {
                    if (extend) {
                        createNew = false;
                        extend = (keyExpiry < expiryTime - Constants.ONE_DAY_MILLIS)
                            || (expiryTime < currentTime + Constants.ONE_DAY_MILLIS) && (keyExpiry < expiryTime);
                    } else if (keyExpiry > currentTime + Constants.ONE_DAY_MILLIS) {
                        createNew = false;
                    }
                }
            }

            if (createNew) {
                userKey = generateUserKey(expiryTime, keySize);
                userKey.setAccountId(accountId);

                try {
                    if (update) {
                        accountDao.updateAccountKey(userKey);
                    } else if (!accountDao.insertAccountKey(userKey)) {
                        long keyExpiry = userKey.getExpiry().getTime();
                        if (keyExpiry <= currentTime) {
                            // current key expired
                            continue;
                        }
                    }
                } catch (DeadlockLoserDataAccessException e) {
                    ex = e;
                    if (logger.isDebugEnabled()) {
                        logger.debug("Deadlock in " + (update ? "updating" : "inserting") + " new user key " + userKey
                            + ", extend=" + extend + " : " + e.toString());
                    }
                    continue;
                } catch (Exception e) {
                    ex = e;
                    logger.warn("Exception in " + (update ? "updating" : "inserting") + " new user key " + userKey
                        + ", extend=" + extend, e);
                    continue;
                }
            } else if (extend) {
                // extend expiry time
                userKey.setExpiry(new Timestamp(expiryTime));
                try {
                    accountDao.updateAccountKey(userKey);
                } catch (DeadlockLoserDataAccessException e) {
                    ex = e;
                    if (logger.isDebugEnabled()) {
                        logger.debug("Deadlock in extending new user key " + userKey + " : " + e.toString());
                    }
                    continue;
                } catch (Exception e) {
                    ex = e;
                    logger.warn("Exception in extending new user key " + userKey, e);
                    continue;
                }
            }

            success = true;
        }

        if (!success) {
            throw new ExecutionException("Cannot generate user key for accountId=" + accountId + ", keySize=" + keySize,
                ex);
        }

        logger.debug("|<createUserKey()");
        return userKey;
    }

    private AccountKey generateUserKey(long expiryTime, int keySize) {
        AccountKey userKey = new AccountKey();
        userKey.setUserKey(StringUtil.secureRandom(keySize).substring(0, keySize));
        userKey.setExpiry(new Timestamp(expiryTime));

        return userKey;
    }

    @Override
    public Account logoutAccount(String userKey) {
        logger.debug(">logoutUser()");
        Account account = accountDao.authenticate(userKey);
        if (account == null) {
            logger.debug("<logoutUser() not authenticated");
            return null;
        }
        accountDao.deleteAccountKey(userKey);
        logger.debug("<logoutUser()");
        return account;
    }

    @Override
    public void resetPassword(int accountId, String newPassword) {
        accountDao.updatePassword(accountId, PasswordUtil.passwordHash(newPassword));

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAccountStates(int accountId) {
        accountDao.deleteAccountRoles(accountId);
        accountDao.updateAccountstates(accountId);
        accountDao.deleteUpAccounts(accountId);
        accountDao.deleteDownAccounts(accountId);
        accountDao.deleteDUpAccounts(accountId);
        accountDao.deleteUDownAccounts(accountId);
    }

    @Override
    public boolean checkUsernameExist(String username) {
        return accountDao.checkUsernameExist(username);
    }

    @Override
    public void insertLoginLog(LoginLog log) {
        accountDao.insertLoginLog(log);
    }

    @Override
    public List<LoginLog> getLoginLogsByOrgId(int orgId) {
        return accountDao.selectLoginLogs(orgId, null);
    }

    @Override
    public List<LoginLog> getLoginLogsByAccountId(int accountId) {
        return accountDao.selectLoginLogs(null, accountId);
    }

    @Override
    public boolean verifyAccountId(Integer accountId, Integer orgId) {
        if (accountId == null) {
            return false;
        }
        Account account = accountDao.selectAccountById(accountId, orgId);
        return account != null;
    }

    @Override
    public int[] selectdownAccounts(int accountId) {
        return accountDao.selectdownAccounts(accountId);
    }

    @Override
    public List<Account> selectAccountByRoleId(int orgId, int roleId) {
        return accountDao.selectAccountByRoleId(orgId, roleId);
    }

    @Override
    public List<Account> selectDownAccountByRoleId(int accountId, int roleId) {
        return accountDao.selectDownAccountByRoleId(accountId, roleId);
    }

    @Override
    public void freezeAccount(Integer accountId) {
        accountDao.freezeAccount(accountId);
    }

    @Override
    public void thawAccount(Integer accountId) {
        accountDao.thawAccount(accountId);
    }

    @Override
    public Integer getAccountCount(Integer orgId) {
        return accountDao.getAccountCount(orgId);
    }

    @Override
    public boolean checkJbbIdExist(Integer jbbUserId) {
         return accountDao.checkJbbIdExist(jbbUserId);
    }
}
