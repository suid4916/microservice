package com.spring.microservice.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.microservice.event.source.ActionEnum;
import com.spring.microservice.event.source.SimpleSourceBean;
import com.spring.microservice.model.Organization;
import com.spring.microservice.repository.OrganizationRepository;

@Service
public class OrganizationService {
	
    @Autowired
    private OrganizationRepository repository;
    
    @Autowired
    SimpleSourceBean simpleSourceBean;

    public Organization create(Organization organization) {
    	organization.setId(UUID.randomUUID().toString());
    	organization = repository.save(organization);
    	simpleSourceBean.publishOrganizationChange(ActionEnum.CREATED, organization.getId());
    	return organization;
    }
    
    public Organization findById(String organizationId) {
    	Optional<Organization> opt = repository.findById(organizationId);
        return (opt.isPresent()) ? opt.get() : null;
    }

    public void update(Organization organization){
    	repository.save(organization);
    }

    public void delete(Organization organization){
    	repository.deleteById(organization.getId());
    }
}
