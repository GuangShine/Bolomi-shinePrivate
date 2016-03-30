package com.serotonin.mango.util;

import com.serotonin.mango.db.dao.BaseDao;

public class PointCode extends BaseDao {
	  private static final String CODE_SELECT ="SELECT REPLACE(code_format, '<NUM>', RIGHT(concat('100000000',current_id), digit)) 'code' FROM PointCode WHERE data_code =?";
	  private static final String CODE_INSERT="update PointCode set current_id = current_id + 1 where data_code = ?";

	  public String get_code(String codename)
	    {
		  Object[] p = new Object[] { codename };
		  String pointid = (String)ejt.queryForObject(CODE_SELECT,p, String.class, null);
	      return pointid;
	    }
	  public void refresh_code(String codename)
		  {
		  ejt.update(CODE_INSERT,  new Object[] {codename});			  
		  }
}
