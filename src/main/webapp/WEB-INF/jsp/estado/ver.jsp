<%-- 
    Document   : ver
    Created on : 14-mar-2012, 11:27:52
    Author     : gibrandemetrioo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %><!DOCTYPE html>
<html>
    <head>
        <title><s:message code="estado.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="estado" />
        </jsp:include>

        <div id="ver-estado" class="content scaffold-list" role="main">
            <h1><s:message code="estado.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='../'/>"><i class="icon-list icon-white"></i> <s:message code='estado.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='../nueva'/>"><i class="icon-user icon-white"></i> <s:message code='estado.nueva.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/estado/elimina" />
            <form:form commandName="estado" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="nombre.label" /></div>
                    <div class="span11">${estado.nombre}</div>
                </div>
                <p class="well">
                    <a href="<c:url value='/estado/edita/${estado.id}' />" class="btn btn-primary"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                <form:hidden path="id" />
                <input type="submit" name="elimina" value="<s:message code='eliminar.button'/>" class="btn btn-danger icon-remove" style="margin-bottom: 2px;" onclick="return confirm('<s:message code="confirma.elimina.message" />');" />
                </p>
            </form:form>
        </div>
    </body>
</html>
