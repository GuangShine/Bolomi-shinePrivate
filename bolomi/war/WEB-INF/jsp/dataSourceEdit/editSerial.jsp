<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.vo.dataSource.serialDatasource.SerialDataSourceVO"%>

<style>
.test-button {
	box-shadow:inset 0px 1px 0px 0px #dcecfb;
	background-color:#80b5ea;
	border:1px solid #53a8fc;
	color:#000000;
	font-size:12px;
	font-weight:bold;
	height:20px;
	line-height:17px;
	width:100px;
	text-align:center;
	text-shadow:1px 1px 0px #528ecc;
}
.test-button:hover {
	background-color:#70a5da;
}.test-button:active {
	position:relative;
	top:1px;
}
</style>

<script type="text/javascript">

    /* On Page Load */
	function initImpl() {
	    
	      logIOChanged();
	    
	      DataSourceEditDwr.getSafeTerminator(function(messageTerminator) {
			$set("messageTerminator", messageTerminator);
			if(${dataSource.useTerminator}) {
				dojo.byId("useTerminator").checked = "true";
			}
			toggleTerminator();
		  });
	}

	/**
	 * Save the DS
	 */
	function saveDataSourceImpl(){

		DataSourceEditDwr.saveSerialDataSource($get("dataSourceName"), $get("dataSourceXid"),
				$get("commPortId"),$get("baudRate"),$get("flowControlIn"),$get("flowControlOut"),$get("dataBits"), 
	              $get("stopBits"),$get("parity"),$get("readTimeout"),$get("useTerminator"),$get("messageTerminator"),
	              $get("messageRegex"),$get("pointIdentifierIndex"),$get("isHex"),$get("isLogIO"),$get("maxMessageSize"),
	              $get("ioLogFileSizeMBytes"), $get("maxHistoricalIOLogs"),saveDataSourceCB);

	}

	/**
	 * Add a Point
	 */
	  function addPointImpl() {
		  DataSourceEditDwr.getPoint(-1, function(point) {
			  editPointCB(point);
		  });
	  }
		
	  function editPointCBImpl(locator) {
		  $set("pointIdentifier",locator.pointIdentifier);
		  $set("valueRegex",locator.valueRegex);
		  $set("valueIndex",locator.valueIndex);
		  $set("dataTypeId",locator.dataTypeId);
	  }
	  
	  /**
	   * Save a Point
	   */
	  function savePointImpl(locator) {
		  delete locator.pointIdentifier;
		  delete locator.valueRegex;
		  delete locator.valueIndex;
		  delete locator.dataTypeId;
		  
		  locator.pointIdentifier = $get("pointIdentifier");
		  locator.valueRegex = $get("valueRegex");
		  locator.valueIndex = $get("valueIndex");
		  locator.dataTypeId = $get("dataTypeId");
		  
		  DataSourceEditDwr.savePointLocator(currentPoint.id, $get("xid"), $get("name"), locator, savePointCB);
	  }
	  
	  /**
	   * Toggle Terminator row
	   */
	  function toggleTerminator() {
		  if(!dojo.byId("useTerminator").checked) {
			  $("terminatorRow").style.visibility = "collapse";
		  	  //$("messageRegexRow").style.visibility = "collapse";
		  	  //$("identifierIndexRow").style.visibility = "collapse";
	  	  } else { 
			  $("terminatorRow").style.visibility = "visible";
			  //$("messageRegexRow").style.visibility = "visible";
			  //$("identifierIndexRow").style.visibility = "visible";
		  }
	  }
	  
	  /**
	   * Runs a test string
	   */
	  function submitTestString() {
		  DataSourceEditDwr.testString($get("testString"), displayResult);
	  }
	  
	  function displayResult(resp) {
		  if(resp.hasMessages) {
			  var message = "";
			  for(k in resp.messages)
				  message += resp.messages[k].contextualMessage + "<br>"
			  $("testMessages").style.visibility = "visible";
			  $("testMessages").innerHTML = message;
		  }
		  else {
			  $("testMessages").innerHTML = "";
			  $("testMessages").style.visibility = "collapse";
		  }
	  }
	  
	  function logIOChanged() {
	      if ($get("isLogIO")){
	          show("ioLogPathMsg");
	          show("maxHistoricalIOLogs_row");
	          show("ioLogFileSizeMBytes_row");
	      }else{
	          hide("ioLogPathMsg");
	          hide("ioLogFileSizeMBytes_row");
	          hide("maxHistoricalIOLogs_row");
	      }
	  }
	  
</script>
<%@ include file="/WEB-INF/jsp/dataSourceEdit/dsHead.jspf" %>

<%-- <tag:dataSourceAttrs descriptionKey="dsEdit.serial.desc" helpId="serialDS">
<tag:serialSettings/> --%>
<tr>
 <td class="formLabelRequired"><fmt:message key="dsEdit.serial.readTimeout"/></td>
 <td><input id="readTimeout" type="number" value="${dataSource.readTimeout}"></input></td>
</tr>

<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.modbusSerial.port"/></td>
  <td class="formField">
    <c:choose>
      <c:when test="${!empty commPortError}">
        <input id="commPortId" type="hidden" value=""/>
        <span class="formError">${commPortError}</span>
      </c:when>
      <c:otherwise>
         <sst:select id="commPortId" value="${dataSource.commPortId}">
          <c:forEach items="${jssccommPorts}" var="port">
            <sst:option value="${port.name}">${port.name}</sst:option>
          </c:forEach>
        </sst:select> 
       <%--   <sst:select id="commPortId" value="Com1">
          
            <sst:option value="Com1">Com1</sst:option>
          
        </sst:select> --%>
      </c:otherwise>
    </c:choose>
  </td>
</tr>
<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.modbusSerial.baud"/></td>
  <td class="formField">
    <sst:select id="baudRate" value="${dataSource.baudRate}">
      <sst:option>110</sst:option>
      <sst:option>300</sst:option>
      <sst:option>1200</sst:option>
      <sst:option>2400</sst:option>
      <sst:option>4800</sst:option>
      <sst:option>9600</sst:option>
      <sst:option>19200</sst:option>
      <sst:option>38400</sst:option>
      <sst:option>57600</sst:option>
      <sst:option>115200</sst:option>
      <sst:option>230400</sst:option>
      <sst:option>460800</sst:option>
      <sst:option>921600</sst:option>
    </sst:select>
  </td>
</tr>

<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.modbusSerial.flowIn"/></td>
  <td class="formField">
    <sst:select id="flowControlIn" value="${dataSource.flowControlIn}">
      <sst:option value="0"><fmt:message key="dsEdit.modbusSerial.flow.none"/></sst:option>
      <sst:option value="1"><fmt:message key="dsEdit.modbusSerial.flow.rtsCts"/></sst:option>
      <sst:option value="4"><fmt:message key="dsEdit.modbusSerial.flow.xonXoff"/></sst:option>
    </sst:select>
  </td>
</tr>

<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.modbusSerial.flowOut"/></td>
  <td class="formField">
    <sst:select id="flowControlOut" value="${dataSource.flowControlOut}">
      <sst:option value="0"><fmt:message key="dsEdit.modbusSerial.flow.none"/></sst:option>
      <sst:option value="2"><fmt:message key="dsEdit.modbusSerial.flow.rtsCts"/></sst:option>
      <sst:option value="8"><fmt:message key="dsEdit.modbusSerial.flow.xonXoff"/></sst:option>
    </sst:select>
  </td>
</tr>
<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.modbusSerial.dataBits"/></td>
  <td class="formField">
    <sst:select id="dataBits" value="${dataSource.dataBits}">
      <sst:option value="5">5</sst:option>
      <sst:option value="6">6</sst:option>
      <sst:option value="7">7</sst:option>
      <sst:option value="8">8</sst:option>
    </sst:select>
  </td>
</tr>

<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.modbusSerial.stopBits"/></td>
  <td class="formField">
    <sst:select id="stopBits" value="${dataSource.stopBits}">
      <sst:option value="1">1</sst:option>
      <sst:option value="3">1.5</sst:option>
      <sst:option value="2">2</sst:option>
    </sst:select>
  </td>
</tr>
<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.modbusSerial.parity"/></td>
  <td class="formField">
    <sst:select id="parity" value="${dataSource.parity}">
      <sst:option value="0"><fmt:message key="dsEdit.modbusSerial.parity.none"/></sst:option>
      <sst:option value="1"><fmt:message key="dsEdit.modbusSerial.parity.odd"/></sst:option>
      <sst:option value="2"><fmt:message key="dsEdit.modbusSerial.parity.even"/></sst:option>
      <sst:option value="3"><fmt:message key="dsEdit.modbusSerial.parity.mark"/></sst:option>
      <sst:option value="4"><fmt:message key="dsEdit.modbusSerial.parity.space"/></sst:option>
    </sst:select>
  </td>
</tr>


<tr>
 <td class="formLabelRequired"><fmt:message key="dsEdit.serial.useTerminator"/></td>
 <td><input id="useTerminator" type="checkbox" onchange="toggleTerminator()"></input></td>
</tr>
<tr id="terminatorRow">
 <td class="formLabelRequired"><fmt:message key="dsEdit.serial.messageTerminator"/></td>
 <td><input id="messageTerminator" type="text"></input></td>
</tr>
<tr id="messageRegexRow">
 <td class="formLabelRequired"><fmt:message key="dsEdit.serial.messageRegex"/></td>
 <td><input id="messageRegex" type="text" value="${dataSource.messageRegex}"></input></td>
</tr>
<tr id="identifierIndexRow">
 <td class="formLabelRequired"><fmt:message key="dsEdit.serial.pointIdentifierIndex"/></td>
 <td><input id="pointIdentifierIndex" type="number" value="${dataSource.pointIdentifierIndex}"></input></td>
</tr>
    <tr>
      <td class="formLabelRequired"><fmt:message key="dsEdit.serial.hex"/></td>
      <td class="formField"><sst:checkbox id="isHex" selectedValue="${dataSource.hex}"/></td>
    </tr>
    <tr>
      <td class="formLabelRequired"><fmt:message key="dsEdit.serial.maxMessageSize"/></td>
      <td class="formField"><input type="number" id="maxMessageSize" value="${dataSource.maxMessageSize}"/></td>
    </tr>
    
    <tr>
      <td class="formLabelRequired"><fmt:message key="dsEdit.serial.logIO"/></td>
      <td class="formField">
        <sst:checkbox id="isLogIO" selectedValue="${dataSource.logIO}" onclick="logIOChanged()"/>
        <div id="ioLogPathMsg">
         <%--  <fmt:message key="dsEdit.serial.log">
            <fmt:param value="${dataSource.ioLogPath}"/>
          </fmt:message> --%>
        </div>
      </td>
    </tr>
    <tr id="ioLogFileSizeMBytes_row">
      <td class="formLabelRequired"><fmt:message key="dsEdit.serial.logIOFileSize"/></td>
      <td class="formField"><input id="ioLogFileSizeMBytes" type="number" value="${dataSource.ioLogFileSizeMBytes}"/></td>
    </tr>
    <tr id="maxHistoricalIOLogs_row">
      <td class="formLabelRequired"><fmt:message key="dsEdit.serial.logIOFiles"/></td>
      <td class="formField"><input id="maxHistoricalIOLogs" type="number" value="${dataSource.maxHistoricalIOLogs}"/></td>
    </tr>
    
<tr>
 <td class="formLabel" style="padding-top:0px;"><button onclick="submitTestString()"><fmt:message key="dsEdit.serial.submitTestString"/></button></td>
 <td><input id="testString" type="text"></input></td>
</tr>
<tr><td id="testMessages" style="color:red;" colspan=2></td>
</tr>
<%-- </tag:dataSourceAttrs> --%>
<%@ include file="/WEB-INF/jsp/dataSourceEdit/dsEventsFoot.jspf" %>
<tag:pointList pointHelpId="serialPP">
  <tr>
    <td class="formLabelRequired"><fmt:message key="dsEdit.pointDataType"/></td>
    <td class="formField"><%--  <tag:dataTypeOptions id="dataTypeId" excludeImage="true"/> --%><select name="dataTypeId">
        <tag:dataTypeOptions excludeImage="true"/>
      </select> </td>
  </tr>
<tr>
 <td class="formLabelRequired"><fmt:message key="dsEdit.serial.pointIdentifier"/></td>
 <td><input id="pointIdentifier" type="text" ></input></td>
</tr>
<tr>
 <td class="formLabelRequired"><fmt:message key="dsEdit.serial.valueIndex"/></td>
 <td><input id="valueIndex" type="number" ></input></td>
</tr>
<tr>
 <td class="formLabelRequired"><fmt:message key="dsEdit.serial.valueRegex"/></td>
 <td><input id="valueRegex" type="text" ></input></td>
</tr>

</tag:pointList>
