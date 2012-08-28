<%-- 
    Document   : traspasoasociaciones
    Created on : 8/05/2012, 11:41:51 AM
    Author     : nujev
--%>

<%@ page language = "java" import = "java.sql.*, java.text.*, java.util.*" session = "true"%>
<%@ include file="conexion.jsp" %>
<%@ include file="conexionreal.jsp" %>
<% PreparedStatement pstmt2 = null;
    String COMANDO = "SELECT * FROM asociacion ";
    COMANDO += "ORDER BY ID";
    PreparedStatement pstmt = conexion_origen.prepareStatement(COMANDO);
    ResultSet rset = pstmt.executeQuery();
    
    while (rset.next()) {
        out.println(rset.getString("nombre"));
        COMANDO = "INSERT INTO asociaciones ";
        COMANDO += "(id, nombre, status, version, union_id) ";
        COMANDO += "VALUES ";
        COMANDO += "(?, ?, 'A', ?, 1294) ";
        pstmt2 = conexion_destino.prepareStatement(COMANDO);
        pstmt2.setInt(1, rset.getInt("id"));
        pstmt2.setString(2, rset.getString("nombre"));
        pstmt2.setInt(3, rset.getInt("version"));
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
        <title>Transpaso de Asociaciones</title>
    </head>
    <body>
        <h1>Transpaso de asociaciones exitoso</h1>
    </body>
</html>