<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>

<title>Login</title>

<link href="resources/assets/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="resources/assets/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
<link href="resources/assets/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="resources/assets/uniform.default.css" rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link href="resources/assets/login2.css" rel="stylesheet" type="text/css" />
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME STYLES -->
<link href="resources/assets/components.css" id="style_components" rel="stylesheet" type="text/css" />
<link href="resources/assets/plugins.css" rel="stylesheet" type="text/css" />
<link href="resources/assets/css/layout.css" rel="stylesheet" type="text/css" />
<link href="resources/assets/css/themes/darkblue.css" rel="stylesheet" type="text/css" id="style_color" />
<link href="resources/assets/css/custom.css" rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->
<link rel="shortcut icon" href="favicon.ico" />

</head>

<body style="font-family:\5FAE\8F6F\96C5\9ED1;" class="login">
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
<div class="menu-toggler sidebar-toggler">
</div>
<!-- END SIDEBAR TOGGLER BUTTON -->
<!-- BEGIN LOGO -->
<div class="logo">
	<a href="index.html">
	<img src="images/logo_login.png"  alt="" />
	</a>
</div>
<!-- END LOGO -->
<!-- BEGIN LOGIN -->
<div class="content">
	<!-- BEGIN LOGIN FORM -->
	<form class="login-form" action="login.htm" method="post" novalidate="novalidate">
		<div class="form-title">
			<span class="form-title">Welcome.</span>
			
		</div>
		<div class="alert alert-danger display-hide">
			<button class="close" data-close="alert"></button>
			<span>
			Enter any username and password. </span>
		</div>
		<div class="form-group">
			<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
			<label class="control-label visible-ie8 visible-ie9"><fmt:message key="login.validation.noUsername"/></label>
			<input class="form-control form-control-solid placeholder-no-fix" type="text" autocomplete="off" placeholder="<fmt:message key="login.validation.noUsername"/>" id="username" name="username" />
		</div>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9"><fmt:message key="login.validation.noPassword"/></label>
			<input class="form-control form-control-solid placeholder-no-fix"  type="password" autocomplete="off" placeholder="<fmt:message key="login.validation.noPassword"/>" id="password" name="password" />
		</div>
		<div class="form-actions">
			<button type="submit" style="font-size: 16px;" class="btn btn-primary btn-block uppercase"><fmt:message key="login.loginButton"/></button>
		</div>
		
		 <spring:bind path="login">
              <c:if test="${status.error}">
               
                  <c:forEach items="${status.errorMessages}" var="error">
                    <c:out value="${error}"/><br/>
                  </c:forEach>
                
              </c:if>
            </spring:bind>
		<div class="create-account">
			<p>
				<fmt:message key="login.userId"/><strong>admin</strong>  <fmt:message key="login.password"/><strong>admin</strong>
			</p>
			<p>
				<fmt:message key="bolomi.vesion"/>
			</p>
		</div>
	</form>
	<!-- END LOGIN FORM -->
	
	
	
	
	
	
</div>

<!-- END LOGIN -->























</body>
</html>