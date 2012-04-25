<%-- 
    Document   : edita
    Created on : 14-mar-2012, 11:26:53
    Author     : gibrandemetrioo
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:message code="asociado.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="asociado" />
        </jsp:include>

        <div id="edita-asociado" class="content scaffold-list" role="main">
            <h1><s:message code="asociado.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='../'/>"><i class="icon-list icon-white"></i> <s:message code='asociado.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="../actualiza" />
            <form:form commandName="asociado" method="post" action="${actualizaUrl}">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>
                <form:hidden path="id" />
                <form:hidden path="version" />

                <fieldset>
                    <s:bind path="asociado.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="asociado.clave">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="clave">
                                <s:message code="clave.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="clave" maxlength="64" required="true" />
                            <form:errors path="clave" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="asociado.telefono">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="telefono">
                                <s:message code="telefono.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="telefono" maxlength="64" required="true" />
                            <form:errors path="telefono" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="asociado.direccion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="direccion">
                                <s:message code="direccion.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="direccion" maxlength="64" required="true" />
                            <form:errors path="direccion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                   
                    <s:bind path="asociado.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="2" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                     <s:bind path="asociado.correo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="correo">
                                <s:message code="correo.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="correo" maxlength="128" required="true" />
                            <form:errors path="correo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <input type="submit" name="actualiza" value="<s:message code='actualizar.button' />" class="btn btn-large btn-primary" />
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#nombre').focus();
            });
        </script> 
    </content>
</body>
</html>