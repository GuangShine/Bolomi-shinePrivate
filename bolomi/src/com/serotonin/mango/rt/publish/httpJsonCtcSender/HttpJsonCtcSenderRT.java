/*
    Mango - Open Source M2M - http://mango.serotoninsoftware.com
    Copyright (C) 2006-2011 Serotonin Software Technologies Inc.
    @author Matthew Lohbihler
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.serotonin.mango.rt.publish.httpJsonCtcSender;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.db.KeyValuePair;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.event.AlarmLevels;
import com.serotonin.mango.rt.event.type.EventType;
import com.serotonin.mango.rt.event.type.PublisherEventType;
import com.serotonin.mango.rt.publish.PublishQueue;
import com.serotonin.mango.rt.publish.PublishQueueEntry;
import com.serotonin.mango.rt.publish.PublishedPointRT;
import com.serotonin.mango.rt.publish.PublisherRT;
import com.serotonin.mango.rt.publish.SendThread;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.publish.httpJsonCtcHeartbeatSender.HttpJsonCtcHeartbeatPointVO;
import com.serotonin.mango.vo.publish.httpJsonCtcSender.HttpJsonCtcPointVO;
import com.serotonin.mango.vo.publish.httpJsonCtcSender.HttpJsonCtcSenderVO;
import com.serotonin.mango.web.servlet.HttpDataSourceServlet;
import com.serotonin.util.StringUtils;
import com.serotonin.web.http.HttpUtils;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 * @author Matthew Lohbihler
 */
public class HttpJsonCtcSenderRT extends PublisherRT<HttpJsonCtcPointVO> {
    public static final String USER_AGENT = "Bolomi M2M HTTP JSON Sender publisher";
    private static final int MAX_FAILURES = 5;

    public static final int SEND_EXCEPTION_EVENT = 11;
    public static final int RESULT_WARNINGS_EVENT = 12;

    final EventType sendExceptionEventType = new PublisherEventType(getId(), SEND_EXCEPTION_EVENT);
    final EventType resultWarningsEventType = new PublisherEventType(getId(), RESULT_WARNINGS_EVENT);
    HashMap<Integer,String> map=new HashMap<Integer,String>();
    final HttpJsonCtcSenderVO vo;

	  public HttpJsonCtcSenderRT(HttpJsonCtcSenderVO vo) {
	      super(vo);
	      this.vo = vo;
	  }

    //
    // /
    // / Lifecycle
    // /
    //
    @Override
    public void initialize() {
        super.initialize(new HttpSendThread());
    }

    PublishQueue<HttpJsonCtcPointVO> getPublishQueue() {
        return queue;
    }

    class HttpSendThread extends SendThread {
        private int failureCount = 0;
        private LocalizableMessage failureMessage;

        HttpSendThread() {
            super("HttpSenderRT.SendThread");
        }

        @Override
        protected void runImpl() {
            int max;
            if (vo.isUsePost())
                max = 1000000;
            else
                max = 10;
           
            while (isRunning()) {
                List<PublishQueueEntry<HttpJsonCtcPointVO>> list = getPublishQueue().get(max);
              
                if (list != null) {
                    if (send(list)) {
                        for (PublishQueueEntry<HttpJsonCtcPointVO> e : list)
                            getPublishQueue().remove(e);
                    }
                    else {
                    	if(!vo.isSendFreShot()){                   		
                    	//hole ? delete
                    		queue.clear();                    	
                    	}
                        // The send failed, so take a break so as not to over exert ourselves.
                        try {
                            Thread.sleep(5000);
                        }
                        catch (InterruptedException e1) {
                            // no op
                        }
                    }
                }
                else
                    waitImpl(10000);
            }
        }

        @SuppressWarnings("synthetic-access")
        private boolean send(List<PublishQueueEntry<HttpJsonCtcPointVO>> list) {
            // Prepare the message
            NameValuePair[] params = null;
            // String strJson=tojson(vo.getStaticParameters(), list);
            if(vo.isSendFreShot())
            {
            	switch(vo.getSendType())
            	{
	            	case 1:
					try {
						Thread.sleep(vo.getSendValue()*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            		break;
	            	case 2:
	            		try {
							Thread.sleep(vo.getSendValue()*1000*60);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		break;
	            	case 3:
	            		try {
							Thread.sleep(vo.getSendValue()*1000*60*60);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		break;
	            	case 8:
	            		try {
							Thread.sleep(vo.getSendValue());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		break;
            		
            	}
            	params=createHistoryNVPs(vo.getStaticParameters(), list);
            }else
            {
            	params=createRealTimeNVPs(vo.getStaticParameters(), list);
            }
            HttpMethodBase method;
            if (vo.isUsePost()) {
                PostMethod post = new PostMethod(vo.getUrl());
                //  StringRequestEntity  strRequestE=new StringRequestEntity (strJson);
                //  post.setRequestEntity(strRequestE);
                post.addParameters(params);		
                method = post;               
              
            }
            else {
                GetMethod get = new GetMethod(vo.getUrl());
                get.setQueryString(params);
                method = get;
            }

            // Add a recognizable header
            method.addRequestHeader("User-Agent", USER_AGENT);

            // Add the user-defined headers.
            for (KeyValuePair kvp : vo.getStaticHeaders())
                method.addRequestHeader(kvp.getKey(), kvp.getValue());

            // Send the request. Set message non-null if there is a failure.
            LocalizableMessage message = null;
            try {
                int code = Common.getHttpClient().executeMethod(method);
                if (code== HttpStatus.SC_OK||code==HttpStatus.SC_ACCEPTED ||code==HttpStatus.SC_MULTI_STATUS ||code==HttpStatus.SC_NO_CONTENT||code==HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION||code==HttpStatus.SC_PARTIAL_CONTENT) {
                    if (vo.isRaiseResultWarning()) {
                        String result = HttpUtils.readResponseBody(method, 1024);
                        if (!StringUtils.isEmpty(result))
                            Common.ctx.getEventManager().raiseEvent(resultWarningsEventType,
                                    System.currentTimeMillis(), false, AlarmLevels.INFORMATION,
                                    new LocalizableMessage("common.default", result), createEventContext());
                    }
                }
                else
                    message = new LocalizableMessage("event.publish.invalidResponse", code);
            }
            catch (Exception ex) {
                message = new LocalizableMessage("common.default", ex.getMessage());
            }
            finally {
                method.releaseConnection();
            }

            // Check for failure.
            if (message != null) {
                failureCount++;
                if (failureMessage == null)
                    failureMessage = message;

                if (failureCount == MAX_FAILURES + 1)
                    Common.ctx.getEventManager().raiseEvent(sendExceptionEventType, System.currentTimeMillis(), true,
                            AlarmLevels.URGENT, failureMessage, createEventContext());

                return false;
            }

            if (failureCount > 0) {
                if (failureCount > MAX_FAILURES)
                    Common.ctx.getEventManager().returnToNormal(sendExceptionEventType, System.currentTimeMillis());

                failureCount = 0;
                failureMessage = null;
            }
            return true;
        }
    }

    NameValuePair[] createHistoryNVPs(List<KeyValuePair> staticParameters, List<PublishQueueEntry<HttpJsonCtcPointVO>> list) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Date date = new Date();
		long strNowDate =Long.parseLong(String.valueOf(date.getTime()));
        String staticAccountAndPwd="{\"Ts\":"+"\""+strNowDate+"\",";
        String pointvalueStr="\"Points\":[";
        for (KeyValuePair kvp : staticParameters)
        {
        	 staticAccountAndPwd=staticAccountAndPwd+"\""+kvp.getKey()+"\":\""+kvp.getValue()+"\",";
        			 
        }       
        for(PublishedPointRT<HttpJsonCtcPointVO> e:pointRTs)
        {
        	pointvalueStr=pointvalueStr+"{\"code\":\""+e.getVo().getParameterName()+"\",\"value\":\"";
        	 for (PublishQueueEntry<HttpJsonCtcPointVO> value : list) 
	      	   {   
        		 if(value.getVo().getDataPointId()==e.getVo().getDataPointId())
	      		   {
	      			   pointvalueStr=pointvalueStr+(strNowDate-value.getPvt().getTime())+","+value.getPvt().getValue()+"|";
	      		   }
	      	   }
        	 pointvalueStr=pointvalueStr+"\"},";        	
        }        
         pointvalueStr = pointvalueStr.substring(0,pointvalueStr.length()-1)+"]}";
         String sumStr=staticAccountAndPwd+pointvalueStr;
         System.out.println(sumStr);
        nvps.add(new NameValuePair("data", sumStr));    
        return nvps.toArray(new NameValuePair[nvps.size()]);
    }
    
    NameValuePair[] createRealTimeNVPs(List<KeyValuePair> staticParameters, List<PublishQueueEntry<HttpJsonCtcPointVO>> list) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        String staticAccountAndPwd="{";
        String pointvalueStr="[";
        for (KeyValuePair kvp : staticParameters)
        {
        	 staticAccountAndPwd=staticAccountAndPwd+"\""+kvp.getKey()+"\":\""+kvp.getValue()+"\",";
        			 
        }       
  
        for (PublishQueueEntry<HttpJsonCtcPointVO> e : list) {
        	HttpJsonCtcPointVO pvo = e.getVo();
            PointValueTime pvt = e.getPvt();
            String tstime="";
    		if (pvo.isIncludeTimestamp()) {

                switch (vo.getDateFormat()) {
                case HttpJsonCtcSenderVO.DATE_FORMAT_BASIC:
                	tstime= HttpDataSourceServlet.BASIC_SDF_CACHE.getObject().format(new Date(pvt.getTime()));
                    break;
                case HttpJsonCtcSenderVO.DATE_FORMAT_TZ:
                	tstime= HttpDataSourceServlet.TZ_SDF_CACHE.getObject().format(new Date(pvt.getTime()));
                    break;
                case HttpJsonCtcSenderVO.DATE_FORMAT_UTC:
                	tstime= Long.toString(pvt.getTime());
                    break;
                default:
                    throw new ShouldNeverHappenException("Unknown date format type: " + vo.getDateFormat());
                }
            }
            String value = "";
            if(pvt.getValue().toString().equals("false"))
            {
            	value="0";
            }
            if(pvt.getValue().toString().equals("true"))
            {
            	value="1";
            }
             if(value!="")
            {
            	  pointvalueStr=pointvalueStr+"{\"code\":\""+pvo.getParameterName()+"\",\"value\":\""+value+"\"},";
                  
            }else{
            pointvalueStr=pointvalueStr+"{\"code\":\""+pvo.getParameterName()+"\",\"value\":\""+pvt.getValue().toString()+"\"},";
        
            }              
        }    
        pointvalueStr = pointvalueStr.substring(0,pointvalueStr.length()-1)+"]";     
        String sumStr=pointvalueStr;
        nvps.add(new NameValuePair("data", sumStr));
        System.out.println(sumStr);
        return nvps.toArray(new NameValuePair[nvps.size()]);
    }

    //TODO code dont't delete

   /* private String tojson(List<KeyValuePair> staticParameters, List<PublishQueueEntry<HttpJsonCtcPointVO>> list)
    {
    	 
         String staticAccountAndPwd="{";
         String pointvalueStr="[";
         for (KeyValuePair kvp : staticParameters)
         {
         	 staticAccountAndPwd=staticAccountAndPwd+"\""+kvp.getKey()+"\":\""+kvp.getValue()+"\",";
         			 
         }       
         for (KeyValuePair kvp : staticParameters)
             nvps.add(new NameValuePair(kvp.getKey(), kvp.getValue()));

         for (PublishQueueEntry<HttpJsonCtcPointVO> e : list) {
         	HttpJsonCtcPointVO pvo = e.getVo();
             PointValueTime pvt = e.getPvt();
             String tstime="";
     		if (pvo.isIncludeTimestamp()) {

                 switch (vo.getDateFormat()) {
                 case HttpJsonCtcSenderVO.DATE_FORMAT_BASIC:
                 	tstime= HttpDataSourceServlet.BASIC_SDF_CACHE.getObject().format(new Date(pvt.getTime()));
                     break;
                 case HttpJsonCtcSenderVO.DATE_FORMAT_TZ:
                 	tstime= HttpDataSourceServlet.TZ_SDF_CACHE.getObject().format(new Date(pvt.getTime()));
                     break;
                 case HttpJsonCtcSenderVO.DATE_FORMAT_UTC:
                 	tstime= Long.toString(pvt.getTime());
                     break;
                 default:
                     throw new ShouldNeverHappenException("Unknown date format type: " + vo.getDateFormat());
                 }
             }
             String value = "";
             if(pvt.getValue().toString().equals("false"))
             {
             	value="0";
             }
             if(pvt.getValue().toString().equals("true"))
             {
             	value="1";
             }
              if(value!="")
             {
             	  pointvalueStr=pointvalueStr+"{\"code\":\""+pvo.getParameterName()+"\",\"value\":\""+value+"\",\"ts\":\""+pvt.getTime()+"\"},";
                   
             }else{
             pointvalueStr=pointvalueStr+"{\"code\":\""+pvo.getParameterName()+"\",\"value\":\""+pvt.getValue().toString()+"\",\"ts\":\""+pvt.getTime()+"\"},";
         
             }              
         }
 	    staticAccountAndPwd=staticAccountAndPwd+"\"data\":";	       
         pointvalueStr = pointvalueStr.substring(0,pointvalueStr.length()-1)+"]}";     
         String sumStr=staticAccountAndPwd+pointvalueStr;
       
         return sumStr;
    	
    }*/
}
