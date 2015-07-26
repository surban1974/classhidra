package examples.ejb.classhidra.framework.web.components;

import it.classhidra.annotation.elements.ActionMapping;
import it.classhidra.annotation.elements.Redirect;

@ActionMapping (
		redirects={
				@Redirect(
						path="/jsp/framework/login.jsp",
						descr="Login",
						mess_id="title_fw_Login"
				),
				@Redirect(
						path="/jsp/amanager/canvas.jsp",
						descr="Users/Groups/Authentications",
						mess_id="title_fw_amanager"
				),
				@Redirect(
						path="/jsp/builder/canvas.jsp",
						descr="Builder actions.xml (BETA)",
						mess_id="title_fw_builder"
				),
				@Redirect(
						path="/jsp/framework/content.jsp",
						descr="Content",
						mess_id="title_fw_Content"
				),
				@Redirect(
						path="/jsp/framework/log.jsp",
						descr="Log",
						mess_id="title_fw_Log"
				),
				@Redirect(
						path="/jsp/framework/log.jsp",
						descr="Log",
						mess_id="title_fw_Log"
				),
				@Redirect(
						path="/jsp/framework/log_users.jsp",
						descr="Users In Session",
						mess_id="title_fw_Log_users"
				),
				@Redirect(
						path="/jsp/framework/messagesUtility.jsp",
						descr="Generatore Messaggi",
						mess_id="title_fw_messagesUtility"
				),
				@Redirect(
						path="/jsp/qmf/qmftool_login.jsp",
						descr="DBMS",
						mess_id="title_fw_Qmf"
				),
				@Redirect(
						path="/jsp/framework/resources.jsp",
						descr="Resources",
						mess_id="title_fw_Resources"
				),
				@Redirect(
						path="/jsp/framework/sendmail.jsp",
						descr="Send Mail",
						mess_id="title_fw_SendMail"
				),
			}
		)

public class Mapper_redirects {

}
