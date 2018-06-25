package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.core.domain.CompanyIntroduction;
import com.jbb.server.core.service.CompanyIntroductionService;
import com.jbb.server.rs.pojo.ActionResponse;

 @Service(CompanyIntroductionAction.ACTION_NAME)
 @Scope(BeanDefinition.SCOPE_PROTOTYPE)
 public class CompanyIntroductionAction extends BasicAction{
	 private static Logger logger = LoggerFactory.getLogger(CompanyIntroductionAction.class);
	  public static final String ACTION_NAME = "CompanyIntroductionAction";
	  @Autowired
	  private CompanyIntroductionService companyIntroductionService;
	  private Response response;
	  @Override
	    protected ActionResponse makeActionResponse() {
	        return response = new Response();
	    }
	  public void getCompanyIntroduction(){
	        this.response.companyIntroduction = companyIntroductionService.getCompanyIntroduction();
	        logger.debug("<CompanyIntroductionAction");
	  }
	  @JsonInclude(JsonInclude.Include.NON_NULL)
	    public static class Response extends ActionResponse {
	        private CompanyIntroduction companyIntroduction;
	        @JsonProperty("company")
	        public CompanyIntroduction getCompanyIntroduction() {
	            return companyIntroduction;
	        }
	  }
 }
