package com.test.webservice.models.response;

public class PermissionPOJO {
	
	private String admin;
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getMaintain() {
		return maintain;
	}
	public void setMaintain(String maintain) {
		this.maintain = maintain;
	}
	public String getPush() {
		return push;
	}
	public void setPush(String push) {
		this.push = push;
	}
	public String getTriage() {
		return triage;
	}
	public void setTriage(String triage) {
		this.triage = triage;
	}
	public String getPull() {
		return pull;
	}
	public void setPull(String pull) {
		this.pull = pull;
	}
	private String maintain;
	private String push;
	private String triage;
	private String pull;
	
	
    

}
