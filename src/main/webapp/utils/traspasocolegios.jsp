<%-- 
    Document   : traspasocolegios
    Created on : 8/05/2012, 10:47:58 AM
    Author     : nujev
--%>

<%@ page language = "java" import = "java.sql.*, java.text.*, java.util.*" session = "true"%>
<%@ include file="conexion.jsp" %>
<%@ include file="conexionreal.jsp" %>
<% PreparedStatement pstmt2 = null;
    String COMANDO = "SELECT * FROM colegio ";
    COMANDO += "ORDER BY ID";
    PreparedStatement pstmt = conexion_real.prepareStatement(COMANDO);
    ResultSet rset = pstmt.executeQuery();
    
    while (rset.next()) {
        out.println(rset.getString("nombre"));
        COMANDO = "INSERT INTO colegios ";
        COMANDO += "(id, nombre, status, version) ";
        COMANDO += "VALUES ";
        COMANDO += "(?, ?, ?, ?) ";
        pstmt2 = conexion.prepareStatement(COMANDO);
        pstmt2.setInt(1, rset.getInt("id"));
        pstmt2.setString(2, rset.getString("nombre"));
        pstmt2.setString(3, rset.getString("status").toUpperCase());
        pstmt2.setInt(4, rset.getInt("version"));
        pstmt2.execute();
        pstmt2.close();
    }
    
    rset.close();
    pstmt.close();
    conexion.close();
    conexion_real.close();
%>
<html>
    <head>
        <title>Transpaso de Colegios</title>
    </head>
    <body>
        <h1>Transpaso de colegios exitoso</h1>
    </body>
</html>