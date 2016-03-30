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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@include file="/WEB-INF/tags/decl.tagf"%>
<%@attribute name="styles" fragment="true" %>
<%@attribute name="dwr" %>
<%@attribute name="js" %>
<%@attribute name="onload" %>

<html>
<head>
  <title><c:choose>
    <c:when test="${!empty instanceDescription}">${instanceDescription}</c:when>
    <c:otherwise><fmt:message key="header.title"/></c:otherwise>
  </c:choose></title>
  
  <!-- Meta -->
  <meta http-equiv="content-type" content="application/xhtml+xml;charset=utf-8"/>
  <meta http-equiv="Content-Style-Type" content="text/css" />
  <meta name="Copyright" content="&copy;2006-2011 Serotonin Software Technologies Inc."/>
  <meta name="DESCRIPTION" content="Mango Serotonin Software"/>
  <meta name="KEYWORDS" content="Mango Serotonin Software"/>

  
  <!-- Style -->
   <link rel="icon" href="images/favicon.ico"/>
  <link rel="shortcut icon" href="images/favicon.ico"/>
  <link href="resources/common.css" type="text/css" rel="stylesheet"/>
  <jsp:invoke fragment="styles"/>

  
   <!-- Scripts -->
  <script type="text/javascript">var djConfig = { isDebug: false, extraLocale: ['en-us', 'nl', 'nl-nl', 'ja-jp', 'fi-fi', 'sv-se', 'zh-cn', 'zh-tw','xx'] };</script>
  <!-- script type="text/javascript" src="http://o.aolcdn.com/dojo/0.4.2/dojo.js"></script -->
  <script type="text/javascript" src="resources/dojo/dojo.js"></script>
  <script type="text/javascript" src="dwr/engine.js"></script>
  <script type="text/javascript" src="dwr/util.js"></script>
  <script type="text/javascript" src="dwr/interface/MiscDwr.js"></script>
  <script type="text/javascript" src="resources/soundmanager2-nodebug-jsmin.js"></script>
  <script type="text/javascript" src="resources/common.js"></script>
  <c:forEach items="${dwr}" var="dwrname">
    <script type="text/javascript" src="dwr/interface/${dwrname}.js"></script></c:forEach>
  <c:forEach items="${js}" var="jsname">
    <script type="text/javascript" src="resources/${jsname}.js"></script></c:forEach>
  <script type="text/javascript">
    mango.i18n = <sst:convert obj="${clientSideMessages}"/>;
  </script>
  <c:if test="${!simple}">
    <script type="text/javascript" src="resources/header.js"></script>
    <script type="text/javascript">
      dwr.util.setEscapeHtml(false);
      <c:if test="${!empty sessionUser}">
        dojo.addOnLoad(mango.header.onLoad);
        dojo.addOnLoad(function() { setUserMuted(${sessionUser.muted}); });
      </c:if>
      
      function setLocale(locale) {
          MiscDwr.setLocale(locale, function() { window.location = window.location });
      }
      
      function setHomeUrl() {
          MiscDwr.setHomeUrl(window.location.href, function() { alert("Home URL saved"); });
      }
      
      function goHomeUrl() {
          MiscDwr.getHomeUrl(function(loc) { window.location = loc; });
      }
    </script>
  </c:if> 
</head>

<body>



<table width="100%" cellspacing="0" cellpadding="0" border="0" id="mainHeader">
  <tr>
    <td>	<a href="index.html"><img style="width:200;height:75px;" src="images/logo3.jpg" alt="Logo"/></a></td>
    <c:if test="${!simple}">
      <td align="center" width="99%">
        <a href="events.shtm">
          <span id="__header__alarmLevelDiv" style="display:none;">
            <img id="__header__alarmLevelImg" src="images/alert.pb" alt="" border="0" title=""/>
            <span id="__header__alarmLevelText"></span>
          </span>
        </a>
      </td>
    </c:if>
    <c:if test="${!empty instanceDescription}">
      <td align="right" valign="bottom" class="smallTitle" style="padding:5px; white-space: nowrap;">${instanceDescription}</td>
    </c:if>
  </tr>
</table>

<c:if test="${!simple}">
  <table width="100%" cellspacing="0" cellpadding="0" border="0" id="subHeader">
    <tr>
      <td style="cursor:default" >
        <c:if test="${!empty sessionUser}">

        <c:if test="${fn:substring(sessionUser.permission,0,1)==1}"> 
          <tag:menuItem  href="watch_list.shtm" png="wathchlist" key="header.watchlist"/>
           </c:if> 
           <%--don't delete --%>
           <%--<tag:menuItem href="views.shtm" png="icon_view" key="header.views"/> --%>
           <c:if test="${fn:substring(sessionUser.permission,2,3)==1}"> 
          <tag:menuItem href="events.shtm" png="alerm" key="header.alarms"/>
          </c:if>
          <%--don't delete --%>
          <%--  <tag:menuItem href="reports.shtm" png="report" key="header.reports"/> --%>
                
          <c:if test="${sessionUser.dataSourcePermission}">
             <c:if test="${fn:substring(sessionUser.permission,6,7)==1}"> 
            <tag:menuItem href="data_sources.shtm" png="datasources" key="header.dataSources"/>
            </c:if>
         	<%--<c:if test="${fn:substring(sessionUser.permission,4,5)==1}"> 
            <tag:menuItem href="event_handlers.shtm" png="systemsetting" key="header.eventHandlers"/>
            </c:if> --%>
            <%--don't delete --%>
			<%--<tag:menuItem href="scheduled_events.shtm" png="dataser" key="header.scheduledEvents"/>
            <tag:menuItem href="compound_events.shtm" png="lingdang" key="header.compoundEvents"/>
            <tag:menuItem href="point_links.shtm" png="links" key="header.pointLinks"/> --%>
          </c:if>
          
         
           <c:if test="${fn:substring(sessionUser.permission,8,9)==1}"> 
          <tag:menuItem href="users.shtm" png="users" key="header.users"/>
          	</c:if>
          <c:if test="${sessionUser.admin}">
         	<%--<c:if test="${fn:substring(sessionUser.permission,10,11)==1}"> 
            <tag:menuItem href="point_hierarchy.shtm" png="point" key="header.pointHierarchy"/>
            </c:if> --%>
           <%--   <c:if test="${fn:substring(sessionUser.permission,12,13)==1}"> 
            <tag:menuItem href="mailing_lists.shtm" png="mail" key="header.mailingLists"/>
            </c:if> --%>
              <c:if test="${fn:substring(sessionUser.permission,14,15)==1}"> 
            <tag:menuItem href="publishers.shtm" png="publish" key="header.publishers"/>
            </c:if> 
           <%--<tag:menuItem href="maintenance_events.shtm" png="hammer" key="header.maintenanceEvents"/> --%>
             <c:if test="${fn:substring(sessionUser.permission,16,17)==1}"> 
            <tag:menuItem href="system_settings.shtm" png="evebthandls" key="header.systemSettings"/>
            </c:if>
             <c:if test="${fn:substring(sessionUser.permission,18,19)==1}"> 
            <tag:menuItem href="emport.shtm" png="impexp" key="header.emport"/>
            </c:if>
             <c:if test="${fn:substring(sessionUser.permission,20,21)==1}"> 
            <%-- <tag:menuItem href="sql.shtm" png="SQL" key="header.sql"/> --%>
            </c:if>
          </c:if>
          
          
          <tag:menuItem href="logout.htm" png="logout" key="header.logout"/>
         <%--<tag:menuItem href="help.shtm" png="help" key="header.help"/>--%>
        </c:if>
        <c:if test="${empty sessionUser}">
         <%--  <tag:menuItem href="login.htm" png="login" key="header.login"/> --%>
        </c:if>
        <div id="headerMenuDescription" class="labelDiv" style="position:absolute;display:none;"></div>
      </td>
      
      <td align="right">
        <c:if test="${!empty sessionUser}">
          <span style="font-size: 15px;" class="copyTitle"><fmt:message key="header.user"/>: <b>${sessionUser.username}</b></span>
          <tag:img  id="userMutedImg" title="header.mute" onclick="MiscDwr.toggleUserMuted(setUserMuted)" onmouseover="hideLayer('localeEdit')"/>
<%--           <tag:img png="house" title="header.goHomeUrl" onclick="goHomeUrl()" onmouseover="hideLayer('localeEdit')"/>
          <tag:img png="house_link" title="header.setHomeUrl" onclick="setHomeUrl()" onmouseover="hideLayer('localeEdit')"/> --%>
        </c:if>
        <div style="display:inline;" onmouseover="showMenu('localeEdit', -40, 10);">
          <!--<tag:img png="world" title="header.changeLanguage"/>-->
          <div id="localeEdit" style="visibility:hidden;left:0px;top:15px;" class="labelDiv" onmouseout="hideLayer(this)">
            <c:forEach items="${availableLanguages}" var="lang">
              <a class="ptr" onclick="setLocale('${lang.key}')">${lang.value}</a><br/>
            </c:forEach>
          </div>
        </div>
      </td>
    </tr>
  </table>
</c:if>

<div style="padding:5px;">
  <jsp:doBody/>
</div>
<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr><td colspan="2">&nbsp;</td></tr>
  <tr>
    <td colspan="2" class="footer" align="center">Copyright © 2014.Cloudinnov All rights reserved.<fmt:message key="footer.rightsReserved"/></td>
  </tr>
</table>
<c:if test="${!empty onload}">
  <script type="text/javascript">dojo.addOnLoad(${onload});</script>
</c:if>

</body>
</html>