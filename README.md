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
&lt;dependency&gt;
&lt;groupId&gt;com.github.surban1974.classhidra&lt;/groupId&gt;
&lt;!-- JAR
&lt;artifactId&gt;classhidra-base&lt;/artifactId&gt;
&lt;artifactId&gt;classhidra-cdi&lt;/artifactId&gt;
&lt;artifactId&gt;classhidra-ejb&lt;/artifactId&gt;
&lt;artifactId&gt;classhidra-jboss7-vfs&lt;/artifactId&gt;
--&gt;
&lt;!-- WAR
&lt;artifactId&gt;classhidra-base-example&lt;/artifactId&gt;
&lt;artifactId&gt;classhidra-cdi-example&lt;/artifactId&gt;
&lt;artifactId&gt;classhidra-ejb-example&lt;/artifactId&gt;
--&gt;
&lt;version&gt;1.5.2&lt;/version&gt;
&lt;/dependency