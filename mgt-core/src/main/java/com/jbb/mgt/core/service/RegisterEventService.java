package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.mq.RegisterEvent;

public interface RegisterEventService {
    
    void publishRegisterEvent(RegisterEvent event);

    void publishRegisterEvent(Integer step, Boolean test, int userId, String channelCode);

    void processMessage(RegisterEvent event);
    
    void processEvent(RegisterEvent event);
    
    void processEventToSelfOrgs(RegisterEvent event);
    
    void processSpecApply(User user);

}
