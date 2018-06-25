package com.jbb.mgt.rs.action.upload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.UserContant;
import com.jbb.mgt.core.service.UserContantService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.UTF8Util;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service(MailListUploadAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MailListUploadAction extends BasicAction {

    public static final String ACTION_NAME = "MailListUploadAction";

    private static final int MAX_CONTENT_LENGTH_M = 10;
    private static final int MAX_CONTENT_LENGTH = MAX_CONTENT_LENGTH_M * 1024 * 1024;

    private static Logger logger = LoggerFactory.getLogger(MailListUploadAction.class);

    private Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new MailListUploadAction.Response();
    }

    @Autowired
    private UserContantService userContantService;

    public void uploadContants(File file, Integer userId) throws IOException {
        logger.debug(">uploadContants()");
        if (null == userId || userId == 0) {
            logger.debug("<uploadContants() missing userId");
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "userId");
        }
        if (file == null) {
            logger.debug("<uploadContants() missing file");
            throw new WrongParameterValueException("jbb.mgt.exception.file.empty");
        }

        InputStream input = new FileInputStream(file);
        byte[] byt = new byte[input.available()];
        input.read(byt);
        if ((byt == null) || (byt.length == 0)) {
            logger.debug("<uploadContants() missing file");
            throw new WrongParameterValueException("jbb.mgt.exception.file.empty");
        }
        if (byt.length > MAX_CONTENT_LENGTH) {
            logger.debug("<uploadContants() file byte > MAX_CONTENT_LENGTH");
            throw new WrongParameterValueException("jbb.mgt.exception.file.maxLength", "zh",
                String.valueOf(MAX_CONTENT_LENGTH_M));
        }
        String code;
        if(UTF8Util.isUtf8(byt)){
            code = "UTF-8";
        }else {
            code = "gb2312";
        }
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), code);
        List<VCard> vCards = Ezvcard.parse(reader).all();
        if (CollectionUtils.isEmpty(vCards)) {
            logger.debug("<uploadContants() error file");
            throw new WrongParameterValueException("jbb.mgt.exception.file.readError");
        }

        ArrayList<UserContant> userContants = new ArrayList<>();
        for (VCard vCard : vCards) {
            // 用户姓名读取
            String userName = getFormattedName(vCard);
            // 手机号读取
            List<Telephone> telephoneNumbers = vCard.getTelephoneNumbers();

            for (Telephone telephone : telephoneNumbers) {
                String phoneNumber = telephone.getText().replaceAll("[^0-9]+", "");
                UserContant userContant = new UserContant(userId, phoneNumber, userName, vCard.toString());
                userContants.add(userContant);
            }
            if (CollectionUtils.isEmpty(telephoneNumbers)) {
                UserContant userContant = new UserContant(userId, "", userName, vCard.toString());
                userContants.add(userContant);
            }
        }
        userContantService.insertOrUpdateUserContant(userContants);
        logger.debug("<uploadContants()");
    }


    private String getFormattedName(VCard vCard) {
        StringBuffer name = new StringBuffer("");
        StructuredName fn = vCard.getStructuredName();
        if (fn == null) {
            return "";
        }
        if (StringUtils.isNotEmpty(fn.getFamily())) {
            name.append(fn.getFamily());
        }
        if (StringUtils.isNotEmpty(fn.getGiven())) {
            name.append(fn.getGiven());
        }
        return String.valueOf(name);
    }

    public void getUserContants(Integer userId) {
        logger.debug(">getUserContants()");
        if (null == userId || userId == 0) {
            logger.debug("<getUserContants() missing userId");
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "userId");
        }
        this.response.userContants = userContantService.selectUserContantByUserId(userId);
        logger.debug("<uploadMailList()");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private List<UserContant> userContants;

        public List<UserContant> getUserContants() {
            return userContants;
        }
    }

}
