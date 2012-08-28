<%-- 
    Document   : conexion
    Created on : 8/05/2012, 10:49:26 AM
    Author     : nujev
--%>

<%@page import = "java.sql.*"%>
<%@page import = "javax.sql.DataSource"%>
<%@page import = "javax.naming.InitialContext"%>
<%@page import = "javax.naming.Context"%>
<%@page import = "javax.naming.NamingException"%>

<%!
Connection conexion_origen;
%>
<%
        try {
		Context initContext = new InitialContext();
		Context envContext  = (Context)initContext.lookup("java:/comp/env");
		DataSource ds = (DataSource)envContext.lookup("jdbc/conexion");
		conexion_origen = ds.getConnection();
	} catch (NamingException e) {
		//no existe un datasource
	        Class.forName("org.postgresql.Driver");
	        conexion_origen = DriverManager.getConnection("jdbc:postgresql://172.16.11.19:5432/colportores","tomcat","tomcat00");
        }
//	conn.setAutoCommit(false);
%>
