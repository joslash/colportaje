<%-- 
    Document   : nueva
    Created on : 14-mar-2012, 11:27:43
    Author     : gibrandemetrioo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="asociado.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="asociado" />
        </jsp:include>

        <div id="nueva-asociado" class="content scaffold-list" role="main">
            <h1><s:message code="asociado.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/asociado'/>"><i class="icon-list icon-white"></i> <s:message code='asociado.lista.label' /></a>
            </p>
            <form:form commandName="asociado" action="crea" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                       <s:bind path="asociado.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="128" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="asociado.clave">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="clave">
                                <s:message code="clave.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="clave" maxlength="128" required="true" />
                            <form:errors path="clave" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                     <s:bind path="asociado.calle">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="calle">
                                <s:message code="calle.label" />
                            </label>
                            <form:input path="calle" maxlength="200"  />
                            <form:errors path="calle" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="asociado.colonia">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="colonia">
                                <s:message code="colonia.label" />
                            </label>
                            <form:input path="colonia" maxlength="200"  />
                            <form:errors path="colonia" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="asociado.municipio">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="municipio">
                                <s:message code="municipio.label" />
                            </label>
                            <form:input path="municipio" maxlength="200"  />
                            <form:errors path="municipio" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="asociado.telefono">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="telefono">
                                <s:message code="telefono.label" /></label>
                            <form:input path="telefono" maxlength="15"  />
                            <form:errors path="telefono" cssClass="alert alert-error" />
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
                $('input#nombre').focus();
            });
        </script> 
    </content>
</body>
</html>