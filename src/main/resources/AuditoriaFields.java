package com.tresjotas.restobar.dominio.entidades.base;

import java.io.Serializable;
import java.util.Date;

public class AuditoriaFields implements Serializable {

    private Date fechaReg; // fec_reg
    private Date fechaAct; // fec_act
    private String usuReg; // usu_reg
    private String usuAct; // usu_act
    private String indDel; // ind_del
    private String orderBy; // campo adicional para permitir customizar los order bys

    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }

    public Date getFechaAct() {
        return fechaAct;
    }

    public void setFechaAct(Date fechaAct) {
        this.fechaAct = fechaAct;
    }

    public String getUsuReg() {
        return usuReg;
    }

    public void setUsuReg(String usuReg) {
        this.usuReg = usuReg;
    }

    public String getUsuAct() {
        return usuAct;
    }

    public void setUsuAct(String usuAct) {
        this.usuAct = usuAct;
    }

    public String getIndDel() {
        return indDel;
    }

    public void setIndDel(String indDel) {
        this.indDel = indDel;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
