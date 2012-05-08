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
Connection conn_aron;
%>
<%
        try {

		Context initContext = new InitialContext();
		Context envContext  = (Context)initContext.lookup("java:/comp/env");
		DataSource ds = (DataSource)envContext.lookup("jdbc/conn_aron");
		conn_aron = ds.getConnection();


	} catch (NamingException e) {
		//no existe un datasource
	        Class.forName("oracle.jdbc.driver.OracleDriver");
	        conn_aron = DriverManager.getConnection("jdbc:oracle:thin:@rigel.um.edu.mx:1521:ora1","aron",".");
        }

//	conn.setAutoCommit(false);
%>
