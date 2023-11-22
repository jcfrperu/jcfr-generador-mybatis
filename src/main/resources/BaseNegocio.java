package com.tresjotas.restobar.negocio.base;

import com.jcfr.utiles.DateTime;
import com.jcfr.utiles.files.JFUtil;
import com.jcfr.utiles.listas.JLUtil;
import com.jcfr.utiles.string.JSUtil;
import com.tresjotas.restobar.dominio.entidades.base.AuditoriaFields;

public class BaseNegocio {

    protected static final JSUtil JS = JSUtil.JSUtil;
    protected static final JFUtil JF = JFUtil.JFUtil;
    protected static final JLUtil JL = JLUtil.JLUtil;

    protected void setAudit(AuditoriaFields entity, DateTime now, String usuarioSession, boolean insert) {

        entity.setFecAct(JS.toDate(now));
        entity.setUsuAct(usuarioSession);

        if (insert) {
            entity.setFecReg(JS.toDate(now));
            entity.setUsuReg(usuarioSession);
        }
    }
}
