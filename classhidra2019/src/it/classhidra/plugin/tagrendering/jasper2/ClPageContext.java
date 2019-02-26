package it.classhidra.plugin.tagrendering.jasper2;

import java.io.IOException;
import java.util.Enumeration;

import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

@SuppressWarnings("deprecation")
public class ClPageContext extends PageContext{
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ClJspWriter writer;
	
	public ClPageContext(HttpServletRequest request, HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
		this.writer = new ClJspWriter();
	}

	
	public void setAttribute(String name, Object value, int scope) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setAttribute(String name, Object value) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void removeAttribute(String name, int scope) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void removeAttribute(String name) {
		// TODO Auto-generated method stub
		
	}
	
	
	public VariableResolver getVariableResolver() {
		return null;
	}
	
	
	public JspWriter getOut() {
		return writer;
	}
	
	
	public ExpressionEvaluator getExpressionEvaluator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public ELContext getELContext() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public int getAttributesScope(String name) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public Enumeration<String> getAttributeNamesInScope(int scope) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Object getAttribute(String name, int scope) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Object findAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void release() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void initialize(Servlet servlet, ServletRequest request, ServletResponse response, String errorPageURL,
			boolean needsSession, int bufferSize, boolean autoFlush)
			throws IOException, IllegalStateException, IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}
	
	
	public void include(String relativeUrlPath, boolean flush) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	
	
	public void include(String relativeUrlPath) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	
	
	public void handlePageException(Throwable t) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	
	
	public void handlePageException(Exception e) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	
	
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return request.getSession();
	}
	
	
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return request.getSession().getServletContext();
	}
	
	
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public ServletResponse getResponse() {
		// TODO Auto-generated method stub
		return response;
	}
	
	
	public ServletRequest getRequest() {
		// TODO Auto-generated method stub
		return request;
	}
	
	
	public Object getPage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Exception getException() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void forward(String relativeUrlPath) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
