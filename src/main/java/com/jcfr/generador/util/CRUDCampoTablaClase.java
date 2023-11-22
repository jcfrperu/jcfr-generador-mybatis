package com.jcfr.generador.util;

import lombok.Data;

@Data
public class CRUDCampoTablaClase {

    // campo bd
    private String campoTabla;

    // campo java
    private String campoClase;

    private String tipo;   // tipo bd

    // precision numérica si es número
    private Long escala;
    private Long precision;

    // longitud si es cadena
    private Long longitud;

    // si es un campo de auditoria de tabla
    private boolean esAuditoria;

    // si campo es parte de un primary key
    private boolean esCampoClave;

    // si el campo es nullable
    private boolean esNullable;
}
