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
        <title><s:message code="temporadaColportor.nueva.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="temporadaColportor" />
        </jsp:include>

        <div id="nueva-temporadaColportor" class="content scaffold-list" role="main">
            <h1><s:message code="temporadaColportor.nueva.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/temporadaColportor'/>"><i class="icon-list icon-white"></i> <s:message code='temporadaColportor.lista.label' /></a>
            </p>
            <form:form commandName="temporadaColportor" action="crea" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>
                
                <fieldset>
                    <s:bind path="temporadaColportor.temporada">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="temporada">
                                <s:message code="temporada.label" />
                                <span class="required-indicator">*</span>
                                <form:select id="temporadaId" path="temporada.id" items="${temporadas}" itemLabel="nombre" itemValue="id" />
                                <form:errors path="temporada" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="temporadaColportor.asociado">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="asociado">
                                <s:message code="asociado.label" />
                                <span class="required-indicator">*</span>
                                <form:select id="asociadoId" path="asociado.id" items="${asociados}" itemLabel="nombre" itemValue="id" />
                                <form:errors path="asociado" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="temporadaColportor.colportor">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="colportor">
                                <s:message code="colportor.label" />
                                <span class="required-indicator">*</span>
                                <form:select id="colportorId" path="colportor.id" items="${colportores}" itemLabel="nombre" itemValue="id" />
                                <form:errors path="colportor" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                    <s:bind path="temporadaColportor.fecha">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fecha">
                                <s:message code="fecha.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <s:message code="fecha.formato.label" /><br>
                            <form:input path="fecha" maxlength="128" required="true" />
                            <form:errors path="fecha" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="temporadaColportor.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                                <form:input path="status" maxlength="2" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="temporadaColportor.objetivo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="objetivo">
                                <s:message code="objetivo.label" />
                                <span class="required-indicator">*</span>
                                <form:input path="objetivo" maxlength="50" required="true"  />
                                <form:errors path="objetivo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                    <s:bind path="temporadaColportor.observaciones">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="observaciones">
                                <s:message code="observaciones.label" />
                                <span class="required-indicator">*</span>
                                <form:textarea path="observaciones" maxlength="4000" required="true"  />
                                <form:errors path="observaciones" cssClass="alert alert-error" type="texttarea"/>
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