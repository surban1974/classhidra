classhidra
=======

ClassHidra, Java open-source MVC ( model view controller ) implementation.
Servlets and JSP technology.
Web Application Development.
Implementation:
- CDI ( context dependency injection ) - @SessionScoped, @ApplicationScoped - classhidra2cdi.jar plugin;
- EJB (enterprise java bean) map action/form as @Stateless, @Stateful, @Singleton;
- Spring from v.1.2.0; 
Compatible:
Google AppEngine - http://classhidra4ape.appspot.com 

Maven dependencies (jar):
&lt;dependency&rt;
&lt;groupId&rt;com.github.surban1974.classhidra&lt;/groupId&rt;
&lt;!-- JAR
&lt;artifactId&rt;classhidra-base&lt;/artifactId&rt;
&lt;artifactId&rt;classhidra-cdi&lt;/artifactId&rt;
&lt;artifactId&rt;classhidra-ejb&lt;/artifactId&rt;
&lt;artifactId&rt;classhidra-jboss7-vfs&lt;/artifactId&rt;
--&rt;
&lt;!-- WAR
&lt;artifactId&rt;classhidra-base-example&lt;/artifactId&rt;
&lt;artifactId&rt;classhidra-cdi-example&lt;/artifactId&rt;
&lt;artifactId&rt;classhidra-ejb-example&lt;/artifactId&rt;
--&rt;
&lt;version&rt;1.5.2&lt;/version&rt;
&lt;/dependency