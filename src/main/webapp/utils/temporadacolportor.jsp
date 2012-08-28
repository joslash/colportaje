<%-- 
    Document   : traspasocolegios
    Created on : 8/05/2012, 10:47:58 AM
    Author     : nujev
--%>

<%@ page language = "java" import = "java.sql.*, java.text.*, java.util.*" session = "true"%>
<%@ include file="conexion.jsp" %>
<%@ include file="conexionreal.jsp" %>
<% PreparedStatement pstmt2 = null;
    String COMANDO = "SELECT * FROM temporada_colportor tc, app_user p where p.id = tc.colportor_id ";
    PreparedStatement pstmt = conexion_origen.prepareStatement(COMANDO);
    ResultSet rset = pstmt.executeQuery();
    
    while (rset.next()) {
        out.println(rset.getString("objetivo"));
        COMANDO = "INSERT INTO temporadacolportores";
        COMANDO += "(id, version, status, fecha, objetivo, observaciones, asociacion_id, asociado_id, colegio_id, colportor_id, temporada_id, union_id ) ";
        COMANDO += "VALUES ";
        COMANDO += "(?, ?, ?, ?, ?, '.', 1, ?, ?, ?, ?, 1) ";
        pstmt2 = conexion_destino.prepareStatement(COMANDO);
        pstmt2.setInt(1, rset.getInt("id"));
        pstmt2.setInt(2, rset.getInt("version"));
        pstmt2.setString(3, rset.getString("status").toUpperCase());
        pstmt2.setDate(4, rset.getDate("fecha_captura"));
        pstmt2.setString(5, rset.getString("objetivo"));
        pstmt2.setInt(6,rset.getInt("asociado_id"));
        pstmt2.setInt(7,rset.getInt("colegio_id"));
        pstmt2.setInt(8,rset.getInt("colportor_id"));
        pstmt2.setInt(9,rset.getInt("temporada_id"));
        
        pstmt2.execute();
        pstmt2.close();
    }
    
    rset.close();
    pstmt.close();
    conexion_origen.close();
    conexion_destino.close();
%>
<html>
    <head>
        <title>Transpaso de Temporada Colportor</title>
    </head>
    <body>
        <h1>Transpaso de Temporada Temporada Colportor exitoso</h1>
    </body>
</html>