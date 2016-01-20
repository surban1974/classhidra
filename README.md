classhidra
=======

ClassHidra, Java open-source MVC ( model view controller ) implementation.
Servlets and JSP technology.
Web Application Development.
Implementation:
- CDI ( context dependency injection ) - @SessionScoped, @ApplicationScoped - classhidra2cdi.jar plugin;
- EJB (enterprise java bean) map action/form as @Stateless, @Stateful, @Singleton;
- Spring(4.*) map action/form as @Component;  
Compatible:
Google AppEngine - http://classhidra4ape.appspot.com 
<br>
<b>Maven</b>:<br>

Maven repository:<br>
&lt;repositories&gt;
&lt;id&gt;classhidra-mvn-repo&lt;/id&gt;
&lt;url&gt;https://github.com/surban1974/classhidra/tree/mvn-repo/&lt;/url&gt;
&lt;snapshots&gt;
&lt;enabled&gt;true&lt;/enabled&gt;
&lt;updatePolicy&gt;always&lt;/updatePolicy&gt;
&lt;/snapshots&gt;
&lt;/repository&gt;
&lt;/repositories&gt;

Maven dependencies (jar):<br>
&lt;dependency&gt;<br>
&lt;groupId&gt;com.github.surban1974.classhidra&lt;/groupId&gt;<br>
&lt;!-- JAR<br>
&lt;artifactId&gt;classhidra-base&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-spring&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-cdi&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-ejb&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-jboss7-vfs&lt;/artifactId&gt;<br>
--&gt;<br>
&lt;!-- WAR<br>
&lt;artifactId&gt;classhidra-base-example&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-spring-example&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-cdi-example&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-ejb-example&lt;/artifactId&gt;<br>
--&gt;<br>
&lt;version&gt;1.5.3&lt;/version&gt;<br>
&lt;/dependency<br>
