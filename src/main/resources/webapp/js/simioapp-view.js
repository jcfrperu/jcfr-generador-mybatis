/** Permite crear y ejecutar una petición Ajax REST. Envía los datos de la petición
 *  (dataPost) y las funciones que se ejecutarán si la petición termina con éxito
 *  (functionOK), con error (functionError) o en cualquier caso (functionAlways).
 *  Además, esta función retorna el objeto promise de la petición ajax para seguir
 *  personalizándola usando done(), fail(), always(), progress().
 *  Esta función es "administrada", en el sentido que, si la petición se termina
 *  con éxito realiza una validación previa (usando la función huboErrorJson())
 *  para ver si ha ocurrido un error ajax de aplicación/negocio, si todo va OK
 *  recién ejecuta la función funcionOK() que se le envía como parámetro.
 *  (NOTA: hay errores de aplicativo/negocio que igual se manejan en el bloque
 *  de éxito; el resto son errores no manejables, como errores de conexión, etc).
 *  Si ha ocurrido un error de aplicativo/negocio, el comportamiento por default
 *  es mostrar el error en un mensaje popup y escribirlo en consola del browser.
 *  Sino se desea este comportamiento "administrado", sólo basta no enviar como
 *  parámetro las funciones functionOK(), functionError() y functionAlways(), y
 *  utilizar las funciones estándar de jQuery done(), fail(), always(), progress().
 * - async :
 *  Valor {true, false} indcando si la petición será asíncrona (default: true).
 *  - url :
 *  URL de la petición.
 * - dataPost :
 *  Objeto javascript con los datos/parámetros a enviar en la petición.
 * - functionOK :
 *  Función opcional que se ejecuta si la operación se realiza con éxito (luego de realizar la validación huboErrorJson() por default).
 * - functionError :
 *  Función opcional que se ejecuta si ocurre un error en la operación ya sea de
 *  aplicación o de comunicación.
 *  Recibe tres parametros: esErrorAjax, response, error.
 *   "esErrorAjax",  true si es error de aplicativo/negocio, false caso contrario
 *   "response", contiene la respuesta de error del aplicativo en formato JSON
 *   "error", contiene la data del error por otros motivos (error.status, error.readyState, error.statusText )
 *  comunicación respectivamente
 * - functionAlways :
 *  Función opcional que se ejecuta ya sea si la operación se realiza con éxito o no (siempre se ejecuta al final)
 *  @param {boolean} async
 *  @param {string} url
 *  @param {object} dataPost
 *  @param {function} functionOK
 *  @param {function} functionError
 *  @param {function} functionAlways
 *  @returns {any}
 */
function callAjax(async, url, dataPost, functionOK, functionError, functionAlways) {

    // request / promesa
    var request = $.ajax({
        'url': url,
        'data': dataPost,
        'type': 'post',
        'dataType': 'json',
        'async': async,
        'cache': false
    });

    // si esta definida la funcion para cuando todo va OK
    if (estaDefinido(functionOK)) {

        request.done(function (response) {

            // si hubo error de ajax/aplicativo
            if (huboErrorJson(response)) {

                // si esta definida la funcion para cuando hay error
                if (estaDefinido(functionError)) {

                    functionError(true, response, null);
                }

                // comportamiento administrado por default
                handleErrorJson(response);

                return;
            }

            // ejecutamos el metodo para cuando todo va OK
            functionOK(response);

        });
    }

    // si esta definida la funcion para cuando hay error
    if (estaDefinido(functionError)) {

        request.fail(function (error) {

            // ejecutamos el metodo cuando hay error
            functionError(false, null, error);

            // comportamiento administrado por default
            handleError(error);

            return;
        });
    }

    // si esta definida la funcion para se completa la operacion ajax
    if (estaDefinido(functionAlways)) {

        request.always(functionAlways);
    }

    return request;
}