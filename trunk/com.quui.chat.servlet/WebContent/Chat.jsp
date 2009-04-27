<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Hollala JSP</title>
<jsp:useBean id="formHandler" class="com.quui.chat.servlet.HollalaBean">
	<jsp:setProperty name="formHandler" property="*" />
	<%
	String t= request.getParameter("topicFile");
	String i = request.getParameter("userInput");
	if(t!=null)
		formHandler.setTopicFileName(t);
	if(i!=null)
		formHandler.setUserInput(i);
	formHandler.doit();
	%>
</jsp:useBean>
</head>
<body onload="document.form1.userInput.focus();">
<table width="100%">
	<tr>
		<td align="left"><font size="1">Welcome to Hollala! It is now <%=new java.util.Date()%>
		</font> <font size="1">| Topic: <jsp:getProperty
			name="formHandler" property="topic" /> | Status: <jsp:getProperty
			name="formHandler" property="emotion" /> | File: 
            <jsp:setProperty name="formHandler" property="topicFileName" />
         	<jsp:getProperty name="formHandler" property="topicFileName" /> 
         </font>
         </td>
		<td align="right"><a href="http://www.quui.com/hollala"> <font
		size="1">home</font> </a></td>
	</tr>
</table>
<hr size="1" />
<table width="100%">
	<tr>
		<td align="left" width="70%">
		<p><font size="1">welcome! when you talk about something i think i
		know about i'll be :) and try to talk to you on that topic. also i
		will try to learn what you say about that topic and related topics. if
		you talk about something thats new to me i'll be :o and try to learn
		it. you can also talk to me on IRC at irc.freenode.net in channel
		#hollala. have fun!</font></p>
		<h2>
		<jsp:getProperty name="formHandler" property="hollalaAnswer" />
		</h2>
		<!-- conversation input -->
		<form name="form1" action="/com.quui.chat.servlet/Chat.jsp"
			method="post"><input name="userInput" size="60" />
		
		<!-- topic file input -->
		
			<input
			name="topicFile" value="<%=formHandler.getTopicFileName() %>"
			size="60" /></form>
		</td>
		<td align="right"><img src="/com.quui.chat.servlet/hollala.jpg" alt="" /></td>
	</tr>
</table>
<hr size="1" />
<table width="100%">
	<tr>
		<!--  td align="right"><font size="1">last input and answer</font></td>-->
	</tr>
</table>
<table width="100%">
	<tr>
		<td align="right">
		<p><font size="1"> <jsp:getProperty name="formHandler"
			property="conversationString" /> </font></p>
		</td>
	</tr>
</table>
</body>
</html>

