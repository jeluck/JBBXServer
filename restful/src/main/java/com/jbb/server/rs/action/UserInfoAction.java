package com.jbb.server.rs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.Constants;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserProperty;
import com.jbb.server.core.service.impl.AliyunServiceImpl;
import com.jbb.server.rs.pojo.ActionResponse;

@Component(UserInfoAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserInfoAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(UserInfoAction.class);
    public static final String ACTION_NAME = "UserInfoAction";

    private Response response;

    @Autowired
    private AliyunServiceImpl aliyunServiceImpl;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void createUserInfo(boolean detail, Integer userId) {
        logger.debug(">createUserInfo(), detail=" + detail + ", userId= " + userId);

        User user = null;
        if (userId != null && this.user.getUserId() != userId) {
            // 获取用户资料，先检查是否授权
            if (this.coreAccountService.hasPriv(this.user.getUserId(), userId, Constants.USER_PRIV_INFO)) {
                user = this.coreAccountService.getUser(userId);
            }
        } else {
            user = this.user;
        }
        
        if (user != null) {
            // 设置头像
            String picPath = getAvatarUrl(user);
            if (detail) {
                user.setProperites(getUserProperties(user.getUserId()));
                user.setVerifyResults(this.coreAccountService.getUserVerifyResults(user.getUserId()));
            }
            this.response.user = user;
            this.response.user.setAvatarPic(picPath);
        }

        logger.debug("<createUserInfo()");
    }

    private List<UserProperty> getUserProperties(int userId) {
        return coreAccountService.searchUserProperties(userId, null);
    }

    private String getAvatarUrl(User user) {
        String avatarPic = user.getAvatarPic();
        if (StringUtil.isEmpty(avatarPic) || avatarPic.indexOf("style/resize") == -1) {
            // 空或者没有应用样式， 重新更新
            avatarPic = aliyunServiceImpl.generatePresignedUrl(user.getUserId(), Constants.LAST_SYSTEM_MILLIS);
            coreAccountService.updateAvatarPic(user.getUserId(), avatarPic);
        }
        return avatarPic + "&t=" + DateUtil.getCurrentTime();
    }

    public void checkNickname(String nickname) {
        this.response.nicknameExist = coreAccountService.checkNickname(nickname);
    }
    
    public void getSimpleUserInfo(int userId){
        User userG = this.coreAccountService.getUser(userId);
        if(userG == null){
            //提示未找到
           return;
        }
        User rUser = new User();
        rUser.setUserId(userId);
        rUser.setUserName(userG.getUserName());
        rUser.setNickName(userG.getNickName());
        rUser.setVerified(userG.isVerified());
        rUser.setVerifyResults(this.coreAccountService.getUserVerifyResults(userId));
        this.response.user = rUser;
    }
    
    public void refreshAvatarUrl(){
        String avatarPic = aliyunServiceImpl.generatePresignedUrl(user.getUserId(), Constants.LAST_SYSTEM_MILLIS);
        coreAccountService.updateAvatarPic(user.getUserId(), avatarPic);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        
        private Boolean nicknameExist;
        private User user;

        public User getUser() {
            return user;
        }

        public Boolean getNicknameExist() {
            return nicknameExist;
        }

    }
}
