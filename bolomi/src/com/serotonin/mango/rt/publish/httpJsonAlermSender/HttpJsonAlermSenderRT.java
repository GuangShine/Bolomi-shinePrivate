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
package com.serotonin.mango.rt.publish.httpJsonAlermSender;

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
import com.serotonin.mango.db.dao.EventDao;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.event.AlarmLevels;
import com.serotonin.mango.rt.event.EventInstance;
import com.serotonin.mango.rt.event.type.EventType;
import com.serotonin.mango.rt.event.type.PublisherEventType;
import com.serotonin.mango.rt.publish.PublishQueue;
import com.serotonin.mango.rt.publish.PublishQueueEntry;
import com.serotonin.mango.rt.publish.PublisherRT;
import com.serotonin.mango.rt.publish.SendThread;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.publish.httpJsonAlermSender.HttpJsonAlermPointVO;
import com.serotonin.mango.vo.publish.httpJsonAlermSender.HttpJsonAlermSenderVO;
import com.serotonin.mango.web.servlet.HttpDataSourceServlet;
import com.serotonin.util.StringUtils;
import com.serotonin.web.http.HttpUtils;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 * @author Matthew Lohbihler
 */
public class HttpJsonAlermSenderRT extends PublisherRT<HttpJsonAlermPointVO> {
    public static final String USER_AGENT = "Bolomi M2M HTTP JSON Alerm Sender publisher";
    private static final int MAX_FAILURES = 5;
   
    public static final int SEND_EXCEPTION_EVENT = 11;
    public static final int RESULT_WARNINGS_EVENT = 12;

    final EventType sendExceptionEventType = new PublisherEventType(getId(), SEND_EXCEPTION_EVENT);
    final EventType resultWarningsEventType = new PublisherEventType(getId(), RESULT_WARNINGS_EVENT);

	EventDao edao=new EventDao();
    final HttpJsonAlermSenderVO vo;

	  public HttpJsonAlermSenderRT(HttpJsonAlermSenderVO vo) {
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

    PublishQueue<HttpJsonAlermPointVO> getPublishQueue() {
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
            	 EventDao eventDao = new EventDao();
            	List<EventInstance> results=eventDao.getPendingEventsByActiveTs(1);
            	if(results!=null)
            	{
            		/*if(!send(results))*/
            		if(!saveEventInfo(results))
            		{
            			   // The send failed, so take a break so as not to over exert ourselves.
                        try {
                            Thread.sleep(5000);
                        }
                        catch (InterruptedException e1) {
                            // no op
                        }            			
            		}         			
            	}           	
            }
        }
        private boolean saveEventInfo(List<EventInstance> list)
        {
        	if(list!=null)
        	{
        		for(int i=0;i<list.size();i++)
        			edao.InsertEventInfo(list.get(i));
        		return true;
        		
        	}else
        	{
        		return false;
        	}
        	
        }

        @SuppressWarnings("synthetic-access")
        private boolean send(List<EventInstance> list) {
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
           /* method.addRequestHeader("Content-Type", headerValue);*/

            // Add the user-defined headers.
            for (KeyValuePair kvp : vo.getStaticHeaders())
                method.addRequestHeader(kvp.getKey(), kvp.getValue());

            // Send the request. Set message non-null if there is a failure.
            LocalizableMessage message = null;
            try {
                int code = Common.getHttpClient().executeMethod(method);
                if (code == HttpStatus.SC_OK) {
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

    NameValuePair[] createNVPs(List<KeyValuePair> staticParameters,List<EventInstance> list) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        String staticAccountAndPwd="";
        String alermvalueStr="[";
        for (KeyValuePair kvp : staticParameters)
        {
        	 staticAccountAndPwd=staticAccountAndPwd+"{\"Account\":\""+kvp.getKey()+"\",\"Password\":\""+kvp.getValue()+"\",\"data\":";
        			 
        }
        /*staticAccountAndPwd = staticAccountAndPwd.substring(0,staticAccountAndPwd.length()-1)+"]";
        nvps.add(new NameValuePair("AcountOtherStr",staticAccountAndPwd));
        System.out.print(staticAccountAndPwd);*/
       /* for (KeyValuePair kvp : staticParameters)
            nvps.add(new NameValuePair(kvp.getKey(), kvp.getValue()));*/
  /*      DataPointVO dpvo=new DataPointVO();
        DataPointDao dpdao=new DataPointDao();
        LocalizableMessage message = null;*/
        for (EventInstance e : list) {
        	/*message=new LocalizableMessage(e.getMessage().getKey(),e.getMessage().getArgs());*/
        	alermvalueStr=alermvalueStr+"{\"id:\":\""+e.getId()+"\",\"message\":\""+Common.getMessage(e.getMessage().getKey(), e.getMessage().getArgs())+"\",\"ts\":\""+e.getActiveTimestamp()+"\"},";
        }
        
        	
        alermvalueStr = alermvalueStr.substring(0,alermvalueStr.length()-1)+"]";      	


        
        String sumStr=staticAccountAndPwd+alermvalueStr;
        
//        pointvalueStr = pointvalueStr.substring(0,pointvalueStr.length()-1);
//        sumStr=sumStr+"]";
        nvps.add(new NameValuePair("data", sumStr));
        Date d=new Date();
        System.out.println(d);
        System.out.println(sumStr);
       
        return nvps.toArray(new NameValuePair[nvps.size()]);
    }
}

