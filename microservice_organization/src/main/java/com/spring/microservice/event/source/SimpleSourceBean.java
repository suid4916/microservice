package com.spring.microservice.event.source;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.spring.microservice.event.model.OrganizationChangeModel;
import com.spring.microservice.model.Organization;
import com.spring.microservice.utils.UserContext;

@Component
public class SimpleSourceBean {
//	private Source source;
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);
	
//	@Autowired
//	public SimpleSourceBean(Source source) {
//		this.source = source;
//	}
	
//	public void publishOrganizationChange(ActionEnum action, String organizationId) {
//		logger.debug("sending kafka message {} for Organization ID: {}", action, organizationId);
//		
//		OrganizationChangeModel change = new OrganizationChangeModel(OrganizationChangeModel.class.getTypeName(), action.toString(), organizationId, UserContext.getCorrelationId());
//		
//		source.output().send(MessageBuilder.withPayload(change).build());
//	}
	
//	@Bean
	public Supplier<String> publishOrganizationChange(ActionEnum action, String organizationId) {
		logger.debug("sending kafka message {} for Organization ID: {}", action, organizationId);
		
		OrganizationChangeModel change = new OrganizationChangeModel(OrganizationChangeModel.class.getTypeName(), action.toString(), organizationId, UserContext.getCorrelationId());
		
		return () ->  change.toString();
	}
}
