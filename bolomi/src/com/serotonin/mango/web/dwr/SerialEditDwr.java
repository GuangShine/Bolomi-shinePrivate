package com.serotonin.mango.web.dwr;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.dataSource.serialDatasource.SerialDataSourceVO;
import com.serotonin.mango.vo.dataSource.serialDatasource.SerialPointLocatorVO;


/*

public class SerialEditDwr extends DataSourceEditDwr{

	   
	    public ProcessResult saveSerialDataSource(BasicDataSourceVO basic, String commPortId, int baudRate, int flowControlIn,
	            int flowControlOut, int dataBits, int stopBits, int parity, int readTimeout, boolean useTerminator,
	            String messageTerminator, String messageRegex, int pointIdentifierIndex,
	            boolean hex, boolean logIO, int maxMessageSize, float ioLogFileSizeMBytes, int maxHistoricalIOLogs) {
	        SerialDataSourceVO ds = (SerialDataSourceVO) Common.getUser().getEditDataSource();

	        setBasicProps(ds, basic);
	        ds.setCommPortId(commPortId);
	        ds.setBaudRate(baudRate);
	        ds.setFlowControlIn(flowControlIn);
	        ds.setFlowControlOut(flowControlOut);
	        ds.setDataBits(dataBits);
	        ds.setStopBits(stopBits);
	        ds.setParity(parity);
	        ds.setReadTimeout(readTimeout);
	        ds.setUseTerminator(useTerminator);
	        ds.setMessageTerminator(StringEscapeUtils.unescapeJava(messageTerminator));
	        ds.setMessageRegex(messageRegex);
	        ds.setPointIdentifierIndex(pointIdentifierIndex);
	        ds.setHex(hex);
	        ds.setLogIO(logIO);
	        ds.setMaxMessageSize(maxMessageSize);
	        ds.setIoLogFileSizeMBytes(ioLogFileSizeMBytes);
	        ds.setMaxHistoricalIOLogs(maxHistoricalIOLogs);
	        
	        return tryDataSourceSave(ds);
	    }	
	
	   
	    public ProcessResult savePointLocator(int id, String xid, String name,SerialPointLocatorVO locator) {
	    	if(locator.getPointIdentifier() == null)
	    		locator.setPointIdentifier(new String()); //Sometimes we want to match an empty string
	        return validatePoint(id, xid, name, locator, null);
	    }
	    
	   
	    public String getSafeTerminator() {
	    	SerialDataSourceVO ds = (SerialDataSourceVO) Common.getUser().getEditDataSource();
	    	return StringEscapeUtils.escapeJava(ds.getMessageTerminator());
	    }
	    
	   
	    public ProcessResult testString(String raw) {
	    	ProcessResult pr = new ProcessResult();
	    	
	    	//Message we will work with
	    	String msg;

	    	SerialDataSourceVO ds = (SerialDataSourceVO) Common.getUser().getEditDataSource();
	    	if(ds.getId() == -1) {
	    		pr.addContextualMessage("testString", "serial.test.needsSave");
	    		return pr;
	    	}
	    	
	    	//Convert the message
	    	msg = StringEscapeUtils.unescapeJava(raw);
	    	
	    	//Are we a hex string
	    	if(ds.isHex()){
		    	 if(!msg.matches("[0-9A-Fa-f]+")){
	    			 pr.addContextualMessage("testString", "serial.validate.notHex");
	    		 }
	    	}
	    	
	    	
	    	DataPointDao dpd = new DataPointDao();
	    	List<DataPointVO> points = dpd.getDataPoints(ds.getId(), null);
	    	if(ds.getUseTerminator()) { 
	    		if(msg.indexOf(ds.getMessageTerminator()) != -1) {
	    			msg = msg.substring(0, msg.indexOf(ds.getMessageTerminator())+1);
	    			Pattern p = Pattern.compile(ds.getMessageRegex());
	    			Matcher m = p.matcher(msg);
	    			if(!m.matches()) {
	    				pr.addContextualMessage("testString", "serial.test.noMessageMatch");
	    				return pr;
	    			}
	    			String identifier = m.group(ds.getPointIdentifierIndex());
	    			for(DataPointVO pnt : points) {
	    				SerialPointLocatorVO plVo = (SerialPointLocatorVO) pnt.getPointLocator();
	    				if(identifier.equals(plVo.getPointIdentifier())) {
	    					Pattern v = Pattern.compile(plVo.getValueRegex());
	    					Matcher vm = v.matcher(msg);
	    					if(vm.find())
	    						pr.addContextualMessage("testString", "serial.test.consumed", pnt.getName(), vm.group(0));
	    				}
	    					
	    			}
	    		}
	    		else {
	    			pr.addContextualMessage("testString", "serial.test.noTerminator");
	    			return pr;
	    		}
	    	}
	    	else {
			    for(DataPointVO pnt : points) {
			    	SerialPointLocatorVO plVo = (SerialPointLocatorVO) pnt.getPointLocator();
			    	Pattern p = Pattern.compile(plVo.getValueRegex());
			    	Matcher m = p.matcher(msg); 
			    	if(m.find())
			    		pr.addContextualMessage("testString", "serial.test.consumed", pnt.getName(), m.group(0));	    		
			    }
	    	}
	    	return pr;
	    }
}
*/