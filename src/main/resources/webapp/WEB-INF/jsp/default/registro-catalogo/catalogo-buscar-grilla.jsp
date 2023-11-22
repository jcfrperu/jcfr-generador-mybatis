<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<table class="table table-striped table-bordered table-hover" id="catalogo-buscar-grilla">

    <thead>
        <tr>
            <th title="Seleccionar Registro">SEL</th>
            <th>Tipo</th>
            <th>Cat&aacute;logo</th>
            <th>Valor Cat&aacute;logo</th>
            <th>Descripci&oacute;n</th>
            <th title="Estado Registro BD">Estado Registro</th>
        </tr>
    </thead>

    <tbody>

        <c:forEach items="${lista}" var="item" varStatus="status">
            <c:choose>
                <c:when test="${status.index % 2 == 0}">
                    <c:set var="claseFila" value="odd" />
                </c:when>
                <c:otherwise>
                    <c:set var="claseFila" value="even" />
                </c:otherwise>
            </c:choose>

            <tr class="${claseFila}">
                <td class="text-center"><input type="radio" name="codigo" title="Seleccionar" value="${item.catalogoID}"></td>
				<td class="text-left">${item.tipoDescripcion}</td>
                <td>${item.catalogo}</td>
                <td>${item.datacatalogo}</td>
                <td>${item.descripcion}</td>
                <td class="text-center">
                    <c:choose>
                        <c:when test="${item.indDel == '0'}">ACTIVO</c:when>
                        <c:when test="${item.indDel == '1'}">INACTIVO</c:when>
                        <c:otherwise>NO DEFINIDO</c:otherwise>
                    </c:choose>
                </td>
            </tr>

        </c:forEach>

    </tbody>
</table>
