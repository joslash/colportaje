<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'admin'}"> class="active"</c:if>><a href="<c:url value='/admin' />"><s:message code="admin.label" /></a></li>
        <li<c:if test="${param.menu eq 'colportor'}"> class="active"</c:if>><a href="<c:url value='/colportor' />"><s:message code="colportor.label" /></a></li>
        <li<c:if test="${param.menu eq 'colegio'}"> class="active"</c:if>><a href="<c:url value='/colegio' />"><s:message code="colegio.label" /></a></li>
        <li<c:if test="${param.menu eq 'documento'}"> class="active"</c:if>><a href="<c:url value='/documento' />"><s:message code="documento.label" /></a></li>
        <li<c:if test="${param.menu eq 'asociado'}"> class="active"</c:if>><a href="<c:url value='/asociado' />"><s:message code="asociado.label" /></a></li>
        <li<c:if test="${param.menu eq 'temporada'}"> class="active"</c:if>><a href="<c:url value='/temporada' />"><s:message code="temporada.label" /></a></li>
        <li<c:if test="${param.menu eq 'temporadaColportor'}"> class="active"</c:if>><a href="<c:url value='/temporadaColportor' />"><s:message code="temporadaColportor.label" /></a></li>
        <li<c:if test="${param.menu eq 'pais'}"> class="active"</c:if>><a href="<c:url value='/pais' />"><s:message code="pais.label" /></a></li>
        <li<c:if test="${param.menu eq 'estado'}"> class="active"</c:if>><a href="<c:url value='/estado' />"><s:message code="estado.label" /></a></li>
        <li<c:if test="${param.menu eq 'ciudad'}"> class="active"</c:if>><a href="<c:url value='/ciudad' />"><s:message code="ciudad.label" /></a></li>
    </ul>
</nav>
