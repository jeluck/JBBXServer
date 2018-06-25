package com.jbb.mgt.core.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbb.mgt.core.service.WeiboService;
import com.jbb.server.common.exception.JbbCallException;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.util.HttpUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("WeiboService")
public class WeiboServiceImpl implements WeiboService {

    private static Logger logger = LoggerFactory.getLogger(WeiboServiceImpl.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public String shorten(String longUrl) {
        if (StringUtils.isEmpty(longUrl)) {
            logger.debug("<weiboShortUrl() missing longUrl");
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "longUrl");
        }
        String url = "https://api.weibo.com/2/short_url/shorten.json?url_long=" + longUrl + "&source=1563329601";
        try {
            HttpUtil.HttpResponse response
                = HttpUtil.sendDataViaHTTP(HttpUtil.HTTP_GET, url, null, null, null);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                String rsp = mapper.readValue(new String(response.getResponseBody()), String.class);
                if (StringUtils.isNotEmpty(rsp)) {
                    return null;
                } else {
                    logger.warn("response code = " + response.getResponseCode());
                    return null;
                }
            } else {
                String bodyStr = new String(response.getResponseBody());
                logger.warn("weiboShortUrl() request =  " + url);
                logger.error("weiboShortUrl() response with error, " + bodyStr);
                return null;
            }
        } catch (IOException e) {
            logger.error("response with error, " + e.getMessage());
            e.printStackTrace();
            throw null;
        }
    }

}
