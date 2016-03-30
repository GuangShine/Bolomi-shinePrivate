package com.serotonin.mango.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao.DataPointRowMapper;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.Key;
import com.serotonin.mango.vo.Key_lk;
import com.serotonin.util.SerializationHelper;

public class KeyDao extends BaseDao {
	 class KeyRowMapper implements GenericRowMapper<Key> {
	        public Key mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	Key keyt = new Key();
	            int i = 0;
	            keyt.setId(rs.getInt(++i));
	            keyt.setKey_Code(rs.getString(++i));
	            keyt.setKey_Name(rs.getString(++i));
	            keyt.setKey_cTime(rs.getString(++i));
	            keyt.setKey_uTime(rs.getString(++i));
	            
	            return keyt;
	        }
	    }
	 class Key_lkRowMapper implements GenericRowMapper<Key_lk> {
	        public Key_lk mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	Key_lk keyt = new Key_lk();
	            int i = 0;
	          keyt.setId(rs.getInt(++i));
	          keyt.setKey_Code(rs.getString(++i));
	          keyt.setRef_Code(rs.getString(++i));
	          keyt.setMangoCode(rs.getString(++i));
	          keyt.setKeyType(rs.getInt(++i));
	          keyt.setKey_Check(rs.getInt(i++));
	          keyt.setKey_lk_cTime(rs.getString(++i));
	          keyt.setKey_lk_uTime(rs.getString(++i));
	            
	            return keyt;
	        }
	    }
	 public List<Key> getKeyTypeList() {
	        List<Key> keytList = query("select id,key_code,key_name,key_ctime,key_utime from keies order by Id", new Object[0], new KeyRowMapper());
	        return keytList;
	    }
	 public List<Key_lk> getKeyTypeListByRefCode(String refcode) {
	        List<Key_lk> keytList = query("select id,key_code,ref_code,mangocode,keytype,key_check,key_lk_ctime,key_lk_utime from key_lk where Ref_Code=?", new Object[]{refcode}, new Key_lkRowMapper());
	        return keytList;
	    }
	 public List<Key_lk> getKey_lkListByCodeAndType(String xid)
	 {
		 List<Key_lk> keytList = query("select id,key_code,ref_code,mangocode,keytype,key_check,key_lk_ctime,key_lk_utime from key_lk where Ref_Code=? and keytype=1", new Object[]{xid}, new Key_lkRowMapper());
	        return keytList;
	 }
	 public void Savekey(Key_lk k)
	 {
		 if(k.getId()==Common.NEW_ID)
		 {
			 insertDataSource_key(k);
		 }else
		 {
			 updateDataSource_key(k);
		 }
			 
	 }
	 public void insertDataSource_key(Key_lk k)
	 {
		 
	       String mangocode=Common.getEnvironmentProfile().getString("db.mangocode");
	       ejt.update("insert into key_lk (Key_Code, Ref_Code, MangoCode,KeyType,Key_lk_cTime) values (?,?,?,?,?)", new Object[] {k.getKey_Code(),
               k.getRef_Code(),mangocode,k.getKeyType(),k.getKey_lk_cTime()}, new int[] { Types.VARCHAR,Types.VARCHAR,
	    		   Types.VARCHAR , Types.INTEGER,Types.VARCHAR });
		 
	 }
	 public void updateDataSource_key(Key_lk k)
	 {
		 
	       String mangocode=Common.getEnvironmentProfile().getString("db.mangocode");
	       ejt.update("delete from key_lk where Ref_code=? and key_code=?", new Object[] {k.getRef_Code(),k.getKey_Code()},new int[]{Types.VARCHAR,Types.VARCHAR});
	       this.insertDataSource_key(k);
	      /* ejt.update("update key_lk set Key_Code=?, Ref_Code=?, MangoCode=?,Key_lk_uTime=? where id=?", new Object[] {k.getKey_Code(),
               k.getRef_Code(),mangocode,k.getKey_lk_uTime(),k.getId()}, new int[] { Types.VARCHAR,Types.VARCHAR,
	    		   Types.VARCHAR , Types.VARCHAR,Types.INTEGER });*/
		 
	 }
	 public void deletekey_lk(String Ref_code)
	 {
		 String mangocode=Common.getEnvironmentProfile().getString("db.mangocode");
	       ejt.update("delete from key_lk where Ref_code=?", new Object[] {Ref_code},new int[]{Types.VARCHAR});
	 }
	 public Key_lk SearchByCode(Key_lk k)
	 {
		 Key_lk klk = queryForObject("select id,key_code,ref_code,mangocode,keytype,key_check,key_lk_ctime,key_lk_utime from key_lk where key_code=? and ref_code=? and keytype=?", new Object[] {k.getKey_Code(),k.getRef_Code(),k.getKeyType() },
	                new Key_lkRowMapper(), null);

	        return klk;
	 }
	 public int SearchByCodeIsE(Key_lk k)
	 {
		 Key_lk klk = queryForObject("select id,key_code,ref_code,mangocode,keytype,key_check,key_lk_ctime,key_lk_utime from key_lk where key_code=? and ref_code=? and keytype=?", new Object[] {k.getKey_Code(),k.getRef_Code(),k.getKeyType() },
	                new Key_lkRowMapper(), null);
		 if(klk!=null)
		 {return 1;}else{return 0;}
	 }

}
