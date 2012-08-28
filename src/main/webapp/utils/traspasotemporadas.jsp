<%-- 
    Document   : traspasotemporadas
    Created on : 8/05/2012, 12:01:59 PM
    Author     : nujev
--%>

<%@ page language = "java" import = "java.sql.*, java.text.*, java.util.*" session = "true"%>
<%@ include file="conexion.jsp" %>
<%@ include file="conexionreal.jsp" %>
<% PreparedStatement pstmt2 = null;
    String COMANDO = "SELECT * FROM temporada ";
    COMANDO += "ORDER BY ID";
    PreparedStatement pstmt = conexion_origen.prepareStatement(COMANDO);
    ResultSet rset = pstmt.executeQuery();
    
    while (rset.next()) {
        out.println(rset.getString("nombre"));
        COMANDO = "INSERT INTO temporadas ";
        COMANDO += "(id, fecha_final, fecha_inicio, nombre, version, asociacion_id) ";
        COMANDO += "VALUES ";
        COMANDO += "(?, ?, ?, ?, ?, 1625) ";
        pstmt2 = conexion_destino.prepareStatement(COMANDO);
        pstmt2.setInt(1, rset.getInt("id"));
        pstmt2.setDate(2, rset.getDate("fecha_final"));
        pstmt2.setDate(3, rset.getDate("fecha_inicial"));
        pstmt2.setString(4, rset.getString("nombre"));
        pstmt2.setInt(5, rset.getInt("version"));
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
        <title>Transpaso de Temporadas</title>
    </head>
    <body>
        <h1>Transpaso de temporadas exitoso</h1>
    </body>
</html>