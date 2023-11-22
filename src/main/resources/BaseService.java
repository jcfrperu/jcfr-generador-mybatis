package com.tresjotas.restobar.servicios.base;

import com.jcfr.utiles.files.JFUtil;
import com.jcfr.utiles.listas.JLUtil;
import com.jcfr.utiles.string.JSUtil;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

public class BaseService {

    protected static final Gson GSON = new Gson();

    // librerias opcionales del jcfr-utiles.jar
    protected static final JSUtil JS = JSUtil.JSUtil;
    protected static final JFUtil JF = JFUtil.JFUtil;
    protected static final JLUtil JL = JLUtil.JLUtil;

    protected String handleMsgError(String codigoError, Exception sos) {

        return handleMsgError(true, codigoError, null, sos);
    }

    protected String handleMsgError(String codigoError, Object aditionalTrace, Exception sos) {

        return handleMsgError(true, codigoError, aditionalTrace, sos);
    }

    protected String handleMsgError(boolean incluirCodigoError, String codigoError, Object aditionalTrace, Exception sos) {

        String codigoErrorResult = StringUtils.EMPTY;

        if (incluirCodigoError) {
            // primero coge el codigo del simioexception
            if (sos instanceof SimioException) {
                codigoErrorResult += (((SimioException) sos).getCodigo() + " ");
            }

            // si el codigo del simioexception vino vacio, tomar el otro
            if (StringUtils.isBlank(codigoErrorResult) && StringUtils.isNotBlank(codigoError)) {
                codigoErrorResult += (codigoError + " ");
            }
        }

        // seteando el mensaje de error
        String msgError = codigoErrorResult + ((sos == null || sos instanceof NullPointerException) ? "Null Pointer Exception" : sos.getMessage());
        if (sos != null && sos.getCause() != null) {
            msgError = msgError + ", CAUSA: " + sos.getCause().getMessage();
        }

        if (aditionalTrace != null) {
            msgError += (", PARAMS=" + GSON.toJson(aditionalTrace));
        }

        return msgError;
    }

    protected Exception handleError(Exception sos) {
        return sos;
    }
}
