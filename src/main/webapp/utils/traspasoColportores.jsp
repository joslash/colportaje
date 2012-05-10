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
	String COMANDO = "SELECT coalesce(u.id,1) id, coalesce(clave,'.') clave, coalesce(address,'.') address,coalesce(province,'.') province,coalesce(phone_number,'.') phone_number,coalesce(version,1) as version, coalesce(u.fecha_nacimiento,now()) fecha_nacimiento,u.matricula,case when tipo_user = 'Alum' then '2' else '0' end as tipo FROM  APP_USER U WHERE  asociado_id is not null ";
	PreparedStatement pstmt = conexion_real.prepareStatement(COMANDO);
	ResultSet rset = pstmt.executeQuery();

	while (rset.next())
	{
            System.out.println(rset.getInt("id")+ " "+ rset.getString("clave")+ " "+rset.getString("matricula"));
            
		COMANDO = "INSERT INTO COLPORTORES ";
		COMANDO += "(id, calle, clave, colonia, municipio, status, telefono, version, fecha, matricula, tipoDeColportor)";
		COMANDO += "VALUES ";
		COMANDO += "(?, ?, ?, '.',?, 'A', ?, ?,?,?,?) ";
		pstmt2 = conexion.prepareStatement(COMANDO);
		pstmt2.setInt(1, rset.getInt("id"));
		pstmt2.setString(2, rset.getString("address"));
		pstmt2.setString(3, rset.getString("clave"));
                pstmt2.setString(4, rset.getString("province"));
                pstmt2.setString(5, rset.getString("phone_number"));
                pstmt2.setInt(6, rset.getInt("version"));
                pstmt2.setDate(7, rset.getDate("fecha_nacimiento"));
                pstmt2.setString(8, rset.getString("matricula"));
                pstmt2.setString(9, rset.getString("tipo"));
                
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
