<%-- 
    Document   : nuevo
    Created on : 28/02/2012, 11:34:19 AM
    Author     : wilbert
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="almacen.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="almacen" />
        </jsp:include>

        <div id="nuevo-almacen" class="content scaffold-list" role="main">
            <h1><s:message code="almacen.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='../almacen'/>"><i class="icon-list icon-white"></i> <s:message code='almacen.lista.label' /></a>
            </p>
            <form:form commandName="almacen" action="crea" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="almacen.clave">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="clave">
                                <s:message code="clave.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="clave" maxlength="128" required="true" />
                            <form:errors path="clave" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                        <s:bind path="almacen.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                  <s:bind path="almacen.asociacion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="asociacion">
                                <s:message code="asociacion.label" />
                                <span class="required-indicator">*</span>
                                <form:select id="asociacionId" path="asociacion.id" items="${asociaciones}" itemLabel="nombre" itemValue="id" />
                                <form:errors path="asociacion" cssClass="alert alert-error" />
                        </div>
                         </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <input type="submit" name="_action_crea" class="btn btn-primary btn-large" value="<s:message code='crear.button'/>" id="crea" />
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#clave').focus();
                });
            </script>                    
        </content>
    </body>
</html>
