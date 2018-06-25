package com.jbb.server.rs.action;

import java.sql.Timestamp;
import java.util.List;
import java.util.TimeZone;

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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.AccessDeniedException;
import com.jbb.server.common.exception.ApiCallLimitException;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.domain.IntendRecord;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserBasic;
import com.jbb.server.core.service.IouStatusService;
import com.jbb.server.core.service.IousService;
import com.jbb.server.core.service.ProductService;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.rs.pojo.RsIou;
import com.jbb.server.rs.pojo.request.ReIou;
import com.jbb.server.rs.pojo.request.ReIouFill;
import com.jbb.server.shared.rs.Util;

@Service(IouAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IouAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(IouAction.class);

    private static DefaultTransactionDefinition NEW_TX_DEFINITION =
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    @Autowired
    private PlatformTransactionManager txManager;

    public static final String ACTION_NAME = "IouAction";

    private Response response;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    @Autowired
    private IousService iousService;

    @Autowired
    private IouStatusService iouStatusService;

    @Autowired
    private ProductService productService;

    /**
     * 发布借条
     * 
     * @param iou
     */
    public void insertIou(ReIou reIou) {
        if (logger.isDebugEnabled()) {
            logger.debug(">insertIou()");
        }

        if (reIou.getRepaymentDateTs() == null && reIou.getDays() == null) {
            throw new MissingParameterException("jbb.error.exception.missing.param", "days");
        }

        // 查找非生效的借条数，进行发布流量控制
        int[] statuses = Constants.IOU_STATUS_INEFFECT_EXCLUDE_FILL; // 非生效的借条数
        int cnt = iousService.countIousByStatuses(this.user.getUserId(), statuses);
        int limitCnt = PropertyManager.getIntProperty("jbb.iou.publish.limit", 1);
        if (cnt >= limitCnt) {
            throw new ApiCallLimitException("jbb.error.exception.api.limitPublishIou", String.valueOf(cnt));
        }

        Iou iou = covert2IouForNew(this.user.getUserId(), reIou);
        iousService.insertIou(iou);
        this.response.iouCode = iou.getIouCode();
        logger.debug("<insertIou()");
    }

    /**
     * 补借条
     * 
     * @param iou
     */
    public void insertIou(String msgCode, boolean isLender, ReIouFill reIou) {
        if (logger.isDebugEnabled()) {
            logger.debug(">insertIou()");
        }

        if (!isLender && !this.coreAccountService.checkMsgCode(this.user.getPhoneNumber(), msgCode)) {
            throw new WrongParameterValueException("jbb.error.exception.ioufill.msgCodeError", "zh");
        }

        int userId = reIou.getUserId();

        // 检查用户是否存在
        boolean isUserExist = this.coreAccountService.checkUserExist(userId);
        if (!isUserExist) {
            throw new WrongParameterValueException("jbb.error.exception.ioufill.userNotExist", "zh",
                String.valueOf(userId));
        }

        Iou iou = covert2IouForNew(isLender, reIou);

        // 检查借款人和出借人 是否实名认证
        User borrower = this.coreAccountService.getUser(iou.getBorrowerId());
        if (borrower == null || !borrower.isVerified()) {
            throw new ApiCallLimitException("jbb.error.exception.ioufill.borrowerNotVerified");
        }
        User lender = this.coreAccountService.getUser(iou.getLenderId());
        if (lender == null || !lender.isVerified()) {
            throw new ApiCallLimitException("jbb.error.exception.ioufill.lenderNotVerified");
        }

        // 如果是借款人，检查是否有打借条的权益
        int fee = PropertyManager.getIntProperty("jbb.wx.pay.iou.fee", 0);
        if (fee != 0 && this.user.getUserId() == iou.getBorrowerId()
            && productService.getProductCount(this.user.getUserId(), Constants.PRODUCT_IOU) == 0) {
            throw new ApiCallLimitException("jbb.error.exception.ioufill.notPay");
        }

        if (iou.getBorrowerId() == iou.getLenderId()) {
            throw new WrongParameterValueException("jbb.error.exception.ioufill.userSame", "zh");
        }

        TransactionStatus txStatus = null;
        try {
            txStatus = txManager.getTransaction(NEW_TX_DEFINITION);

            iousService.insertIou(iou);
            this.response.iouCode = iou.getIouCode();
            // lock, get for update
            productService.getProductCountForUpdate(userId, Constants.PRODUCT_IOU);
            // reduce count
            productService.reduceProductCount(userId, Constants.PRODUCT_IOU);
            txManager.commit(txStatus);
            txStatus=null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("insertIou error , error = " + e.getMessage());
        } finally {
            // roll back not committed transaction
            rollbackTransaction(txStatus);
        }

        logger.debug("<insertIou()");
    }

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

    public void exceedLimitOfPublishIou() {
        if (logger.isDebugEnabled()) {
            logger.debug(">exceedLimitOfPublishIou()");
        }
        // 查找非生效的借条数，进行发布流量控制
        int[] statuses = Constants.IOU_STATUS_INEFFECT_EXCLUDE_FILL; // 非生效的借条数
        int cnt = iousService.countIousByStatuses(this.user.getUserId(), statuses);
        int limitCnt = PropertyManager.getIntProperty("jbb.iou.publish.limit", 1);
        this.response.num = cnt;
        this.response.limit = limitCnt;
        if (cnt >= limitCnt) {
            this.response.exceedLimitOfPublish = true;
        } else {
            this.response.exceedLimitOfPublish = false;
        }
        logger.debug("<exceedLimitOfPublishIou()");
    }

    /**
     * 修改借条
     * 
     * @param iou
     */
    public void updateIou(String iouCode, ReIou reIou, Integer intentionUserId) {
        if (logger.isDebugEnabled()) {
            logger.debug(">updateIou(), iouCode = " + iouCode + ", reIou=" + reIou);
        }
        Iou iou = iousService.getIouByIouCode(iouCode);

        if (iou == null) {
            throw new ObjectNotFoundException("jbb.error.exception.iouNotFournd", "zh", iouCode);
        }

        if (iou.getBorrowerId() != this.user.getUserId()) {
            throw new AccessDeniedException("jbb.error.exception.iouAccessDenied");
        }

        // 判断借条是否处于可修改的状态
        int status = iou.getStatus();
        if (!(status == 0 || status == 2)) {
            // 只有发布和修改申请状态下的借条可修改, 如果不是，则抛出异常
            throw new WrongParameterValueException("jbb.error.exception.canotmodify", "zh");
        }

        if (intentionUserId != null) {
            // 检查意向人是否存在
            User intentionUser = this.coreAccountService.getUser(intentionUserId);
            if (intentionUser == null) {
                throw new ObjectNotFoundException("jbb.error.exception.notExistIntentUser", "zh",
                    String.valueOf(intentionUserId));
            }
        }

        covert2IouForUpdate(iou, reIou, intentionUserId);
        iousService.updateIou(iou, intentionUserId);

        // 把借款人推荐给出借人， 发送消息至消息队列
        // TODO
        // MqClient.send(Queues.USER_REGISTER_QUEUE_ADDR, String.valueOf(this.user.getUserId()).getBytes(), 0);
        // 发送消息结束

        logger.debug("<updateIou()");
    }

    public void deleteIou(String iouCode) {
        if (logger.isDebugEnabled()) {
            logger.debug(">deleteIou(), iouCode = " + iouCode);
        }
        Iou iou = iousService.getIouByIouCode(iouCode);
        if (iou == null) {
            throw new ObjectNotFoundException("jbb.error.exception.iouNotFournd", "zh", iouCode);
        }

        // 判断借条是否处于可修改的状态
        int status = iou.getStatus();
        if (!(status == 0 || status == 2 || status == 20 || status == 30 || status == 21 || status == 31)) {
            // 只有发布和修改申请状态下的借条可修改, 如果不是，则抛出异常
            throw new WrongParameterValueException("jbb.error.exception.canotmodify", "zh");
        }

        boolean isBorrower = (iou.getBorrowerId() == this.user.getUserId());
        boolean isLender = (iou.getLenderId() != null && (int)iou.getLenderId() == this.user.getUserId());
        if (isBorrower) {
            iousService.deleteIouForBorrower(iouCode);
        } else if (isLender) {
            iousService.deleteIouForLender(iouCode);
        } else {
            throw new AccessDeniedException("jbb.error.exception.iouAccessDenied", "zh");
        }
        logger.debug("<deleteIou()");
    }

    public Iou covert2IouForNew(int borrowerId, ReIou reIou) {
        // TODO check fields
        Iou iou = new Iou();
        Timestamp now = DateUtil.getCurrentTimeStamp();
        iou.setAnnualRate(reIou.getAnnualRate());
        iou.setBorrowingAmount(reIou.getBorrowingAmount());
        iou.setPurpose(reIou.getPurpose());
        if (reIou.getRepaymentDateTs() != null) {
            iou.setRepaymentDate(new Timestamp(reIou.getRepaymentDateTs()));
        }
        if (reIou.getDays() != null) {
            iou.setRepaymentDate(DateUtil.calTimestamp(now.getTime(), reIou.getDays() * DateUtil.DAY_MILLSECONDES));
        }
        iou.setBorrowerId(borrowerId);
        iou.setBorrowingDate(new Timestamp(DateUtil.getTodayStartCurrentTime()));
        iou.setLastUpdateStatusDate(now);
        iou.setCreationDate(now);
        iou.setStatus(0);
        iou.setDevice(reIou.getDevice());
        return iou;
    }

    public Iou covert2IouForNew(boolean isLender, ReIouFill reIou) {
        // TODO check fields
        Iou iou = new Iou();
        TimeZone tz = getTimezone();
        Timestamp borrowingDate = Util.parseTimestamp(reIou.getBorrowingDate(), tz);
        Timestamp repaymentDate = Util.parseTimestamp(reIou.getRepaymentDate(), tz);
        iou.setRepaymentDate(repaymentDate);
        iou.setBorrowingDate(borrowingDate);
        iou.setAnnualRate(reIou.getAnnualRate());
        iou.setBorrowingAmount(reIou.getBorrowingAmount());
        iou.setPurpose(reIou.getPurpose());

        if (isLender) {
            iou.setLenderId(this.user.getUserId());
            iou.setBorrowerId(reIou.getUserId());
            iou.setStatus(30);
        } else {
            iou.setLenderId(reIou.getUserId());
            iou.setBorrowerId(this.user.getUserId());
            iou.setStatus(20);
        }
        Timestamp now = DateUtil.getCurrentTimeStamp();
        iou.setLastUpdateStatusDate(now);
        iou.setCreationDate(now);

        iou.setDevice(reIou.getDevice());
        return iou;
    }

    public Iou covert2IouForUpdate(Iou iou, ReIou reIou, Integer intentionUserId) {
        // TODO check fields
        Timestamp now = DateUtil.getCurrentTimeStamp();
        iou.setAnnualRate(reIou.getAnnualRate());
        iou.setBorrowingAmount(reIou.getBorrowingAmount());
        iou.setPurpose(reIou.getPurpose());

        iou.setBorrowingDate(new Timestamp(DateUtil.getTodayStartCurrentTime()));
        if (reIou.getRepaymentDateTs() != null) {
            iou.setRepaymentDate(new Timestamp(reIou.getRepaymentDateTs()));
        }
        if (reIou.getDays() != null) {
            iou.setRepaymentDate(DateUtil.calTimestamp(now.getTime(), reIou.getDays() * DateUtil.DAY_MILLSECONDES));
        }
        iou.setLastUpdateStatusDate(now);
        iou.setStatus(2);
        iou.setDevice(reIou.getDevice());

        return iou;
    }

    public void getIouByIouCode(String iouCode) {
        if (logger.isDebugEnabled()) {
            logger.debug(">searchIouByIouCode(), iouCode = ", iouCode);
        }

        if (StringUtil.isEmpty(iouCode)) {
            throw new MissingParameterException("jbb.error.exception.missing.param", "zh", "iouCode");
        }

        Iou iou = iousService.getIouByIouCode(iouCode);

        if (iou == null) {
            return;
        }

        // IouStatusEnum iouStatusE = IouStatusEnum.getIouStatus(iou.getStatus());
        // 其他人查看借条详细信息
        RsIou rsIou = null;
        int borrowerId = iou.getBorrowerId();
        Integer lenderId = iou.getLenderId();
        if (this.user != null && this.user.getUserId() == borrowerId) {
            rsIou = prepareIouInfoForBorrower(iou);
            // 准备返回给借款人的信息
        } else if (this.user != null && lenderId != null && this.user.getUserId() == lenderId) {
            // 准备返回给出借人的信息
            rsIou = prepareIouInfoForLender(iou);
        } else {
            // 准备返回给非借款人和出借人的信息
            rsIou = prepareIouInfoForOther(iou);
        }
        if (this.user != null) {
            int userId = this.user.getUserId();
            rsIou.setIntentionInfo(iousService.getIntentionByUserId(iouCode, userId));
            rsIou.setIntention(iousService.checkExistUserIntention(iouCode, userId));
            rsIou.setFollowed(iousService.checkExistUserFollowed(iouCode, userId));
        }

        this.response.rsIou = rsIou;
        logger.debug("<searchIouByIouCode()");
    }

    // 返回借条，意向出借人的统计（仅在发布状态统计意向出借人）
    private RsIou prepareIouInfoForOther(Iou iou) {
        int cnt = 0;
        Integer lenderId = iou.getLenderId();
        // 还没有出借成功，统计意向数据
        if (lenderId == null) {
            cnt = iousService.countIntentionalUsers(iou.getIouCode());
        }
        User borrower = this.coreAccountService.getUser(iou.getBorrowerId());
        RsIou rsIou = new RsIou(iou, false);
        rsIou.setIntentionalUserCnt(cnt);
        rsIou.setBorrower(new UserBasic(borrower, false));
        return rsIou;
    }

    private RsIou prepareIouInfoForBorrower(Iou iou) {
        RsIou rsIou = new RsIou(iou);
        User borrower = this.coreAccountService.getUser(iou.getBorrowerId());
        rsIou.setBorrower(new UserBasic(borrower, true));
        Integer lenderId = iou.getLenderId();
        if (lenderId == null) {
            // 还没有出借成功，返回意向列表
            List<IntendRecord> users = iousService.getIntentionUsers(iou.getIouCode());
            rsIou.setIntentionalUsers(users);
            rsIou.setIntentionalUserCnt(users.size());
        } else {
            // 出借成功，返回出借人信息
            User lender = this.coreAccountService.getUser(iou.getLenderId());
            rsIou.setLender(new UserBasic(lender, true));
        }
        return rsIou;

    }

    private RsIou prepareIouInfoForLender(Iou iou) {
        RsIou rsIou = new RsIou(iou);
        User borrower = this.coreAccountService.getUser(iou.getBorrowerId());
        rsIou.setBorrower(new UserBasic(borrower, true));
        User lender = this.coreAccountService.getUser(iou.getLenderId());
        rsIou.setLender(new UserBasic(lender, true));
        return rsIou;
    }

    /**
     * 修改状态
     * 
     * @param userId
     * @param iouCode
     * @param status
     */
    public void changeStatus(String iouCode, int status, String msgCode, String extentionDateTs, Integer lenderId) {
        int userId = this.user.getUserId();

        Iou iou = iousService.getIouByIouCode(iouCode);
        if (iou == null) {
            throw new ObjectNotFoundException("jbb.error.exception.iouNotFournd", "zh", iouCode);
        }

        if (iou.isDeleted() || iou.isLenderDeleted()) {
            throw new ObjectNotFoundException("jbb.error.exception.iouDeleted", "zh", iouCode);
        }

        if (iou.getStatus() != 20 && status == 1
            && !this.coreAccountService.checkMsgCode(this.user.getPhoneNumber(), msgCode)) {
            throw new AccessDeniedException("jbb.error.exception.accessDenied.msgCodeExpire");
        }

        if (lenderId == null) {
            lenderId = iou.getLenderId();
        }

        // 如果借款人，将借条状态变为1生效，检查借款人是否付费
        int fee = PropertyManager.getIntProperty("jbb.wx.pay.auth.fee", 0);
        if (fee != 0 && status == 1 && this.user.getUserId() == iou.getBorrowerId()
            && productService.getProductCount(this.user.getUserId(), Constants.PRODUCT_IOU) == 0) {
            throw new ApiCallLimitException("jbb.error.exception.ioufill.notPay");
        }

        TransactionStatus txStatus = null;
        try {
            txStatus = txManager.getTransaction(NEW_TX_DEFINITION);

            iouStatusService.updateStatus(iou, status, userId, extentionDateTs, lenderId);

            // 如果借款人，使出借人的借条生效，需要扣费
            if (status == 1 && this.user.getUserId() == iou.getBorrowerId()) {
                // get count for update
                productService.getProductCountForUpdate(userId, Constants.PRODUCT_IOU);
                //reduce count
                productService.reduceProductCount(userId, Constants.PRODUCT_IOU);
            }
            txManager.commit(txStatus);
            txStatus = null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("changeStatus error , error = " + e.getMessage());
        } finally {
            // roll back not committed transaction
            rollbackTransaction(txStatus);
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private Boolean exceedLimitOfPublish;
        private Integer num;
        private Integer limit;

        private String iouCode;

        // 借条信息
        private RsIou rsIou;

        @JsonProperty("iouInfo")
        public RsIou getRsIou() {
            return rsIou;
        }

        public String getIouCode() {
            return iouCode;
        }

        public Boolean getExceedLimitOfPublish() {
            return exceedLimitOfPublish;
        }

        public Integer getNum() {
            return num;
        }

        public Integer getLimit() {
            return limit;
        }

    }
}
