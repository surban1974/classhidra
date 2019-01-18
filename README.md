classhidra
=======

ClassHidra, Java open-source MVC ( model view controller ) implementation.
Servlets and JSP technology.
Web Application Development.
Implementation:
- CDI ( context dependency injection ) - @SessionScoped, @ApplicationScoped - classhidra2cdi.jar plugin;
- EJB (enterprise java bean) map action/form as @Stateless, @Stateful, @Singleton;
- Spring(4.*) map action/form as @Component;  
- RESTfull compatible (alfa)

Compatible:
Google AppEngine - http://classhidra4ape.appspot.com 
<br>
Maven:<br>

Maven repository:<br>
&lt;repositories&gt;<br>
&lt;id&gt;classhidra-mvn-repo&lt;/id&gt;<br>
&lt;url&gt;https://github.com/surban1974/classhidra/raw/mvn-repo/ &lt;/url&gt;<br>
&lt;snapshots&gt;<br>
&lt;enabled&gt;true&lt;/enabled&gt;<br>
&lt;updatePolicy&gt;always&lt;/updatePolicy&gt;<br>
&lt;/snapshots&gt;<br>
&lt;/repository&gt;<br>
&lt;/repositories&gt;<br>

Maven dependencies (jar):<br>
&lt;dependency&gt;<br>
&lt;groupId&gt;com.github.surban1974.classhidra&lt;/groupId&gt;<br>
&lt;!-- RAR<br>
&lt;artifactId&gt;classhidra-origin&lt;/artifactId&gt;<br>
--&gt;<br>
&lt;!-- JAR<br>
&lt;artifactId&gt;classhidra-base&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-base-7&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-spring&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-spring-7&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-cdi&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-cdi-7&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-ejb&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-ejb-7&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-jboss7-vfs&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-jboss7-vfs-7&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-jetty-embedded&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-tag-components&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-tag-components-7&lt;/artifactId&gt;<br>
--&gt;<br>
&lt;!-- WAR<br>
&lt;artifactId&gt;classhidra-base-example&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-spring-example&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-cdi-example&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-ejb-example&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-cdi-ejb-example&lt;/artifactId&gt;<br>
&lt;artifactId&gt;classhidra-starter-7&lt;/artifactId&gt;<br>
--&gt;<br>
&lt;version&gt;1.5.5&lt;/version&gt;<br>
&lt;/dependency&gt;<br>
