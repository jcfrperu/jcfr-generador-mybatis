function SimioTablaDAO(nombreBD, nombreTabla) {

    // bd y tabla sobre la cual hacer la operacion
    this.nombreBD = nombreBD;
    this.nombreTabla = nombreTabla;

    /** Permite agregar un item de la tabla.
     *  - item
     *  Objeto javascript que se va agregar a la tabla.
     *  - functionOK :
     *  Función que se ejecuta si la operación se realiza con éxito.
     *  - functionError :
     *  Función que se ejecuta si ocurre un error en la operación.
     *  @param {object} item
     *  @param {function} functionOK
     *  @param {function} functionError
     */
    this.addItem = function (item, functionOK, functionError) {

        // insertar un item a la tabla
        $.simiosDB(this.nombreBD).objectStore(this.nombreTabla).add(item).then(functionOK, functionError);
    }

    /** Permite modificar un ítem de la tabla
     *  - item
     *  Objeto javscript a modificar de la tabla
     *  - functionOK :
     *  Función que se ejecuta si la operación se realiza con éxito.
     *  - functionError :
     *  Función que se ejecuta si ocurre un error en la operación.
     *  @param {object} item
     *  @param {function} functionOK
     *  @param {function} functionError
     */
    this.putItem = function (item, functionOK, functionError) {

        // actualizar un item de la tabla
        $.simiosDB(this.nombreBD).objectStore(this.nombreTabla).put(item).then(functionOK, functionError);
    }

    /** Permite remover un item de una tabla usando su ID
     *  - itemID :
     *  Identificador del objeto a remover de la tabla.
     *  - functionOK :
     *  Función que se ejecuta si la operación se realiza con éxito.
     *  - functionError :
     *  Función que se ejecuta si ocurre un error en la operación.
     *  @param {string} itemID
     *  @param {function} functionOK
     *  @param {function} functionError
     */
    this.deleteItem = function (itemID, functionOK, functionError) {

        // eliminar un item de la tabla
        $.simiosDB(this.nombreBD).objectStore(this.nombreTabla)['delete'](itemID).then(functionOK, functionError);
    }


    /** Permite recoger un item de la tabla usando su ID
     *  - itemID :
     *  Identificador del objeto a buscar en la tabla.
     *  - functionOK :
     *  Función que se ejecuta si la operación se realiza con éxito.
     *  - functionError :
     *  Función que se ejecuta si ocurre un error en la operación.
     *  @param {string} itemID
     *  @param {function} functionOK
     *  @param {function} functionError
     */
    this.getItem = function (itemID, functionOK, functionError) {

        // obtener un item de la tabla
        $.simiosDB(this.nombreBD).objectStore(this.nombreTabla).get(itemID).then(functionOK, functionError);
    }


    /** Permite recorrer todos los elementos de la tabla.
     *  - functionEach :
     *  Función que se ejecuta con cada item de la tabla. Recibe un parámetro item (de tipo object) que contiene el registro/item de la tabla.
     *  @param functionEach
     */
    this.eachItem = function (functionEach) {

        // listar los elementos de una tabla
        $.simiosDB(this.nombreBD).objectStore(this.nombreTabla).each(functionEach);
    }


    /** Permite remover todos los elementos de la tabla.
     *  - functionOK :
     *  Función que se ejecuta si la operación se realiza con éxito.
     *  - functionError :
     *  Función que se ejecuta si ocurre un error en la operación.
     *  @param {function} functionOK
     *  @param {function} functionError
     */
    this.clearItems = function (functionOK, functionError) {

        // listar los elementos de una tabla
        $.simiosDB(this.nombreBD).objectStore(this.nombreTabla).clear().then(functionOK, functionError);
    }

}

function SimioTXDAO(nombreBD, tablasArray) {

    // bd y tabla sobre la cual hacer la operacion
    this.nombreBD = nombreBD;
    this.tablasArray = tablasArray;

    this.execute = function (functionExecute, functionOK, functionError) {

        // iniciar una transaccion
        $.simiosDB(this.nombreBD).transaction(this.tablasArray).then(functionOK, functionError, functionExecute);
    }

    this.addItem = function (transaction, item, nombreTabla) {

        if (this.tablasArray.indexOf(nombreTabla) >= 0) {

            // insertar un item a la tabla
            transaction.objectStore(nombreTabla).add(item);
        }
    }

    this.putItem = function (transaction, item, nombreTabla) {

        if (this.tablasArray.indexOf(nombreTabla) >= 0) {

            // actualizar un item de la tabla
            transaction.objectStore(nombreTabla).put(item);
        }
    }

    this.deleteItem = function (transaction, itemID, nombreTabla) {

        if (this.tablasArray.indexOf(nombreTabla) >= 0) {

            // eliminar un item de la tabla
            transaction.objectStore(nombreTabla)['delete'](itemID);
        }
    }

    this.getItem = function (transaction, itemID, nombreTabla) {

        if (this.tablasArray.indexOf(nombreTabla) >= 0) {

            // obtener un item de la tabla
            transaction.objectStore(nombreTabla).get(itemID);
        }
    }

    this.clearItems = function (transaction, nombreTabla) {

        if (this.tablasArray.indexOf(nombreTabla) >= 0) {

            // eliminar los elementos de una tabla
            transaction.objectStore(nombreTabla).clear();
        }
    }

}
