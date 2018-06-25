package com.jbb.mgt.rs.action.batch;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbb.mgt.core.domain.TeleMarketingDetail;
import com.jbb.mgt.core.service.TeleMarketingDetailService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.server.shared.rs.Util;

@Component(TeleFileDownloadAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeleFileDownloadAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(TeleFileDownloadAction.class);
    public static final String ACTION_NAME = "TeleFileDownloadAction";

    @Autowired
    TeleMarketingDetailService detailService;

    public Response.Status teledownload(HttpServletResponse response, Integer batchId) {
        logger.debug(">teledownload(), batchId=" + batchId);
        if (batchId == null) {
            logger.debug("< betchId is empty");
            return Response.Status.BAD_REQUEST;
        }
        List<TeleMarketingDetail> teleList = detailService.selectTeleMarketingDetails(batchId);
        if (teleList.size() == 0) {
            logger.debug("<teledownload() empty content");
            return Response.Status.NO_CONTENT;
        }

        StringBuilder buf = new StringBuilder();
        buf.append("电话号码").append(",").append("姓名").append(",").append("归属地").append(",").append("运营商类型").append(",")
            .append("是否扣费").append(",").append("最近使用时间").append(",").append("状态").append(",").append("更新时间")
            .append("\r\n");
        for (int j = 0; teleList != null && !teleList.isEmpty() && j < teleList.size(); j++) {
            TeleMarketingDetail t = (TeleMarketingDetail)teleList.get(j);
            String status = null;
            switch (t.getStatus()) {
                case 0:
                    status = "空号";
                    break;
                case 1:
                    status = "实号";
                    break;
                case 2:
                    status = "停机";
                    break;
                case 3:
                    status = "库无";
                    break;
                case 4:
                    status = "沉默号";
                    break;
                case 5:
                    status = "导入";
                    break;
                case 6:
                    status = "重复号";
                    break;
                case 7:
                    status = "不符合电话格式";
                    break;
                default:
                    break;
            }
            buf.append(printStr(t.getTelephone())).append(",").append(printStr(t.getUsername())).append(",")
                .append(printStr(t.getArea())).append(",").append(printStr(t.getNumberType())).append(",")
                .append(printStr(t.getChargesStatus())).append(",").append(Util.printDateTime(t.getLastDate()))
                .append(",").append(status).append(",").append(Util.printDateTime(t.getUpdateDate())).append("\r\n");
        }
        try {
            byte[] content = buf.toString().getBytes();
            response.setContentType("text/plain");
            response.setHeader("Content-Length", "" + content.length);
            response.setHeader("Content-Disposition", "attachment; filename=" + batchId + ".csv");
            OutputStream os = response.getOutputStream();
            os.write(content);
            os.flush();
            os.close();
        } catch (IOException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception in downloading file: batchId=" + batchId + " : " + e.toString());
            }
        }
        logger.debug("<teledownload()");
        return null;
    }

    private String printStr(String str) {
        return str == null ? "" : str;
    }

}
