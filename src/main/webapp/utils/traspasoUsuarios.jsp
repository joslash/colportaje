<html>
<head>
<title>Traspasar cuentas de auxiliar</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<%@ page language = "java"  import = "java.sql.*, java.text.*, java.util.*" session = "true"%>
<%@ include file="conexion.jsp" %>
<%@ include file="conexionreal.jsp" %>

<%
	PreparedStatement pstmt2 = null;
	String COMANDO = "SELECT * FROM app_user";
	PreparedStatement pstmt = conexion_origen.prepareStatement(COMANDO);
	ResultSet rset = pstmt.executeQuery();

	while (rset.next())
	{
            
		COMANDO = "INSERT INTO USUARIOS ";
		COMANDO += "(id, account_expired, account_locked, apellidom, apellidop,credentials_expired, enabled, nombre, password, username, version, asociacion_id, entity_tipe)";
		COMANDO += "VALUES ";
		COMANDO += "(?, ?, ?, '.', ?, ?, ?, ?, ?, ?, ?, ?, ?, user) ";
		pstmt2 = conexion_destino.prepareStatement(COMANDO);
		pstmt2.setInt(1, rset.getInt("id"));
		pstmt2.setBoolean(2, rset.getBoolean("account_expired"));
		pstmt2.setBoolean(3, rset.getBoolean("account_locked"));
                pstmt2.setString(4, rset.getString("last_name"));
                pstmt2.setBoolean(5, rset.getBoolean("credentials_expired"));
                pstmt2.setBoolean(6, rset.getBoolean("account_enabled"));
                pstmt2.setString(7, rset.getString("first_name"));
                 // pstmt2.setString(8, rset.getString("open_id"));
                pstmt2.setString(8, rset.getString("password"));
                 pstmt2.setString(9, rset.getString("username"));
                pstmt2.setInt(10, rset.getInt("version"));
                pstmt2.setInt(11, 1);
                pstmt2.setInt(12, rset.getInt("asociado_id"));
                pstmt2.execute();
		pstmt2.close();
	}
	rset.close();
	pstmt.close();
        conexion_origen.close();
        conexion_destino.close();
        
%>

<body>
</body>
</html>
