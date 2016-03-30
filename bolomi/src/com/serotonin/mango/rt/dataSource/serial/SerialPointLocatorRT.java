package com.serotonin.mango.rt.dataSource.serial;

import java.util.regex.Pattern;

import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.vo.dataSource.serialDatasource.SerialPointLocatorVO;


public class SerialPointLocatorRT extends PointLocatorRT{

	private SerialPointLocatorVO vo;
	private Pattern pattern;
	
	public SerialPointLocatorRT(SerialPointLocatorVO vo){
		this.vo = vo;
		pattern = Pattern.compile(vo.getValueRegex());
	}
	
	@Override
	public boolean isSettable() {
		return this.vo.isSettable();
	}

	public SerialPointLocatorVO getVo(){
		return this.vo;
	}
	
	public Pattern getPattern() {
		return pattern;
	}
	
}
