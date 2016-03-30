package com.serotonin.mango.vo;

import com.serotonin.mango.Common;

public class mangoServers {
	
	private int id;
	private String code;
	private String name;
	private String serverurl;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = Common.getEnvironmentProfile().getString("db.mangocode");
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getServerurl() {
		return serverurl;
	}
	public void setServerurl(String serverurl) {
		this.serverurl = serverurl;
	}
	

}
