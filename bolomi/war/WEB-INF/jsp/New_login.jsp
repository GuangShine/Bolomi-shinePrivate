<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>

<title>Login</title>

<link rel="stylesheet" type="text/css" href="resources/Login_style.css"/>

</head>

<body>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<h2 align="center">
<img src="images/logo_login.png"></img></h2>
<div class="login_frame"></div>
<div class="LoginWindow">
  <div>
    <form method="post" action="login.htm"class="login">
    <p>
      <label for="login"><fmt:message key="login.userId"/></label>
       <input id="username" type="text" name="username" value="${status.value}" maxlength="40"/>
    </p>

    <p>
      <label for="password"><fmt:message key="login.password"/></label>
      <input id="password" type="password" name="password" value="${status.value}" maxlength="20"/>
    </p>

    <p class="login-submit">
      <button type="submit" class="login-button"><fmt:message key="login.loginButton"/></button>
    </p>

    </form>   
  </div>
   <spring:bind path="login">
              <c:if test="${status.error}">
                <td colspan="3" class="formError">
                  <c:forEach items="${status.errorMessages}" var="error">
                    <c:out value="${error}"/><br/>
                  </c:forEach>
                </td>
              </c:if>
            </spring:bind>
</div>

</body>
</html>