package com.test.webservice.models.request;

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class CreateNewRepoPOJO {
	private String name;
	private String description;
	private String homepage;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getPrivate() {
		return Private;
	}
	public void setPrivate(String private1) {
		Private = private1;
	}
	private String Private;
	
	
	
	

	
	
	
	

}
