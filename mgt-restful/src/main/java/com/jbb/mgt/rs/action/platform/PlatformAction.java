package com.jbb.mgt.rs.action.platform;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.IouPlatform;
import com.jbb.mgt.core.service.IouPlatformService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.RsIouPlatform;
import com.jbb.mgt.server.rs.pojo.RsPermissions;

@Service(PlatformAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PlatformAction extends BasicAction {
    public static final String ACTION_NAME = "PlatformAction";

    private static Logger logger = LoggerFactory.getLogger(PlatformAction.class);
    @Autowired
    private IouPlatformService iouPlatformService;
    private Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void getPlatforms() {
        logger.debug(">getPlatforms");
        List<IouPlatform> IouPlatforms = iouPlatformService.selectPlatforms();
        this.response.IouPlatforms = new ArrayList<RsIouPlatform>(IouPlatforms.size());
        for (IouPlatform iouPlatform : IouPlatforms) {
            this.response.IouPlatforms.add(new RsIouPlatform(iouPlatform));
        }
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        public List<RsIouPlatform> IouPlatforms;
       
    }

}
