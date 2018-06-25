package com.jbb.mgt.rs.action.Property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Property;
import com.jbb.mgt.core.service.SystemService;
import com.jbb.mgt.rs.action.channel.ChannelAction;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.RsProperty;
import com.jbb.server.common.exception.AccessDeniedException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Property Action类
 *
 * @author wyq
 * @date 2018/6/6 19:49
 */
@Service(PropertyAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PropertyAction extends BasicAction {

    public static final String ACTION_NAME = "PropertyAction";

    private static Logger logger = LoggerFactory.getLogger(PropertyAction.class);

    private Response response;

    @Autowired
    private SystemService systemService;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new PropertyAction.Response();
    }

    public void getPropertiesByName(String name) {
        logger.debug(">getPropertiesByName, name=" + name);
        if (this.account.getOrgId() != 1) {
            throw new AccessDeniedException("jbb.mgt.exception.HaveNoRightToAccess");
        }
        List<Property> list = systemService.selectSystemPropertiesByName(name);
        this.response.properties = new ArrayList<RsProperty>(list.size());
        for (Property property : list) {
            this.response.properties.add(new RsProperty(property));
        }
        logger.debug(">getPropertiesByName");
    }

    public void saveOrUpdatePropertiesByName(String name, String value) {
        logger.debug(">saveOrUpdatePropertiesByName, name=" + name + "value=" + value);
        if (this.account.getOrgId() != 1) {
            throw new AccessDeniedException("jbb.mgt.exception.HaveNoRightToAccess");
        }
        String value1 = systemService.getSystemPropertyValue(name);
        if (StringUtil.isEmpty(name) || null == value1) {
            systemService.saveSystemProperty(name, value);
        } else {
            systemService.updateSystemProperty(name, value);
        }
        logger.debug(">saveOrUpdatePropertiesByName");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private List<RsProperty> properties;

        public List<RsProperty> getProperties() {
            return properties;
        }
    }

}
