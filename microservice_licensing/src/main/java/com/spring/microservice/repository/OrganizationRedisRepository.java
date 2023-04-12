package com.spring.microservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spring.microservice.model.Organization;

@Repository
public interface OrganizationRedisRepository extends CrudRepository<Organization, String>{

}
