package com.jbb.mgt.core.service.impl;

import java.io.IOException;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbb.mgt.core.dao.TdPreloanReportDao;
import com.jbb.mgt.core.dao.UserDao;
import com.jbb.mgt.core.domain.PreloanReport;
import com.jbb.mgt.core.domain.TdPreloanRiskResponse;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.service.AliyunService;
import com.jbb.mgt.core.service.TdRiskService;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.HttpUtil;
import com.jbb.server.common.util.HttpUtil.HttpResponse;

@Service("TdRiskService")
public class TdRiskServiceImpl implements TdRiskService {
    private static Logger logger = LoggerFactory.getLogger(TdRiskServiceImpl.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Autowired
    private TdPreloanReportDao preloanReportDao;

    @Autowired
    private AliyunService aliyunService;

    @Autowired
    private UserDao userDao;

    @Override
    public PreloanReport getPreloanReportByReportId(String reportId) {
        PreloanReport report = preloanReportDao.selectPreloanReportByReportId(reportId);
        if (report == null) {
            // TODO 抛出异常，报告不存在
            return null;
        }
        if (report != null && report.getStatus() == PreloanReport.STATUS_SUCC) {
            report.setRsp(getReportFromOss(report.getReportId()));
        } else {
            User user = userDao.selectUserById(report.getUserId());
            report = getPreloanReport(user, report.getApplyId());
        }
        return report;
    }

    @Override
    public PreloanReport getPreloanReportByApplyId(int applyId) {
        PreloanReport report = preloanReportDao.selectPreloanReportByApplyId(applyId);
        if (report != null && report.getStatus() == PreloanReport.STATUS_SUCC) {
            report.setRsp(getReportFromOss(report.getReportId()));
        } else {
            User user = userDao.selectUserByApplyId(applyId);
            report = getPreloanReport(user, applyId);
        }
        return report;
    }

    @Override
    public PreloanReport getPreloanReportByUserId(int userId) {
        PreloanReport report = preloanReportDao.selectPreloanReportByUserId(userId);
        if (report != null && report.getStatus() == PreloanReport.STATUS_SUCC) {
            report.setRsp(getReportFromOss(report.getReportId()));
        } else {
            User user = userDao.selectUserById(userId);
            report = getPreloanReport(user, 0);
        }
        return report;
    }

    public PreloanReport getPreloanReport(User user, int applyId) {
        // 先提交申请
        PreloanReport report = preloanApply(user, applyId);
        if (report == null) {
            // 提交失败，请重新获取
            return null;
        }
        // 再获取返回
        TdPreloanRiskResponse rsp = getReportFromTd(report.getReportId());
        if (rsp != null && rsp.getSuccess()) {
            report.setReportDate(new Timestamp(rsp.getReportTime()));
            report.setApplyDate(new Timestamp(rsp.getApplyTime()));
            report.setStatus(PreloanReport.STATUS_SUCC);
            report.setFinalDecision(rsp.getFinalDecision());
            report.setFinalScore(rsp.getFinalScore());
            // 更新状态
            preloanReportDao.updatePreloanReport(report);
            // 存储至OSS服务器
            try {
                String content = mapper.writeValueAsString(rsp);
                report.setRsp(content);// 设备返回结果， JSON串
                saveReportToOss(report.getReportId(), mapper.writeValueAsString(rsp));
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

        return report;
    }

    private PreloanReport preloanApply(User user, Integer applyId) {
        if (user == null) {
            throw new ObjectNotFoundException("jbb.mgt.exception.userNotFound");
        }

        if (user.getUserName() == null || user.getIdCard() == null) {
            logger.info("username or idcard is empty, userId=" + user.getUserId());
            return null;
        }

        String code = PropertyManager.getProperty("jbb.mgt.td.partner.code");
        String key = PropertyManager.getProperty("jbb.mgt.td.partner.key");
        String url = PropertyManager.getProperty("jbb.mgt.td.preload.apply.url");
        String appname = PropertyManager.getProperty("jbb.mgt.td.partner.appname");
        url += "?partner_code=" + code + "&partner_key=" + key + "&app_name=" + appname;

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("name=");
        stringBuilder.append(HttpUtil.encodeURLParam(user.getUserName()));
        stringBuilder.append("&id_number=");
        stringBuilder.append(user.getIdCard());
        stringBuilder.append("&mobile=");
        stringBuilder.append(user.getPhoneNumber());
        String data = stringBuilder.toString();
        try {
            HttpResponse response = HttpUtil.sendDataViaHTTP(HttpUtil.HTTP_POST, url,
                HttpUtil.CONTENT_TYPE_X_WWW_FORM_URLENCODED, data, null);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                TdPreloanRiskResponse tdRsp =
                    mapper.readValue(new String(response.getResponseBody()), TdPreloanRiskResponse.class);
                if (tdRsp.getSuccess()) {
                    PreloanReport report = new PreloanReport();
                    report.setApplyId(applyId);
                    report.setReportId(tdRsp.getReportId());
                    report.setUserId(user.getUserId());
                    report.setReq(data);
                    report.setApplyDate(DateUtil.getCurrentTimeStamp());
                    preloanReportDao.insertPreloanReport(report);
                    return report;
                } else {
                    logger.debug("response = " + new String(response.getResponseBody()));
                    return null;
                }

            } else {
                logger.debug("response code = " + response.getResponseCode());
            }

        } catch (IOException e) {
            logger.debug("response with error, " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private TdPreloanRiskResponse getReportFromTd(String reportId) {
        String code = PropertyManager.getProperty("jbb.mgt.td.partner.code");
        String key = PropertyManager.getProperty("jbb.mgt.td.partner.key");
        String url = PropertyManager.getProperty("jbb.mgt.td.preload.getreport.url");
        String appname = PropertyManager.getProperty("jbb.mgt.td.partner.appname");
        url += "?partner_code=" + code + "&partner_key=" + key + "&app_name=" + appname + "&report_id=" + reportId;

        try {
            HttpResponse response = HttpUtil.sendDataViaHTTP(HttpUtil.HTTP_GET, url,
                HttpUtil.CONTENT_TYPE_X_WWW_FORM_URLENCODED, null, null);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                String content = new String(response.getResponseBody());
                TdPreloanRiskResponse tdRsp = mapper.readValue(content, TdPreloanRiskResponse.class);
                if (tdRsp.getSuccess()) {

                } else {
                    logger.debug("response = " + tdRsp.getSuccess());
                }
                return tdRsp;

            } else {
                logger.debug("response code = " + response.getResponseCode());
            }

        } catch (IOException e) {
            logger.debug("response with error, " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private String getReportFromOss(String reportId) {
        try {
            return aliyunService.getObject(Constants.OSS_BUCKET_TD_PRELOAN, reportId);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void saveReportToOss(String reportId, String content) {
        aliyunService.putObject(Constants.OSS_BUCKET_TD_PRELOAN, reportId, content);
    }

}
