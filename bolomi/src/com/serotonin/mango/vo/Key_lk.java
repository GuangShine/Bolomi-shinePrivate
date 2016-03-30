package com.serotonin.mango.vo;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.mango.Common;

@JsonRemoteEntity
public class Key_lk {
	private int id=Common.NEW_ID;
	private String key_Code;
	private String ref_Code;
	private String mangoCode;
	private int keyType;
	private int key_Check;
	public int getKey_Check() {
		return key_Check;
	}
	public void setKey_Check(int key_Check) {
		this.key_Check = key_Check;
	}
	private String key_lk_cTime;
	private String key_lk_uTime;
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
	public String getRef_Code() {
		return ref_Code;
	}
	public void setRef_Code(String ref_Code) {
		this.ref_Code = ref_Code;
	}
	public String getMangoCode() {
		return mangoCode;
	}
	public void setMangoCode(String mangoCode) {
		this.mangoCode = mangoCode;
	}
	public int getKeyType() {
		return keyType;
	}
	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}
	public String getKey_lk_cTime() {
		return key_lk_cTime;
	}
	public void setKey_lk_cTime(String key_lk_cTime) {
		this.key_lk_cTime = key_lk_cTime;
	}
	public String getKey_lk_uTime() {
		return key_lk_uTime;
	}
	public void setKey_lk_uTime(String key_lk_uTime) {
		this.key_lk_uTime = key_lk_uTime;
	}
	

}
