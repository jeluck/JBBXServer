package com.jbb.mgt.core.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jbb.mgt.core.domain.DataYxUrls;
import com.jbb.mgt.core.service.DataYxUrlsService;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.shared.rs.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbb.mgt.core.dao.YxReportDao;
import com.jbb.mgt.core.domain.YxNotify;
import com.jbb.mgt.core.domain.YxReport;
import com.jbb.mgt.core.domain.YxReportRsp;
import com.jbb.mgt.core.service.AliyunService;
import com.jbb.mgt.core.service.YxReportService;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.util.HttpUtil;
import com.jbb.server.common.util.HttpUtil.HttpResponse;
import com.jbb.server.common.util.MD5;
import com.jbb.server.common.util.StringUtil;

@Service("YxReportService")
public class YxReportServiceImpl implements YxReportService {

    private static Logger logger = LoggerFactory.getLogger(YxReportServiceImpl.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Autowired
    private AliyunService aliyunService;

    @Autowired
    private YxReportDao yxReportDao;

    @Autowired
    private DataYxUrlsService dataYxUrlsService;

    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public void saveReportInfo(YxReport report) {
        yxReportDao.saveReportInfo(report);

    }

    @Override
    public YxReport getReport(Integer userId, Integer applyId, String taskId, String reportType) {
        if (userId == null && applyId == null && taskId == null) {
            return null;
        }
        YxReport report = yxReportDao.selectReport(userId, applyId, taskId, reportType);
        String data = null;
        if (report == null) {
            return null;
        }
        try {
            data = aliyunService.getObject(Constants.OSS_BUCKET_YX_PRELOAN, report.getTaskId());
        } catch (Exception e) {
            logger.warn("not found yx report from oss, taskId =" + report.getTaskId());
        }
        if (data == null && report.getToken() != null) {
            YxReportRsp rsp = getReportFormYx(report.getToken());
            try {
                data = mapper.writeValueAsString(rsp);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.warn("getReport: writeValueAsString, error =" + e.getMessage());
            }
            saveYxReportRsp(report.getTaskId(), rsp);
            // 存储至DB
            report.setStatus(YxReport.STATUS_COMPLATE);
            saveReportInfo(report);

        }
        report.setData(data);
        return report;
    }

    @Override
    public void notify(YxNotify notifyInfo) {
        if (!YxNotify.SUCCESS_TRUE.equals(notifyInfo.getSuccess())) {
            return;
        }
        String orgId = PropertyManager.getProperty("jbb.mgt.yixiang.api.appId");
        String appKey = PropertyManager.getProperty("jbb.mgt.yixiang.api.appKey");
        String sign = MD5.MD5Encode(orgId + appKey + notifyInfo.getToken());
        if (!sign.equals(notifyInfo.getSign())) {
            logger.warn("yx notify, sign error ");
            return;
        }
        YxReport report = new YxReport(notifyInfo);
        // 获取
        YxReportRsp rsp = getReportFormYx(notifyInfo.getToken());
        saveYxReportRsp(notifyInfo.getTaskId(), rsp);
        // 存储至DB
        report.setStatus(YxReport.STATUS_COMPLATE);
        saveReportInfo(report);
    }

    private void saveYxReportRsp(String taskId, YxReportRsp rsp) {
        if (rsp == null) {
            return;
        }
        // 存储至OSS
        try {
            aliyunService.putObject(Constants.OSS_BUCKET_YX_PRELOAN, taskId, mapper.writeValueAsString(rsp));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.warn(e.getMessage());
        }

    }

    @Override
    public String generateH5Url(int userId, int applyId, String reportType) {
        if (StringUtil.isEmpty(reportType) || userId == 0 || applyId == 0) {
            return null;
        }

        DataYxUrls dataYxUrls = dataYxUrlsService.selectDataYxUrlsByUserIdAndReportType(userId, reportType);
        if (dataYxUrls != null) {
            Timestamp creationDate = dataYxUrls.getCreationDate();
            if (creationDate.getTime() - DateUtil.getTodayStartCurrentTime() < DateUtil.DAY_MILLSECONDES) {
                return dataYxUrls.getH5Url();
            }
        }

        String server = PropertyManager.getProperty("jbb.mgt.yixiang.api.url.server");
        // ${host}/apiui/{orgId}/{sign}/{timeMark}/{website}/{taskId}?jumpUrl={jumpUrl}
        String orgId = PropertyManager.getProperty("jbb.mgt.yixiang.api.appId");
        String appKey = PropertyManager.getProperty("jbb.mgt.yixiang.api.appKey");
        String jumpUrl = PropertyManager.getProperty("jbb.mgt.yixiang.ui.jump.url");
        String timeMark = df.format(new Date());
        String website = reportType;
        String taskId = generateTaskId(userId, applyId, reportType);
        StringBuffer bf = new StringBuffer();

        String sign = MD5.MD5Encode(orgId + timeMark + website + taskId + appKey);
        bf.append(server).append("/apiui/").append(orgId).append("/").append(sign).append("/").append(timeMark)
            .append("/").append(website).append("/").append(taskId).append("?jumpUrl=").append(jumpUrl);
        String h5Url = bf.toString();

        if (dataYxUrls != null) {
            dataYxUrlsService
                .updateDataYxUrls(new DataYxUrls(userId, reportType, h5Url, DateUtil.getCurrentTimeStamp()));
        } else {
            dataYxUrlsService
                .insertDataYxUrls(new DataYxUrls(userId, reportType, h5Url, DateUtil.getCurrentTimeStamp()));
        }
        return h5Url;
    }

    private String generateTaskId(int userId, int applyId, String reportType) {
        String typeNum = null;
        switch (reportType) {
            case YxReport.TYPE_JIEDAIBAO:
                typeNum = YxReport.TYPE_JIEDAIBAO_NUM;
                break;
            case YxReport.TYPE_MIFANG:
                typeNum = YxReport.TYPE_MIFANG_NUM;
                break;
            case YxReport.TYPE_WUYOU:
                typeNum = YxReport.TYPE_WUYOU_NUM;
                break;
            case YxReport.TYPE_JINJIEDAO:
                typeNum = YxReport.TYPE_JINJIEDAO_NUM;
                break;
        }
        String randomStr = StringUtil.randomAlphaNum(4);
        Date now = new Date();
        StringBuffer bf = new StringBuffer();
        bf.append(typeNum).append(YxReport.SPLIT_TASK_ID_CHAR).append(randomStr).append(YxReport.SPLIT_TASK_ID_CHAR)
            .append(df.format(now)).append(YxReport.SPLIT_TASK_ID_CHAR).append(userId)
            .append(YxReport.SPLIT_TASK_ID_CHAR).append(applyId);
        return bf.toString();
    }

    @Override
    public YxReportRsp getReportFormYx(String token) {
        String server = PropertyManager.getProperty("jbb.mgt.yixiang.api.url.server");
        String api = PropertyManager.getProperty("jbb.mgt.yixiang.api.url.get");
        String orgId = PropertyManager.getProperty("jbb.mgt.yixiang.api.appId");
        String appKey = PropertyManager.getProperty("jbb.mgt.yixiang.api.appKey");
        String sign = MD5.MD5Encode(orgId + appKey);
        String url = server + api + "?token=" + token + "&orgId=" + orgId + "&sign=" + sign;

        try {
            HttpResponse response =
                HttpUtil.sendDataViaHTTP(HttpUtil.HTTP_GET, url, HttpUtil.CONTENT_TYPE_JSON, null, null);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                String content = new String(response.getResponseBody());
                YxReportRsp rsp = mapper.readValue(content, YxReportRsp.class);
                logger.debug("response = " + content);
                if (rsp.isSuccess()) {
                    // 获取成功
                }

                return rsp;

            } else {
                logger.debug("response code = " + response.getResponseCode());
            }

        } catch (IOException e) {
            logger.debug("response with error, " + e.getMessage());
            e.printStackTrace();
        }
        return null;

    }

}
