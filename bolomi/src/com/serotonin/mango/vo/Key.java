package com.serotonin.mango.vo;




import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonSerializable;
import com.serotonin.mango.Common;
@JsonRemoteEntity
public class Key
{
	private int id=Common.NEW_ID;
	private String key_Code;
	private String key_Name;	
	private String key_cTime;
	private String key_uTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKey_Code() {
		return key_Code;
	}
	public void setKey_Code(String key_Code) {
		this.key_Code = key_Code;
	}
	public String getKey_Name() {
		return key_Name;
	}
	public void setKey_Name(String key_Name) {
		this.key_Name = key_Name;
	}
	public String getKey_cTime() {
		return key_cTime;
	}
	public void setKey_cTime(String key_cTime) {
		this.key_cTime = key_cTime;
	}
	public String getKey_uTime() {
		return key_uTime;
	}
	public void setKey_uTime(String key_uTime) {
		this.key_uTime = key_uTime;
	}


}
