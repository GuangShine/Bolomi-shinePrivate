package com.serotonin.mango.db.dao;

import java.sql.Types;

import com.serotonin.mango.Common;

import com.serotonin.mango.vo.mangoServers;

public class MangoServerDao extends BaseDao {
	  private void insertMangoServer(mangoServers ms) {
	    	String mangocode=Common.getEnvironmentProfile().getString("db.mangocode");
	        doInsert("insert into mangoserver (code, name, serverurl) values (?,?,?)", new Object[] {
	               mangocode,ms.getName(),ms.getServerurl() }, new int[] {
	                Types.VARCHAR, Types.VARCHAR,Types.VARCHAR });
	    }

}
