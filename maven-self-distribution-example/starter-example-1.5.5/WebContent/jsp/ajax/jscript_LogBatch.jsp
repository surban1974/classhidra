<%@ taglib uri="/WEB-INF/tlds/bsController.tld" prefix="bs" %> 


<bs:sequence name="elementsview">				
	<bs:bean name="current_batch" property="elementsview" index="sequence"/>
<bs:equal bean="current_batch" name="state" value="0"> isrc="images/decor/24_start_g.gif" </bs:equal>	
<bs:equal bean="current_batch" name="state" value="-1"> isrc="images/decor/24_start_y.gif" </bs:equal>	
<bs:equal bean="current_batch" name="state" value="1"> isrc="images/decor/24_stop_r.gif" </bs:equal>		
document.getElementById("img_operation_<bs:formelement bean="current_batch" name="cd_btch" />").src=isrc;
document.getElementById("tm_last_<bs:formelement bean="current_batch" name="cd_btch"/>").innerHTML="<bs:formelement bean="current_batch" name="tm_last" formatOutput="dd/MM/yyyy HH:mm"/>";

</bs:sequence>
