<html>
<head>
<title>Traspasar cuentas de auxiliar</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<%@ page language = "java"  import = "java.sql.*, java.text.*, java.util.*" session = "true"%>
<%@ include file="conexion.jsp" %>


<%
	PreparedStatement pstmt2 = null;
	String COMANDO = "SELECT * FROM COLEGIOS";
	COMANDO += "ORDER BY ID";
	COMANDO += "ORDER BY ID_NIVEL ";
	PreparedStatement pstmt = conn_aron.prepareStatement(COMANDO);
	ResultSet rset = pstmt.executeQuery();

	while (rset.next())
	{
            
		/*COMANDO = "INSERT INTO MATEO.CONT_CCOSTO ";
		COMANDO += "(ID_EJERCICIO, ID_CCOSTO, NOMBRE, DETALLE) ";
		COMANDO += "VALUES ";
		COMANDO += "(?, ?, ?, ?) ";
		pstmt2 = conn.prepareStatement(COMANDO);
		pstmt2.setString(1, rset.getString("ID_Cont"));
		pstmt2.setString(2, rset.getString("ID_Nivel"));
		pstmt2.setString(3, rset.getString("Nombre").toUpperCase());
		pstmt2.setString(4, rset.getString("Status"));
		pstmt2.execute();
		pstmt2.close();*/
	}
	rset.close();
	pstmt.close();
        conn_aron.close();
        
%>

<body>
</body>
</html>
