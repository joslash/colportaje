<%-- 
    Document   : traspasocostos
    Created on : 8/05/2012, 10:47:58 AM
    Author     : nujev
--%>

<%@ page language = "java" import = "java.sql.*, java.text.*, java.util.*" session = "true"%> <%@ page contentType="text/html" errorPage = "../../handleError.jsp"%> <%@ include file="../../conecta.jsp" %> <%@ include file="../../conecta_aron.jsp" %> <% PreparedStatement pstmt2 = null;
    String COMANDO = "SELECT ID_CONT, ID_NIVEL, NOMBRE, ";
    COMANDO += "CASE CONTADOR WHEN 0 THEN 'S' ELSE 'N' END AS STATUS ";
    COMANDO += "FROM ARON.CONT_NIVEL ";
    COMANDO += "ORDER BY ID_NIVEL ";
    PreparedStatement pstmt = conn_aron.prepareStatement(COMANDO);
    ResultSet rset = pstmt.executeQuery();
    while (rset.next()) {
        COMANDO = "INSERT INTO MATEO.CONT_CCOSTO ";
        COMANDO += "(ID_EJERCICIO, ID_CCOSTO, NOMBRE, DETALLE) ";
        COMANDO += "VALUES ";
        COMANDO += "(?, ?, ?, ?) ";
        pstmt2 = conn.prepareStatement(COMANDO);
        pstmt2.setString(1, rset.getString("ID_Cont"));
        pstmt2.setString(2, rset.getString("ID_Nivel"));
        pstmt2.setString(3, rset.getString("Nombre").toUpperCase());
        pstmt2.setString(4, rset.getString("Status"));
        pstmt2.execute();
        pstmt2.close();
    }
    rset.close();
    pstmt.close();%>