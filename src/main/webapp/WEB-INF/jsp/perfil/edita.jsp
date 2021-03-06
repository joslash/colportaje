<%-- 
    Document   : edita
    Created on : Feb 13, 2012, 3:15:52 PM
    Author     : J. David Mendoza <jdmendoza@um.edu.mx>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="perfil.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="principal" />
        </jsp:include>

        <div id="edita-usuario" class="content scaffold-list" role="main">
            <h1><s:message code="perfil.edita.label" /></h1>
            <c:url var="actualizaUrl" value="/perfil" />
            <form:form commandName="usuario" method="post" action="${actualizaUrl}">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="usuario.asociacion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="asociacion">
                                <s:message code="perfil.asociacion.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:select path="asociacion.id" id="asociacionId" items="${asociaciones}" itemLabel="nombreCompleto" itemValue="id"/>
                            <form:errors path="asociacion" cssClass="alert alert-error" />
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
                    $('#asociacionId').focus();
                });
            </script>                    
        </content>
    </body>
</html>
