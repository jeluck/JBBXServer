
package com.jbb.mgt.rs.action.batch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.OrgRecharges;
import com.jbb.mgt.core.domain.TeleMarketing;
import com.jbb.mgt.core.domain.TeleMarketingDetail;
import com.jbb.mgt.core.service.OrgRechargesService;
import com.jbb.mgt.core.service.TeleMarketingDetailService;
import com.jbb.mgt.core.service.TeleMarketingService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.common.util.UTF8Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service(TeleFileUploadAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeleFileUploadAction extends BasicAction {

    public static final String ACTION_NAME = "TeleFileUploadAction";

    private static final int MAX_CONTENT_LENGTH_M = 2;
    private static final int MAX_CONTENT_LENGTH = MAX_CONTENT_LENGTH_M * 1024 * 1024;

    private static final int MAX_LINES = 3000;
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    private final static String txt = "txt";
    private final static String csv = "csv";

    private static Logger logger = LoggerFactory.getLogger(TeleFileUploadAction.class);
    int unitPrice = PropertyManager.getIntProperty("jbb.mgt.phonecheck.price", 5);

    @Autowired
    TeleMarketingService teleMarketingService;

    @Autowired
    TeleMarketingDetailService detailService;
    @Autowired
    private OrgRechargesService orgRechargesService;

    private Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void teleUpload(Integer price, byte[] content, String fileName) throws IOException {
        logger.debug(">teleUpload()");
        if ((content == null) || (content.length == 0)) {
            logger.debug("<uploadFile() missing file content");
            throw new WrongParameterValueException("jbb.mgt.exception.file.empty");
        }

        if (content.length > MAX_CONTENT_LENGTH) {
            logger.debug("<uploadFile() file content > MAX_CONTENT_LENGTH");
            throw new WrongParameterValueException("jbb.mgt.exception.file.maxLength", "zh",
                String.valueOf(MAX_CONTENT_LENGTH_M));
        }
        String text = null;
        List<TeleMarketingDetail> teleList = new ArrayList<TeleMarketingDetail>();
        if (StringUtil.isEmpty(fileName) || fileName.endsWith(txt) || fileName.endsWith(csv)) {
            try {
                boolean b = UTF8Util.isUtf8(content);
                if (b) {
                    text = new String(content, "UTF-8");
                } else {
                    text = new String(content, "gb2312");
                }

            } catch (UnsupportedEncodingException e) {
                throw new WrongParameterValueException("jbb.mgt.exception.file.readError");
            }
            // 去除bom头
            if (text.startsWith("\uFEFF")) {
                text = text.replace("\uFEFF", "");
            }
            String[] lines = text.split("\r\n");

            for (String line : lines) {
                String[] lineArr = line.split(",");
                teleList.add(new TeleMarketingDetail(
                    lineArr[0].trim().length() > 11 ? lineArr[0].trim().substring(0, 11) : lineArr[0].trim(),
                    lineArr.length > 1 && lineArr[1].trim().length() < 20 ? lineArr[1].trim() : null,
                    TeleMarketingDetail.STATUS_INIT));
            }
        } else if (fileName.endsWith(xls) || fileName.endsWith(xlsx)) {
            InputStream input = new ByteArrayInputStream(content);
            List<String[]> List = POIUtil.readExcel(fileName, input);
            for (int i = 0; i < List.size(); i++) {
                String[] cell = List.get(i);
                teleList.add(new TeleMarketingDetail(
                    cell[0].trim().length() > 11 ? cell[0].trim().substring(0, 11) : cell[0].trim(),
                    cell.length > 1 && cell[1].trim().length() < 20 ? cell[1].trim() : null,
                    TeleMarketingDetail.STATUS_INIT));
            }
        } else {
            throw new WrongParameterValueException("jbb.mgt.exception.file.readError");
        }

        int cnt = teleList.size();
        if (MAX_LINES < cnt) {
            throw new WrongParameterValueException("jbb.mgt.exception.file.maxLines", "zh", String.valueOf(MAX_LINES));
        }
        if (cnt == 0) {
            logger.debug("<uploadFile() no valid phonenumber");
            throw new WrongParameterValueException("jbb.mgt.exception.phonenumber.empty");
        }
        int validCnt = 0;
        int priceInt = price == null ? 0 : price;
        int totalPrice = cnt * unitPrice;
        int orgId = this.account.getOrgId();

        OrgRecharges o = orgRechargesService.selectOrgRecharges(orgId);

        if (o == null || o.getTotalSmsAmount() - o.getTotalSmsExpense() < totalPrice) {
            throw new WrongParameterValueException("jbb.mgt.exception.smsAmount.notEnough");
        }
        int remainAmount = o.getTotalSmsAmount() - o.getTotalSmsExpense();
        // 1. 插入批次数据
        TeleMarketing batchInfo = new TeleMarketing();
        batchInfo.setAccountId(this.account.getAccountId());
        batchInfo.setCreationDate(DateUtil.getCurrentTimeStamp());
        batchInfo.setCnt(cnt);
        batchInfo.setValidCnt(validCnt);
        batchInfo.setPrice(priceInt);
        teleMarketingService.insertTeleMarketing(batchInfo);
        int batchId = batchInfo.getBatchId();
        // 2. 插入明细数据
        detailService.insertMobiles(batchId, teleList);
        this.response.batchId = batchId;
        this.response.cnt = cnt;
        this.response.totalPrice = totalPrice;
        this.response.remainAmount = remainAmount;
        logger.debug("<teleUpload()");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        public Integer batchId;
        public Integer cnt;
        public Integer totalPrice;
        public Integer remainAmount;
    }
}
