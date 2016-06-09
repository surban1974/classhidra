<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 
<%@ include file="../framework/header.jsp" %> 

	<link href="css/default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="javascript2012/jquery.min.js"></script>
	<script src="javascript2012/mobilyblocks.js" type="text/javascript"></script>

	<style type="text/css">

		div.comp {
			background:url(images/decor/128_pdf.gif) no-repeat center center;
			width:128px;
			height:128px;
			display:block;
			cursor:pointer;
			position:relative;
			margin:0 auto;
		}

	</style>

	<script type="text/javascript">

		$(function(){
			$('.comp').mobilyblocks({
				trigger: 'click',
				direction: 'counter',
				duration:500,
				zIndex:50,
				widthMultiplier:1.40
			});
		});

	</script>
<script language="javascript" src="javascript2012/application/login.js"></script>

<body >

<div id="content_Panel_canvas" 
style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; overflow:hidden;" 
>
<table border="0" width="100%" height="100%" cellpadding="0" cellspacing="0">
<tr>
<td align="center" bgcolor="">

<jsp:include page="/jsp/included/panel_top.jsp" >
	<jsp:param name="panel-id" value="panel_Login" />	
	<jsp:param name="panel-onclose" value="window.close();" />

</jsp:include>	

<div id="page" style=" width: 900; height: 530; background-color: white">
<%@ include file="../included/content_page_header.jsp" %>

<center>
	<bs:form method="post" onhistory="">
<table border="0" width="100%" height="100%" style="width: 100%; height: 100%;">
<tr>

<td align="center">
	<div class="wrapper">
		<div class="inner">
			<div id="4click" class="comp"> 

				<ul class="reset">

						<li><a href="javascript:void(window.open('http://neohort4ape.appspot.com'));"><img src="images/decor/neohort/download.gif"  /></a><br>
						<nobr>
						<table  cellspacing="0" cellpadding="0" border="0" onclick="javascript:void(window.open('http://neohort4ape.appspot.com'));">
							<tr>							
								<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
								<td style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><b>Download</b></span></nobr></td>
								<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
								
							</tr>
						</table>				
						</nobr>
						</li>
		




						<li><a href="javascript:void(showAsIFrame('report_neohort?source=/jsp/neohort/chart0.jsp',900,500));"><img src="images/decor/neohort/chart0.gif"  /></a><br>
						<nobr>
						<table  cellspacing="0" cellpadding="0" border="0" onclick="javascript:void(showAsIFrame('report_neohort?source=/jsp/neohort/chart0.jsp',900,500));">
							<tr>							
								<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
								<td style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><b>Chart Example 1 </b></span></nobr></td>
								<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
								
							</tr>
						</table>				
						</nobr>
						</li>


						<li><a href="javascript:void(showAsIFrame('report_neohort?source=/jsp/neohort/chart1.jsp',900,500));"><img src="images/decor/neohort/chart1.gif"  /></a><br>
						<nobr>
						<table  cellspacing="0" cellpadding="0" border="0" onclick="javascript:void(showAsIFrame('report_neohort?source=/jsp/neohort/chart1.jsp',900,500));">
							<tr>							
								<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
								<td style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><b>Chart Example 2 </b></span></nobr></td>
								<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
								
							</tr>
						</table>				
						</nobr>
						</li>
						

						<li><a href="javascript:void(showAsIFrame('report_neohort?source=/jsp/neohort/chart2.jsp',900,500));"><img src="images/decor/neohort/chart2.gif"  /></a><br>
						<nobr>
						<table  cellspacing="0" cellpadding="0" border="0" onclick="javascript:void(showAsIFrame('report_neohort?source=/jsp/neohort/chart2.jsp',900,500));">
							<tr>							
								<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
								<td style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><b>Chart Example 3 </b></span></nobr></td>
								<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
								
							</tr>
						</table>				
						</nobr>
						</li>

						<li><a href="javascript:void(showAsIFrame('report_neohort?source=/jsp/neohort/chart3.jsp',900,500));"><img src="images/decor/neohort/chart3.gif"  /></a><br>
						<nobr>
						<table  cellspacing="0" cellpadding="0" border="0" onclick="javascript:void(showAsIFrame('report_neohort?source=/jsp/neohort/chart3.jsp',900,500));">
							<tr>							
								<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
								<td style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><b>Chart Example 4 </b></span></nobr></td>
								<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
								
							</tr>
						</table>				
						</nobr>
						</li>

						<li><a href="javascript:void(showAsIFrame('report_neohort?source=/jsp/neohort/chart4.jsp',900,500));"><img src="images/decor/neohort/chart4.gif"  /></a><br>
						<nobr>
						<table  cellspacing="0" cellpadding="0" border="0" onclick="javascript:void(showAsIFrame('report_neohort?source=/jsp/neohort/chart4.jsp',900,500));">
							<tr>							
								<td  width="0%"  height="20"><img src="images/corners/vb_left.gif" border="0"/></td>
								<td style="background-image:url('images/corners/vb_center.gif')" align="center" width="0%" ><nobr><span class="title_section"><b>Chart Example 5 </b></span></nobr></td>
								<td  width="0%"  height="20"><img src="images/corners/vb_right.gif" border="0"/></td>
								
							</tr>
						</table>				
						</nobr>
						</li>


				</ul>			

			</div>	
		</div>	
		</div>	

</td>
</tr>
</table>
	</bs:form>
<script>
$(document).ready(function(){$(".trigger").click();})
</script>
</center>



</div>
<jsp:include  page="/jsp/included/panel_bottom.jsp" />


</td>
</tr>
</table>
</div>
</body>
<%@ include file="../framework/footer.jsp" %>

