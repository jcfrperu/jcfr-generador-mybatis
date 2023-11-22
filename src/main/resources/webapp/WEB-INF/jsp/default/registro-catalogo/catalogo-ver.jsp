<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div>&nbsp;</div>

<div class="panel panel-info">
    <div class="panel-heading"> <strong> CAT&Aacute;LOGO </strong> </div>
    <div class="panel-body">
        
        <div>&nbsp;</div>
        
        <!-- instancia de la barra de error superior -->
        <div id="divMsgGeneralErrorInstance"></div>

        <div class="row" id="div-panel-registro">
            <div class="col-md-12">

                <form id="form-registro" method="POST" role="form">
                                
                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label class="control-label">C&oacute;digo</label>
                                <input readonly name="int_catalogoID" type="text" title="C&oacute;digo" maxlength="8" class="form-control input-sm" value="${registro.catalogoID}">
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>
                                
                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label class="control-label">Cat&aacute;logo</label>
                                <input readonly name="txt_catalogo" type="text" title="Cat&aacute;logo" maxlength="50" class="form-control input-sm" value="${registro.catalogo}">
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>
                                
                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label class="control-label">Valor Cat&aacute;logo</label>
                                <input readonly name="txt_datacatalogo" type="text" title="Valor Cat&aacute;logo" maxlength="200" class="form-control input-sm" value="${registro.datacatalogo}">
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>
                                
                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label class="control-label">Descripci&oacute;n</label>
                                <textarea readonly name="txt_descripcion" maxlength="200" title="Descripci&oacute;n" class="form-control input-sm" rows="4">${registro.descripcion}</textarea>
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>
                                
                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label class="control-label">Descripci&oacute;n Corta</label>
                                <input readonly name="txt_descripcionCorta" type="text" title="Descripci&oacute;n Corta" maxlength="200" class="form-control input-sm" value="${registro.descripcionCorta}">
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>
                                
                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label class="control-label">Tipo</label>
                                <select disabled class="form-control input-sm" title="Tipo" name="cbo_tipo">
                                    <c:forEach items="${cboTipos.items}" var="item" >
                                        <option <c:if test="${item.id == cboTipos.idSeleccionado}">selected</c:if> value="${item.id}">${item.label}</option>
                                    </c:forEach>
                                </select>
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>   

                    <div class="row">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label class="control-label">Usuario registro</label>
                                <input readonly name="txt_usuReg" type="text" title="Usuario registro" maxlength="50" class="form-control input-sm" value="${registro.usuReg}">
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label class="control-label div-date-picker">Fecha registro</label>
                                <input readonly name="fec_fechaReg" type="text" title="Fecha de Registro" maxlength="50" class="form-control input-sm" value="<fmt:formatDate pattern='dd/MM/yyyy HH:mm:ss' value='${registro.fechaReg}' />">
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label class="control-label">Usuario de la &uacute;ltima modificaci&oacute;n</label>
                                <input readonly name="txt_usuAct" type="text" title="Usuario de la &uacute;ltima modificaci&oacute;n" maxlength="50" class="form-control input-sm" value="${registro.usuAct}">
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group div-date-picker">
                                <label class="control-label">&Uacute;ltima modificaci&oacute;n</label>
                                <input readonly name="fec_fechaAct" type="text" title="&Uacute;ltima modificaci&oacute;n" maxlength="50" class="form-control input-sm " value="<fmt:formatDate pattern='dd/MM/yyyy HH:mm:ss' value='${registro.fechaAct}' />">
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label class="control-label">Estado Registro</label>
                                <select disabled class="form-control input-sm" title="Estado Registro" name="cbo_indDel">
                                    <c:forEach items="${cboEstados.items}" var="item" >
                                        <option <c:if test="${item.id == cboEstados.idSeleccionado}">selected</c:if> value="${item.id}">${item.label}</option>
                                    </c:forEach>
                                </select>
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>

                </form>

            </div>
        </div>

        <div>&nbsp;</div>

        <div class="row" id="div-panel-botones">

            <div class="col-md-2 col-md-offset-5">
                <button id="btn_cancelar" title="Regresar" class="btn btn-sm btn-primary btn-block">&nbsp; REGRESAR</button>
            </div>

        </div>

        <div>&nbsp;</div>
				
	</div>
</div>

<div>&nbsp;</div>

<script>

    $(document).ready(function() {
        pageInit();
        pageEvents();
        pagePostInit();
    });

    function pageInit() {

    }

    function pagePostInit() {

    }

    function pageEvents() {

        eventCancelar();
    }

    function eventCancelar() {

        $('#btn_cancelar').on('click', function(e) {

            $(location).prop('href', 'registro-catalogo.htm?action=mostrarBuscar');
        });
    }


</script>