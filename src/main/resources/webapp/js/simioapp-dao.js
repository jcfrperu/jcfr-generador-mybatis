// Clase SimioDAO
// Clase que agrupa los DAO de las tablas de la aplicacion.
// Usar esta clase para acceder a una tabla de la BD y luego hacer una operacion basica ella
// Usa la variable global: TABLAS
function SimioDAO(nombreBD) {

    this.nombreBD = nombreBD;

    // DAOS de tablas bd
    this.area = new SimioTablaDAO(this.nombreBD, TABLAS.area);
    this.bien = new SimioTablaDAO(this.nombreBD, TABLAS.bien);
    this.catalogo = new SimioTablaDAO(this.nombreBD, TABLAS.catalogo);
    this.catalogoBien = new SimioTablaDAO(this.nombreBD, TABLAS.catalogoBien);
    this.clase = new SimioTablaDAO(this.nombreBD, TABLAS.clase);
    this.cuenta = new SimioTablaDAO(this.nombreBD, TABLAS.cuenta);
    this.dependencia = new SimioTablaDAO(this.nombreBD, TABLAS.dependencia);
    this.empleado = new SimioTablaDAO(this.nombreBD, TABLAS.empleado);
    this.empleadoUbicacion = new SimioTablaDAO(this.nombreBD, TABLAS.empleadoUbicacion);
    this.session = new SimioTablaDAO(this.nombreBD, TABLAS.session);
    this.entidad = new SimioTablaDAO(this.nombreBD, TABLAS.entidad);
    this.grupo = new SimioTablaDAO(this.nombreBD, TABLAS.grupo);
    this.grupoClase = new SimioTablaDAO(this.nombreBD, TABLAS.grupoClase);
    this.locales = new SimioTablaDAO(this.nombreBD, TABLAS.locales);
    this.oficina = new SimioTablaDAO(this.nombreBD, TABLAS.oficina);
    this.parametro = new SimioTablaDAO(this.nombreBD, TABLAS.parametro);
    this.ubigeo = new SimioTablaDAO(this.nombreBD, TABLAS.ubigeo);
    this.inventario = new SimioTablaDAO(this.nombreBD, TABLAS.inventario);
    this.inventarioBien = new SimioTablaDAO(this.nombreBD, TABLAS.inventarioBien);
    this.inventarioApertura = new SimioTablaDAO(this.nombreBD, TABLAS.inventarioApertura);
    this.test = new SimioTablaDAO(this.nombreBD, TABLAS.test);

    // METODO PARA CREAR UNA TRANSACCION (se le pasan las tablas como parametros)
    this.crearTXDAO = function (tablasArray) {
        // PRE: tablasArray debe ser un arreglo de cadenas ["nombreTabla01", "nombreTabla02"]
        return new SimioTXDAO(this.nombreBD, tablasArray);
    }
}

// Clase SimioObjectMaker
// Clase que permite crear objetos javascript (que hacen match con las tablas de BD local)
// Favor de no modificar los campos de estos objetos (salvo consenso con el equipo sobre la estructura de tablas de la BD local).
// Si por algun motivo, se desea aprovechar el objeto para almacenar mas informacion/datos adicionales,
// usar el campo 'localInfo' y ahi agregar la info adicional en formato de objeto
function SimioObjectMaker() {

    // Fabrica para crear objetos que reprentan una fila en la base de datos local.
    // fuente: es un objeto que si tiene sus campos iguales al del objeto a crear, copia sus valores al crearlo
    // parseToString: es un true/false que indica si al copiar los campos de fuente, cada campito se va copiar tal como es (false) o si lo convertira en string primero (true)

    // crea un objeto area, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeAreaObject = function (fuente, parseToString) {

        var itemBD = {
            'areaID': '',               // integer
            'abreviaturaArea': '',      // string
            'entidadID': '',            // integer
            'localesID': '',            // integer
            'nombreArea': '',           // string
            'localInfo': {
                'pk': 'areaID'
            },                          // objeto con info adicional
            'localKey': ''              // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto bien, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeBienObject = function (fuente, parseToString) {

        var itemBD = {
            'bienID': '',                                 // integer
            'codigoPatrimonial': '',                      // string
            'entidadID': '',                              // integer
            'localesID': '',                              // integer
            'areaID': '',                                 // integer
            'empleadoID': '',                             // integer
            'oficinaID': '',                              // integer
            'tipoCausalAlta': '',                         // string
            'anho': '',                                   // integer
            'matricula': '',                              // string
            'ancho': '',                                  // double
            'valorLibro': '',                             // double
            'cuentaConSeguro': '',                        // string
            'estadoBien': '',                             // string
            'dimension': '',                              // string
            'condicion': '',                              // string
            'numeroDocAdquisicion': '',                   // string
            'color': '',                                  // string
            'fechaAdquisicion': '',                       // date
            'entDisp': '',                                // string
            'entArre': '',                                // string
            'fechaVafec': '',                             // date
            'edad': '',                                   // string
            'motivoEliminacionBien': '',                  // string
            'valorAdquisicion': '',                       // double
            'anioFabricacion': '',                        // string
            'resolAfec': '',                              // string
            'especie': '',                                // string
            'raza': '',                                   // string
            'fechaAfec': '',                              // date
            'flgActo': '',                                // string
            'modelo': '',                                 // string
            'flgCausal': '',                              // string
            'marca': '',                                  // string
            'numeroResolucionBaja': '',                   // string
            'resolArre': '',                              // string
            'dscOtros': '',                               // string
            'fechaArre': '',                              // date
            'fechaDisposicion': '',                       // date
            'resolDisp': '',                              // string
            'altura': '',                                 // string
            'numeroChasis': '',                           // string
            'valortasa': '',                              // string
            'placa': '',                                  // string
            'sitBinv': '',                                // string
            'numeroCuentaContable': '',                   // string
            'serie': '',                                  // string
            'estBien': '',                                // string
            'entAfec': '',                                // string
            'codanterio': '',                             // string
            'tipoCuenta': '',                             // string
            'estGestio': '',                              // string
            'docBaja': '',                                // string
            'numeroMotor': '',                            // string
            'longitud': '',                               // string
            'pais': '',                                   // string
            'fechaBaja': '',                              // date
            'fechaVarre': '',                             // date
            'denominacionBien': '',                       // string
            'fechaDepreciacion': '',                      // date
            'valorDeprecEjercicio': '',                   // double
            'valorDeprecAcumulado': '',                   // double
            'valorNeto': '',                              // double
            'tipUsoCuenta': '',                           // string
            'catalogoBienID': '',                         // integer
            'otrasCaract': '',                            // string
            'descripcionUbicacionBien': '',               // string
            'causalBaja': '',                             // string
            'actoDisposicionBien': '',                    // string
            'entidadBeneficiadaActoDisposicion': '',      // string
            'actoAdministracionBien': '',                 // string
            'numeroResolucionAdministracion': '',         // string
            'fechaAdministracion': '',                    // date
            'fechaVencActoAdmin': '',                     // date
            'entidadBeneficiadaActoAdmin': '',            // string
            'docAltaSbn': '',                             // string
            'docBajaSbn': '',                             // string
            'tipo': '',                                   // string
            'numeroResolucionDisp': '',                   // string
            'codigoBarras': '',                           // string
            'localInfo': {
                'pk': 'bienID'
            },                                            // objeto con info adicional
            'localKey': ''                                // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto inventario bien, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeInventarioBienObject = function (fuente, parseToString) {

        var itemBD = {
            'inventarioBienID': '',                       // integer
            'bienID': '',                                 // integer
            'inventarioID': '',                           // integer
            'estadoSubida': '',                           // string
            'sobranteFaltante': '',                       // string
            'inventariador': '',                          // string
            'codigoPatrimonial': '',                      // string
            'entidadID': '',                              // integer
            'localesID': '',                              // integer
            'areaID': '',                                 // integer
            'empleadoID': '',                             // integer
            'oficinaID': '',                              // integer
            'tipoCausalAlta': '',                         // string
            'anho': '',                                   // integer
            'matricula': '',                              // string
            'ancho': '',                                  // double
            'valorLibro': '',                             // double
            'cuentaConSeguro': '',                        // string
            'estadoBien': '',                             // string
            'dimension': '',                              // string
            'condicion': '',                              // string
            'numeroDocAdquisicion': '',                   // string
            'color': '',                                  // string
            'fechaAdquisicion': '',                       // date
            'entDisp': '',                                // string
            'entArre': '',                                // string
            'fechaVafec': '',                             // date
            'edad': '',                                   // string
            'motivoEliminacionBien': '',                  // string
            'valorAdquisicion': '',                       // double
            'anioFabricacion': '',                        // string
            'resolAfec': '',                              // string
            'especie': '',                                // string
            'raza': '',                                   // string
            'fechaAfec': '',                              // date
            'flgActo': '',                                // string
            'modelo': '',                                 // string
            'flgCausal': '',                              // string
            'marca': '',                                  // string
            'numeroResolucionBaja': '',                   // string
            'resolArre': '',                              // string
            'dscOtros': '',                               // string
            'fechaArre': '',                              // date
            'fechaDisposicion': '',                       // date
            'resolDisp': '',                              // string
            'altura': '',                                 // string
            'numeroChasis': '',                           // string
            'valortasa': '',                              // string
            'placa': '',                                  // string
            'sitBinv': '',                                // string
            'numeroCuentaContable': '',                   // string
            'serie': '',                                  // string
            'estBien': '',                                // string
            'entAfec': '',                                // string
            'codanterio': '',                             // string
            'tipoCuenta': '',                             // string
            'estGestio': '',                              // string
            'docBaja': '',                                // string
            'numeroMotor': '',                            // string
            'longitud': '',                               // string
            'pais': '',                                   // string
            'fechaBaja': '',                              // date
            'fechaVarre': '',                             // date
            'denominacionBien': '',                       // string
            'fechaDepreciacion': '',                      // date
            'valorDeprecEjercicio': '',                   // double
            'valorDeprecAcumulado': '',                   // double
            'valorNeto': '',                              // double
            'tipUsoCuenta': '',                           // string
            'catalogoBienID': '',                         // integer
            'otrasCaract': '',                            // string
            'descripcionUbicacionBien': '',               // string
            'causalBaja': '',                             // string
            'actoDisposicionBien': '',                    // string
            'entidadBeneficiadaActoDisposicion': '',      // string
            'actoAdministracionBien': '',                 // string
            'numeroResolucionAdministracion': '',         // string
            'fechaAdministracion': '',                    // date
            'fechaVencActoAdmin': '',                     // date
            'entidadBeneficiadaActoAdmin': '',            // string
            'docAltaSbn': '',                             // string
            'docBajaSbn': '',                             // string
            'tipo': '',                                   // string
            'numeroResolucionDisp': '',                   // string
            'codigoBarras': '',                           // string
            'localInfo': {
                'pk': 'inventarioBienID',
                'accion': ''                              // I: insertar, A: actualizar, E: eliminar
            },                                            // objeto con info adicional
            'localKey': ''                                // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto inventario apertura, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeInventarioAperturaObject = function (fuente, parseToString) {

        var itemBD = {
            'inventarioAperturaID': '',                   // integer
            'bienID': '',                                 // integer
            'inventarioID': '',                           // integer
            'estadoSubida': '',                           // string
            'sobranteFaltante': '',                       // string
            'inventariador': '',                          // string
            'codigoPatrimonial': '',                      // string
            'entidadID': '',                              // integer
            'localesID': '',                              // integer
            'areaID': '',                                 // integer
            'empleadoID': '',                             // integer
            'oficinaID': '',                              // integer
            'tipoCausalAlta': '',                         // string
            'anho': '',                                   // integer
            'matricula': '',                              // string
            'ancho': '',                                  // double
            'valorLibro': '',                             // double
            'cuentaConSeguro': '',                        // string
            'estadoBien': '',                             // string
            'dimension': '',                              // string
            'condicion': '',                              // string
            'numeroDocAdquisicion': '',                   // string
            'color': '',                                  // string
            'fechaAdquisicion': '',                       // date
            'entDisp': '',                                // string
            'entArre': '',                                // string
            'fechaVafec': '',                             // date
            'edad': '',                                   // string
            'motivoEliminacionBien': '',                  // string
            'valorAdquisicion': '',                       // double
            'anioFabricacion': '',                        // string
            'resolAfec': '',                              // string
            'especie': '',                                // string
            'raza': '',                                   // string
            'fechaAfec': '',                              // date
            'flgActo': '',                                // string
            'modelo': '',                                 // string
            'flgCausal': '',                              // string
            'marca': '',                                  // string
            'numeroResolucionBaja': '',                   // string
            'resolArre': '',                              // string
            'dscOtros': '',                               // string
            'fechaArre': '',                              // date
            'fechaDisposicion': '',                       // date
            'resolDisp': '',                              // string
            'altura': '',                                 // string
            'numeroChasis': '',                           // string
            'valortasa': '',                              // string
            'placa': '',                                  // string
            'sitBinv': '',                                // string
            'numeroCuentaContable': '',                   // string
            'serie': '',                                  // string
            'estBien': '',                                // string
            'entAfec': '',                                // string
            'codanterio': '',                             // string
            'tipoCuenta': '',                             // string
            'estGestio': '',                              // string
            'docBaja': '',                                // string
            'numeroMotor': '',                            // string
            'longitud': '',                               // string
            'pais': '',                                   // string
            'fechaBaja': '',                              // date
            'fechaVarre': '',                             // date
            'denominacionBien': '',                       // string
            'fechaDepreciacion': '',                      // date
            'valorDeprecEjercicio': '',                   // double
            'valorDeprecAcumulado': '',                   // double
            'valorNeto': '',                              // double
            'tipUsoCuenta': '',                           // string
            'catalogoBienID': '',                         // integer
            'otrasCaract': '',                            // string
            'descripcionUbicacionBien': '',               // string
            'causalBaja': '',                             // string
            'actoDisposicionBien': '',                    // string
            'entidadBeneficiadaActoDisposicion': '',      // string
            'actoAdministracionBien': '',                 // string
            'numeroResolucionAdministracion': '',         // string
            'fechaAdministracion': '',                    // date
            'fechaVencActoAdmin': '',                     // date
            'entidadBeneficiadaActoAdmin': '',            // string
            'docAltaSbn': '',                             // string
            'docBajaSbn': '',                             // string
            'tipo': '',                                   // string
            'numeroResolucionDisp': '',                   // string
            'codigoBarras': '',                           // string
            'localInfo': {
                'pk': 'inventarioAperturaID'
            },                                            // objeto con info adicional
            'localKey': ''                                // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto catalogo, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeCatalogoObject = function (fuente, parseToString) {

        var itemBD = {
            'catalogoID': '',            // integer
            'catalogo': '',              // string
            'datacatalogo': '',          // string
            'tipo': '',                  // string
            'descripcion': '',           // string
            'descripcionCorta': '',      // string
            'localInfo': {
                'pk': 'catalogoID'
            },                          // objeto con info adicional
            'localKey': ''               // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto catalogo bien, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeCatalogoBienObject = function (fuente, parseToString) {

        var itemBD = {
            'catalogoBienID': '',      // integer
            'codigoBien': '',          // string
            'denominacion': '',        // string
            'grupoID': '',             // integer
            'claseID': '',             // integer
            'grupo': '',               // string
            'clase': '',               // string
            'dtMarca': '',             // string
            'dtModelo': '',            // string
            'dtTipo': '',              // string
            'dtDimension': '',         // string
            'dtColor': '',             // string
            'dtSerie': '',             // string
            'dtAnio': '',              // string
            'dtPlaca': '',             // string
            'dtRaza': '',              // string
            'dtMotor': '',             // string
            'dtNombre': '',            // string
            'dtChasis': '',            // string
            'dtEdad': '',              // string
            'dtBtnOtro': '',           // string
            'localInfo': {
                'pk': 'catalogoBienID'
            },                         // objeto con info adicional
            'localKey': ''             // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto clase, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeClaseObject = function (fuente, parseToString) {

        var itemBD = {
            'claseID': '',          // integer
            'clase': '',            // string
            'descripcion': '',      // string
            'localInfo': {
                'pk': 'claseID'
            },                      // objeto con info adicional
            'localKey': ''          // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto cuenta, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeCuentaObject = function (fuente, parseToString) {

        var itemBD = {
            'cuentaID': '',      // integer
            'cuenta': '',        // string
            'denomina': '',      // string
            'tipoCta': '',       // string
            'flagCta': '',       // string
            'usoCta': '',        // string
            'localInfo': {
                'pk': 'cuentaID'
            },                   // objeto con info adicional
            'localKey': ''       // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto dependencia, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeDependenciaObject = function (fuente, parseToString) {

        var itemBD = {
            'dependenciaID': '',          // integer
            'codigoDependencia': '',      // string
            'descripcion': '',            // string
            'localInfo': {
                'pk': 'dependenciaID'
            },                            // objeto con info adicional
            'localKey': ''                // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto empleado, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeEmpleadoObject = function (fuente, parseToString) {

        var itemBD = {
            'empleadoID': '',                  // integer
            'codigo': '',                      // string
            'entidadID': '',                   // integer
            'numeroDocIdentPersonal': '',      // string
            'tipoDocIdentidad': '',            // string
            'nombres': '',                     // string
            'apellidos': '',                   // string
            'modalidadContrato': '',           // string
            'apellidoPaterno': '',             // string
            'apellidoMaterno': '',             // string
            'localInfo': {
                'pk': 'empleadoID'
            },                                 // objeto con info adicional
            'localKey': ''                     // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto empleadoUbicacion, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeEmpleadoUbicacionObject = function (fuente, parseToString) {

        var itemBD = {
            'empleadoUbicacionID': '',         // integer
            'empleadoID': '',                  // integer
            'entidadID': '',                   // integer
            'localesID': '',                   // integer
            'areaID': '',                      // integer
            'oficinaID': '',                   // integer
            'tipoAsociacion': '',              // string
            'localInfo': {
                'pk': 'empleadoUbicacionID'
            },                                 // objeto con info adicional
            'localKey': ''                     // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto entidad, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeEntidadObject = function (fuente, parseToString) {

        var itemBD = {
            'entidadID': '',          // integer
            'codigoEntidad': '',      // string
            'entidad': '',            // string
            'dependenciaID': '',      // integer
            'rucEntidad': '',         // string
            'localInfo': {
                'pk': 'entidadID'
            },                        // objeto con info adicional
            'localKey': ''            // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto grupo, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeGrupoObject = function (fuente, parseToString) {

        var itemBD = {
            'grupoID': '',          // integer
            'grupo': '',            // string
            'descripcion': '',      // string
            'localInfo': {
                'pk': 'grupoID'
            },                      // objeto con info adicional
            'localKey': ''          // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto grupoClase, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeGrupoClaseObject = function (fuente, parseToString) {

        var itemBD = {
            'grupoID': '',       // integer
            'claseID': '',       // integer
            'cuenta': '',        // string
            'localInfo': {
                'pk': ['grupoID', 'claseID']
            },                   // objeto con info adicional
            'localKey': ''       // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto locales, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeLocalesObject = function (fuente, parseToString) {

        var itemBD = {
            'localesID': '',         // integer
            'codigo': '',            // string
            'entidadID': '',         // integer
            'nombreLocal': '',       // string
            'direccion': '',         // string
            'dscMz': '',             // string
            'dscTomo': '',           // string
            'ctdArea': '',           // double
            'dscFojas': '',          // string
            'codigoUbigeo': '',      // string
            'flgPropie': '',         // string
            'dscBenefi': '',         // string
            'dscAsinab': '',         // string
            'dscPelect': '',         // string
            'flgTipovi': '',         // string
            'flgTipore': '',         // string
            'dscCtactb': '',         // string
            'dscAsient': '',         // string
            'codigoPredio': '',      // string
            'ctdValor': '',          // double
            'flgUm': '',             // string
            'numeroMun': '',         // string
            'dscLote': '',           // string
            'dscPropie': '',         // string
            'flgTipomo': '',         // string
            'localInfo': {
                'pk': 'localesID'
            },                       // objeto con info adicional
            'localKey': ''           // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto oficina, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeOficinaObject = function (fuente, parseToString) {

        var itemBD = {
            'oficinaID': '',               // integer
            'abreviaturaOficina': '',      // string
            'entidadID': '',               // integer
            'localesID': '',               // integer
            'areaID': '',                  // integer
            'nombreOficina': '',           // string
            'pisoOficina': '',             // string
            'localInfo': {
                'pk': 'oficinaID'
            },                             // objeto con info adicional
            'localKey': ''                 // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto parametro, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeParametroObject = function (fuente, parseToString) {

        var itemBD = {
            'parametroID': '',      // integer
            'nombre': '',           // string
            'valor': '',            // string
            'tipoValor': '',        // string
            'descripcion': '',      // string
            'localInfo': {
                'pk': 'parametroID'
            },                      // objeto con info adicional
            'localKey': ''          // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto ubigeo, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeUbigeoObject = function (fuente, parseToString) {

        var itemBD = {
            'ubigeoID': '',          // integer
            'codigoUbigeo': '',      // string
            'descripcion': '',       // string
            'localInfo': {
                'pk': 'ubigeoID'
            },                       // objeto con info adicional
            'localKey': ''           // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // crea un objeto inventario, fuente y parseToString son opcionales por si desea crearlo a partir de otro objeto
    this.makeInventarioObject = function (fuente, parseToString) {

        var itemBD = {
            'inventarioID': '',      // integer
            'entidadID': '',         // integer
            'nombre': '',            // string
            'fechaApertura': '',     // date
            'fechaCierre': '',       // date
            'estado': '',            // string
            'localInfo': {
                'pk': 'inventarioID'
            },                       // objeto con info adicional
            'localKey': ''           // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    this.makeSessionObject = function (fuente, parseToString) {

        var itemBD = {
            'usuario': '',            // string
            'usuarioNombre': '',      // string
            'entidad': '',            // string
            'entidadNombre': '',      // string
            'inventario': '',         // string
            'inventarioNombre': '',   // string
            'token': '',              // string
            'localInfo': {
                'pk': 'usuario'
            },                       // objeto con info adicional
            'localKey': ''           // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    this.makeTestObject = function (fuente, parseToString) {

        var itemBD = {
            'testID': '',            // string
            'campo': '',             // string
            'localInfo': {
                'pk': 'testID'
            },                       // objeto con info adicional
            'localKey': ''           // pk de base de datos local
        };

        this.fillItemBD(fuente, itemBD, parseToString);

        return itemBD;
    }

    // metodo general para generar un bean por el ombre de la tabla
    this.makeTablaObject = function (nombreTabla, fuente, parseToString) {

        if (nombreTabla == TABLAS.area) return this.makeAreaObject(fuente, parseToString);
        if (nombreTabla == TABLAS.bien) return this.makeBienObject(fuente, parseToString);
        if (nombreTabla == TABLAS.catalogo) return this.makeCatalogoObject(fuente, parseToString);
        if (nombreTabla == TABLAS.catalogoBien) return this.makeCatalogoBienObject(fuente, parseToString);
        if (nombreTabla == TABLAS.clase) return this.makeClaseObject(fuente, parseToString);
        if (nombreTabla == TABLAS.cuenta) return this.makeCuentaObject(fuente, parseToString);
        if (nombreTabla == TABLAS.dependencia) return this.makeDependenciaObject(fuente, parseToString);
        if (nombreTabla == TABLAS.empleado) return this.makeEmpleadoObject(fuente, parseToString);
        if (nombreTabla == TABLAS.empleadoUbicacion) return this.makeEmpleadoUbicacionObject(fuente, parseToString);
        if (nombreTabla == TABLAS.entidad) return this.makeEntidadObject(fuente, parseToString);
        if (nombreTabla == TABLAS.grupo) return this.makeGrupoObject(fuente, parseToString);
        if (nombreTabla == TABLAS.grupoClase) return this.makeGrupoClaseObject(fuente, parseToString);
        if (nombreTabla == TABLAS.locales) return this.makeLocalesObject(fuente, parseToString);
        if (nombreTabla == TABLAS.oficina) return this.makeOficinaObject(fuente, parseToString);
        if (nombreTabla == TABLAS.parametro) return this.makeParametroObject(fuente, parseToString);
        if (nombreTabla == TABLAS.ubigeo) return this.makeUbigeoObject(fuente, parseToString);
        if (nombreTabla == TABLAS.inventario) return this.makeInventarioObject(fuente, parseToString);
        if (nombreTabla == TABLAS.inventarioBien) return this.makeInventarioBienObject(fuente, parseToString);
        if (nombreTabla == TABLAS.inventarioApertura) return this.makeInventarioAperturaObject(fuente, parseToString);
        if (nombreTabla == TABLAS.session) return this.makeSessionObject(fuente, parseToString);
        if (nombreTabla == TABLAS.test) return this.makeTestObject(fuente, parseToString);

        return null;
    }

    // llena el objeto itemBD usando el objeto fuente (si este objeto fuente esta definido)
    this.fillItemBD = function (fuente, itemBD, parseToString) {

        // si esta definida la fuente, copia sus valores a itemBD
        if (estaDefinido(fuente)) {

            // copiando los campos que vienen de fuente
            copiarValores(fuente, itemBD, parseToString);

            // siempre setea su localKey al itemBD
            buildLocalKey(itemBD);
        }
    }
}

function buildLocalKey(itemBD, valorLocalKey) {

    if (estaDefinido(valorLocalKey)) {

        // si viene con data, lo setea
        itemBD['localKey'] = valorLocalKey;

    } else {

        // si no viene con data, usa su metadata localInfo que contiene su pk
        if ($.type(itemBD.localInfo.pk) === "array") {

            // concatenar los keys separados por ::, corta al
            var keyValue = '';

            $.each(itemBD.localInfo.pk, function (i, item) {
                if (i == 0) {
                    keyValue += itemBD[item];
                } else {
                    keyValue += ('::' + itemBD[item]);
                }
            });

            itemBD['localKey'] = keyValue;

        } else {

            itemBD['localKey'] = itemBD[itemBD.localInfo.pk];
        }
    }
}