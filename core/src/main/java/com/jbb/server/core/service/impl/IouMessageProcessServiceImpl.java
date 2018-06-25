package com.jbb.server.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.dao.AccountDao;
import com.jbb.server.core.dao.IousDao;
import com.jbb.server.core.dao.MessageDao;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.domain.Message;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.doman.notification.NotificationRequest;
import com.jbb.server.core.service.IouMessageProcessService;
import com.jbb.server.core.service.LeanCloudService;

@Service("iouMessageProcessService")
public class IouMessageProcessServiceImpl implements IouMessageProcessService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private IousDao iousDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    LeanCloudService leanCloudService;

    @Override
    public void sendIouMessage(int preStatus, Iou iou) {

        int[] borrowerMsgStatuses = PropertyManager.getIntProperties("jbb.iou.status.borrower.msg.statuses", -1);
        int[] lenderMsgStatuses = PropertyManager.getIntProperties("jbb.iou.status.lender.msg.statuses", -1);

        int[] type1 = PropertyManager.getIntProperties("jbb.iou.message.type1", -1);
        int[] type2 = PropertyManager.getIntProperties("jbb.iou.message.type2", -1);
        int[] type3 = PropertyManager.getIntProperties("jbb.iou.message.type3", -1);

        int status = iou.getStatus();

        String subject = PropertyManager.getProperty("jbb.iou.status." + status + ".subject");
        String text = PropertyManager.getProperty("jbb.iou.status." + status + ".text");

        String borrowerText = PropertyManager.getProperty("jbb.iou.status.borrower");
        String lenderText = PropertyManager.getProperty("jbb.iou.status.lender");
        int toUserId = 0;
        if (status == 1) {
            if (preStatus == 20) {
                //借款人补合同，出借人确认， 发给借款人
                //取出借人名字
                User borrower = accountDao.getUser(iou.getLenderId());
                //发给出借人
                toUserId = iou.getBorrowerId();
                text = text.replace("[0]", lenderText);
                text = text.replace("[1]", borrower.getUserName());
            } else {
                //出借人补合同， 借款人同意， 发给出借人
                //取借款人的名字
                User borrower = accountDao.getUser(iou.getBorrowerId());
                //发给出借人
                toUserId = iou.getLenderId();
                text = text.replace("[0]", borrowerText);
                text = text.replace("[1]", borrower.getUserName());
            }

        } else if (CommonUtil.inArray(status, lenderMsgStatuses)) {
            //以下合同状态，将发消息给出借人
            toUserId = iou.getLenderId();
            //取借款人的名字
            User borrower = accountDao.getUser(iou.getBorrowerId());
            text = text.replace("[0]", borrower.getUserName());

        } else if (CommonUtil.inArray(status, borrowerMsgStatuses)) {
            //以下合同状态，将发消息给借款人
            toUserId = iou.getBorrowerId();
            //取出借人的名字
            User lender = accountDao.getUser(iou.getLenderId());
            text = text.replace("[0]", lender.getUserName());

        }

        int messageType = 0;
        if (CommonUtil.inArray(status, type1)) {
            messageType = Message.TYPE_IOU_MESSAGE_NOTIFY;
        } else if (CommonUtil.inArray(status, type2)) {
            messageType = Message.TYPE_IOU_MESSAGE_APPROVE;
        } else if (CommonUtil.inArray(status, type3)) {
            messageType = Message.TYPE_IOU_MESSAGE_REJECT;
        }

        if (messageType == 0) {
            return;
        }

        Message message = new Message(toUserId, subject, messageType, text, false, false, iou.getIouCode(),
            DateUtil.getCurrentTimeStamp());

        // Save message And Send pushNotification
        saveMssageAndPushNotifcation(message);

    }

    @Override
    public void sendIouAlertMessage(Iou iou, boolean isLenderMsg, int days) {
        // 根据iou的时间，给出相应的警告信息
        // 排除空的iou, days超过一天的消息告警
        if (iou == null || iou.getBorrowerId() == 0 || iou.getLenderId() == 0 || days > 1 || days < -1) {
            return;
        }
        int messageType = Message.TYPE_IOU_MESSAGE_ALERT;
        String subject = PropertyManager.getProperty("jbb.iou.status.alert.subject");

        User user = null;
        String text = null;
        if (isLenderMsg) {
            user = accountDao.getUser(iou.getBorrowerId());
            text = PropertyManager.getProperty("jbb.iou.status.alert.lender.text.days." + days);

        } else {
            user = accountDao.getUser(iou.getLenderId());
            text = PropertyManager.getProperty("jbb.iou.status.alert.borrower.text.days." + days);

        }
        if (text == null || user == null) {
            return;
        }
        try {
            String userName = user.getUserName();
            if (userName != null) {
                text = text.replace("[0]", user.getUserName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message message = new Message(user.getUserId(), subject, messageType, text, false, false, iou.getIouCode(),
            DateUtil.getCurrentTimeStamp());

        System.out.println(message);
        saveMssageAndPushNotifcation(message);
        iousDao.insertIouAlertRecord(message.getParameters(), message.getMessageType(), DateUtil.getCurrentTimeStamp());

    }

    private void saveMssageAndPushNotifcation(Message message) {
        messageDao.insertMessage(message);
        // 推送消息 TODO 后续移到消息队列处理
        leanCloudService.pushNotification(new NotificationRequest(message));
    }

}
