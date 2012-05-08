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
	        Class.forName("org.postgresql.Driver");
	        conn_aron = DriverManager.getConnection("jdbc.url=jdbc:postgresql://172.16.11.19:5432/colportaje","tomcat","tomcat00");
        }

//	conn.setAutoCommit(false);
%>
