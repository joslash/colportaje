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
	String COMANDO = "SELECT u.id, coalesce(clave,'.') clave, address,province,phone_number,version, first_name, last_name, email FROM  APP_USER U WHERE  tipo_user ='S'";
	PreparedStatement pstmt = conexion_origen.prepareStatement(COMANDO);
	ResultSet rset = pstmt.executeQuery();

	while (rset.next())
	{
            
		COMANDO = "INSERT INTO USUARIOS";
		COMANDO += "(id,calle,clave,colonia,municipio,status,telefono,version, asociacion_id, entity_type, account_expired, account_locked, credentials_expired, enabled, nombre, apellidop, apellidom, username ) ";
		COMANDO += "VALUES ";
		COMANDO += "(?, ?, ?, '.',?, 'A', ?, ?, 1625, 'asociado', false, false, false, true, ?, ?, ?,?) ";
		pstmt2 = conexion_destino.prepareStatement(COMANDO);
		pstmt2.setInt(1, rset.getInt("id"));
		pstmt2.setString(2, rset.getString("address").toUpperCase());
		pstmt2.setString(3, rset.getString("clave"));
                pstmt2.setString(4, rset.getString("province"));
                pstmt2.setString(5, rset.getString("phone_number"));
                pstmt2.setInt(6, rset.getInt("version"));
                pstmt2.setString(7, rset.getString("first_name"));
                pstmt2.setString(8, rset.getString("last_name"));
                pstmt2.setString(9, rset.getString("last_name"));
                pstmt2.setString(10, rset.getString("email"));
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
