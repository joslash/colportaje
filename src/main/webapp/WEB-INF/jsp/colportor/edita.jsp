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
       <title><s:message code="colportor.edita.label" /></title>
    </head>
    <body>
          <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="colportor" />
        </jsp:include>
            
       <div id="edita-colportor" class="content scaffold-list" role="main">
            <h1><s:message code="colportor.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='../'/>"><i class="icon-list icon-white"></i> <s:message code='colportor.lista.label' /></a>
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

                <fieldset>
                      <s:bind path="colportor.tipoDeColportor">
                          <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="tipoDeColportor">
                                <s:message code="tipoDeColportor.label" />
                                <span class="required-indicator">*</span>
                            </label>
                     <form:select path="TipoDeColportor">
                                    <form:option value="0" label="Tiempo_Completo" />
                                    <form:option value="1" label="Tiempo_Parcial" />
                                     <form:option value="2" label="Estudiante" />
                                   </form:select>
                          </div>
                     </s:bind>
                     <s:bind path="colportor.matricula">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="matricula">
                                <s:message code="matricula.label" />
                              </label>
                            <form:input path="matricula" maxlength="10" />
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
                    $('input#nombre').focus();
                });
            </script>                    
        </content>
    </body>
</html>