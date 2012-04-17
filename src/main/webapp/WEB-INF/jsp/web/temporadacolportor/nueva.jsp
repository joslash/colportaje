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
        <title><s:message code="temporadaColportor.nuevo.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>

                <li class="active"><a href="<s:url value='/web/temporadacolportor'/>" ><s:message code="temporadaColportor.label" /></a></li>

            </ul>
        </nav>

        <div id="nueva-temporadaColportor" class="content scaffold-list" role="main">
            <h1><s:message code="temporadaColportor.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/web/temporadacolportor'/>"><i class="icon-list icon-white"></i> <s:message code='temporadaColportor.lista.label' /></a>
            </p>
            <form:form commandName="temporadacolportor" action="crea" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="temporadacolportor.fecha">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="fecha">
                                <s:message code="fecha.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fecha" maxlength="128" required="true" />
                            <form:errors path="fecha" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="temporadacolportor.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            
                                <form:input path="status" maxlength="50" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="temporadacolportor.objetivo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="objetivo">
                                <s:message code="objetivo.label" />
                                <span class="required-indicator">*</span>
                            <form:input path="objetivo" maxlength="50" required="true"  />
                            <form:errors path="objetivo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    
                    <s:bind path="temporadacolportor.observacion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="observacion">
                                <s:message code="observacion.label" />
                                <span class="required-indicator">*</span>
                                <form:textarea path="observacion" maxlength="4000" required="true"  />
                            <form:errors path="observacion" cssClass="alert alert-error" type="texttarea"/>
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