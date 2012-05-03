<%-- 
    Document   : ver
    Created on : 14/03/2012, 03:39:39 PM
    Author     : wilbert
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="colportor.ver.label" /></title>
    </head>
    <body>
         <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="colportor" />
        </jsp:include>

        <div id="ver-colportor" class="content scaffold-list" role="main">
            <h1><s:message code="colportor.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='..'/>"><i class="icon-list icon-white"></i> <s:message code='colportor.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='../nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='colportor.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="../elimina" />
            <form:form commandName="colportor" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />          
                  <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="tipoDeColportor.label" /></div>
                    <div class="span11">${colportor.tipoDeColportor}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="matricula.label" /></div>
                    <div class="span11">${colportor.matricula}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="status.label" /></div>
                    <div class="span11">${colportor.status}</div>
                </div>
                
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="clave.label" /></div>
                    <div class="span11">${colportor.clave}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="fechaDeNacimiento.label" /></div>
                    <div class="span11">${colportor.fechaDeNacimiento}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="calle.label" /></div>
                    <div class="span11">${colportor.calle}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="colonia.label" /></div>
                    <div class="span11">${colportor.colonia}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="municipio.label" /></div>
                    <div class="span11">${colportor.municipio}</div>
                </div>
                 <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="telefono.label" /></div>
                    <div class="span11">${colportor.telefono}</div>
                </div>

                <p class="well">
                    <a href="<c:url value='../edita/${colportor.id}' />" class="btn btn-primary"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <input type="submit" name="elimina" value="<s:message code='eliminar.button'/>" class="btn btn-danger icon-remove" style="margin-bottom: 2px;" onclick="return confirm('<s:message code="confirma.elimina.message" />');" />
                </p>
            </form:form>
        </div>
    </body>
</html>
