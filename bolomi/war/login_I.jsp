<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
 function onload()
{
	 var url = location.href; //获取url中"?"符后的字串
	 var theRequest = new Object();
	 if (url.indexOf("?") != -1) {
	      var str = url.substr(url.indexOf("?")+1);
	      strs = str.split("&");
	      for(var i = 0; i < strs.length; i ++) {
	         theRequest[strs[i].split("=")[0]]=(strs[i].split("=")[1]);
	      }
	   }
	 var myForm = document.createElement("form"); 
	 myForm.method="Post" ; 
	 myForm.action = "login.htm" ; 
	 var myInput = document.createElement("input") ; 
	 myInput.setAttribute("name", "username") ; 
	 myInput.setAttribute("value", theRequest.username); 
	 myForm.appendChild(myInput) ; 

	 var myInput2 = document.createElement("input") ; 
	 myInput2.setAttribute("name", "password") ; 
	 myInput2.setAttribute("value", theRequest.password); 
	 myForm.appendChild(myInput2) ; 
	
	 document.body.appendChild(myForm) ; 
	 myForm.submit() ; 
	 document.body.removeChild(myForm) ;
} 
/* function onload()
{
	 alert(""); 
	
	 var myForm = document.createElement("form"); 
	 myForm.method="Post" ; 
	 myForm.action = "http://www.baidu.com" ; 
	
	 document.body.appendChild(myForm) ; 
	 myForm.submit() ; 
	 document.body.removeChild(myForm) ;
} */
</script>
</head>
<body onload="onload()">

</body>
</html>