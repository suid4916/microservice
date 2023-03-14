package com.spring.microservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name="licenses")
public class License extends RepresentationModel<License>{
//	private int id;
	@Id
	@Column(name="license_id", nullable = false)
	private String licenseId;
	private String description;
	@Column(name="organization_id", nullable = false)
	private String organizationId;
	@Column(name="product_name", nullable = false)
	private String productName;
	@Column(name="license_type", nullable = false)
	private String licenseType;
	@Column(name="comment")
	private String comment;
	@Transient
	private String organizationName;
	@Transient
	private String contactName;
	@Transient
	private String contactEmail;
	@Transient
	private String contactPhone;
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
	public String getLicenseId() {
		return licenseId;
	}
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public License withComment(String comment) {
		this.setComment(comment);
		return this;
	}
}
