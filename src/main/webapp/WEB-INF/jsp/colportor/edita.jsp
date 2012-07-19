<%-- 
    Document   : edita
    Created on : 14/03/2012, 03:34:26 PM
    Author     : wilbert
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><s:message code="usuario.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="usuario" />
        </jsp:include>

        <div id="edita-colportor" class="content scaffold-list" role="main">
            <h1><s:message code="usuario.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='../'/>"><i class="icon-list icon-white"></i> <s:message code='usuario.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="../actualiza" />
            <form:form commandName="colportor" method="post" action="${actualizaUrl}">
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
                <form:hidden path="username" />

                <fieldset>

                    <s:bind path="colportor.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="colportor.apellidoP">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="apellidoP">
                                <s:message code="apellidoP.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="apellidoP" maxlength="128" required="true" />
                            <form:errors path="apellidoP" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="colportor.apellidoM">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="apellidoM">
                                <s:message code="apellidoM.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="apellidoM" maxlength="128" required="true" />
                            <form:errors path="apellidoM" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="colportor.tipoDeColportor">
                        <div class="row-fluid">
                            <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="tipoDeColportor">
                                    <s:message code="tipoDeColportor.label" />
                                    <span class="required-indicator">*</span>
                                </label>
                                <div class="span2">
                                    <form:radiobutton path="TipoDeColportor" value="TC" title="Tiempo Completo"  />Tiempo Completo
                                </div>
                                <div class="span2">
                                    <form:radiobutton path="TipoDeColportor" value="TP" title="Tiempo Parcial"  />Tiempo Parcial
                                </div>
                                <div class="span1">
                                    <form:radiobutton path="TipoDeColportor" value="ES" title="Estudiante" />Estudiante

                                </div>
                                <div class="span9">&nbsp;</div>
                            </div>
                        </div>
                    </s:bind>

                    <s:bind path="colportor.matricula">
                        <div class="row-fluid">
                            <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="matricula">
                                    <s:message code="matricula.label" />
                                </label>
                                <form:input path="matricula" maxlength="10" />
                            </div>
                        </div>
                    </s:bind>
                    <s:bind path="colportor.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="2" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                    <s:bind path="colportor.clave">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="clave">
                                <s:message code="clave.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="clave" maxlength="5" required="true" />
                            <form:errors path="clave" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="colportor.fechaDeNacimiento">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="fechaDeNacimiento">
                                <s:message code="fechaDeNacimiento.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <s:message code="fecha.formato.label" /><br>
                            <form:input path="fechaDeNacimiento" maxlength="50" required="true" />

                            <form:errors path="fechaDeNacimiento" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="colportor.calle">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="calle">
                                <s:message code="calle.label" />

                            </label>
                            <form:input path="calle" maxlength="200" required="false" />
                            <form:errors path="calle" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="colportor.colonia">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="colonia">
                                <s:message code="colonia.label" />

                            </label>
                            <form:input path="colonia" maxlength="200" required="false" />
                            <form:errors path="colonia" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="colportor.municipio">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="municipio">
                                <s:message code="municipio.label" />

                            </label>
                            <form:input path="municipio" maxlength="200" required="false" />
                            <form:errors path="municipio" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                    <s:bind path="colportor.telefono">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="telefono">
                                <s:message code="telefono.label" />

                            </label>
                            <form:input path="telefono" maxlength="25" required="false" />
                            <form:errors path="telefono" cssClass="alert alert-error" />
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
                $('input#matricula').focus();
            });
        </script>                    
    </content>
</body>
</html>