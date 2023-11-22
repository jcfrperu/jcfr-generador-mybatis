// VARIABLES GLOBALES
var TABLAS = new SimioTabla();
var CONSTANTES = new SimioConstantes();

var gSimioConfig = new SimioConfigApp();                         // configuracion de la BD
var gSimioDAO = new SimioDAO(gSimioConfig.getNombreBD());        // agrupador de DAOs para manipular las tablas de la BD
var gSimioMaker = new SimioObjectMaker();                        // objeto para crear beans con la estructura correcta para insertar en una tabla
var gSimioRAM = {'tabla': {}};                                 // objeto que contiene las tablas en memoria

var data_tb_catalogo_bien = [];

/*Variables de RAM para cuando se seleccione un usuario guarde en memoria las areas en las que se encuentra*/
var areaIDPorEmpleado = [];
var indexAreaIDPorEmpleado = 0;

function initApp() {

    // validar que llegue desde el backend y que no haya escrito la URL en el navegador

    gSimioConfig.existeBD(function () {

        initHidden();
        initRAM();
        initEventos();
        initCombos();
        changeCatalogoBien();
        changeTipoCuenta();
        changeUsuario();
        changeLocales();
        changeAreas();
        initDatosUsuario();

        // TODO/FIXME: coordinar con JOSE para que sepa que al inicio cargara el listado y ya no su pantalla de registro
        mostrarPagina('#div-vista-registro');

    }, function () {

        showMensaje('Mensaje', 'No tiene permiso para esta acci&oacute;n', function () {
            bloquearApp();
        });
    });
}

function mostrarPagina(divID) {

    // oculta todas las vistas
    ocultarVistas();

    // muestra solo la del parametro
    $(divID).show();
}

function ocultarVistas() {

    // ocultar todas los divs asociados a las vistas
    $('#div-vista-listado').hide();
    $('#div-vista-registro').hide();
    $('#div-vista-ver').hide();
    $('#div-vista-resumen').hide();
    $('#div-vista-conexion').hide();
}

function cleanApp() {

    TABLAS = null;
    CONSTANTES = null;

    gSimioConfig = null;
    gSimioDAO = null;
    gSimioMaker = null;
    gSimioRAM = null;
}

function bloquearApp() {

    ocultarVistas();
    cleanApp();
    eliminarBD();
}

function eliminarBD() {

    gSimioConfig.eliminarBD(function () {
        consoleLog('se borro la bd');
    }, function () {
        consoleLog('no se pudo borrar la bd');
    });
}

function initHidden() {

    $('#hiddenForms').hide();
}

function initDatosUsuario() {

    if (estaDefinido(gSimioRAM) && estaDefinido(gSimioRAM.tabla) && estaDefinido(gSimioRAM.tabla.session)) {

        var sessionData = gSimioRAM.tabla.session[0];

        if (estaDefinido(sessionData)) {

            $('#div-header-inventariador').html(sessionData.usuarioNombre);
            $('#div-header-empresa').html( 'Entidad ' + sessionData.entidad);
            $('#div-header-inventario').html(sessionData.inventarioNombre);
        }
    }
}


function initRAM() {

    // copia todas las tablas a variables javascript clasicas (en memoria)
    copiarTablaRAM('area');
    copiarTablaRAM('bien');
    copiarTablaRAM('catalogo');
    copiarTablaRAM('catalogoBien');
    copiarTablaRAM('clase');
    copiarTablaRAM('cuenta');
    copiarTablaRAM('dependencia');
    copiarTablaRAM('empleado');
    copiarTablaRAM('empleadoUbicacion');
    copiarTablaRAM('entidad');
    copiarTablaRAM('grupo');
    copiarTablaRAM('grupoClase');
    copiarTablaRAM('locales');
    copiarTablaRAM('oficina');
    copiarTablaRAM('parametro');
    copiarTablaRAM('ubigeo');
    copiarTablaRAM('inventario');
    copiarTablaRAM('inventarioBien');
    copiarTablaRAM('inventarioApertura');
    copiarTablaRAM('session');
}

function copiarTablaRAM(nombreTabla) {

    // crear dentro del objeto "tabla", un arreglo llamado "nombreTabla"
    gSimioRAM.tabla[nombreTabla] = [];

    // accediendo al DAO con el nombre "nombreTabla"
    gSimioDAO[nombreTabla].eachItem(function (item) {

        // agregando el item de la BD a la tabla en momeria
        gSimioRAM.tabla[nombreTabla].push(item.value);
    });
}

function initEventos() {

    // TODO/FIXME: coordinar con jose para que el registro de archivos se haga en los archivos simioapp-listado.js y simioapp-registro.js
    $('#bnt_grabar').on('click', clickBtnkAgregar);

    $('#btn-menu-der-ir-modo-online').on('click', clickBtnIrModoOnline);

    $('#btn-menu-izq-listado-bienes').on('click', clickBtnListadoBienes);   // dentro del fichero simioapp-listado.js
    $('#btn-menu-izq-nuevo-bien').on('click', clickBtnNuevoBien);           // dentro del fichero simioapp-registro.js
    $('#btn-menu-izq-editar-bien').on('click', clickBtnEditarBien);         // dentro del fichero simioapp-registro.js
    $('#btn-menu-izq-quitar-bien').on('click', clickBtnQuitarBien);         // dentro del fichero simioapp-listado.js
    $('#btn-menu-izq-ver-bien').on('click', clickBtnVerBien);               // dentro del fichero simioapp-listado.js
    $('#btn-menu-izq-resumen-toma').on('click', clickBtnResumenToma);       // dentro del fichero simioapp-listado.js
    $('#btn-menu-der-revisar-conexion').on('click', clickBtnConexion);      // dentro del fichero simioapp-listado.js
}

function clickBtnIrModoOnline() {

    if (estaDefinido(gSimioRAM) && estaDefinido(gSimioRAM.tabla) && estaDefinido(gSimioRAM.tabla.session)) {

        var sessionData = gSimioRAM.tabla.session[0];

        if (estaDefinido(sessionData)) {

            var form = $('#form-ir-modo-online');

            form.find('input[name="usuario"]').val(sessionData.usuario);
            form.find('input[name="entidad"]').val(sessionData.entidad);
            form.find('input[name="inventario"]').val(sessionData.inventario);
            form.find('input[name="token"]').val(sessionData.token);

            form.submit();
        }
    }
}