<%--
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
    along with this program.  If not, see http://www.gnu.org/licenses/.
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.Common"%>

<tag:page dwr="DataSourceEditDwr" onload="init">
  
  <table class="borderDiv marB" cellpadding="0" cellspacing="0" id="alarmsTable" style="display:none;"><tr><td>
     <div  style="overflow:scroll;heigth:300px;width:1000px;">
    <table width="100%">
      <tr>
        <td class="smallTitle"><fmt:message key="dsEdit.currentAlarms"/></td>
        <td align="right"><tag:img png="control_repeat_blue" title="common.refresh" onclick="getAlarms()"/></td>
      </tr>
    </table>
    <table style="heigth:900px;">
      <tr id="noAlarmsMsg"><td><b><fmt:message key="dsEdit.noAlarms"/></b></td></tr>
      <tbody id="alarmsList"></tbody>
    </table>  </div>
  </td></tr></table>
  <c:choose>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.VIRTUAL']}">
      <jsp:include page="dataSourceEdit/editVirtual.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.MODBUS_SERIAL']}">
      <jsp:include page="dataSourceEdit/editModbus.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.MODBUS_IP']}">
      <jsp:include page="dataSourceEdit/editModbus.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.SPINWAVE']}">
      <jsp:include page="dataSourceEdit/editSpinwave.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.SNMP']}">
      <jsp:include page="dataSourceEdit/editSnmp.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.SQL']}">
      <jsp:include page="dataSourceEdit/editSql.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.HTTP_RECEIVER']}">
      <jsp:include page="dataSourceEdit/editHttpReceiver.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.ONE_WIRE']}">
      <jsp:include page="dataSourceEdit/editOneWire.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.META']}">
      <jsp:include page="dataSourceEdit/editMeta.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.BACNET']}">
      <jsp:include page="dataSourceEdit/editBacnetIp.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.HTTP_RETRIEVER']}">
      <jsp:include page="dataSourceEdit/editHttpRetriever.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.POP3']}">
      <jsp:include page="dataSourceEdit/editPop3.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.NMEA']}">
      <jsp:include page="dataSourceEdit/editNmea.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.GALIL']}">
      <jsp:include page="dataSourceEdit/editGalil.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.HTTP_IMAGE']}">
      <jsp:include page="dataSourceEdit/editHttpImage.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.EBI25']}">
      <jsp:include page="dataSourceEdit/editEBI25.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.VMSTAT']}">
      <jsp:include page="dataSourceEdit/editVMStat.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.VICONICS']}">
      <jsp:include page="dataSourceEdit/editViconics.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.M_BUS']}">
      <jsp:include page="dataSourceEdit/editMBus.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.OPEN_V_4_J']}">
      <jsp:include page="dataSourceEdit/editOpenV4J.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.DNP3_IP']}">
      <jsp:include page="dataSourceEdit/editDnp3.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.DNP3_SERIAL']}">
      <jsp:include page="dataSourceEdit/editDnp3.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.PACHUBE']}">
      <jsp:include page="dataSourceEdit/editPachube.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.PERSISTENT']}">
      <jsp:include page="dataSourceEdit/editPersistent.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.OPC']}">
      <jsp:include page="dataSourceEdit/editOpc.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.JMX']}">
      <jsp:include page="dataSourceEdit/editJmx.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.INTERNAL']}">
      <jsp:include page="dataSourceEdit/editInternal.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.SERIAL']}">
      <jsp:include page="dataSourceEdit/editSerial.jsp"/>
    </c:when>
  </c:choose>
   <%--  id:${dataSource.type.id}
    static:${applicationScope['constants.DataSourceVO.Types.SERIAL']},${ applicationScope['constants.DataSourceVO.Types.INTERNAL']}
 --%>  <script type="text/javascript">
    var currentPoint;
    var dp_key_lklist;
    var pointListColumnFunctions = new Array();
    var pointListOptions;
	var nowpage;
	var page1_InfoCount;
	var page1_NowPage;
	var page1_PageSize;
	var page1_PageCount;
	var page2_InfoCount;
	var page2_NowPage;
	var page2_PageSize;
	var page2_PageCount;
	
	
	 function goSelect(selectPageSize)  
     {  
         var dxsize = page2_InfoCount;  
         if(selectPageSize>dxsize)  
         {  
             selectPageSize = page2_InfoCount;  
         }  
         DataSourceEditDwr.selectInfo(selectPageSize,initCB);  
     }  
	 function goPage()  
     {  
         var nowPage = document.getElementById("initValue").value;  
         if(nowPage=="")  
         {  
             document.getElementById("message").innerHTML = "Sorry, the page number must be completed";  
         }else  
         {  
        	 DataSourceEditDwr.goInfo(nowPage,page2_PageCount,page2_PageSize,initCB);  
             document.getElementById("message").innerHTML = "";  
         }  
     }  
	
	  function goupPage()  
      {  
            
		  nowPage = page2_NowPage;  
          nowPage--;  
          if(nowPage<=0)  
          {  
              document.getElementById("message").innerHTML = "Sorry,Is the first page";  
          }else  
          {  
        	  DataSourceEditDwr.upInfo(nowPage,page1_PageCount,page1_PageSize,initCB);
              /* test.next(nowPage,page1_PageCount,page1_PageSize,hallnpsot);  */ 
              /* document.getElementById("message").innerHTML = "";  
              test.goup(nowPage,mmmmmmm.pageCount,mmmmmmm.pageSize,hallnpsot);   */
              document.getElementById("message").innerHTML = "";  
          }  
      }  
	  function nextPage()  
      {  
          nowPage = page2_NowPage;  
          nowPage++;  
          if(nowPage > page2_PageCount)  
          {  
              document.getElementById("message").innerHTML = "Sorry,Is the last page";  
          }else  
          {  
          	DataSourceEditDwr.nextInfo(nowPage,page1_PageCount,page1_PageSize,initCB);
              /* test.next(nowPage,page1_PageCount,page1_PageSize,hallnpsot);  */ 
              document.getElementById("message").innerHTML = "";  
          }  
      }  
	
    function hallnpsot(InfoCount,NowPage,PageSize,PageCount)  
    {  
    	page1_InfoCount=InfoCount;
    	page1_NowPage=NowPage;
    	page1_PageSize=PageSize;
    	page1_PageCount=PageCount;
    	page2_InfoCount=InfoCount;
    	page2_NowPage=NowPage;
    	page2_PageSize=PageSize;
    	page2_PageCount=PageCount;
        //alert(infoList);  
        /* page1 = infoList[0];  
        page1 = infoList[0];  */ 
        $(selectElement).options[0].value =page1_InfoCount;  
        document.getElementById("msg").innerHTML = "<font color='red'>Sum:"+page2_InfoCount+"     "+page2_PageSize+"T/Page"+"     "+"Sum:"+page2_PageCount+"      Now:"+page2_NowPage+"Page</font>";  
       /*  //获取一个表格  
        var table = document.getElementById("tableInfo");  
        //清楚表中的数据  
        while(table.rows.length>0)  
        {  
            table.deleteRow(0);  
        }  
          
        //给表格添加头部  
        var newTh = table.insertRow();  
          
        newTh.style.backgroundColor="#C8ECEC";  
        newTh.align="center";  
          
        //表头TD  
        var newTh1 = newTh.insertCell();  
        var newTh2 = newTh.insertCell();  
        var newTh3 = newTh.insertCell();  
        var newTh4 = newTh.insertCell();  
        var newTh5 = newTh.insertCell();  
        var newTh6 = newTh.insertCell();  
        var newTh7 = newTh.insertCell();  
        newTh1.style.width="100";  
        newTh2.style.width="130";  
        newTh3.style.width="190";  
        newTh4.style.width="190";  
        newTh5.style.width="190";  
        newTh6.style.width="60";  
        newTh7.style.width="60";  
        newTh.className = "style2";  
          
        //添加表头内容  
        newTh1.innerHTML = "部门编号";  
        newTh2.innerHTML = "部门名称";  
        newTh3.innerHTML = "部门邮箱";  
        newTh4.innerHTML = "部门电话";  
        newTh5.innerHTML = "部门地址";  
        newTh6.innerHTML = "编辑";  
        newTh7.innerHTML = "删除";  
          
          
        for(var i=0;i<deptInfo.length;i++)  
        {  
            //获取传递过来的数据  
            var id = deptInfo[i].id;  
            var name = deptInfo[i].name;  
            var email = deptInfo[i].email;  
            var tel = deptInfo[i].tel;  
            var address = deptInfo[i].address;  
            //添加一行  
            var newTr = table.insertRow();  
            newTr.style.backgroundColor="#E6E6E6";  
            newTr.align="center";  
            newTr.className = "style3";  
              
            //添加5列  
            var newTd1 = newTr.insertCell();  
            var newTd2 = newTr.insertCell();  
            var newTd3 = newTr.insertCell();  
            var newTd4 = newTr.insertCell();  
            var newTd5 = newTr.insertCell();  
            var newTd6 = newTr.insertCell();  
            var newTd7 = newTr.insertCell();  
            //给单元格添加数据  
            newTd1.innerHTML = id;  
            newTd2.innerHTML = name;  
            newTd3.innerHTML = email;  
            newTd4.innerHTML = tel;  
            newTd5.innerHTML = address;  
            newTd6.innerHTML = "<a href='javascript:void(0)' onclick=''>编辑</a>";  
            newTd7.innerHTML = "<a href='javascript:void(0)' onclick=''>删除</a>";   */
        }  
    
    function init() {
        var pointListColumnHeaders = new Array();
        
        pointListColumnHeaders.push("<fmt:message key="dsEdit.name"/>");
        pointListColumnFunctions.push(function(p) { return "<b>"+ p.name +"</b>"; });
        
        pointListColumnHeaders.push("<fmt:message key="dsEdit.pointDataType"/>");
        pointListColumnFunctions.push(function(p) { return p.dataTypeMessage; });
        
        pointListColumnHeaders.push("<fmt:message key="dsEdit.status"/>");
        pointListColumnFunctions.push(function(p) {
                var id = "toggleImg"+ p.id;
                var onclick = "togglePoint("+ p.id +")";
                if (p.enabled)
                    return writeImage(id, null, "brick_go", "<fmt:message key="common.enabledToggle"/>", onclick);
                return writeImage(id, null, "brick_stop", "<fmt:message key="common.disabledToggle"/>", onclick);
        });
        
        if (typeof appendPointListColumnFunctions == 'function')
            appendPointListColumnFunctions(pointListColumnHeaders, pointListColumnFunctions);
        
        pointListColumnHeaders.push("");
        pointListColumnFunctions.push(function(p) {
                return writeImage("editImg"+ p.id, null, "icon_comp_edit", "<fmt:message key="common.edit"/>", "editPoint("+ p.id +")");
        });
         pointListColumnHeaders.push("");
        pointListColumnFunctions.push(function(p) {
            return writeImage("copyImg"+ p.id, null, "icon_comp_add", "<fmt:message key="common.copy"/>", "copyPoint("+ p.id +")");
       
        });
        pointListColumnHeaders.push("");
        pointListColumnFunctions.push(function(p) {
            return writeImage("DelImg"+ p.id, null, "icon_comp_delete", "<fmt:message key="common.delete"/>", "listDeletePoint("+ p.id +")");
        });        
        var headers = $("pointListHeaders");
        var td;
        for (var i=0; i<pointListColumnHeaders.length; i++) {
            td = document.createElement("td");
            if (typeof(pointListColumnHeaders[i]) == "string")
                td.innerHTML = pointListColumnHeaders[i];
            else
                pointListColumnHeaders[i](td);
            headers.appendChild(td);
        }
        
        pointListOptions = {
                rowCreator: function(options) {
                    var tr = document.createElement("tr");
                    tr.mangoId = "p"+ options.rowData.id;
                    tr.className = "row"+ (options.rowIndex % 2 == 0 ? "" : "Alt");
                    return tr;
                },
                cellCreator: function(options) {
                    var td = document.createElement("td");
                    if (options.cellNum == 2)
                        td.align = "center";
                    return td;
                }
        };
        
        var dsStatus = $("dsStatusImg");
        setDataSourceStatusImg(${dataSource.enabled}, dsStatus);
        hide(dsStatus);
        
        if (typeof initImpl == 'function') initImpl();
        
        DataSourceEditDwr.editInit(initCB);
       /*  DataSourceEditDwr.getPages(getpagesCB); */
        showMessage("dataSourceMessage");
        showMessage("pointMessage");
    }
  /*   function getpagesCB(pages)
    {
    	var pagezzz=pages;
    } */
    
    function initCB(response) {
    	if(response.data.errorMes==null){
        writePointList(response.data.points);
        writeAlarms(response.data.alarms);
        Changeck();
        hallnpsot(response.data.InfoCount,response.data.NowPage,response.data.PageSize,response.data.PageCount);
        <c:if test="${!empty param.pid}">
          // Default the selection if the parameter was provided.
          editPoint(${param.pid});
        </c:if>}
    }
    function Changeck()
    {
    	var listck=$get("keytype_datasource");  
    	
    }
  /*   function getPointsCount(p)
    {
    	DWREngine.setAsync(false);
    	 DataSourceEditDwr.getPointsCount(p[1].dataSourceId,getPointsCountCB);
    	 DWREngine.setAsync(true);
    }
    function getPointsCountCB(points)
    {
    	countNum= points;    	
    } */
    function saveDataSource() {
        startImageFader("dsSaveImg", true);
        hideContextualMessages($("dataSourceProperties"));
        saveDataSourceImpl();
    }
    
    function saveDataSourceCB(response) {
        stopImageFader("dsSaveImg");
        if (response.hasMessages)
            showDwrMessages(response.messages, "dataSourceGenericMessages");
        else {
            showMessage("dataSourceMessage", "<fmt:message key="dsEdit.saved"/>");
            DataSourceEditDwr.getPoints(writePointList);
        }
        getAlarms();
    }
    
    function toggleDataSource() {
        if (typeof toggleDataSourceImpl == 'function') toggleDataSourceImpl();
        
        var imgNode = $("dsStatusImg");
        if (!hasImageFader(imgNode)) {
            DataSourceEditDwr.toggleEditDataSource(toggleDataSourceCB);
            startImageFader(imgNode);
        }
    }
    
    function toggleDataSourceCB(result) {
        var imgNode = $("dsStatusImg");
        stopImageFader(imgNode);
        setDataSourceStatusImg(result.enabled, imgNode);
        getAlarms();
    }
    
    function togglePoint(pointId) {
        DataSourceEditDwr.togglePoint(pointId, togglePointCB);
        startImageFader("toggleImg"+ pointId, true);
    }
    
    function togglePointCB(response) {
        stopImageFader("toggleImg"+ response.data.id);
        writePointList(response.data.points);
    }
    
    function deletePoint() {
        if (confirm("<fmt:message key="dsEdit.deleteConfirm"/>")) {
            DataSourceEditDwr.deletePoint(currentPoint.id, deletePointCB);
            startImageFader("pointDeleteImg", true);
        }
    }
    function listDeletePoint(id) {
        if (confirm("<fmt:message key="dsEdit.deleteConfirm"/>")) {
            DataSourceEditDwr.deletePoint(id, deletePointCB);
            startImageFader("pointDeleteImg", true);
        }
    }
    
    function deletePointCB(points) {
        stopImageFader("pointDeleteImg");
        hide("pointDetails");
        currentPoint = null;
        writePointList(points);
    }
    
    function writePointList(points) {
        if (typeof writePointListImpl == 'function') writePointListImpl(points);
        
        if (!points)
            return;
        show("pointProperties");
        show("alarmsTable");
        show("dsStatusImg");
    
        if (currentPoint)
            stopImageFader("editImg"+ currentPoint.id);
        dwr.util.removeAllRows("pointsList");
        dwr.util.addRows("pointsList", points, pointListColumnFunctions, pointListOptions);
    }
    
    function addPoint(ref) {
        if (!dojo.html.isShowing("pointProperties")) {
            alert("<fmt:message key="dsEdit.saveWarning"/>");
            return;
        }
        
        if (currentPoint)
            stopImageFader("editImg"+ currentPoint.id);
        
        startImageFader("editImg"+ <c:out value="<%= Common.NEW_ID %>"/>);
        hideContextualMessages("pointProperties");
        
        addPointImpl(ref);
    }
    
    //copypoint
    
    function copyPoint(pointId) {
    	/* startImageFader("copyImg"+ pointId);
    	  DataSourceListDwr.copyDataSource(fromDataSourceId, function(toDataSourceId) {
              window.location = "data_source_edit.shtm?dsid="+ toDataSourceId;
          });
    	DataSourceListDwr.copyDataPoint(pointId,d); */
    	/*   if (currentPoint){
              	stopImageFader("copyImg"+ currentPoint.id);
    	 		/* stopImageFader("editImg"+ currentPoint.id); } */
    	DataSourceEditDwr.copyGetPoint(pointId, copyPointCB);
          hideContextualMessages("pointProperties");
    }
    function copyPointCB(point) {
        currentPoint = point;
        display("pointDeleteImg", point.id != <c:out value="<%= Common.NEW_ID %>"/>);
        var locator = currentPoint.pointLocator;
        
        $set("name","copy of "+ currentPoint.name);
        $set("xid", currentPoint.xid);
        var cancel;
        if (typeof editPointCBImpl == 'function') cancel = editPointCBImpl(locator);
        if (!cancel) {
          /*   startImageFader("copyImg"+ point.id); */
            show("pointDetails");
        }
    }
    
    
   
    function editProPoint(id)
    {
    	document.getElementById('pointpro').src='data_point_edit.shtm?dpid='+id;    	
    }
    function editPoint(pointId) {
        /* if (currentPoint){
        	stopImageFader("copyImg"+ currentPoint.id );
 		stopImageFader("editImg"+ currentPoint.id);} */
        DataSourceEditDwr.getPoint(pointId, editPointCB);
        hideContextualMessages("pointProperties");
    }
    function getdp_key_list(str)
    {
    	dp_key_lklist=str;
    }
    function editPointCB(point) {
        currentPoint = point;
        display("pointDeleteImg", point.id != <c:out value="<%= Common.NEW_ID %>"/>);
         DWREngine.setAsync(false); 
        var locator = currentPoint.pointLocator;
        
        DataSourceEditDwr.getkey_lk(currentPoint.xid, getdp_key_list);
        $set("name", currentPoint.name);
        $set("xid", currentPoint.xid);
        /* var src="images/off_1.png";
        alert(src.indexOf('t'));
        alert(src.indexOf('i'));
        alert(src.indexOf('g'));
       */
        //var cblist=$get("keytype_point");
      
        var cancel;
        if (typeof editPointCBImpl == 'function') cancel = editPointCBImpl(locator);
        if (!cancel) {
           /*  startImageFader("editImg"+ point.id); */
            show("pointDetails");
        }
        var cblist=document.getElementsByName("keytype_point");
        
        for(var i=0;i<cblist.length;i++)
        	{
        	 cblist[i].checked=false;
        	/*   alert(dp_key_lklist.toString().indexOf(cblist[i].value))
        	} */
        		 if(dp_key_lklist.toString().indexOf(cblist[i].value)>=0)
        			{
        			 cblist[i].checked=true;
        			}
        	
        	}
        DWREngine.setAsync(true); 
        
    }
    
    function cancelEditPoint() {
        if (currentPoint) {
            stopImageFader("editImg"+ currentPoint.id);
            currentPoint = null;
            hide("pointDetails");
        }
    }
    
    function savePoint() {
        startImageFader("pointSaveImg", true);
        hideContextualMessages("pointProperties");
        var locator = currentPoint.pointLocator;
        
        // Prevents DWR warnings
        delete locator.configurationDescription;
        delete locator.dataTypeMessage;
        
        savePointImpl(locator);
    }
    
    function savePointCB(response) {
        stopImageFader("pointSaveImg");
        if (response.hasMessages)
            showDwrMessages(response.messages);
        else {
            writePointList(response.data.points);
            editPoint(response.data.id,null);
            showMessage("pointMessage", "<fmt:message key="dsEdit.pointSaved"/>");
        }
    }
    
    function getAlarms() {
        DataSourceEditDwr.getAlarms(writeAlarms);
    }
    
    function writeAlarms(alarms) {
        dwr.util.removeAllRows("alarmsList");
        if (alarms.length == 0) {
            show("noAlarmsMsg");
            hide("alarmsList");
        }
        else {
            hide("noAlarmsMsg");
            show("alarmsList");
            dwr.util.addRows("alarmsList", alarms, [
                    function(alarm) {
                        var div = document.createElement("div");
                        var img = document.createElement("img");
                        setAlarmLevelImg(alarm.alarmLevel, img);
                        div.appendChild(img);
                        
                        var span = document.createElement("span");
                        span.innerHTML = alarm.prettyActiveTimestamp +": "+ alarm.message;
                        div.appendChild(span);
                        
                        return div; 
                    }],
                    {
                        cellCreator: function(options) {
                            var td = document.createElement("td");
                            td.className = "formError";
                            return td;
                        }
                    });
        }
    }
    
    function alarmLevelChanged(eventId) {
        var alarmLevel = $get("alarmLevel"+ eventId);
        DataSourceEditDwr.updateEventAlarmLevel(eventId, alarmLevel);
        setAlarmLevelImg(alarmLevel, "alarmLevelImg"+ eventId);
    }
  </script>
 

   <div class="style1" id="message"></div>
            <table border="0" width="800">
				<tr align="left">
					<td align="left" id="msg" width="250">
						<br>

						<br>
					</td>
					<td width="150">
						<a href="javascript:void(0)" onclick="goupPage()">LastPage</a>
						&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="nextPage()">NextPage</a>
					</td>
					<td align="left" width="80">
						<input type="text" id="initValue" size="5" />
						<input type="button" id="gonum" value="GO" onclick="goPage()" />
					</td>
					<td>
						Count:
						<select id="selectElement" onchange="goSelect(this.value)">
							<option value="">
								ALL
							</option>
							<option value="1">
								1
							</option>
							<option value="3">
								3
							</option>
							<option value="5">
								5
							</option>
							<option value="10">
								10
							</option>
							<option value="20">
								20
							</option>
							<option value="50">
								50
							</option>
						</select>
					</td>
				</tr>
			</table>       
        
</tag:page>
