// Clase SimioConfigApp
// Clase configuracion de la app: nombre de la bd, esquema de bd, servicios REST, etc
// Usa la variable global: TABLAS
function SimioConfigApp() {

    // NOMBRE DE LA BD
    this.getNombreBD = function () {
        return 'simioBD';
    }

    // ESQUEMA DE LA BD
    this.getEsquemaBD = function () {

        return {
            'schema': {
                '1': function (versionTransaction) {
                    // CREACION DE TABLAS

                    // EJEMPLO: creando la tabla nombre_tabla
                    /*
                    var nombre_tabla = versionTransaction.createObjectStore('nombre_tabla', {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // nombre_tabla.createIndex('price');
                    // nombre_tabla.createIndex('by_name', 'name', { unique : false });
                     */

                    // LLENADO DE TABLAS

                    // TABLA area
                    var area = versionTransaction.createObjectStore(TABLAS.area, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA bien
                    var bien = versionTransaction.createObjectStore(TABLAS.bien, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA catalogo
                    var catalogo = versionTransaction.createObjectStore(TABLAS.catalogo, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA catalogoBien
                    var catalogoBien = versionTransaction.createObjectStore(TABLAS.catalogoBien, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA clase
                    var clase = versionTransaction.createObjectStore(TABLAS.clase, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA cuenta
                    var cuenta = versionTransaction.createObjectStore(TABLAS.cuenta, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA dependencia
                    var dependencia = versionTransaction.createObjectStore(TABLAS.dependencia, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA empleado
                    var empleado = versionTransaction.createObjectStore(TABLAS.empleado, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    var empleadoUbicacion = versionTransaction.createObjectStore(TABLAS.empleadoUbicacion, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA entidad
                    var entidad = versionTransaction.createObjectStore(TABLAS.entidad, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA grupo
                    var grupo = versionTransaction.createObjectStore(TABLAS.grupo, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA grupoClase
                    var grupoClase = versionTransaction.createObjectStore(TABLAS.grupoClase, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA locales
                    var locales = versionTransaction.createObjectStore(TABLAS.locales, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA oficina
                    var oficina = versionTransaction.createObjectStore(TABLAS.oficina, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA parametro
                    var parametro = versionTransaction.createObjectStore(TABLAS.parametro, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA ubigeo
                    var ubigeo = versionTransaction.createObjectStore(TABLAS.ubigeo, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA inventario
                    var inventario = versionTransaction.createObjectStore(TABLAS.inventario, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA inventarioBien
                    var inventarioBien = versionTransaction.createObjectStore(TABLAS.inventarioBien, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // TABLA inventarioApertura
                    var inventarioApertura = versionTransaction.createObjectStore(TABLAS.inventarioApertura, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    // OTROS
                    var session = versionTransaction.createObjectStore(TABLAS.session, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                    var test = versionTransaction.createObjectStore(TABLAS.test, {
                        'keyPath': 'localKey',
                        'autoIncrement': false
                    });

                }
            }
        };
    }


    // OPERACIONES PARA ABRIR Y CERRAR BASE DE DATOS

    // abre o crea una base si esta no existe
    this.abrirCrearBD = function (functionOK, functionError) {

        $.simiosDB(this.getNombreBD(), this.getEsquemaBD()).then(functionOK, functionError);
    }

    // elimina una bd existente
    this.eliminarBD = function (functionOK, functionError) {

        $.simiosDB(this.getNombreBD()).deleteDatabase().then(functionOK, functionError);
    }

    // este metodo borra la bd existente y crea una nueva
    this.eliminarYCrearBD = function (functionOK, functionError) {

        var nombreBD = this.getNombreBD();
        var esquemaBD = this.getEsquemaBD();

        // primero se elimina
        $.simiosDB(nombreBD).deleteDatabase().then(function() {

            // abrirCrear la BD que existente que se acaba de borrar
            $.simiosDB(nombreBD, esquemaBD).then(functionOK, functionError);

        }, function() {

            // puede que ocurrio error al borrar una bd no existente, en este caso, igual corresponde crearla
            $.simiosDB(nombreBD, esquemaBD).then(functionOK, functionError);
        });
    }

    // este metodo valida si existe la bd o no
    this.existeBD = function(functionSI, functionNO) {

        // solo para test: 'testID' sobre la tabla 'test'
        $.simiosDB(this.getNombreBD()).objectStore(TABLAS.test).get('testID').then(functionSI, functionNO);
    }

}