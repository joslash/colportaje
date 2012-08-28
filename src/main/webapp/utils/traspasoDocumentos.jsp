<html>
<head>
<title>Traspasar cuentas de auxiliar</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<%@ page language = "java"  import = "java.sql.*, java.text.*, java.util.*,mx.edu.um.mateo.Constantes" session = "true"%>
<%@ include file="conexion.jsp" %>
<%@ include file="conexionreal.jsp" %>

<%
	PreparedStatement pstmt2 = null;
	String COMANDO = "SELECT * from documento_colportor ";
	PreparedStatement pstmt = conexion_origen.prepareStatement(COMANDO);
	ResultSet rset = pstmt.executeQuery();
String tipoDocumento=null;
	while (rset.next())
	{
             switch (rset.getInt("tipo_documento_id")) {
            case 0:
                tipoDocumento=Constantes.DEPOSITO_CAJA;
                break;
            case 1:
                 tipoDocumento=Constantes.DEPOSITO_BANCO;
                break;
            case 2:
                 tipoDocumento=Constantes.DIEZMO;
                break;
            case 3:
                 tipoDocumento=Constantes.NOTAS_DE_COMPRA;
                break;
            case 4:
                 tipoDocumento=Constantes.BOLETIN;
                break;
            case 5:
                 tipoDocumento=Constantes.INFORME;
                break;
        }
            
		COMANDO = "INSERT INTO DOCUMENTOS ";
		COMANDO += "(id, fecha, folio, importe, observaciones, tipodedocumento, version, temporadacolportor_id)";
		COMANDO += "VALUES ";
		COMANDO += "(?, ?, ?, ?, ?, ?, ?, ?) ";
		pstmt2 = conexion_destino.prepareStatement(COMANDO);
		pstmt2.setInt(1, rset.getInt("id"));
		pstmt2.setDate(2, rset.getDate("fecha"));
		pstmt2.setString(3, rset.getString("folio"));
                pstmt2.setBigDecimal(4, rset.getBigDecimal("importe"));
                pstmt2.setString(5, rset.getString("observaciones"));
                pstmt2.setString(6, tipoDocumento);
                pstmt2.setInt(7, rset.getInt("version"));
                pstmt2.setInt(8, rset.getInt("temporada_colportor_id"));
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
