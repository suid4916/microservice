package com.spring.microservice.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.spring.microservice.model.Organization;
import com.spring.microservice.repository.OrganizationRedisRepository;
import com.spring.microservice.utils.UserContext;

@Component
public class OrganizationRestTemplateClient {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	OrganizationRedisRepository redisRepository;

	private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

//	public Organization getOrganization(String organizationId) {
//		ResponseEntity<Organization> restExchange = restTemplate.exchange(
//				"http://organization-service/v1/organization/{organizationId}", HttpMethod.GET, null,
//				Organization.class, organizationId);
//		return restExchange.getBody();
//	}

	private Organization checkRedisCache(String organizationId) {
		try {
			return redisRepository.findById(organizationId).orElse(null);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Error encountered while trying to retrieve organization {} check Redis Cache. Exception {}",
					organizationId, e);
			return null;
		}
	}

	private void cacheOrganiationObject(Organization organization) {
		try {
			redisRepository.save(organization);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Unable to cache Organization {} in Redis. Exception", organization.getId(), e);
		}
	}

	public Organization getOrganization(String organizationId) {
		logger.debug("In Licensing Service.getOrganization: {}", UserContext.getCorrelationId());
		Organization organization = checkRedisCache(organizationId);

		if (organization != null) {
			logger.debug("I have successfully retrieved an organization {} from the redis cache: {}", organizationId,
					organization);
			return organization;
		}

		logger.debug("Unable to locate organization from the redis cache: {}", organizationId);

//		ResponseEntity<Organization> restExchange = restTemplate.exchange(
//				"http://gateway:8072/organization/v1/organization/{organizationId}", HttpMethod.GET, null,
//				Organization.class, organizationId);
		ResponseEntity<Organization> restExchange = restTemplate.exchange(
				"http://gateway-server:8072/organization/v1/organization/{organizationId}", HttpMethod.GET, null,
				Organization.class, organizationId);

		/* save Redis Cache */
		organization = restExchange.getBody();

		if (organization != null) {
			cacheOrganiationObject(organization);
		}
		return restExchange.getBody();
	}

//	public Organization getOrganization(String organizationId, String requestMethod) {
//		// TODO Auto-generated method stub
//		ResponseEntity<Organization> restExchange = restTemplate.exchange(
//				"http://organization-service/v1/organization/{organizationId}/post", HttpMethod.POST, null,
//				Organization.class, organizationId);
//		return restExchange.getBody();
//	}

}
