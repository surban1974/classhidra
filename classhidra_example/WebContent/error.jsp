
  
 
<html> 
<head>
</head>
<%  
	Exception erroraction = null;
	try{
		erroraction = (Exception)request.getAttribute("$erroraction");
	}catch(Exception e){
	}	
%>
<body>
<center>
<table width="500">
<tr>
<td bgcolor=red >
		<table  width="500">
			<tr>
				<td align="center" bgcolor="white"> 
					<font color=red>
					<b>Generic Controller ERROR </b><br><%if(erroraction!=null){%><%=erroraction.toString()%><%}%>
					</font>
				</td>
			</tr>			
		</table>
</td>
</tr>
</table>
</center>
</body>
</html>