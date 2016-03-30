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
package com.serotonin.mango.rt.publish.httpJsonSender;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.db.KeyValuePair;
import com.serotonin.mango.Common;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.event.AlarmLevels;
import com.serotonin.mango.rt.event.type.EventType;
import com.serotonin.mango.rt.event.type.PublisherEventType;
import com.serotonin.mango.rt.publish.PublishQueue;
import com.serotonin.mango.rt.publish.PublishQueueEntry;
import com.serotonin.mango.rt.publish.PublisherRT;
import com.serotonin.mango.rt.publish.SendThread;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.publish.httpJsonSender.HttpJsonPointVO;
import com.serotonin.mango.vo.publish.httpJsonSender.HttpJsonSenderVO;
import com.serotonin.mango.web.servlet.HttpDataSourceServlet;
import com.serotonin.util.StringUtils;
import com.serotonin.web.http.HttpUtils;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 * @author Matthew Lohbihler
 */
public class HttpJsonSenderRT extends PublisherRT<HttpJsonPointVO> {
    public static final String USER_AGENT = "Bolomi M2M HTTP JSON Sender publisher";
    private static final int MAX_FAILURES = 5;

    public static final int SEND_EXCEPTION_EVENT = 11;
    public static final int RESULT_WARNINGS_EVENT = 12;

    final EventType sendExceptionEventType = new PublisherEventType(getId(), SEND_EXCEPTION_EVENT);
    final EventType resultWarningsEventType = new PublisherEventType(getId(), RESULT_WARNINGS_EVENT);

    final HttpJsonSenderVO vo;

	  public HttpJsonSenderRT(HttpJsonSenderVO vo) {
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

    PublishQueue<HttpJsonPointVO> getPublishQueue() {
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
                max = 100;
            else
                max = 10;
           
            while (isRunning()) {
                List<PublishQueueEntry<HttpJsonPointVO>> list = getPublishQueue().get(max);
/*                int num1=vo.getSnapshotSendPeriods()*1000;
                int num2=vo.getSnapshotSendPeriods()*1000*60;
                int num3=vo.getSnapshotSendPeriods()*1000*60*60;
                System.out.print(num1+"++++"+num2+"+++"+num3);*/
              
                if (list != null) {
                    if (send(list)) {
                        for (PublishQueueEntry<HttpJsonPointVO> e : list)
                            getPublishQueue().remove(e);
                    }
                    else {
                    	//hole ? delete
                    	 for (PublishQueueEntry<HttpJsonPointVO> e : list)
                             getPublishQueue().remove(e);
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
        private boolean send(List<PublishQueueEntry<HttpJsonPointVO>> list) {
            // Prepare the message
            NameValuePair[] params = createNVPs(vo.getStaticParameters(), list);

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
            		
            	}            	
            }
            HttpMethodBase method;
            if (vo.isUsePost()) {
                PostMethod post = new PostMethod(vo.getUrl());
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

    NameValuePair[] createNVPs(List<KeyValuePair> staticParameters, List<PublishQueueEntry<HttpJsonPointVO>> list) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        String staticAccountAndPwd="";
        String pointvalueStr="[";
        for (KeyValuePair kvp : staticParameters)
        {
        	 staticAccountAndPwd=staticAccountAndPwd+"{\"Account\":\""+kvp.getKey()+"\",\"Password\":\""+kvp.getValue()+"\",\"data\":";
        			 
        }
        /*staticAccountAndPwd = staticAccountAndPwd.substring(0,staticAccountAndPwd.length()-1)+"]";
        nvps.add(new NameValuePair("AcountOtherStr",staticAccountAndPwd));
        System.out.print(staticAccountAndPwd);*/
       /* for (KeyValuePair kvp : staticParameters)
            nvps.add(new NameValuePair(kvp.getKey(), kvp.getValue()));*/
        DataPointVO dpvo=new DataPointVO();
        DataPointDao dpdao=new DataPointDao();
        for (PublishQueueEntry<HttpJsonPointVO> e : list) {
        	HttpJsonPointVO pvo = e.getVo();
            PointValueTime pvt = e.getPvt();
            String tstime="";
    		if (pvo.isIncludeTimestamp()) {

                switch (vo.getDateFormat()) {
                case HttpJsonSenderVO.DATE_FORMAT_BASIC:
                	tstime= HttpDataSourceServlet.BASIC_SDF_CACHE.getObject().format(new Date(pvt.getTime()));
                    break;
                case HttpJsonSenderVO.DATE_FORMAT_TZ:
                	tstime= HttpDataSourceServlet.TZ_SDF_CACHE.getObject().format(new Date(pvt.getTime()));
                    break;
                case HttpJsonSenderVO.DATE_FORMAT_UTC:
                	tstime= Long.toString(pvt.getTime());
                    break;
                default:
                    throw new ShouldNeverHappenException("Unknown date format type: " + vo.getDateFormat());
                }
            }
            String value = "";
            String test =pvt.getValue().toString();
            if(pvt.getValue().toString().equals("false"))
            {
            	value="0";
            }
            if(pvt.getValue().toString().equals("true"))
            {
            	value="1";
            }
        /*  don't delete
         *    if(value!="")
            {
            	  pointvalueStr=pointvalueStr+"{\"code\":\""+pvo.getParameterName()+"\",\"value\":\""+value+"\",\"ts\":\""+tstime+"\"},";
                  
            }else{
            pointvalueStr=pointvalueStr+"{\"code\":\""+pvo.getParameterName()+"\",\"value\":\""+pvt.getValue().toString()+"\",\"ts\":\""+tstime+"\"},";
        
            }*/           
            
            dpvo=dpdao.getDataPoint(pvo.getDataPointId());
            if(value!="")
            {            	
            	  pointvalueStr=pointvalueStr+"{\"name\":\""+pvo.getParameterName()+"\",\"xid\":\""+dpvo.getXid()+"\",\"value\":\""+value+"\",\"ts\":\""+pvt.getTime()+"\"},";
                  
            }else{
            pointvalueStr=pointvalueStr+"{\"name\":\""+pvo.getParameterName()+"\",\"xid\":\""+dpvo.getXid()+"\",\"value\":\""+pvt.getValue().toString()+"\",\"ts\":\""+pvt.getTime()+"\"},";
        
            }
            
        }
/*        if(staticAccountAndPwd=="")
        {
        	staticAccountAndPwd=staticAccountAndPwd+"{\"data\":";
        }*/
        //pointvalueStr = pointvalueStr.substring(0,pointvalueStr.length()-1)+"]}";
        pointvalueStr = pointvalueStr.substring(0,pointvalueStr.length()-1)+"]";

        
        String sumStr=staticAccountAndPwd+pointvalueStr;
        
//        pointvalueStr = pointvalueStr.substring(0,pointvalueStr.length()-1);
//        sumStr=sumStr+"]";
        nvps.add(new NameValuePair("", sumStr));
        //nvps.add(new NameValuePair("data", sumStr));
        Date d=new Date();
        System.out.println(d);
        System.out.println(sumStr);
        return nvps.toArray(new NameValuePair[nvps.size()]);
    }
}
