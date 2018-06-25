package com.jbb.server.rs.action;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbb.server.rs.pojo.ActionResponse;

@Component(BasicAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BasicAction extends BasicComponent {
    public static final String ACTION_NAME = "BasicAction";

    protected static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    protected ActionResponse makeActionResponse() {
        return new ActionResponse();
    }
}
