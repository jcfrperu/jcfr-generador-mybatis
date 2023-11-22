<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div>&nbsp;</div>

<div class="panel panel-info">
    <div class="panel-heading"> <strong> NUEVO CAT&Aacute;LOGO </strong> </div>
    <div class="panel-body">

        <div>&nbsp;</div>

        <!-- instancia de la barra de error superior -->
        <div id="divMsgGeneralErrorInstance"></div>

        <div class="row" id="div-panel-registro">
            <div class="col-md-12">

                <form id="form-registro" method="POST" role="form">
                                
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label">Tipo</label>
                                <select class="form-control input-sm" title="Tipo" name="cbo_tipo">
                                    <c:forEach items="${cboTipos.items}" var="item" >
                                        <option <c:if test="${item.id == cboTipos.idSeleccionado}">selected</c:if> value="${item.id}">${item.label}</option>
                                    </c:forEach>
                                </select>
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>   
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label">Cat&aacute;logo</label>
                                <input name="txt_catalogo" type="text" title="Cat&aacute;logo" maxlength="50" class="form-control input-sm text-uppercase">
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label">Valor Cat&aacute;logo</label>
                                <input name="txt_datacatalogo" type="text" title="Valor Cat&aacute;logo" maxlength="50" class="form-control input-sm">
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>
                                
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label">Descripci&oacute;n</label>
                                <textarea name="txt_descripcion" maxlength="200" title="Descripci&oacute;n" class="form-control input-sm text-uppercase" rows="4"></textarea>
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label">Descripci&oacute;n Corta</label>
                                <input name="txt_descripcionCorta" type="text" title="Descripci&oacute;n Corta" maxlength="200" class="form-control input-sm text-uppercase">
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>
                                
                </form>

            </div>
        </div>

        <div>&nbsp;</div>

        <div class="row" id="div-panel-botones">

            <div class="col-md-2 col-md-offset-4">
                <button id="btn_guardar" class="btn btn-sm btn-primary btn-block" title="Guardar"><i class="fa fa-floppy-o"></i>&nbsp; GUARDAR</button>
            </div>

            <div class="col-md-2">
                <button id="btn_cancelar" class="btn btn-sm btn-primary btn-block .btnCancelar" title="Cancelar">&nbsp; CANCELAR</button>
            </div>

        </div>

        <!-- popups -->
        <c:import url="/admin.htm">
            <c:param name="action" value="cargarPage"/>
            <c:param name="_page" value="popups"/>
            <c:param name="_module" value=""/>
            <c:param name="_template" value="default"/>
        </c:import>

        <!-- divMsgGeneralError -->
        <c:import url="/admin.htm">
            <c:param name="action" value="cargarPage"/>
            <c:param name="_page" value="barra-error"/>
            <c:param name="_module" value=""/>
            <c:param name="_template" value="default"/>
        </c:import>

        <div>&nbsp;</div>
				
	</div>
</div>

<script>

    $(document).ready(function() {
        pageInit();
        pageEvents();
        pagePostInit();
    });

    function pageInit() {

        $('textarea').css('resize','none');
		initMask();

    }

	function initMask() {
    	
    	// componente input mask
    	// mas info: https://github.com/RobinHerbots/jquery.inputmask
    	
    	var div = $( '#div-panel-registro' );

        var control = div.find( 'input[name="txt_catalogo"]' );
//        control.inputmask( "[*||_]{0,50}", {placeholder: ''});
        control.prop( 'maxlength', 50 );

        control = div.find( 'input[name="txt_datacatalogo"]' );
//        control.inputmask( "[*|| ||-]{0,50}", {placeholder: ''});
        control.prop( 'maxlength', 50 );

        control = div.find( 'input[name="txt_descripcion"]' );
//        control.inputmask( "[*|| ||_]{0,200}", {placeholder: ''});
        control.prop( 'maxlength', 200 );

        control = div.find( 'input[name="txt_descripcionCorta"]' );
//        control.inputmask( "[*|| ]{0,200}", {placeholder: ''});
        control.prop( 'maxlength', 200 );


    }  
    
    function pagePostInit() {

        $( '#div-panel-registro select[name=cbo_tipo]' ).trigger( 'change' );
    }

    function pageEvents() {

        eventGuardar();
        eventCancelar();
        eventChangeTipo();
    }

    function eventChangeTipo() {
    	
    	$( '#div-panel-registro select[name=cbo_tipo]' ).on('change', function (e) {
    	
    		var txt_datacatalogo = $( '#div-panel-registro input[name=txt_datacatalogo]' );
    		
    		var valor = $(this).val();
    		
    		if ( valor == 'CAB' ) {
    			
    			txt_datacatalogo.val( '-' );
    			txt_datacatalogo.prop( 'readonly', true );
    			
    		} else { // es DET o 'Seleccione'

    			txt_datacatalogo.val( '' );
    			txt_datacatalogo.prop( 'readonly', false );
    		}
    		
    	});
    	
    }
    
    function eventGuardar() {

        $('#btn_guardar').on('click', function(e) {

            e.preventDefault();

            showConfirmation('Guardar Registro', '\u00BFSeguro que desea guardar el registro?', function() {

                var restore = estadoInputInicial('#div-panel-registro');

                $.ajax({
                    url: 'registro-catalogo.htm?action=guardarNuevo',
                    cache: false,
                    data: $('#form-registro').serialize(),
                    async: true,
                    type: 'POST',
                    dataType: 'json',
                    success: function(data) {

                        if (huboErrorJson(data)) {

                            if (huboErrorValidacionJson(data)) {
                                estadoInputError('#div-panel-registro', data, restore);
                                return;
                            }

                            handleErrorJson(data);
                            return;
                        }

                        console.log('data: ' + JSON.stringify(data));

                        showMensaje('Mensaje', "La operaci\u00F3n se realiz\u00F3 con \u00E9xito");

                        habilitarControl('#btn_guardar', false);
                        habilitarControles('#div-panel-registro', false);

                        $('#btn_cancelar').html( 'REGRESAR' );

                    },
                    error: function(data, textStatus, errorThrown) {
                        handleError(data);
                    }
                });

            });

        });
    }

    function eventCancelar() {

        $('#btn_cancelar').on('click', function(e) {

            $(location).prop('href', 'registro-catalogo.htm?action=mostrarBuscar');
        });
    }


</script>