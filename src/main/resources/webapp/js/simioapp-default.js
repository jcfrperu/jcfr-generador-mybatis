/* ========================================================================
 * simioapp 1.0
 * ========================================================================
 * Copyright SiMiOS Coders
 * ======================================================================== */

function preventDefaultEvent(event) {
	if (estaDefinido(event)) {
		event.preventDefault();
	}
}

function consoleLog(consoleText) {
	if (window.console && window.console.log) {
		console.log(consoleText);
	}
}

function formToObject(formID, incluirDisabled) {

	// convierte todos los campos de un formulario a un objeto javascript.
	// se usa para poder deserializar directamente con los beans view object

	var formularioObject = {};
	var formularioArray = $('#' + formID).serializeArray();

	$.each(formularioArray, function (i, v) {
		formularioObject[v.name] = v.value;
	});

	if (estaDefinido(incluirDisabled) && incluirDisabled) {
		$('#' + formID).find('input:disabled, select:disabled').each(function (i, v) {
			formularioObject[v.name] = v.value;
		});
	}

	return formularioObject;
}

function copiarValores(fuenteFrom, destinoTo, usarTrim) {
	// fuenteFrom -> destinoTo
	// fuenteFrom es el objeto javascript que contiene los datos a copiarValores
	// destinoTo es el objeto javascript que va recibir los datos de fuenteFrom
	// usarTrim, si se desea convertir todo a string, y si son cadenas quitar los espacios

	var trimear = estaDefinido(usarTrim) ? usarTrim : false;

	// copia solo los campos de destinoTo(object) que existen en fuenteFrom (los demas que tenga destinoTo los ignora)
	$.each(destinoTo, function (key, value) {

		if (estaDefinido(fuenteFrom[key])) {

			destinoTo[key] = trimear ? $.trim(fuenteFrom[key]) : fuenteFrom[key];
		}

	});
}


function huboErrorJson(data) {

    // solo si viene definido data, data.estado
    if (estaDefinido(data) && estaDefinido(data.estado)) {

		// data.estado = { ok, error, errorValidacio }
        if (data.estado != 'ok') {
            return true;
        }
    }

    return false;
}

function huboErrorValidacionJson(data) {
	// TODO/FIXME: revisar este metodo, no tiene el mismo comportamiento de huboErrorJson()
    return data != null && data.estado == 'errorValidacion';
}

function handleErrorJson(data) {

    // si no viene data
	if (!estaDefinido(data)) {

        showMensaje('Mensaje', 'Respuesta JSON no seteada');

        return true;
    }

    // si no viene estado
	if (!estaDefinido(data.estado)) {

        showMensaje('Mensaje', 'Atributo estado de respuesta JSON no seteado');

        return true;
    }

    // si viene error de aplicacion (data.estado = { ok, error, errorValidacio } )
    if (data.estado != 'ok') {

        showMensaje('Mensaje', data.msg);

        consoleLog('error app -> ' + data.msg);

        return true;
    }

    return false;
}

function handleErrorBD(info) {

	if (estaDefinido(info)) {

		var msg = 'Ha ocurrido un error en BD';

		if (estaDefinido(info.code)) {
			msg = msg + ', code: ' + info.code;
		}

		if (estaDefinido(info.message)) {
			msg = msg + ', message: ' + info.message;
		}

		if (estaDefinido(info.name)) {
			msg = msg + ', name: ' + info.name;
		}

		if (estaDefinido(info.type)) {
			msg = msg + ', type: ' + info.type;
		}

		consoleLog(msg);
	}
}

function handleError(error) {

	var msg = 'No se obtuvo respuesta del servidor';

	if (estaDefinido(error)) {
		msg = 'error -> status: ' + error.status + ', readyState: ' + error.readyState + ', statusText: ' + error.statusText;
	}

	alert(msg);
	consoleLog(msg);
}

function estaDefinido(valor) {
	return !(valor == null || (typeof valor == 'undefined'));
}

function getTrimValue(inputQuery) {
	return $.trim($(inputQuery).val());
}

function toNumero(valor, defaultValue) {
	// valor por default en caso que no valor no sea número
	var defaultValueResult = !esNumero(defaultValue) ? 0.0 : Number(defaultValue);

	return !esNumero(valor) ? defaultValueResult : Number(valor);
}

function esNumero(valor) {
	// que no sea null, ni blanco, ni indefinido y que pase la validación de jquery isNumeric
	return estaDefinido(valor) && valor != '' && $.isNumeric(valor);
}


function roundComasMilesString(valor, digitos) {
	var val = roundString(valor, digitos);

	var parts = val.toString().split(".");

	// formato coma como separador de miles, punto como separador decimal
	return parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (parts[1] ? "." + parts[1] : "");
}

function roundString(valor, digitos) {
	// redondear
	var round = roundNumero(digitos, parseFloat(valor));

	// formatear completando el nro de digitos en los decimales
	return round.toFixed(digitos) + '';
}

function roundNumero(valor, digitos) {

	return Math.round(parseFloat(valor) * Math.pow(10, digitos)) / Math.pow(10, digitos);
}

function recortarDigitosEnteros(numeroString, maxDigEnteros) {

	if (!estaDefinido(numeroString)) return numeroString;

	var num = (numeroString + '').replace(/,/g, '');

	var posComa = num.indexOf('.');

	if (posComa >= 0) {

		var izq = num.substring(0, posComa);
		var der = num.substring(posComa + 1, num.length);

		if (izq.length > maxDigEnteros) {
			izq = izq.substring(0, maxDigEnteros);
		}

		num = izq + '.' + der;

	} else {

		if (num.length > maxDigEnteros) {
			num = num.substring(0, maxDigEnteros);
		}

	}

	return num;
}

function esHoraValida(hora) {
	return isValidoFormatHour(hora);
}

function esFechaValida(fecha) {
	var fechaTrim = $.trim(fecha);
	return fechaTrim != '' && checkdate(fechaTrim);
}

function esFechaMayor(fecha1, fecha2) {
	var result = compararFechas(fecha1, fecha2);
	return result == 1;
}

function esFechaMayorIgual(fecha1, fecha2) {
	var result = compararFechas(fecha1, fecha2);
	return result == 1 || result == 0;
}

function esFechaMenor(fecha1, fecha2) {
	var result = compararFechas(fecha1, fecha2);
	return result == 2;
}

function esFechaMenorIgual(fecha1, fecha2) {
	var result = compararFechas(fecha1, fecha2);
	return result == 2 || result == 0;
}

function esFechaIgual(fecha1, fecha2) {
	var result = compararFechas(fecha1, fecha2);
	return result == 0;
}

function compararFechas(fecha1, fecha2) {
	// se crea un wrapper por un bug en el compara fecha de sunat
	return checkcomparafecha($.trim(fecha1), $.trim(fecha2));
}

function checkcomparafecha(fecha1, fecha2) {

	/* -1: err, 1: f1>f2, 2: f1<f2, 0: f1=f2 */
	if (!checkdate(fecha1) || !checkdate(fecha2)) return -1;

	var dia = fecha1.substring(0, 2)
    var mes = fecha1.substring(3, 5)
    var anho = fecha1.substring(6, 10)

    var fecha1x = anho + mes + dia

    var dia = fecha2.substring(0, 2)
    var mes = fecha2.substring(3, 5)
    var anho = fecha2.substring(6, 10)
    var fecha2x = anho + mes + dia

	return (fecha1x > fecha2x ? 1 : (fecha1x < fecha2x ? 2 : 0));
}

function checkdate(fecha) {

	var err = 0

	if (fecha.length != 10) err = 1

    var dia = fecha.substring(0, 2)
    var slash1 = fecha.substring(2, 3)
    var mes = fecha.substring(3, 5)
    var slash2 = fecha.substring(5, 6)
    var anho = fecha.substring(6, 10)

	if (dia < 1 || dia > 31) err = 1
	if (slash1 != '/') err = 1
	if (mes < 1 || mes > 12) err = 1
	if (slash1 == '/' && slash2 != '/') err = 1
	if (anho < 0 || anho > 2200) err = 1
	if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
		if (dia == 31) err = 1
	}

	if (mes == 2) {
		var g = parseInt(anho / 4)
		if (isNaN(g)) {
			err = 1
		}
		if (dia > 29) err = 1
		if (dia == 29 && ((anho / 4) != parseInt(anho / 4))) err = 1
	}

	return (!(err == 1));
}


function compararHoras(hora1, hora2) {

	/* -1: err, 1: f1>f2, 2: f1<f2, 0: f1=f2 */

	// validar que venga data
	if (!estaDefinido(hora1) || !estaDefinido(hora2)) return -1;

	var h1Split = $.trim(hora1).split(':');
	var h2Split = $.trim(hora2).split(':');

	// validar formato: validar que tenga un solo ':'
	if (h1Split == null || h1Split.length != 2) return -1;
	if (h2Split == null || h2Split.length != 2) return -1;

	// pasarlo todo a minutos
	var h1 = toNumero(h1Split[0]) * 60 + toNumero(h1Split[1]);
	var h2 = toNumero(h2Split[0]) * 60 + toNumero(h2Split[1]);

	if (h1 > h2) return 1;	// 1
	if (h1 < h2) return 2;	// 2

	return 0;
}

function convertirEnMinutos(horaHHMM) {

	// validar que venga data
	if (!estaDefinido(horaHHMM)) return -1;

	var horaSplit = $.trim(horaHHMM).split(':');
	// validar formato: validar que tenga un solo ':'
	if (horaSplit == null || horaSplit.length != 2) return -1;

	// pasarlo todo a minutos
	return toNumero(horaSplit[0]) * 60 + toNumero(horaSplit[1]);
}

function roundDiferenciaHoras(horaMayorHHMM, horaMenorHHMM) {
	// PRE: horaMayorHHMM > horaMenorHHMM (en formato HH:MM )

	// convertir a minutos
	var mayorMins = convertirEnMinutos(horaMayorHHMM);
	var menorMins = convertirEnMinutos(horaMenorHHMM);

	// convertir a horas y redondear a 1 digito
	return roundString((mayorMins - menorMins) / 60.0, 1);
}

function isValidoFormatDate(valueDate) {
	var formatDate = /^(?:(?:31(\/|-|\.)(?:0?[13578]|1[02]))\1|(?:(?:29|30)(\/|-|\.)(?:0?[1,3-9]|1[0-2])\2))(?:(?:1[6-9]|[2-9]\d)?\d{2})$|^(?:29(\/|-|\.)0?2\3(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\d|2[0-8])(\/|-|\.)(?:(?:0?[1-9])|(?:1[0-2]))\4(?:(?:1[6-9]|[2-9]\d)?\d{2})$/;
	var isValid = formatDate.test(valueDate);
	if (isValid && (valueDate.indexOf("-") > -1)) {
		isValid = false;
	}
    return isValid;
}

function isValidoFormatHour(valueHour) {
	var formatHour = /^([0-1]?[0-9]|2[0-4]):([0-5][0-9])(:[0-5][0-9])?$/;
	return formatHour.test(valueHour);
}

function showMensaje(titulo, mensaje, functionAceptar) {
	return showMensajeModal(titulo, mensaje, functionAceptar, 'primary');
}

function showMensajeExito(titulo, mensaje, functionAceptar) {
	return showMensajeModal(titulo, mensaje, functionAceptar, 'exito');
}

function showMensajeError(titulo, mensaje, functionAceptar) {
	return showMensajeModal(titulo, mensaje, functionAceptar, 'error');
}

function showMensajeAlert(titulo, mensaje, functionAceptar) {
	return showMensajeModal(titulo, mensaje, functionAceptar, 'alert');
}

function showMensajeInfo(titulo, mensaje, functionAceptar) {
	return showMensajeModal(titulo, mensaje, functionAceptar, 'info');
}

function showMensajeDefault(titulo, mensaje, functionAceptar) {
	return showMensajeModal(titulo, mensaje, functionAceptar, 'default');
}

function showMensajeModal(titulo, mensaje, functionAceptar, tipoMensaje) {

	// NOTA: En este sistema, no se usa el parámetro título.

	// por si se desea manejar los colores de bootstrap
	if (tipoMensaje == 'exito' || tipoMensaje == 'success') {
		$('#divPopupPanelClass').prop('class', 'panel panel-success');
	} else if (tipoMensaje == 'error' || tipoMensaje == 'danger') {
		$('#divPopupPanelClass').prop('class', 'panel panel-danger');
	} else if (tipoMensaje == 'alert' || tipoMensaje == 'warning') {
		$('#divPopupPanelClass').prop('class', 'panel panel-warning');
	} else if (tipoMensaje == 'info') {
		$('#divPopupPanelClass').prop('class', 'panel panel-info');
	} else if (tipoMensaje == 'default') {
		$('#divPopupPanelClass').prop('class', 'panel panel-default');
	} else {
		$('#divPopupPanelClass').prop('class', 'panel panel-primary');
	}

	var mensajeTrim = $.trim(mensaje);

	if (mensajeTrim.length < 50) {
		$('#divPopupContainerClass').prop('class', 'container appMsgConfirmContainer verticalAlignmentHelper');
	} else {
		$('#divPopupContainerClass').prop('class', 'container appMsgConfirmContainerBigger verticalAlignmentHelper');
	}

	if ($('#divModalPopup').length) {

		// si se tiene el div de popup
		$('#divPopupMensaje').html(mensajeTrim);

		$('#divModalPopup').modal({
			keyboard: false
		});

		$('#btnPopupAceptar').off('click');
		if (estaDefinido(functionAceptar)) {
			$('#btnPopupAceptar').on('click', functionAceptar);
		}

		// pone el foco el boton aceptar, y de paso fix el bug que deja el foco
		// en algún control de la pantalla padre y puede efectuar operaciones con él.
		setTimeout(function () {
			$('#btnPopupAceptar').focus();
		}, 200);

	} else {

		// sino imprimir un simple alert
		alert(mensaje);
	}

}

function showMensajeConfirm(titulo, mensaje, functionAceptar, functionCancelar) {

	// NOTA: En este sistema, no se usa el parámetro título.
	var mensajeTrim = $.trim(mensaje);

	if (mensajeTrim.length < 50) {
		$('#divPopupContainerClassSINO').prop('class', 'container appMsgConfirmContainer verticalAlignmentHelper');
	} else {
		$('#divPopupContainerClassSINO').prop('class', 'container appMsgConfirmContainerBigger verticalAlignmentHelper');
	}

	$('#divPopupPanelClassSINO').prop('class', 'panel panel-primary');

	if ($('#divModalPopupSINO').length) {

		// si se tiene el div de popup
		$('#divPopupMensajeSINO').html(mensajeTrim);

		$('#divModalPopupSINO').modal({
			keyboard: false
		});

		$('#btnPopupAceptarSINO').off('click');
		if (estaDefinido(functionAceptar)) {
			$('#btnPopupAceptarSINO').on('click', functionAceptar);
		}

		$('#btnPopupCancelarSINO').off('click');
		if (estaDefinido(functionCancelar)) {
			$('#btnPopupCancelarSINO').on('click', functionCancelar);
		}

		// pone el foco el boton aceptar, y de paso fix el bug que deja el foco
		// en algún control de la pantalla padre y puede efectuar operaciones con él.
		setTimeout(function () {
			$('#btnPopupAceptarSINO').focus();
		}, 200);

	}

}

function showConfirmation(titulo, mensaje, functionAceptar, functionCancelar) {
	showMensajeConfirm( titulo, mensaje, functionAceptar, functionCancelar );
}

function makeInputMask( controlQuery, mask, maxlength, valorInicial, caracterMascara ) {
	
	var control = $( controlQuery );
	
	var caracter_mask = '';
	if ( caracterMascara != null )  caracter_mask = caracterMascara;
	
	control.inputmask( mask, {placeholder: caracter_mask});
	
	if ( maxlength != null ) {
		control.prop('maxlength', maxlength);	
	}
	
	if ( valorInicial != null ) {
		control.val( valorInicial );	
	}
	
}

// OTROS METODOS
function estadoInputInicial(divID) {

    ocultarMsgGeneralError();

    if ($(divID + ' :input').hasClass('date-picker') || $(divID + ' :input').hasClass('clas_email')) {
        $(divID + ' :input').parent().parent().find('span:last').html('');
        $(divID + ' :input').parent().parent().find('p:last').html('');
    } else {
        $(divID + ' :input').parent().find('span:last').html('');
    }

    $(divID + ' :input').closest('.form-group').removeClass('has-error');
    $(divID + ' select').closest('.form-group').removeClass('has-error');
    $(divID + ' textarea').closest('.form-group').removeClass('has-error');

    return null;
}

function mostrarMsgGeneralError(msgError) {

    // copiar la plantilla a la instancia
    $('#divMsgGeneralErrorInstance').html($('#divMsgGeneralError').html());

    // setear el mensaje en la plantilla
    $('#spanMsgGeneralError').html(msgError == null ? '' : msgError);

    // mostrar
    $('#divMsgGeneralErrorInstance').show();
}

function ocultarMsgGeneralError() {

    // copiar la plantilla a la instancia
    $('#divMsgGeneralErrorInstance').html($('#divMsgGeneralError').html());

    // limpiar el mensaje en la plantilla
    $('#spanMsgGeneralError').html('');

    // ocultar
    $('#divMsgGeneralErrorInstance').hide();
}

function estadoInputError(divID, data, estadoRestore) {

    //$('#divMsgGeneralError').hide();

    var msgError = data.msg;
    var campoError = data.dataJson.campoError;

    // sino viene con campo poco se puede hacer
    if (campoError == null) return;

    mostrarMsgGeneralError(msgError);

    // si el campo es un error general, mostrarlo como popup
    if (campoError == 'general') {
        showMensaje( 'Mensaje', msgError);
        return;
    }

    var selInput = $(divID + ' input[name=' + campoError + ']');
    var selSelec = $(divID + ' select[name=' + campoError + ']');
    var selTextA = $(divID + ' textarea[name=' + campoError + ']');

    if (selInput.length) {

        selInput.focus();

		if ($(divID + ' input[name=' + campoError + ']').hasClass('date-picker') || $(divID + ' input[name=' + campoError + ']').hasClass('clas_email')) {
			selInput.parent().parent().find('span:last').html(msgError);
			selInput.parent().parent().find('p:last').html(msgError);
		} else {
			var spanFecha = selInput.parent().parent().find('span:last');
			if ( spanFecha.length && spanFecha.hasClass('help-block') ) {
				spanFecha.html(msgError);
			} else {
				selInput.parent().find('span:last').html(msgError);
			}
		}

        selInput.closest('.form-group').addClass('has-error');
        selInput.focus();
    }

    if (selSelec.length) {

        selSelec.focus();
        selSelec.next().html(msgError);
        selSelec.closest('.form-group').addClass('has-error');
        selSelec.focus();
    }

    if (selTextA.length) {

        selTextA.focus();
        selTextA.next().html(msgError);
        selTextA.closest('.form-group').addClass('has-error');
        selTextA.focus();
    }

    // FIXME: si se llega a utilizar, con el estadoRestore reestablecer la estructura de datos
}

function handleUploadErrorJson(inputControl, nameControl, data) {
    if (inputControl != null) {
        cleanControlError(inputControl);
        uploadResetInputFile(inputControl);
    }
    $('#' + nameControl + '-barra').html($('#divErrorUpload').html());
}

function handleUploadError(inputControl, nameControl, data) {
    if (inputControl != null) {
        cleanControlError(inputControl);
        uploadResetInputFile(inputControl);
    }
    $('#' + nameControl + '-barra').html($('#divErrorUpload').html());
    handleError(data);
}

function handleUploadSuccess(inputControl, nameControl, data) {
    // NOTA: inputControl, data no se usan (por ahora)
    $('#' + nameControl + '-barra').html($('#divExitoUpload').html());
}

function uploadResetInputFile(inputControl) {
    inputControl.wrap('<form>').parent('form').trigger('reset');
    inputControl.unwrap();
}

function cleanControlError(inputControl) {
    // limpiar estilo de error
    if (inputControl != null) {
        inputControl.next().html('');
        inputControl.closest('.form-group').removeClass('has-error');
    }
}

function habilitarControles( divID, habilitar ) {
	$( divID ).find( 'input, textarea, button, select' ).prop( 'disabled', !habilitar);
}

function habilitarControl( controlID, habilitar ) {
	$( controlID ).prop( 'disabled', !habilitar);
}	

function eventSelectOnTD(tableID, tipo) {

    // seleccionar un input ( 'radio' | 'checkbox' segun tipo ) clickeando en todo el td
    $(tableID + ' tbody').on('click', 'td', function(e) {
        var input = $(this).find('input:' + tipo);
        input.prop('checked', !input.prop('checked'));
    });

    $(tableID + ' input:radio').on('click', function(e) {
        e.stopPropagation();
    });

}

function eventSelectOnTR(tableID) {

	// datatable
	var table = $( tableID ).DataTable();
	
    // seleccionar un input clickeando en todo el tr
    $(tableID + ' tbody').on('click', 'tr', function(e) {
    	
    	var tr = $(this);
    	
    	// cambiar de estilo seleccionado/deseleccionado
		if ( tr.hasClass('info') ) {
			tr.removeClass('info');
		} else {
			table.$('tr.info').removeClass('info');
			tr.addClass('info');
		}
    	
    	// seleccionar/deseleccionar el radio button
        var inputArray = tr.find('td input:radio');
        if (inputArray.length && inputArray[0] != null) {
            var checked = $(inputArray[0]).prop('checked');
            $(inputArray[0]).prop('checked', !checked);
        }
        
    });
    
    $(tableID + ' tbody tr td input:radio').on('click', function(e) {
    	
    	e.stopPropagation();
    	
    	var checked = $(this).prop('checked');
    	
    	consoleLog( 'checked: ' + checked );
    	
    	var tr = $(this).parent().parent();
    	
    	// cambiar de estilo seleccionado/deseleccionado
		if ( tr.hasClass('info') ) {
			tr.removeClass('info');
		} else {
			table.$('tr.info').removeClass('info');
			tr.addClass('info');
		}
 
    });

}

function initDG(tableID, showPaginar, showSearch, configColumnDefs) {

    var grillaObject = $(tableID).dataTable({
        "lengthMenu": [
            [10, 25, 50, -1],
            [10, 25, 50, "All"]
        ],
        "responsive": true,
        "bAutoWidth": false,
        "paging": showPaginar,
        "searching": showSearch,
        "language": {
        	"url": "libs/datatables/idiomas/Spanish.json"
        },
        "columnDefs": configColumnDefs
    });
    
    /*
    $(window).on('resize', function () {
    	grillaObject.fnAdjustColumnSizing();
    });
    */

    return grillaObject;
}

function extraerDataDG(tableID, arrayColumnsIgnorar) {

    var dataJson = $(tableID).DataTable().rows().data();

    // validar que haya data para exportar
    if (dataJson == null || dataJson == '' || dataJson.length == 0) {
        return null;
    }

    var data = [];
    var dataFila = {};
	var col = 0;
    var ignorarCol = false;

    for (var i = 0; i < dataJson.length; i++) {

        dataFila = {};
        col = 0;

        for (var j in dataJson[i]) {

            // bloque para ignorar columnas (como links, radio buttons, etc)
            ignorarCol = false;
            if (arrayColumnsIgnorar != null && arrayColumnsIgnorar.length > 0) {

                for (var indexIgnorar in arrayColumnsIgnorar) {

                    if (j == arrayColumnsIgnorar[indexIgnorar]) {
                        ignorarCol = true;
                        break;
                    }

                }

            }

            // si bandera ignorar es true continuar con la siguiente columna j
            if (ignorarCol) continue;

            //             	dataFila[ 'col' + col ] =  dataJson[i][j];
            dataFila['col' + j] = dataJson[i][j];

            col++;

        }

        data[i] = dataFila;

    }

    return {
        filas: (dataJson == null || dataJson.length == null ? 0 : dataJson.length),
        columnas: (col == null ? 0 : col),
        data: data
    };
}

function JSONToCSVConvertor(JSONData, nombreArchivo, tableID) {
    var arrData = typeof JSONData !== 'object' ? JSON.parse(JSONData) : JSONData;
    var CSV = '';

    var row = "<table border='1'><tr>";
    for (var index in arrData[0]) {
        row += '<td>' + index + '</td>';
    }
    row += '</tr>';
    CSV += row + '\r\n';
    for (var i = 0; i < arrData.length; i++) {
        var row = "<tr>";
        for (var index in arrData[i]) {
            row += '<td>' + arrData[i][index] + '</td>';
        }
        row += '</tr>';

        CSV += row + '\r\n';
    }
    row += '</table>';

    CSV += row + '\r\n';
    CSV = "<table border=1>" + $(tableID).html() + "</table>";
    console.debug(CSV);
    var ua = window.navigator.userAgent.toLowerCase();
    if (ua.indexOf('firefox') !== -1) {
        ua = 'firefox';
    } else {
        if (ua.indexOf('chrome') !== -1) {
            ua = 'chrome';
        } else { //ie
            ua = 'ie';
        }
    }

    if (ua !== 'ie') {
        if (CSV === '') {
            console.debug("datos invalidos");
            return;
        }
        // TODO/FIXME: escape() esta deprecado, buscar otro
        var uri = 'data:application/vnd.ms-excel,' + escape(CSV);
        var link = document.createElement("a");
        link.href = uri;
        link.style = "visibility:hidden";
        link.download = nombreArchivo + ".XLS";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    } else {
        var blobObject = new Blob([CSV]);
        window.navigator.msSaveBlob(blobObject, 'msSaveBlob_testFile.xls'); // The user only has the option of clicking the Save button.
    }
}
