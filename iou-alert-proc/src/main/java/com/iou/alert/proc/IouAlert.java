package com.iou.alert.proc;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.server.common.Constants;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.service.IouMessageProcessService;
import com.jbb.server.core.service.IousService;
import com.jbb.server.core.util.SpringUtil;

public class IouAlert {

    public static void main(String[] args) {

        IousService iouService = SpringUtil.getBean("IousService", IousService.class);

        IouMessageProcessService iouMessageProcessService =
            SpringUtil.getBean("iouMessageProcessService", IouMessageProcessService.class);

        System.out.println(iouService);

        // 获取数据并进行“借条提醒消息处理”
        int[] statuses = Constants.IOU_STATUS_EFFECT_WITHOUT_COMPLETED;
        int pageSize = 999999;

        // 需要 查找， 昨天、今天、明天 还款日期到期、生效且未完成的借条。
        // 每次查出N条(N暂取100)， 每执行一条，

        // 还款提醒，向出借人***的借款还有1天到期 4 定时任务 到期时间且未还 1
        // 还款提醒，向出借人***的借款今天到期 4 定时任务 到期时间且未还
        // 还款提醒，借款人***的借款今天到期 4 定时任务 到期时间且未还
        // 逾期提醒，向出借人***的借款已逾期 逾期后第一天推一次即可 4 定时任务 到期时间且未还

        // 此处分组的逻辑为 将符合发送告警消息类型的借条，用消息类型分别查出。例如：查询还有一天的到期的借条列表，过滤掉已经发送的部分，即为获得的结果集

        long todayTs = DateUtil.getTodayStartTs();
        long yesterdayBegainTs = todayTs - 1 * DateUtil.DAY_MILLSECONDES;
        long tomorrowBegainTs = todayTs + 1 * DateUtil.DAY_MILLSECONDES;
        long tomorrowNextBegainTs = todayTs + 2 * DateUtil.DAY_MILLSECONDES;

        Timestamp yesterdayBegain = new Timestamp(yesterdayBegainTs);
        Timestamp tomorrowBegain = new Timestamp(tomorrowBegainTs);
        Timestamp tomorrowNextBegain = new Timestamp(tomorrowNextBegainTs);
        Timestamp today = new Timestamp(todayTs);

        Timestamp sendDate = DateUtil.getCurrentTimeStamp();
        int messageType = 4;
        List<Iou> iouList1 =
            iouService.selectIousForMessage(statuses, pageSize, messageType, sendDate, yesterdayBegain, today);

        if (iouList1 != null && iouList1.size() != 0) {
            for (Iou iou : iouList1) {
                iouMessageProcessService.sendIouAlertMessage(iou, false, -1);
            }
        }

        List<Iou> iouList2 = iouService.selectIousForMessage(statuses, pageSize, 4, sendDate, today, tomorrowBegain);
        if (iouList2 != null && iouList2.size() != 0) {
            for (Iou iou : iouList2) {
                iouMessageProcessService.sendIouAlertMessage(iou, false, 0);
                iouMessageProcessService.sendIouAlertMessage(iou, true, 0);
            }
        }

        List<Iou> iouList3 =
            iouService.selectIousForMessage(statuses, pageSize, 4, sendDate, yesterdayBegain, tomorrowNextBegain);
        if (iouList3 != null && iouList3.size() != 0) {
            for (Iou iou : iouList3) {
                iouMessageProcessService.sendIouAlertMessage(iou, true, 1);
            }
        }

    }

}
