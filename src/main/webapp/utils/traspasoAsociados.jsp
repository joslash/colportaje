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
	String COMANDO = "SELECT p.id, p.clave, address,province,phone_number,p.version FROM PERSONA P, APP_USER U WHERE U.CLAVE =P.CLAVE AND P.DISCRIMINATOR_COL ='S'";
	PreparedStatement pstmt = conexion_real.prepareStatement(COMANDO);
	ResultSet rset = pstmt.executeQuery();

	while (rset.next())
	{
            
		COMANDO = "INSERT INTO ASOCIADOS ";
		COMANDO += "(id,calle,clave,colonia,municipio,status,telefono,version) ";
		COMANDO += "VALUES ";
		COMANDO += "(?, ?, ?, '.',?, 'A', ?, ?) ";
		pstmt2 = conexion.prepareStatement(COMANDO);
		pstmt2.setInt(1, rset.getInt("id"));
		pstmt2.setString(2, rset.getString("address").toUpperCase());
		pstmt2.setString(3, rset.getString("clave"));
                pstmt2.setString(4, rset.getString("province"));
                pstmt2.setString(5, rset.getString("phone_number"));
                pstmt2.setInt(6, rset.getInt("version"));
		pstmt2.execute();
		pstmt2.close();
	}
	rset.close();
	pstmt.close();
        conexion_real.close();
        conexion.close();
        
%>

<body>
</body>
</html>
