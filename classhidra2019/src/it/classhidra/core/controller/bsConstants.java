/**
* Creation date: (07/04/2006)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com 
*/

/********************************************************************************
*
*	Copyright (C) 2005  Svyatoslav Urbanovych
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General public final License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General public final License for more details.

* You should have received a copy of the GNU General public final License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*********************************************************************************/
package it.classhidra.core.controller;

 
public interface bsConstants {

	public final static String CONST_DB_DATI 							=	"db_dati";
	
	public final static String CONST_ID_USER_INIT 						=	"application.class.init";
	public final static String CONST_ID_PATHCONFIG 						=	"application.path.config";
	public final static String CONST_ID_PATHROOT 						=	"application.path.root";
	public final static String CONST_ID 								=	"$id_action";
	public final static String CONST_ID_CALL 							=	"$id_action_call";
	public final static String CONST_ID_COMPLETE						=	"$id_action_complete";
	public final static String CONST_ID_REQUEST_TYPE 					= 	"$request_type";
	public final static String CONST_ID_EXEC_TYPE 						= 	"$exec_type";
	public final static String CONST_ID_JS4AJAX 						=	"js4ajax";	
	public final static String CONST_REQUEST_TYPE_FORWARD 				=	"$forward";
	public final static String CONST_REQUEST_TYPE_INCLUDE 				=	"$include";
	public final static String CONST_AUTH_TICKER 						=	"classHidra_ticker";
	public final static String CONST_XML_PREFIX 						=	"classhidra-";
	public final static String CONST_XML_ACTIONS 						=	"actions.xml";
	public final static String CONST_XML_MENU 							=	"menu.xml";
	public final static String CONST_XML_AUTHENTIFICATIONS 				=	"authentication.xml";
	public final static String CONST_XML_AUTHENTIFICATIONS_FOLDER		=	"authentications";
	public final static String CONST_XML_MESSAGES 						=	"messages.xml";
	public final static String CONST_XML_MESSAGES_FOLDER				=	"messages";
	public final static String CONST_XML_ACTIONS_FOLDER 				=	"actions";
	public final static String CONST_XML_USERS 							=	"users.xml";
	public final static String CONST_XML_JAAS 							= 	"jaas.config";
	public final static String CONST_XML_ORGANIZATION					=	"organization.xml";
	
	public final static String CONST_LOG_FOLDER 						=	"log";
	
	public final static String CONST_ID_$ACTION 						=	"$action";
	public final static String CONST_ID_MENU_MENUSOURCE 				=	"menuSource";
	public final static String CONST_ID_$MIDDLE_ACTION 					=	"middleAction";
	public final static String CONST_ID_$COUNT_ACTIONS 					=	"countActions";
	
	public final static String CONST_ID_$REDIRECT_ACTION 				=	"$redirect_action";

	public final static String CONST_ID_$ACTION_HELP 					=	"$help";
	public final static String CONST_ID_$NAVIGATION 					=	"$navigation";
	public final static String CONST_ID_$LAST_NAVIGATION 				=	"$last_navigation";
	public final static String CONST_ID_$CSRF 							=	"$csrf";	
	
//	TODO	@Deprecated
	public final static String CONST_ID_$ONLYISSESSION_ 				=	"$onlyissession";
	
	public final static String CONST_ID_$ACTION_FROM 					=	"$action_from";
	public final static String CONST_ID_$ACTION_WAC 					=	"$wac_fascia";
	public final static String CONST_BEAN_$AUTHENTIFICATION 			=	"$authentication";
	public final static String CONST_REST_$AUTHENTIFICATION 			=	"$restauthentication";

//	TODO	@Deprecated
	public final static String CONST_BEAN_$NAVIGATION 					=	"$navigation";

//	TODO	@Deprecated
	public final static String CONST_BEAN_$ONLYINSSESSION 				=	"$onlyinssession";
	
	public final static String CONST_BEAN_$SERVLETCONTEXT 				=	"$servletcontext";
	
	public final static String CONST_BEAN_$ONLYASLASTINSTANCE			=	"$onlyaslastinstance";
	
//	TODO	@Deprecated
	public final static String CONST_BEAN_$LOCAL_CONTAINER 				=	"$local_container";
	public final static String CONST_BEAN_$ACTION_CONFIG 				=	"$action_config";
	public final static String CONST_BEAN_$MESS_CONFIG 					=	"$mess_config";
	public final static String CONST_BEAN_$AUTH_CONFIG 					=	"$auth_config";
	public final static String CONST_BEAN_$ORGANIZATION_CONFIG 			=	"$organization_config";
	public final static String CONST_BEAN_$MENU_CONFIG 					=	"$menu_config";
	public final static String CONST_BEAN_$USERS_CONFIG 				=	"$users_config";
	
	public final static String CONST_SERVICE_$PARENT_POINTOFLAUNCH 		=	"$parent_pointOfLaunch";
	public final static String CONST_SERVICE_$CHILD_POINTOFRETURN 		=	"$child_pointOfReturn";
	public final static String CONST_SERVICE_$ID_POINTOFSERVICE 		=	"$id_pointOfService";
	public final static String CONST_SERVICE_$ID_RETURNPOINTOFSERVICE 	=	"$id_returnPointOfService";

	public final static String CONST_BEAN_$PREVINSTANCEACTION 			=	"$previnstanceaction";
	public final static String CONST_BEAN_$INSTANCEACTION 				=	"$instanceaction";
	public final static String CONST_BEAN_$INSTANCEACTIONPOOL			=	"$instanceactionpool";
	public final static String CONST_BEAN_$ERRORACTION 					=	"$erroraction";							
	public final static String CONST_BEAN_$LISTMESSAGE 					=	"$listmessages";
	public final static String CONST_BEAN_$LISTGETPERMITED 				=	"$listgetpermited";
	
	public final static String CONST_ROLE_SEPARATOR 					=	"^";
	
	public final static String CONST_TAG_REQUESTPARAMETER 				=	"REQUEST.PARAMETER";
	public final static String CONST_TAG_SYSTEMPROPERTY 				=	"SYSTEM.PROPERTY";
	
	public final static String CONST_AUTH_TARGET_DOMINO 				=	"DOMINO";
	public final static String CONST_AUTH_TARGET_DOMAIN 				=	"DOMAIN";
	public final static String CONST_AUTH_TARGET_BRANCH 				=	"BRANCH";
	public final static String CONST_AUTH_TARGET_ISTITUTION   			=	"ISTITUTION";
	
	
	public final static String CONST_PROVIDER_PATH						=	"it.classhidra.plugin.provider.";
	public final static String CONST_SESSION_LOG						=	"classhidra.SESSION_LOG";
	public final static String CONST_DB_CONNECTION_STATE				=	"classhidra.DB_CONNECTION_STATE";
	
	public final static String app_id_property 							=	"application.app.property";
	public final static String auth_id_property 						=	"application.auth.property";
	public final static String db_id_property 							=	"application.db.property";
	public final static String log_id_property 							=	"application.log.property";
	public final static String resources_id_property 					=	"application.resources.property";
	public final static String synonyms_id_property 					=	"application.synonyms.property";

	public final static String actions_id_xml							=	"application.actions.xml";
	public final static String authentication_id_xml					=	"application.authentication.xml";
	public final static String users_id_xml								=	"application.users.xml";
	public final static String menu_id_xml								=	"application.menu.xml";
	public final static String messages_id_xml							=	"application.messages.xml";
	public final static String messages_id_folder_xml					=	"application.messages.folder_xml";
	public final static String model_id_folder_xml						=	"application.model.folder_xml";
	public final static String organization_id_xml 						= 	"application.organization.xml";
	
	public final static String CONST_SSOID								=	"ssoid";
	
	public final static String CONST_ID_INPUTBASE64						=	"$inputBase64";
	public final static String CONST_ID_OUTPUT4SOAP						=	"$output4SOAP";
	public final static String CONST_ID_OUTPUT4JSON						=	"$output4JSON";

	public final static String CONST_ID_OUTPUT4BYTE						=	"$output4BYTE";
	public final static String CONST_ID_TRANSFORMATION4WRAPPER			=	"$transformation4WRAPPER";
	public final static String CONST_ID_TRANSFORMATION4CONTROLLER		=	"$transformation4CONTROLLER"; 
	public final static String CONST_ID_SOURCESTREAM					=	"$source_stream"; 
	
	public final static String CONST_TRANSFORMATION_ELMODE_INCLUDE		=	"include";
	public final static String CONST_TRANSFORMATION_ELMODE_FORWARD		=	"forward";
	public final static String CONST_TRANSFORMATION_ELMODE_BOTH			=	"both";

	public final static String CONST_TRANSFORMATION_ELPOINT_CONTROLLER	=	"controller";
	public final static String CONST_TRANSFORMATION_ELPOINT_FILTER		=	"filter";

	public final static String CONST_EXTENTION_DO						=	".bs";
	public final static String CONST_SESSION_ACTIONS_INSTANCE			=	"$session_actions_instance";
	
	public final static String CONST_ID_STATISTIC_STACK					=	"$statisticStack";
	public final static String CONST_ID_STATISTIC_PROVIDER				=	"$statisticProvider";
	public final static int    CONST_LEN_STATISTIC_STACK				=	100;
	
	public final static String CONST_DIRECTINDACTION_bsLog				=	"bsLog";
	public final static String CONST_DIRECTINDACTION_bsActions			=	"bsActions";
	public final static String CONST_DIRECTINDACTION_bsLogS				=	"bsLogS";
	public final static String CONST_DIRECTINDACTION_Controller			=	"Controller";
	public final static String CONST_DIRECTINDACTION_bsResource			=	"bsResource";
	public final static String CONST_DIRECTINDACTION_bsStatistics		=	"bsStatistics";
	public final static String CONST_DIRECTINDACTION_bsTransformation	=	"bsTransformation";
	public final static String CONST_DIRECTINDACTION_bsLoadFromResources=	"bsLoadFromResources";
	public final static String CONST_DIRECTINDACTION_bsLoadFromFramework=	"bsLoadFromFramework";
	public final static String CONST_DIRECTINDACTION_bsLoadSrc			=	"src";
	public final static String CONST_DIRECTINDACTION_bsLoadType			=	"type";
	public final static String CONST_DIRECTINDACTION_bsLoadCache		=	"cache";
	public final static String CONST_DIRECTINDACTION_bsLoadEncoding		=	"encoding";
	
	public final static String CONST_ID_EXEC_TYPE_CLONED				= 	"cloned";
	
	public final static String CONST_CONTAINER_REFMETHODS				=	"$container_refmethods";
	public final static String CONST_CONTAINER_STREAMS_INSTANCE			=	"$container_streams_instance";
	public final static String CONST_BEAN_$SCHEDULER_CONTAINER 			=	"$scheduler_container";
	public final static String CONST_REST_URLMAPPEDPARAMETERS			=	"$restUrlMappedParameters";
	
	public final static String CONST_RECOVERED_REQUEST_CONTENT			=	"$recoveredRequestContent";
	public final static String CONST_ASYNC_INFO							=	"$async_info";




	
}
