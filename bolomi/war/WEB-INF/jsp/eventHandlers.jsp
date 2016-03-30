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
<%@page import="com.serotonin.mango.vo.event.EventHandlerVO"%>
<%@page import="com.serotonin.mango.DataTypes"%>
<c:set var="NEW_ID"><%= Common.NEW_ID %></c:set>

<tag:page dwr="EventHandlersDwr" js="emailRecipients" onload="init">
  <script>
  	<!--new js code -->
  
    function init() {
        EventHandlersDwr.getInitData(initCB);
        
        var tree = dojo.widget.manager.getWidgetById('eventTypeTree');
        dojo.event.topic.subscribe("eventTypeTree/titleClick", new TreeClickHandler(), 'handle');
    }
    
    var allPoints;
    var defaultHandlerId;
    var emailRecipients;
    //new code 
    var emailRecipientssn;
    var escalRecipientssn;
    var inactiveRecipientssn;
    //
    var escalRecipients;
    var inactiveRecipients;
    
    var strtj;
    var lkehdata;
    
    
    function initCB(data) {
        <c:if test="${!empty param.ehid}">
          defaultHandlerId = ${param.ehid};
        </c:if>

        var i, j, k;
        var dp, ds, p, et;
        var pointNode, dataSourceNode, publisherNode, etNode, wid;
        
        
/*         var handlerslist=new Array();
      	if(strtj!=null)
		{      	
		for(var i=0;i<data.systemEvents.length;i++){
			for(var j=0;j<data.systemEvents[i].handlers.length;j++)
				{
					if(data.systemEvents[i].handlers[j].alias!=strtj)
						{
							data.systemEvents[i].handlers.splice(j,1);
						}else
							{
								handlerslist.push(data.systemEvents[i].handlers[j]);
							
							}
				}
		}
		for(var i=0;i<handlerslist.length;i++){
			var handlerlk = handlerslist[i]

	        if (!selectedHandlerNode) {
	            selectedHandlerNode = createHandlerNode(handlerlk);
	            selectedEventTypeNode.addChild(selectedHandlerNode);
	            selectedEventTypeNode.expand();
	            selectedHandlerNode.onTitleClick();
	        }
	        else
	            $set(handlerlk.id +"Msg", handlerlk.message);
	        
	        selectedHandlerNode.object = handlerlk;
		}
		}
 */
        
        allPoints = data.allPoints;
        
        //
  
        
        emailRecipientssn = new mango.erecip.EmailRecipientssn("emailRecipientssn",
                "<sst:i18n key="eventHandlers.recipTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
        emailRecipientssn.write("emailRecipientssn", "emailRecipientssn", null,
        		"<sst:i18n key="eventHandlers.emailRecipients" escapeDQuotes="true"/>");
        
        escalRecipientssn = new mango.erecip.EmailRecipientssn("escalRecipientssn",
                "<sst:i18n key="eventHandlers.escalTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
        escalRecipientssn.write("escalRecipientssn", "escalRecipientssn", "escalationAddresses2sn",
        		"<sst:i18n key="eventHandlers.escalRecipients" escapeDQuotes="true"/>");
        
        inactiveRecipientssn = new mango.erecip.EmailRecipientssn("inactiveRecipientssn",
                "<sst:i18n key="eventHandlers.inactiveTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
        inactiveRecipientssn.write("inactiveRecipientssn", "inactiveRecipientssn", "inactiveAddresses2sn",
        		"<sst:i18n key="eventHandlers.inactiveRecipients" escapeDQuotes="true"/>");
        
        //
          emailRecipients = new mango.erecip.EmailRecipients("emailRecipients",
                "<sst:i18n key="eventHandlers.recipTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
        emailRecipients.write("emailRecipients", "emailRecipients", null,
        		"<sst:i18n key="eventHandlers.emailRecipients" escapeDQuotes="true"/>");
       
        
        escalRecipients = new mango.erecip.EmailRecipients("escalRecipients",
                "<sst:i18n key="eventHandlers.escalTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
        escalRecipients.write("escalRecipients", "escalRecipients", "escalationAddresses2",
        		"<sst:i18n key="eventHandlers.escalRecipients" escapeDQuotes="true"/>");
        
        inactiveRecipients = new mango.erecip.EmailRecipients("inactiveRecipients",
                "<sst:i18n key="eventHandlers.inactiveTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
        inactiveRecipients.write("inactiveRecipients", "inactiveRecipients", "inactiveAddresses2",
        		"<sst:i18n key="eventHandlers.inactiveRecipients" escapeDQuotes="true"/>");
        
        var pointRoot = dojo.widget.manager.getWidgetById('rootPoint');
        for (i=0; i<data.dataPoints.length; i++) {
            dp = data.dataPoints[i];
            pointNode = dojo.widget.createWidget("TreeNode", {
                    title: "<img src='images/icon_comp.png'/> "+ dp.name,
                    object: dp
            });
            pointRoot.addChild(pointNode);
            
            for (j=0; j<dp.eventTypes.length; j++) {
                et = dp.eventTypes[j];
                createEventTypeNode("ped"+ et.typeRef2, et, pointNode);
            }
        }
        pointRoot.expand();
        
        var scheduledRoot = dojo.widget.manager.getWidgetById('rootScheduled');
        for (i=0; i<data.scheduledEvents.length; i++) {
            et = data.scheduledEvents[i];
            createEventTypeNode("sch"+ et.typeRef1, et, scheduledRoot);
        }
        scheduledRoot.expand();
        
        var compoundRoot = dojo.widget.manager.getWidgetById('rootCompound');
        for (i=0; i<data.compoundEvents.length; i++) {
            et = data.compoundEvents[i];
            createEventTypeNode("ced"+ et.typeRef1, et, compoundRoot);
        }
        compoundRoot.expand();
        
        var dataSourceRoot = dojo.widget.manager.getWidgetById('rootDataSource');
        for (i=0; i<data.dataSources.length; i++) {
            ds = data.dataSources[i];
            dataSourceNode = dojo.widget.createWidget("TreeNode", {
                    title: "<img src='images/icon_ds.png'/> "+ ds.name,
                    object: ds
            });
            dataSourceRoot.addChild(dataSourceNode);
            
            for (j=0; j<ds.eventTypes.length; j++) {
                et = ds.eventTypes[j];
                createEventTypeNode("dse"+ et.typeRef1 +"/"+ et.typeRef2, et, dataSourceNode);
            }
        }
        
        if (data.publishers) {
            var publisherRoot = dojo.widget.manager.getWidgetById('rootPublisher');
            for (i=0; i<data.publishers.length; i++) {
                p = data.publishers[i];
                publisherNode = dojo.widget.createWidget("TreeNode", {
                        title: "<img src='images/transmit.png'/> "+ p.name,
                        object: p
                });
                publisherRoot.addChild(publisherNode);
                
                for (j=0; j<p.eventTypes.length; j++) {
                    et = p.eventTypes[j];
                    createEventTypeNode("pube"+ et.typeRef1 +"/"+ et.typeRef2, et, publisherNode);
                }
            }
        }
        
        if (data.maintenanceEvents) {
            var maintenanceRoot = dojo.widget.manager.getWidgetById('rootMaintenance');
            for (i=0; i<data.maintenanceEvents.length; i++) {
                et = data.maintenanceEvents[i];
                createEventTypeNode("maint"+ et.typeRef1, et, maintenanceRoot);
            }
        }
        
        if (data.systemEvents) {
            var systemRoot = dojo.widget.manager.getWidgetById('rootSystem');
            for (i=0; i<data.systemEvents.length; i++) {
                et = data.systemEvents[i];
                createEventTypeNode("sys"+ et.typeRef1, et, systemRoot);
            }
        }
        
        if (data.auditEvents) {
            var auditRoot = dojo.widget.manager.getWidgetById('rootAudit');
            for (i=0; i<data.auditEvents.length; i++) {
                et = data.auditEvents[i];
                createEventTypeNode("aud"+ et.typeRef1, et, auditRoot);
            }
        }
        
        hide("loadingImg");
        show("tree");
        
        // Default the selection of the parameter was provided.
        if (selectedHandlerNode) {
            selectedHandlerNode.onTitleClick();
            var parent = selectedHandlerNode.parent;
            while (parent && parent.expand) {
                parent.expand();
                parent = parent.parent;
            }
        }
        defaultHandlerId = null;
    }
    
    function createEventTypeNode(widgetId, eventType, parent) {
        var node = dojo.widget.createWidget("TreeNode", {
                title: "<img id='"+ widgetId +"Img'/> "+ eventType.description,
                widgetId: widgetId,
                object: eventType
        });
        parent.addChild(node);
        setAlarmLevelImg(eventType.alarmLevel, $(widgetId +"Img"));
        addHandlerNodes(eventType.handlers, node);
    }
    
    function addHandlerNodes(handlers, parent) {
        for (var i=0; i<handlers.length; i++)
            parent.addChild(createHandlerNode(handlers[i]));
    }
    
    function createHandlerNode(handler) {
        var img = "images/cog_wrench.png";
        if (handler.handlerType == <c:out value="<%= EventHandlerVO.TYPE_EMAIL %>"/>)
            img = "images/cog_email.png";
        else if (handler.handlerType == <c:out value="<%= EventHandlerVO.TYPE_PROCESS %>"/>)
            img = "images/cog_process.png";
             var cbstrimg="<input type='checkbox' name='wrenchcb' value='"+handler.id+"' id='cboxitem"+handler.id+"' onclick='selectedCbItem("+handler.id+",this)' />";
            if(img=="images/cog_email.png")
            {
            	var cbstrimg="<input type='checkbox' name='emailcb' value='"+handler.id+"' id='cboxitem"+handler.id+"' onclick='selectedCbItem("+handler.id+",this)' />";
            }else if(img=="images/cog_process.png")
            {
            	var cbstrimg="<input type='checkbox' name='processcb' value='"+handler.id+"' id='cboxitem"+handler.id+"' onclick='selectedCbItem("+handler.id+",this)' />";
            }
           
        var node = dojo.widget.createWidget("TreeNode", {
                title: "<img src='"+ img +"'/> <span id='"+ handler.id +"Msg'>"+ handler.message +"</span>"+cbstrimg,
                widgetId: "h"+ handler.id,
                object: handler
        });
        
        if (handler.id == defaultHandlerId)
            selectedHandlerNode = node;
        
        return node;
    }
    
    var selectedEventTypeNode;
    var selectedHandlerNode;
    
    var TreeClickHandler = function() {
        this.handle = function(message) {
            var widget = message.source;
            var wid = widget.widgetId;
            if (wid.startsWith("ped") || wid.startsWith("sch") || wid.startsWith("ced") || 
                    wid.startsWith("dse") || wid.startsWith("pube") || wid.startsWith("sys") || 
                    wid.startsWith("aud") || wid.startsWith("maint")) {
                selectedEventTypeNode = widget;
                selectedHandlerNode = null;
                showHandlerEdit();
            }
            else if (wid.startsWith("h")) {
                selectedHandlerNode = widget;
                selectedEventTypeNode = selectedHandlerNode.parent;
                showHandlerEdit();
            }
            else
                hide("handlerEditDiv");
        }
    }
    
    function showHandlerEdit() {
    	show("handlerEditDiv");
        setUserMessage("");
        
        // Set the target points.
        var pointSelect = $("targetPointSelect");
        dwr.util.removeAllOptions(pointSelect);
        for (var i=0; i<allPoints.length; i++) {
            dp = allPoints[i];
            if (dp.settable)
                pointSelect.options[pointSelect.options.length] = new Option(dp.name, dp.id);
        }
        
        if (selectedHandlerNode) {
            $("saveImg").src = "images/save.png";
            show("deleteImg");
            
            // Put values from the handler object into the input controls.
            var handler = selectedHandlerNode.object;
            $set("handlerTypeSelect", handler.handlerType);
            $("handlerTypeSelect").disabled = true;
            $set("xid", handler.xid);
            $set("alias", handler.alias);
            $set("disabled", handler.disabled);
            if (handler.handlerType == <c:out value="<%= EventHandlerVO.TYPE_SET_POINT %>"/>) {
                $set("targetPointSelect", handler.targetPointId);
                $set("activeAction", handler.activeAction);
                $set("inactiveAction", handler.inactiveAction);
            }
            else if (handler.handlerType == <c:out value="<%= EventHandlerVO.TYPE_EMAIL %>"/>) {
            	emailRecipients.updateRecipientList(handler.activeRecipients);
            	emailRecipientssn.updateRecipientList(handler.activeRecipients);
                $set("sendEscalation", handler.sendEscalation);
                $set("escalationDelayType", handler.escalationDelayType);
                $set("escalationDelay", handler.escalationDelay);
                escalRecipients.updateRecipientList(handler.escalationRecipients);
                escalRecipientssn.updateRecipientList(handler.escalationRecipients);
                $set("sendInactive", handler.sendInactive);
                $set("inactiveOverride", handler.inactiveOverride);
                inactiveRecipients.updateRecipientList(handler.inactiveRecipients);
                inactiveRecipientssn.updateRecipientList(handler.inactiveRecipients);
            }
            else if (handler.handlerType == <c:out value="<%= EventHandlerVO.TYPE_PROCESS %>"/>) {
                $set("activeProcessCommand", handler.activeProcessCommand);
                $set("inactiveProcessCommand", handler.inactiveProcessCommand);
            }
        }
        else {
            $("saveImg").src = "images/save_add.png";
            hide("deleteImg");
            $("handlerTypeSelect").disabled = false;
            
            // Clear values that may be left over from another handler.
            $set("xid", "");
            $set("alias", "");
            $set("disabled", false);
            $set("activeAction", <c:out value="<%= EventHandlerVO.SET_ACTION_NONE %>"/>);
            $set("inactiveAction", <c:out value="<%= EventHandlerVO.SET_ACTION_NONE %>"/>);
            $set("sendEscalation", false);
            $set("escalationDelayType", <c:out value="<%= Common.TimePeriods.HOURS %>"/>);
            $set("escalationDelay", 1);
            $set("sendInactive", false);
            $set("inactiveOverride", false);
            $set("activeProcessCommand", "");
            $set("inactiveProcessCommand", "");
            
            // Clear the recipient lists.
            emailRecipients.updateRecipientList();
            escalRecipients.updateRecipientList();
            inactiveRecipients.updateRecipientList();
            // new clear recipient lists.
            emailRecipientssn.updateRecipientList();
            escalRecipientssn.updateRecipientList();
            inactiveRecipientssn.updateRecipientList();
        }
        
        // Set the use source value checkbox.
        handlerTypeChanged();
        activeActionChanged();
        inactiveActionChanged();
        targetPointSelectChanged();
        //new function 
        activeActionsnChanged();
        inactiveActionsnChanged();
        targetPointSelectsnChanged();
        sendEscalationsnChanged();
        sendInactivesnChanged();
        
        sendEscalationChanged();
        sendInactiveChanged();
    }
    
    var currentHandlerEditor;
    function handlerTypeChanged() {
        setUserMessage();
        var handlerId = $get("handlerTypeSelect");
        if (currentHandlerEditor) {
        	hide(currentHandlerEditor);
        	hide($(currentHandlerEditor.id +"Img"));
        }
        currentHandlerEditor = $("handler"+ handlerId);
        show(currentHandlerEditor);
        show($(currentHandlerEditor.id +"Img"));
    }
    
    function targetPointSelectChanged() {
        var selectControl = $("targetPointSelect");
        
        // Make sure there are points in the list.
        if (selectControl.options.length == 0)
            return;
        
        // Get the content for the value to set section.
        var targetPointId = selectControl.value;
        var activeValueStr = "";
        var inactiveValueStr = "";
        if (selectedHandlerNode) {
            activeValueStr = selectedHandlerNode.object.activeValueToSet;
            inactiveValueStr = selectedHandlerNode.object.inactiveValueToSet;
        }
        EventHandlersDwr.createSetValueContent(targetPointId, activeValueStr, "Active",
                function(content) { $("activeValueToSetContent").innerHTML = content; });
        EventHandlersDwr.createSetValueContent(targetPointId, inactiveValueStr, "Inactive",
                function(content) { $("inactiveValueToSetContent").innerHTML = content; });
        
        // Update the source point lists.
        var targetDataTypeId = getPoint(targetPointId).dataType;
        var activeSourceSelect = $("activePointId");
        dwr.util.removeAllOptions(activeSourceSelect);
        var inactiveSourceSelect = $("inactivePointId");
        dwr.util.removeAllOptions(inactiveSourceSelect);
        for (var i=0; i<allPoints.length; i++) {
            dp = allPoints[i];
            if (dp.id != targetPointId && dp.dataType == targetDataTypeId) {
                activeSourceSelect.options[activeSourceSelect.options.length] = new Option(dp.name, dp.id);
                inactiveSourceSelect.options[activeSourceSelect.options.length] = new Option(dp.name, dp.id);
            }
        }
        if (selectedHandlerNode) {
            $set(activeSourceSelect, selectedHandlerNode.object.activePointId);
            $set(inactiveSourceSelect, selectedHandlerNode.object.inactivePointId);
        }
    }
    
    function activeActionChanged() {
        var action = $get("activeAction");
        if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>) {
        	show("activePointIdRow");
            hide("activeValueToSetRow");
        }
        else if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>) {
        	hide("activePointIdRow");
        	show("activeValueToSetRow");
        }
        else {
        	hide("activePointIdRow");
        	hide("activeValueToSetRow");
        }
    }

    
    function inactiveActionChanged() {
        var action = $get("inactiveAction");
        if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>) {
        	show("inactivePointIdRow");
            hide("inactiveValueToSetRow");
        }
        else if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>) {
        	hide("inactivePointIdRow");
        	show("inactiveValueToSetRow");
        }
        else {
        	hide("inactivePointIdRow");
        	hide("inactiveValueToSetRow");
        }
    }
    function sendEscalationsnChanged() {
        if ($get("sendEscalationsn")) {
        	show("escalationAddresses1sn");
            show("escalationAddresses2sn");
        }
        else {
        	hide("escalationAddresses1sn");
        	hide("escalationAddresses2sn");
        }
    }
    
    function sendEscalationChanged() {
        if ($get("sendEscalation")) {
        	show("escalationAddresses1");
            show("escalationAddresses2");
        }
        else {
        	hide("escalationAddresses1");
        	hide("escalationAddresses2");
        }
    }
    
    function getPoint(id) {
        return getElement(allPoints, id);
    }
    
    function saveHandler() {
        setUserMessage();
        hideContextualMessages("scheduledEventDetails")
        hideGenericMessages("genericMessages")
        
        var handlerId = ${NEW_ID};
        if (selectedHandlerNode)
            handlerId = selectedHandlerNode.object.id;
        
        // Do some validation.
        var handlerType = $get("handlerTypeSelect");
        var xid = $get("xid");
        var alias = $get("alias");
        var disabled = $get("disabled");
        if (handlerType == <c:out value="<%= EventHandlerVO.TYPE_EMAIL %>"/>) {
            var emailList = emailRecipients.createRecipientArray();
            var escalList = escalRecipients.createRecipientArray();
            var inactiveList = inactiveRecipients.createRecipientArray();
            EventHandlersDwr.saveEmailEventHandler(selectedEventTypeNode.object.typeId,
                    selectedEventTypeNode.object.typeRef1, selectedEventTypeNode.object.typeRef2, handlerId, xid, alias,
                    disabled, emailList, $get("sendEscalation"), $get("escalationDelayType"), $get("escalationDelay"), 
                    escalList, $get("sendInactive"), $get("inactiveOverride"), inactiveList, saveEventHandlerCB);
        }
        else if (handlerType == <c:out value="<%= EventHandlerVO.TYPE_SET_POINT %>"/>) {
            EventHandlersDwr.saveSetPointEventHandler(selectedEventTypeNode.object.typeId,
                    selectedEventTypeNode.object.typeRef1, selectedEventTypeNode.object.typeRef2, handlerId, xid, alias,
                    disabled, $get("targetPointSelect"), $get("activeAction"), $get("setPointValueActive"), 
                    $get("activePointId"), $get("inactiveAction"), $get("setPointValueInactive"), 
                    $get("inactivePointId"), saveEventHandlerCB);
        }
        else if (handlerType == <c:out value="<%= EventHandlerVO.TYPE_PROCESS %>"/>) {
            EventHandlersDwr.saveProcessEventHandler(selectedEventTypeNode.object.typeId,
                    selectedEventTypeNode.object.typeRef1, selectedEventTypeNode.object.typeRef2, handlerId, xid,
                    alias, disabled, $get("activeProcessCommand"), $get("inactiveProcessCommand"), saveEventHandlerCB);
        }
    }
    
    function saveEventHandlerCB(response) {
        if (response.hasMessages)
            showDwrMessages(response.messages, $("genericMessages"));
        else {
            var handler = response.data.handler;
            setUserMessage("<fmt:message key="eventHandlers.saved"/>");
            if (!selectedHandlerNode) {
                selectedHandlerNode = createHandlerNode(handler);
              	selectedEventTypeNode.addChild(selectedHandlerNode); 
              //selectedEventTypeNode.doAddChild(selectedHandlerNode); 
                selectedEventTypeNode.expand();             
                selectedHandlerNode.onTitleClick();
            }
            else
                $set(handler.id +"Msg", handler.message);
            
            selectedHandlerNode.object = handler;
        }
    }
    
    function deleteHandler() {
        EventHandlersDwr.deleteEventHandler(selectedHandlerNode.object.id);
        selectedEventTypeNode.removeNode(selectedHandlerNode);
        hide("handlerEditDiv");
    }
    
    function setUserMessage(msg) {
        showMessage("userMessage", msg);
    }
    function setUserMessagesn(msg) {
        showMessage("userMessagesn", msg);
    }
    
    function testProcessCommand(nodeId) {
    	EventHandlersDwr.testProcessCommand($get(nodeId), function(msg) {
    		if (msg)
    			alert(msg);
    	});
    }
    function testProcessCommandsn(nodeId) {
    	EventHandlersDwr.testProcessCommand($get(nodeId), function(msg) {
    		if (msg)
    			alert(msg);
    	});
    }
    
    function sendInactiveChanged() {
        if ($get("sendInactive")) {
            show("inactiveAddresses1");
            inactiveOverrideChanged();
        }
        else {
            hide("inactiveAddresses1");
            hide("inactiveAddresses2");
        }
    }
    
    function sendInactivesnChanged() {
        if ($get("sendInactivesn")) {
            show("inactiveAddresses1sn");
            inactiveOverrideChanged();
        }
        else {
            hide("inactiveAddresses1sn");
            hide("inactiveAddresses2sn");
        }
    }
    
    //new code
    function targetPointSelectsnChanged() {
        var selectControl = $("targetPointSelectsn");
        
        // Make sure there are points in the list.
        if (selectControl.options.length == 0)
            return;
        
        // Get the content for the value to set section.
        var targetPointId = selectControl.value;
        var activeValueStr = "";
        var inactiveValueStr = "";
        if (selectedHandlerNode) {
            activeValueStr = selectedHandlerNode.object.activeValueToSet;
            inactiveValueStr = selectedHandlerNode.object.inactiveValueToSet;
        }
        EventHandlersDwr.createSetValueContent(targetPointId, activeValueStr, "Active",
                function(content) { $("activeValueToSetContentsn").innerHTML = content; });
        EventHandlersDwr.createSetValueContent(targetPointId, inactiveValueStr, "Inactive",
                function(content) { $("inactiveValueToSetContentsn").innerHTML = content; });
        
        // Update the source point lists.
        var targetDataTypeId = getPoint(targetPointId).dataType;
        var activeSourceSelect = $("activePointIdsn");
        dwr.util.removeAllOptions(activeSourceSelect);
        var inactiveSourceSelect = $("inactivePointIdsn");
        dwr.util.removeAllOptions(inactiveSourceSelect);
        for (var i=0; i<allPoints.length; i++) {
            dp = allPoints[i];
            if (dp.id != targetPointId && dp.dataType == targetDataTypeId) {
                activeSourceSelect.options[activeSourceSelect.options.length] = new Option(dp.name, dp.id);
                inactiveSourceSelect.options[activeSourceSelect.options.length] = new Option(dp.name, dp.id);
            }
        }
        if (selectedHandlerNode) {
            $set(activeSourceSelect, selectedHandlerNode.object.activePointId);
            $set(inactiveSourceSelect, selectedHandlerNode.object.inactivePointId);
        }
    }
    
        function activeActionsnChanged() {
        var action = $get("activeActionsn");
        if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>) {
        	show("activePointIdRowsn");
            hide("activeValueToSetRowsn");
        }
        else if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>) {
        	hide("activePointIdRowsn");
        	show("activeValueToSetRowsn");
        }
        else {
        	hide("activePointIdRowsn");
        	hide("activeValueToSetRowsn");
        }
    }
     function inactiveActionsnChanged() {
        var action = $get("inactiveActionsn");
        if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>) {
        	show("inactivePointIdRowsn");
            hide("inactiveValueToSetRowsn");
        }
        else if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>) {
        	hide("inactivePointIdRowsn");
        	show("inactiveValueToSetRowsn");
        }
        else {
        	hide("inactivePointIdRowsn");
        	hide("inactiveValueToSetRowsn");
        }
    }
    
    function inactiveOverrideChanged() {
        if ($get("inactiveOverride"))
            show("inactiveAddresses2");
        else
            hide("inactiveAddresses2");
    }
    function inactiveOverridesnChanged() {
        if ($get("inactiveOverridesn"))
            show("inactiveAddresses2sn");
        else
            hide("inactiveAddresses2sn");
    }
    
    	function selectedCbItem(id,cbitem)
  	{
  	
  		var proitem=document.getElementsByName("processcb");
	  	var wreitem=document.getElementsByName("wrenchcb");
	  	var emailitem=document.getElementsByName("emailcb");
  	if(cbitem.checked==true){
  		targetPointSelectsnChanged();
  	 	activeActionsnChanged();
        inactiveActionsnChanged();
        
  		var pointSelect = $("targetPointSelectsn");
        dwr.util.removeAllOptions(pointSelect);
        for (var i=0; i<allPoints.length; i++) {
            dp = allPoints[i];
            if (dp.settable)
                pointSelect.options[pointSelect.options.length] = new Option(dp.name, dp.id);
        }
   
  		<!--wrenchcb processcb;-->
  		if(cbitem.name=="emailcb")
	  		{
  			$set("typehandle","email");
  			show("handler2sn");
	  			for(var i=0;i<proitem.length;i++)
	  			{
	  				proitem[i].disabled=true;
	  			}
	  			for(var i=0;i<wreitem.length;i++)
	  			{
	  				wreitem[i].disabled=true;
	  			}
	  		}
	  		if(cbitem.name=="wrenchcb")
	  		{
	  			$set("typehandle","wrench");
	  			show("handler1sn");
	  		for(var i=0;i<emailitem.length;i++)
	  			{
	  				emailitem[i].disabled=true;
	  			}
	  			for(var i=0;i<proitem.length;i++)
	  			{
	  				proitem[i].disabled=true;
	  			}
	  		}
	  		if(cbitem.name=="processcb")
	  		{
	  			$set("typehandle","process");
	  			show("handler3sn");
	  		for(var i=0;i<emailitem.length;i++)
	  			{
	  				emailitem[i].disabled=true;
	  			}
	  			for(var i=0;i<wreitem.length;i++)
	  			{
	  				wreitem[i].disabled=true;
	  			}
	  		}
	  		show("handcbs");
  		}else
	  	{
	  		var lkcbitem=document.getElementsByName(cbitem.name);
	  		var lknum=0;
	  		for(var i=0;i<lkcbitem.length;i++)
	  		{
	  			if(lkcbitem[i].checked==true)
	  			{
	  				lknum++;
	  			}
	  		}
	  		if(lknum==0){

	  			hide("handler1sn");
	  			hide("handler2sn");
	  			hide("handler3sn");
	  			$set("typehandle","");
	  		hide("handcbs");hide("activePointIdRow");  hide("activeValueToSetRow");
		  		for(var i=0;i<emailitem.length;i++)
		  		{
		  			emailitem[i].disabled=false;
		  		}
		  		for(var i=0;i<wreitem.length;i++)
		  		{
		  			wreitem[i].disabled=false;
		  		}
		  		for(var i=0;i<proitem.length;i++)
		  		{
		  				proitem[i].disabled=false;
		  		}	  		
	  		}
	  	}
  	}
  	
  	    function saveHandlersn() {
  	    	
  	    	var typeh=$get("typehandle");
     /*    setUserMessage();
        hideContextualMessages("scheduledEventDetails")
        hideGenericMessages("genericMessages")
        
        var handlerId = ${NEW_ID};
        if (selectedHandlerNode)
            handlerId = selectedHandlerNode.object.id;
        
        // Do some validation.
        var handlerType = $get("handlerTypeSelect"); */
        if(typeh=="email"){

       		var cblist=$get("emailcb");
            var emailList = emailRecipientssn.createRecipientArray();
            var escalList = escalRecipientssn.createRecipientArray();
            var inactiveList = inactiveRecipientssn.createRecipientArray();
            for(var i=0;i<cblist.length;i++){
            EventHandlersDwr.saveEmailEventHandlersn(selectedEventTypeNode.object.typeId,
                    selectedEventTypeNode.object.typeRef1, selectedEventTypeNode.object.typeRef2, cblist[i], emailList, $get("sendEscalationsn"), $get("escalationDelayTypesn"), $get("escalationDelaysn"), 
                    escalList, $get("sendInactivesn"), $get("inactiveOverridesn"), inactiveList, saveEventHandlersnCB);
        }}
        else if (typeh=="wrench") {
        	var cblist=$get("wrenchcb");
        	 for(var i=0;i<cblist.length;i++){
            EventHandlersDwr.saveSetPointEventHandlersn(selectedEventTypeNode.object.typeId,
                    selectedEventTypeNode.object.typeRef1, selectedEventTypeNode.object.typeRef2, cblist[i],$get("targetPointSelectsn"), $get("activeActionsn"), $get("setPointValueActivesn"), 
                    $get("activePointIdsn"), $get("inactiveActionsn"), $get("setPointValueInactivesn"), 
                    $get("inactivePointIdsn"), saveEventHandlersnCB);
        	 }
        }
        else if (typeh=="process") {
        	var cblist=$get("processcb");
       	 for(var i=0;i<cblist.length;i++){
            EventHandlersDwr.saveProcessEventHandlersn(selectedEventTypeNode.object.typeId,
                    selectedEventTypeNode.object.typeRef1, selectedEventTypeNode.object.typeRef2, cblist[i], $get("activeProcessCommand"), $get("inactiveProcessCommand"), saveEventHandlersnCB);
        }
        }
  	    }
  	  function saveEventHandlersnCB(response) {
          if (response.hasMessages)
              showDwrMessages(response.messages, $("genericMessagessn"));
          else {
              var handler = response.data.handler;
              setUserMessagesn("<fmt:message key="eventHandlers.saved"/>");
              if (!selectedHandlerNode) {
                  selectedHandlerNode = createHandlerNode(handler);
                  selectedEventTypeNode.addChild(selectedHandlerNode);
                  selectedEventTypeNode.expand();
                  selectedHandlerNode.onTitleClick();
              }
              else
                  $set(handler.id +"Msg", handler.message);
              
              selectedHandlerNode.object = handler;
          }
      }
  	  
/* 
  	function encode(s){
  	  return s.replace(/&/g,"&").replace(/</g,"<").replace(/>/g,">").replace(/([\\\.\*\[\]\(\)\$\^])/g,"\\$1");
  	}
  	function decode(s){
  	  return s.replace(/\\([\\\.\*\[\]\(\)\$\^])/g,"$1").replace(/>/g,">").replace(/</g,"<").replace(/&/g,"&");
  	}
  	function highlight(){
  	  var s=$get("s");
  		if (s.length==0){

  	    return false;
  	  }
  	  s=encode(s);
  	  var obj=document.getElementsByTagName("body")[0];
  	  var t=obj.innerHTML.replace(/<span\s+class=.?highlight.?>([^<>]*)<\/span>/gi,"$1");
  	  obj.innerHTML=t;
  	  var cnt=loopSearch(s,obj);
  	  t=obj.innerHTML
  	  var r=/{searchHL}(({(?!\/searchHL})|[^{])*){\/searchHL}/g
  	  t=t.replace(r,"<span class='highlight'>$1</span>");
  	  obj.innerHTML=t;

  	}
  	function loopSearch(s,obj){
  	  var cnt=0;
  	  if (obj.nodeType==3){
  	    cnt=replace(s,obj);
  	    return cnt;
  	  }
  	  for (var i=0,c;c=obj.childNodes[i];i++){
  	    if (!c.className||c.className!="highlight")
  	      cnt+=loopSearch(s,c);
  	  }
  	  return cnt;
  	}
  	function replace(s,dest){
  	  var r=new RegExp(s,"g");
  	  var tm=null;
  	  var t=dest.nodeValue;
  	  var cnt=0;
  	  if (tm=t.match(r)){
  	    cnt=tm.length;
  	    t=t.replace(r,"{searchHL}"+decode(s)+"{/searchHL}")
  	    dest.nodeValue=t;
  	  }
  	  return cnt;
  	} */
  	function highlight()
  	{
  		 setUserMessage();
         hideContextualMessages("scheduledEventDetails")
         hideGenericMessages("genericMessages")
         
         var handlerId = ${NEW_ID};
         if (selectedHandlerNode)
             handlerId = selectedHandlerNode.object.id;
  	
  	  var s=$get("s");
  	//EventHandlersDwr.getOneByTj(s,saveEventHandlerCB);
  	EventHandlersDwr.getOneByTj(s,highlightcb);
  		
  	  /*  var key = s.split('|');
  	   for (var i=0; i<key.length; i++) 
  		{
	  	   var rng = document.body.createTextRange();
	  	   
	  	   while (rng.findText(key[i]))
	  	  //rng.pasteHTML(rng.text.fontcolor('red'));
	  	  rng.pasteHTML('<span style="color:#F00">' + rng.text + '</span>');
  	 	} */
  	}
  	   function highlightcb(data)
  	   {
  		 var handlerlk = data.data.handler;

	       
	            selectedHandlerNode = createHandlerNode(handlerlk);
	            selectedEventTypeNode.doRemoveNode(handlerlk);
	            //selectedEventTypeNode.removeNode(selectedHandlerNode);
	            //selectedEventTypeNode.removeNode(handlerlk);
	            selectedEventTypeNode.addChild(selectedHandlerNode);
	            selectedEventTypeNode.expand();
	            selectedHandlerNode.onTitleClick();
	            
	            
	        
	        selectedHandlerNode.object = handlerlk;
  	   }
 
  	
  	/*  function searchcb() {
  		var s=$get("s");  
  	
         EventHandlersDwr.getInitData(s,initCB);
         
         var tree = dojo.widget.manager.getWidgetById('eventTypeTree');
         dojo.event.topic.subscribe("eventTypeTree/titleClick", new TreeClickHandler(), 'handle');
     }
  	 */
  </script>
  <input name="s" id="s" title="search key"/>
  <input type="button" onclick="highlight()" value="<fmt:message key="eventHandlers.search"/>"/>
  <table class="borderDiv marB"><tr><td>
    <tag:img png="cog" title="eventHandlers.eventHandlers"/>
    <span class="smallTitle"><fmt:message key="eventHandlers.eventHandlers"/></span>
    <tag:help id="eventHandlers"/>
  </td></tr></table>
  
  <table cellpadding="0" cellspacing="0">
    <tr>
      <td valign="top">
        <div id="treelists" class="borderDivPadded marR">
          <span class="smallTitle"><fmt:message key="eventHandlers.types"/></span>
          <div dojoType="TreeBasicController" widgetId="controller"></div>
          <img src="images/hourglass.png" id="loadingImg"/>
          <div id="tree" style="display:none;">
            <div dojoType="Tree" widgetId="eventTypeTree" listeners="controller" toggle="wipe">
              <div dojoType="TreeNode" title="<img src='images/bell.png'/> <fmt:message key="eventHandlers.pointEventDetector"/>" widgetId="rootPoint"></div>
              <div dojoType="TreeNode" title="<img src='images/clock.png'/> <fmt:message key="scheduledEvents.ses"/>" widgetId="rootScheduled"></div>
              <div dojoType="TreeNode" title="<img src='images/multi_bell.png'/> <fmt:message key="compoundDetectors.compoundEventDetectors"/>" widgetId="rootCompound"></div>
              <div dojoType="TreeNode" title="<fmt:message key="eventHandlers.dataSourceEvents"/>" widgetId="rootDataSource"></div>
              <div dojoType="TreeNode" title="<fmt:message key="eventHandlers.publisherEvents"/>" widgetId="rootPublisher"></div>
              <div dojoType="TreeNode" title="<img src='images/hammer.png'/> <fmt:message key="eventHandlers.maintenanceEvents"/>" widgetId="rootMaintenance"></div>
              <div dojoType="TreeNode" title="<fmt:message key="eventHandlers.systemEvents"/>" widgetId="rootSystem"></div>
              <div dojoType="TreeNode" title="<fmt:message key="eventHandlers.auditEvents"/>" widgetId="rootAudit"></div>
            </div>
          </div>
        </div>
      </td>
      
      <td valign="top">
        <div id="handlerEditDiv" class="borderDivPadded" style="display:none;">
          <table width="100%">
            <tr>
              <td class="smallTitle"><fmt:message key="eventHandlers.eventHandler"/></td>
              <td align="right">
                <tag:img id="deleteImg" png="delete" title="common.delete" onclick="deleteHandler();"/>
                <tag:img id="saveImg" png="save" title="common.save" onclick="saveHandler();"/>
              </td>
            </tr>
            <tr><td class="formError" id="userMessage"></td></tr>
          </table>
          
          <table width="100%">
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.type"/></td>
              <td class="formField">
                <select id="handlerTypeSelect" onchange="handlerTypeChanged()">
                  <option value="<c:out value="<%= EventHandlerVO.TYPE_EMAIL %>"/>"><fmt:message key="eventHandlers.type.email"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.TYPE_SET_POINT %>"/>"><fmt:message key="eventHandlers.type.setPoint"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.TYPE_PROCESS %>"/>"><fmt:message key="eventHandlers.type.process"/></option>
                </select>
                <tag:img id="handler1Img" png="cog_wrench" title="eventHandlers.type.setPointHandler" style="display:none;"/>
                <tag:img id="handler2Img" png="cog_email" title="eventHandlers.type.emailHandler" style="display:none;"/>
                <tag:img id="handler3Img" png="cog_process" title="eventHandlers.type.processHandler" style="display:none;"/>
              </td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="common.xid"/></td>
              <td class="formField"><input type="text" id="xid"/></td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.alias"/></td>
              <td class="formField"><input id="alias" type="text"/></td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="common.disabled"/></td>
              <td class="formField"><input type="checkbox" id="disabled"/></td>
            </tr>
            
            <tr><td class="horzSeparator" colspan="2"></td></tr>
          </table>
          
          <table id="handler<c:out value="<%= EventHandlerVO.TYPE_SET_POINT %>"/>" style="display:none" width="100%">
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.target"/></td>
              <td class="formField">
                <select id="targetPointSelect" onchange="targetPointSelectChanged()"></select>
              </td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.activeAction"/></td>
              <td class="formField">
                <select id="activeAction" onchange="activeActionChanged()">
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_NONE %>"/>"><fmt:message key="eventHandlers.action.none"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>"><fmt:message key="eventHandlers.action.point"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>"><fmt:message key="eventHandlers.action.static"/></option>
                </select>
              </td>
            </tr>
          
            <tr id="activePointIdRow">
              <td class="formLabel"><fmt:message key="eventHandlers.sourcePoint"/></td>
              <td class="formField"><select id="activePointId"></select></td>
            </tr>
          
            <tr id="activeValueToSetRow">
              <td class="formLabel"><fmt:message key="eventHandlers.valueToSet"/></td>
              <td class="formField" id="activeValueToSetContent"></td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.inactiveAction"/></td>
              <td class="formField">
                <select id="inactiveAction" onchange="inactiveActionChanged()">
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_NONE %>"/>"><fmt:message key="eventHandlers.action.none"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>"><fmt:message key="eventHandlers.action.point"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>"><fmt:message key="eventHandlers.action.static"/></option>
                </select>
              </td>
            </tr>
          
            <tr id="inactivePointIdRow">
              <td class="formLabel"><fmt:message key="eventHandlers.sourcePoint"/></td>
              <td class="formField"><select id="inactivePointId"></select></td>
            </tr>
          
            <tr id="inactiveValueToSetRow">
              <td class="formLabel"><fmt:message key="eventHandlers.valueToSet"/></td>
              <td class="formField" id="inactiveValueToSetContent"></td>
            </tr>
          </table>
            
          <table id="handler<c:out value="<%= EventHandlerVO.TYPE_EMAIL %>"/>" style="display:none" width="100%">
            <tbody id="emailRecipients"></tbody>
            
            <tr><td class="horzSeparator" colspan="2"></td></tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.escal"/></td>
              <td class="formField"><input id="sendEscalation" type="checkbox" onclick="sendEscalationChanged()"/></td>
            </tr>
            
            <tr id="escalationAddresses1">
              <td class="formLabelRequired"><fmt:message key="eventHandlers.escalPeriod"/></td>
              <td class="formField">
                <input id="escalationDelay" type="text" class="formShort"/>
                <select id="escalationDelayType">
                  <tag:timePeriodOptions min="true" h="true" d="true"/>
                </select>
              </td>
            </tr>
              
            <tbody id="escalRecipients"></tbody>
            
            <tr><td class="horzSeparator" colspan="2"></td></tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.inactiveNotif"/></td>
              <td class="formField"><input id="sendInactive" type="checkbox" onclick="sendInactiveChanged()"/></td>
            </tr>
            
            <tr id="inactiveAddresses1">
              <td class="formLabelRequired"><fmt:message key="eventHandlers.inactiveOverride"/></td>
              <td class="formField"><input id="inactiveOverride" type="checkbox" onclick="inactiveOverrideChanged()"/></td>
            </tr>
              
            <tbody id="inactiveRecipients"></tbody>
          </table>
          
          <table id="handler<c:out value="<%= EventHandlerVO.TYPE_PROCESS %>"/>" style="display:none" width="100%">
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.activeCommand"/></td>
              <td class="formField">
                <input type="text" id="activeProcessCommand" class="formLong"/>
                <tag:img png="cog_go" onclick="testProcessCommand('activeProcessCommand')" title="eventHandlers.commandTest.title"/>
              </td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.inactiveCommand"/></td>
              <td class="formField">
                <input type="text" id="inactiveProcessCommand" class="formLong"/>
                <tag:img png="cog_go" onclick="testProcessCommand('inactiveProcessCommand')" title="eventHandlers.commandTest.title"/>
              </td>
            </tr>
          </table>
          
          <table>
            <tbody id="genericMessages"></tbody>
          </table>
        </div>
      </td>
      <td valign="top">
      	<div id="handcbs" class="borderDivPadded" style="display:none;">
      	<input type="hidden" id="typehandle" value="">
      	<table width="100%">
            <tr>
              <td class="smallTitle"><fmt:message key="eventHandlers.eventHandler"/></td>
              <td align="right">
               
                <tag:img id="saveImg" png="save" title="common.save" onclick="saveHandlersn();"/>
              </td>
            </tr>
            <tr><td class="formError" id="userMessagesn"></td></tr>
          </table>
      	 <table id="handler1sn" style="display: none;" width="100%">
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.target"/></td>
              <td class="formField">
                <select id="targetPointSelectsn" onchange="targetPointSelectsnChanged()"></select>
              </td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.activeAction"/></td>
              <td class="formField">
                <select id="activeActionsn" onchange="activeActionsnChanged()">
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_NONE %>"/>"><fmt:message key="eventHandlers.action.none"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>"><fmt:message key="eventHandlers.action.point"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>"><fmt:message key="eventHandlers.action.static"/></option>
                </select>
              </td>
            </tr>
          
            <tr id="activePointIdRowsn" style="display: none;">
              <td class="formLabel"><fmt:message key="eventHandlers.sourcePoint"/></td>
              <td class="formField"><select id="activePointIdsn"></select></td>
            </tr>
          
            <tr id="activeValueToSetRowsn" style="display: none;">
              <td class="formLabel"><fmt:message key="eventHandlers.valueToSet"/></td>
              <td class="formField" id="activeValueToSetContentsn"></td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.inactiveAction"/></td>
              <td class="formField">
                <select id="inactiveActionsn" onchange="inactiveActionsnChanged()">
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_NONE %>"/>"><fmt:message key="eventHandlers.action.none"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>"><fmt:message key="eventHandlers.action.point"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>"><fmt:message key="eventHandlers.action.static"/></option>
                </select>
              </td>
            </tr>
          
            <tr id="inactivePointIdRowsn" style="display: none;">
              <td class="formLabel"><fmt:message key="eventHandlers.sourcePoint"/></td>
              <td class="formField"><select id="inactivePointIdsn"></select></td>
            </tr>
          
            <tr id="inactiveValueToSetRowsn" style="display: none;">
              <td class="formLabel"><fmt:message key="eventHandlers.valueToSet"/></td>
              <td class="formField" id="inactiveValueToSetContentsn"></td>
            </tr>
          </table>
            
           <table id="handler2sn" style="display: none;"  width="100%">
            <tbody id="emailRecipientssn"></tbody>
            
            <tr><td class="horzSeparator" colspan="2"></td></tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.escal"/></td>
              <td class="formField"><input id="sendEscalationsn" type="checkbox" onclick="sendEscalationsnChanged()"/></td>
            </tr>
            
            <tr id="escalationAddresses1sn">
              <td class="formLabelRequired"><fmt:message key="eventHandlers.escalPeriod"/></td>
              <td class="formField">
                <input id="escalationDelaysn" type="text" class="formShort"/>
                <select id="escalationDelayTypesn">
                  <tag:timePeriodOptions min="true" h="true" d="true"/>
                </select>
              </td>
            </tr>
              
            <tbody id="escalRecipientssn"></tbody>
            
            <tr><td class="horzSeparator" colspan="2"></td></tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.inactiveNotif"/></td>
              <td class="formField"><input id="sendInactivesn" type="checkbox" onclick="sendInactivesnChanged()"/></td>
            </tr>
            
            <tr id="inactiveAddresses1sn">
              <td class="formLabelRequired"><fmt:message key="eventHandlers.inactiveOverride"/></td>
              <td class="formField"><input id="inactiveOverridesn" type="checkbox" onclick="inactiveOverridesnChanged()"/></td>
            </tr>
              
            <tbody id="inactiveRecipientssn"></tbody>
          </table>

          <table id="handler3sn"  style="display: none;" width="100%">
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.activeCommand"/></td>
              <td class="formField">
                <input type="text" id="activeProcessCommandsn" class="formLong"/>
                <tag:img png="cog_go" onclick="testProcessCommandsn('activeProcessCommandsn')" title="eventHandlers.commandTest.title"/>
              </td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.inactiveCommand"/></td>
              <td class="formField">
                <input type="text" id="inactiveProcessCommandsn" class="formLong"/>
                <tag:img png="cog_go" onclick="testProcessCommandsn('inactiveProcessCommandsn')" title="eventHandlers.commandTest.title"/>
              </td>
            </tr>
          </table>
           <table>
            <tbody id="genericMessagessn"></tbody>
          </table>
        </div>
      </td>
    </tr>
  </table>
</tag:page>