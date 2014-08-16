<!DOCTYPE html>
<html>
<head


</head>
<body>
    <ul>
<g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
    <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
</g:each>
</ul>
</body>
</html>


