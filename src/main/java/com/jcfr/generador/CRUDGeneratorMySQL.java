package com.jcfr.generador;

import com.jcfr.generador.dao.Cnx;
import com.jcfr.generador.util.CRUDCampoTablaClase;
import com.jcfr.generador.util.CRUDTablaClase;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CRUDGeneratorMySQL {

    // parametros obligatorios para configurar
    private final static String RUTA_OUT_BASE = "/home/juaneco/jcfr-out/temp/generador-mysql/";
    private final static String RUTA_ARCHIVO_PLANTILLA_BASE = "/home/juaneco/jcfr-out/git/jcfr-generador-mybatis/src/main/resources/";

    // private final static String TABLE_OWNER = "root";
    private final static String NOMBRE_BASE_DATOS = "colegiobd";

    private final static String PAQUETE_BASE = "com.boliquesos.schoolkids";

    // por defecto deber ser false, pero poner true si hay campos upper case dado que postgree es case sensitive
    private final static boolean ENCERRAR_EN_COMILLA_CAMPO_DE_BD = false;

    // los servicios buscan por su secuencial ID, que casi siempre es Long, sino le pone Integer
    private final static boolean USAR_LONG_COMO_PRIMARY_KEY_PARA_SERVICIOS = false;

    // por defecto debe ser true, para que los campos del bean siempre tome con minusculas.
    // pero si se sabe que los nombres de la bd son camelCase como java poner true
    private final static boolean PASAR_A_MINUSCULAS_CAMPOS_DE_BD = false;

    // incluir anotacion @Qualifer, es cuestion de gustos
    private final static boolean INCLUIR_ANOTACION_QUALIFIER = false;

    // incluir annotation @Mapper en las interfaces DAO/Mapper (en las versiones nuevas del mybatis las necesita)
    private final static boolean INCLUIR_ANNOTATION_MAPPER_EN_LOS_DAOS = true;

    // lombok se usa mucho, por defecto dejarlo activado
    private final static boolean UTILIZAR_LOMBOK = true;

    // algunas veces quieres un diseño mas sencillo de menos capas, activando esta opcion, juntas la capa service y negocio
    private final static boolean JOIN_SERVICE_AND_BUSINESS_TIERS = true;

    // it's nice to have option to order
    private final static boolean UTILIZAR_ORDER_BY = true;

    // private final static String campos_auditoria[] = {};
    private final static String[] campos_auditoria = {"fec_reg", "fec_act", "usu_reg", "usu_act", "ind_del"};

    // este valor siempre deberia ser false, yaque mysql no maneja secuencias ni tiene el método nextval como postgres
    private final static boolean INCLUIR_NEXTVAL_SEQUENCE = false;

    // si quieres hacer inyección por constructor que es lo más actual (por default true)
    private static final boolean INYECCION_POR_CONTRUCTOR = true;

    // si en tus beans quieres utilizar validación por grupos
    private static final boolean INCLUIR_VALIDACION_GRUPOS_EN_BEANS = true;

    // USAR ESTE METODO PARA CUSTOMIZAR LA CREACION DE LOS CAMPOS DE LOS BEANS
    // SE INVOCA ANTES DE TOCA CONVERSION AUTOMATICA
    private String customField(String nombreCampoLowCase) {

        return nombreCampoLowCase;
    }

    // otros parametros
    private final static String PAQUETE_UTILS = PAQUETE_BASE + ".util";
    private final static String PAQUETE_DAO = PAQUETE_BASE + ".dao";
    private final static String PAQUETE_DAO_IMPL = PAQUETE_DAO;
    private final static String PAQUETE_ENTIDADES = PAQUETE_BASE + ".domain.entities";
    private final static String PAQUETE_VALIDATIONS = PAQUETE_BASE + ".validations";
    private final static String PAQUETE_NEGOCIO = PAQUETE_BASE + ".business";
    private final static String PAQUETE_SERVICE = PAQUETE_BASE + ".service";
    private final static String PAQUETE_SERVICE_IMPL = PAQUETE_SERVICE + ".impl";
    private final static String PAQUETE_CONTROLLER = PAQUETE_BASE + ".controllers";
    private final static String PAQUETE_VIEWS = PAQUETE_BASE + ".views";

    private final static String RUTA_OUT_DAO = RUTA_OUT_BASE + PAQUETE_DAO.replace('.', '/') + "/";
    private final static String RUTA_OUT_DAO_IMPL = RUTA_OUT_BASE + PAQUETE_DAO_IMPL.replace('.', '/') + "/";
    private final static String RUTA_OUT_ENTIDADES = RUTA_OUT_BASE + PAQUETE_ENTIDADES.replace('.', '/') + "/";
    private final static String RUTA_OUT_NEGOCIO = RUTA_OUT_BASE + PAQUETE_NEGOCIO.replace('.', '/') + "/";
    private final static String RUTA_OUT_SERVICE = RUTA_OUT_BASE + PAQUETE_SERVICE.replace('.', '/') + "/";
    private final static String RUTA_OUT_SERVICE_IMPL = RUTA_OUT_BASE + PAQUETE_SERVICE_IMPL.replace('.', '/') + "/";
    private final static String RUTA_OUT_CONTROLLERS_WEB = RUTA_OUT_BASE + PAQUETE_CONTROLLER.replace('.', '/') + "/web/";
    private final static String RUTA_OUT_CONTROLLERS_REST = RUTA_OUT_BASE + PAQUETE_CONTROLLER.replace('.', '/') + "/rest/";
    private final static String RUTA_OUT_VIEWS = RUTA_OUT_BASE + PAQUETE_VIEWS.replace('.', '/') + "/";

    // configuracion adicional para generar las vista
    private final static String RUTA_ARCHIVO_PLANTILLA_EDITAR = RUTA_ARCHIVO_PLANTILLA_BASE + "template-editar.template";
    private final static String RUTA_ARCHIVO_PLANTILLA_NUEVO = RUTA_ARCHIVO_PLANTILLA_BASE + "template-nuevo.template";
    private final static String RUTA_ARCHIVO_PLANTILLA_VER = RUTA_ARCHIVO_PLANTILLA_BASE + "template-ver.template";
    private final static String RUTA_ARCHIVO_PLANTILLA_BUSCAR = RUTA_ARCHIVO_PLANTILLA_BASE + "template-buscar.template";
    private final static String RUTA_ARCHIVO_PLANTILLA_BUSCAR_GRILLA = RUTA_ARCHIVO_PLANTILLA_BASE + "template-buscar-grilla.template";
    private final static String RUTA_ARCHIVO_PLANTILLA_CONTROLLER_WEB = RUTA_ARCHIVO_PLANTILLA_BASE + "template-controller.template";
    private final static String RUTA_ARCHIVO_PLANTILLA_CONTROLLER_REST = RUTA_ARCHIVO_PLANTILLA_BASE + "template-controller-rest.template";

    private final static String URL_BASE_PARA_LA_APP = "admin.htm";
    private final static String URL_PREFIJO_MODULO = "registro-";
    private final static String URL_EXTENSION_MODULO = ".htm";


    // private final static String ESQUEMA = "public";
    // private final static String ESQUEMA_POSTGRE = "public";

    private final static String tabular = "    ";
    // private final static String salto = new String("\n");
    private final static String salto = System.getProperty("line.separator");

    private static int secuencia_exception = 0;

    private final HashMap<String, Object> cache = new HashMap<>();

    public static void main(String[] args) {

        System.out.println("proceso iniciado");

        CRUDGeneratorMySQL instance = new CRUDGeneratorMySQL();

        instance.crearBean();
        instance.crearDao();
        instance.crearDaoXML();

        if (JOIN_SERVICE_AND_BUSINESS_TIERS) {
            instance.crearServicio();
            instance.crearServicioNegocioImpl();

        } else {
            instance.crearNegocio();
            instance.crearServicio();
            instance.crearServicioImpl();
        }

        instance.crearVistaEditar();
        instance.crearVistaNuevo();
        instance.crearVistaVer();
        instance.crearVistaBuscar();
        instance.crearVistaBuscarGrilla();

        instance.crearWebControllers();
        instance.crearRestControllers();

        System.out.println("proceso culminado");
    }

    public String nextSecError() {

        secuencia_exception++;

        if (secuencia_exception >= 1000) {
            secuencia_exception = 1;
        }

        if (secuencia_exception > 99) {
            return secuencia_exception + "";
        }
        if (secuencia_exception > 9) {
            return "0" + secuencia_exception + "";
        }

        return "00" + secuencia_exception;

    }

    private String toFirstLowerCase(String cadena) {
        if (cadena == null || cadena.length() == 0) {
            return cadena;
        }
        if (cadena.length() == 1) {
            return cadena.toLowerCase();
        }

        return cadena.substring(0, 1).toLowerCase() + cadena.substring(1);
    }

    private String getAliasPost(String tabla) {
        if (tabla == null || tabla.length() == 0) {
            return "";
        }
        return tabla.substring(0, 1).toLowerCase() + ".";
    }

    private String getAlias(String tabla) {
        if (tabla == null || tabla.length() == 0) {
            return "";
        }
        return tabla.substring(0, 1).toLowerCase();
    }

    private String toFirstUpperCase(String cadena) {
        if (cadena == null || cadena.length() == 0) {
            return cadena;
        }
        if (cadena.length() == 1) {
            return cadena.toUpperCase();
        }

        return cadena.substring(0, 1).toUpperCase() + cadena.substring(1);
    }

    private String encerrarCampoBD(String campoBD) {

        if (ENCERRAR_EN_COMILLA_CAMPO_DE_BD) {

            String comilla = "\"";

            boolean tieneUpper = false;
            for (int i = 0; i < campoBD.length(); i++) {
                if (Character.isUpperCase(campoBD.charAt(i))) {
                    tieneUpper = true;
                    break;
                }
            }

            // tieneUpper = true;
            if (tieneUpper) {
                return comilla + campoBD + comilla;
            }

            return campoBD;
        }

        return campoBD;

    }

    // --------------------- CONFIGURAR LOS DAO XML
    @SuppressWarnings("unused")
    public void crearDaoXML() {

        /* CONFIGURANDO LA CABECERA DEL XML */
        String cabeceraXML = "";
        cabeceraXML += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        cabeceraXML += salto;
        cabeceraXML += "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">";
        cabeceraXML += salto;

        String tabla;
        String tablaOracle;
        String entidad;
        String esquema = NOMBRE_BASE_DATOS;

        // String estandar = new String("RNITV_");
        String estandar = "";

        List<CRUDCampoTablaClase> listaCampos;

        try {
            String tablaFinal;

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tablaFinal = "";
                tabla = itemTabla.getClase();

                /* CONFIGURAR EL NOMBRE DE LA TABLA ORACLE */
                tablaOracle = itemTabla.getTabla();

                /* CONFIGURAMOS LAS ENTIDADES */
                entidad = PAQUETE_ENTIDADES + "." + tabla + "Entity";

                /* DEFINICION DEL XML */
                tablaFinal += cabeceraXML;
                tablaFinal += salto;
                tablaFinal += "<mapper namespace=\"" + PAQUETE_DAO + "." + tabla + "DAO\">";
                tablaFinal += salto;

                tablaFinal += salto + tabular;
                tablaFinal += "<cache />";
                tablaFinal += salto;

                /* CONFIGURAR EL RESULTMAP */

                tablaFinal += salto;
                tablaFinal += tabular;

                tablaFinal += "<resultMap id=\"" + toFirstLowerCase(tabla) + "ResultMap\" type=\"" + entidad + "\">";
                /* INSERTAMOS EL CAMPO ID DE LA CLASE */

                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (!itemCampo.isEsCampoClave()) {
                        continue;
                    }

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular;
                    tablaFinal += "<id column=\"" + itemCampo.getCampoTabla() + "\""; // COLUMNA
                    tablaFinal += " property=\"" + generarField(null, itemCampo.getCampoClase()) + "\""; // PROPIEDAD
                    tablaFinal += " jdbcType=\"" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala()) + "\""; // JDBC
                    // TYPE
                    tablaFinal += " />"; // FIN DE ID
                }

                /* INSERTAMOS LOS CAMPOS DEL RESULSET */
                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (itemCampo.isEsCampoClave()) {
                        continue;
                    }

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular;
                    tablaFinal += "<result column=\"" + itemCampo.getCampoTabla() + "\""; // COLUMNA
                    tablaFinal += " property=\"" + generarField(null, itemCampo.getCampoClase()) + "\""; // PROPIEDAD
                    tablaFinal += " jdbcType=\"" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala()) + "\""; // JDBC
                    // TYPE
                    tablaFinal += " />"; // FIN DE ID
                }

                /* CERRAMOS EL RESULSET */
                tablaFinal += salto;
                tablaFinal += tabular;
                tablaFinal += "</resultMap>";

                tablaFinal += salto + salto;

                /* AGREGAMOS EL METODO SELECT SOLO CAMPO CLAVE */

                boolean sw;

                tablaFinal += tabular;
                tablaFinal += "<select id=\"selectByID\" resultMap=\"" + toFirstLowerCase(tabla) + "ResultMap\" useCache=\"false\" ";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += " parameterType=\"" + PAQUETE_ENTIDADES + "." + tabla + "Entity\">";

                tablaFinal += salto + salto;
                tablaFinal += tabular + tabular;

                // SELECT
                tablaFinal += "SELECT";
                // CAMPOS

                int saltar = 0;
                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    if (saltar % 4 == 0) {
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular;
                    }
                    tablaFinal += encerrarCampoBD(itemCampo.getCampoTabla()) + ", ";
                    saltar++;
                }

                // RETIRANDO LA ULTIMA COMA (tiene espacio)
                tablaFinal = tablaFinal.substring(0, tablaFinal.length() - 2);

                // FROM
                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += "FROM ";
                tablaFinal += salto;
                tablaFinal += tabular + tabular + tabular;
                tablaFinal += esquema + "." + tablaOracle;
                tablaFinal += salto;

                tablaFinal += tabular + tabular;

                tablaFinal += "WHERE";

                sw = false;

                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (!itemCampo.isEsCampoClave()) {
                        continue;
                    }

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular;
                    if (sw) {
                        tablaFinal += "AND ";
                    }
                    tablaFinal += encerrarCampoBD(itemCampo.getCampoTabla());

                    tablaFinal += " = ";

                    tablaFinal += "#{";
                    tablaFinal += generarField(null, itemCampo.getCampoClase());
                    tablaFinal += ",";
                    tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                    tablaFinal += "}";

                    sw = true;
                }

                tablaFinal += salto;
                tablaFinal += tabular;
                tablaFinal += "</select>";

                /* AGREGAMOS EL METODO SELECT ALL FILTROS */

                tablaFinal += salto + salto;
                tablaFinal += tabular;
                tablaFinal += "<select id=\"select\" resultMap=\"" + toFirstLowerCase(tabla) + "ResultMap\" useCache=\"false\" ";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += " parameterType=\"" + PAQUETE_ENTIDADES + "." + tabla + "Entity\">";

                tablaFinal += salto + salto;
                tablaFinal += tabular + tabular;

                // SELECT
                tablaFinal += "SELECT";
                // CAMPOS

                saltar = 0;
                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    if (saltar % 4 == 0) {
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular;
                    }
                    tablaFinal += encerrarCampoBD(itemCampo.getCampoTabla()) + ", ";
                    saltar++;
                }

                // RETIRANDO LA ULTIMA COMA (tiene espacio)
                tablaFinal = tablaFinal.substring(0, tablaFinal.length() - 2);

                // FROM
                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += "FROM ";
                tablaFinal += salto;
                tablaFinal += tabular + tabular + tabular;
                tablaFinal += esquema + "." + tablaOracle;
                tablaFinal += salto;

                tablaFinal += tabular + tabular;

                tablaFinal += "<where>";

                // todos
                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular;

                    tablaFinal += "<if test=\"";
                    tablaFinal += generarField(null, itemCampo.getCampoClase());
                    tablaFinal += " != null\">";

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular + tabular;
                    tablaFinal += " AND ";
                    tablaFinal += encerrarCampoBD(itemCampo.getCampoTabla());

                    tablaFinal += " = ";

                    tablaFinal += "#{";
                    tablaFinal += generarField(null, itemCampo.getCampoClase());
                    tablaFinal += ",";
                    tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                    tablaFinal += "}";

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular;

                    tablaFinal += "</if>";

                }

                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += "</where>";

                if (UTILIZAR_ORDER_BY) {
                    tablaFinal += salto + salto;
                    tablaFinal += tabular + tabular;

                    tablaFinal += "<if test=\"orderBy != null\">";

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular;
                    tablaFinal += " ORDER BY ${orderBy}";

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular;

                    tablaFinal += "</if>";
                }

                tablaFinal += salto + salto;
                tablaFinal += tabular;
                tablaFinal += "</select>";

                /*
                  AGREGAMOS EL METODO SELECT ALL FILTROS - BUSCAR - USANDO LIKE Y RANGOS, RECIBE UN MAPA Y RETORNA UN BEAN
                 */

                tablaFinal += salto + salto;
                tablaFinal += tabular;
                tablaFinal += "<select id=\"selectByMap\" resultMap=\"" + toFirstLowerCase(tabla) + "ResultMap\" useCache=\"false\" parameterType=\"java.util.Map\">";

                tablaFinal += salto + salto;
                tablaFinal += tabular + tabular;

                // SELECT
                tablaFinal += "SELECT";
                // CAMPOS

                saltar = 0;
                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    if (saltar % 4 == 0) {
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular;
                    }
                    tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla()) + ", ";
                    saltar++;
                }

                // RETIRANDO LA ULTIMA COMA (tiene espacio)
                tablaFinal = tablaFinal.substring(0, tablaFinal.length() - 2);

                // FROM
                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += "FROM ";
                tablaFinal += salto;
                tablaFinal += tabular + tabular + tabular;
                tablaFinal += esquema + "." + tablaOracle + " " + getAlias(tabla);
                tablaFinal += salto;

                tablaFinal += tabular + tabular;

                tablaFinal += "<where>";

                // todos
                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular;

                    String tipo = tipoBuscarLikeEqualRango(itemCampo.getTipo(), itemCampo.getEscala());

                    if ("like".equals(tipo)) {

                        tablaFinal += "<if test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += " != null\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;

                        tablaFinal += "<choose>";

                        // if, xxx_type=igual
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'igual'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " = ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type=is_null
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'is_null'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " IS NULL ";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type=is_not_null
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'is_not_null'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " IS NOT NULL ";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // otherwise
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<otherwise>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " like ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</otherwise>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;

                        tablaFinal += "</choose>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular;

                        tablaFinal += "</if>";

                    } else if ("rango".equals(tipo)) {

                        tablaFinal += "<if test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += " != null\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;

                        tablaFinal += "<choose>";

                        // if, xxx_type: [] rango_incluye
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'rango_incluye'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &gt;= ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_ini";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &lt;= ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_fin";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type: <> rango_excluye
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'rango_excluye'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &gt; ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_ini";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &lt; ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_fin";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type=is_null
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'is_null'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " IS NULL ";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type=is_not_null
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'is_not_null'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " IS NOT NULL ";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";


                        // otherwise
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<otherwise>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " = ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</otherwise>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;

                        tablaFinal += "</choose>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular;

                        tablaFinal += "</if>";

                    } else if ("rango-decimal".equals(tipo) || "rango-fecha".equals(tipo)) {

                        tablaFinal += "<if test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += " != null\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;

                        tablaFinal += "<choose>";

                        // if, xxx_type: [] rango_incluye
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'rango_incluye'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &gt;= ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_ini";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &lt;= ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_fin";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type: <> rango_excluye
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'rango_excluye'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &gt; ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_ini";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &lt; ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_fin";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type: =
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'igual'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " = ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type=is_null
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'is_null'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " IS NULL ";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type=is_not_null
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'is_not_null'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " IS NOT NULL ";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // otherwise
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<otherwise>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &gt;= ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_ini";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &lt;= ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_fin";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</otherwise>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;

                        tablaFinal += "</choose>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular;

                        tablaFinal += "</if>";

                    } else {

                        tablaFinal += "<if test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += " != null\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " = ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular;

                        tablaFinal += "</if>";

                    }

                }

                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += "</where>";

                if (UTILIZAR_ORDER_BY) {
                    tablaFinal += salto + salto;
                    tablaFinal += tabular + tabular;

                    tablaFinal += "<if test=\"orderBy != null\">";

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular;
                    tablaFinal += " ORDER BY ${orderBy}";

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular;

                    tablaFinal += "</if>";
                }

                tablaFinal += salto + salto;
                tablaFinal += tabular;
                tablaFinal += "</select>";


                /*
                  AGREGAMOS EL METODO SELECT ALL FILTROS (selectByMapGrilla ) - USANDO LIKE Y RANGOS, RECIBE UN MAPA Y RETORNA UN MAPA
                 */

                tablaFinal += salto + salto;
                tablaFinal += tabular;
                tablaFinal += "<select id=\"selectByMapGrilla\" useCache=\"false\" parameterType=\"java.util.Map\" resultType=\"java.util.Map\">";

                tablaFinal += salto + salto;
                tablaFinal += tabular + tabular;

                // SELECT
                tablaFinal += "SELECT";
                // CAMPOS

                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {
                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular;
                    tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla()) + " as \"" + generarField(null, itemCampo.getCampoClase()) + "\", ";
                }

                // RETIRANDO LA ULTIMA COMA (tiene espacio)
                tablaFinal = tablaFinal.substring(0, tablaFinal.length() - 2);

                // FROM
                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += "FROM ";
                tablaFinal += salto;
                tablaFinal += tabular + tabular + tabular;
                tablaFinal += esquema + "." + tablaOracle + " " + getAlias(tabla);
                tablaFinal += salto;

                tablaFinal += tabular + tabular;

                tablaFinal += "<where>";

                // todos
                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular;

                    String tipo = tipoBuscarLikeEqualRango(itemCampo.getTipo(), itemCampo.getEscala());

                    if ("like".equals(tipo)) {

                        tablaFinal += "<if test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += " != null\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;

                        tablaFinal += "<choose>";

                        // if, xxx_type=igual
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'igual'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " = ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type=is_null
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'is_null'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " IS NULL ";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type=is_not_null
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'is_not_null'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " IS NOT NULL ";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // otherwise
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<otherwise>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " like ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</otherwise>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;

                        tablaFinal += "</choose>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular;

                        tablaFinal += "</if>";

                    } else if ("rango".equals(tipo)) {

                        tablaFinal += "<if test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += " != null\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;

                        tablaFinal += "<choose>";

                        // if, xxx_type: [] rango_incluye
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'rango_incluye'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &gt;= ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_ini";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &lt;= ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_fin";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type: <> rango_excluye
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'rango_excluye'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &gt; ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_ini";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &lt; ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_fin";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type=is_null
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'is_null'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " IS NULL ";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type=is_not_null
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'is_not_null'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " IS NOT NULL ";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";


                        // otherwise
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<otherwise>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " = ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</otherwise>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;

                        tablaFinal += "</choose>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular;

                        tablaFinal += "</if>";

                    } else if ("rango-decimal".equals(tipo) || "rango-fecha".equals(tipo)) {

                        tablaFinal += "<if test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += " != null\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;

                        tablaFinal += "<choose>";

                        // if, xxx_type: [] rango_incluye
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'rango_incluye'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &gt;= ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_ini";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &lt;= ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_fin";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type: <> rango_excluye
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'rango_excluye'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &gt; ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_ini";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &lt; ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_fin";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type: =
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'igual'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " = ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type=is_null
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'is_null'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " IS NULL ";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // if, xxx_type=is_not_null
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<when test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_type";
                        tablaFinal += " == 'is_not_null'\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += tabular + " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " IS NOT NULL ";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</when>";

                        // otherwise
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "<otherwise>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &gt;= ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_ini";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " &lt;= ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase()) + "_fin";
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular + tabular;

                        tablaFinal += "</otherwise>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;

                        tablaFinal += "</choose>";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular;

                        tablaFinal += "</if>";

                    } else {

                        tablaFinal += "<if test=\"";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += " != null\">";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular + tabular;
                        tablaFinal += " AND ";
                        tablaFinal += getAliasPost(tabla) + encerrarCampoBD(itemCampo.getCampoTabla());

                        tablaFinal += " = ";

                        tablaFinal += "#{";
                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += ",";
                        tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += "}";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular + tabular;

                        tablaFinal += "</if>";

                    }

                }

                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += "</where>";

                if (UTILIZAR_ORDER_BY) {
                    tablaFinal += salto + salto;
                    tablaFinal += tabular + tabular;

                    tablaFinal += "<if test=\"orderBy != null\">";

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular;
                    tablaFinal += " ORDER BY ${orderBy}";

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular;

                    tablaFinal += "</if>";
                }

                tablaFinal += salto + salto;
                tablaFinal += tabular;
                tablaFinal += "</select>";

                if (INCLUIR_NEXTVAL_SEQUENCE) {
                    /*
                      AGREGAMOS EL METODO QUE BUSCA EL AUTOGENERADO - VALOR SIGUIENTE
                     */
                    /* NOTA: HAY QUE CAMBIAR ESTA IMPLEMENTACION QUE SEA DINAMICO */
                    tablaFinal += salto + salto;
                    tablaFinal += tabular;

                    if (USAR_LONG_COMO_PRIMARY_KEY_PARA_SERVICIOS) {
                        tablaFinal += "<select id=\"selectNextID\" useCache=\"false\" resultType=\"java.lang.Long\">";
                    } else {
                        tablaFinal += "<select id=\"selectNextID\" useCache=\"false\" resultType=\"java.lang.Integer\">";
                    }

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular;
                    tablaFinal += "select nextval('" + tablaOracle + "_" + tablaOracle + "_id_seq')";
                    tablaFinal += salto;
                    tablaFinal += tabular;
                    tablaFinal += "</select>";

                    /*
                      AGREGAMOS EL METODO QUE BUSCA EL AUTOGENERADO - VALOR ACTUAL
                     */
                    /* NOTA: HAY QUE CAMBIAR ESTA IMPLEMENTACION QUE SEA DINAMICO */
                    tablaFinal += salto + salto;
                    tablaFinal += tabular;

                    if (USAR_LONG_COMO_PRIMARY_KEY_PARA_SERVICIOS) {
                        tablaFinal += "<select id=\"selectCurrentID\" useCache=\"false\" resultType=\"java.lang.Long\">";
                    } else {
                        tablaFinal += "<select id=\"selectCurrentID\" useCache=\"false\" resultType=\"java.lang.Integer\">";
                    }

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular;
                    tablaFinal += "select currval('" + tablaOracle + "_" + tablaOracle + "_id_seq')";
                    tablaFinal += salto;
                    tablaFinal += tabular;
                    tablaFinal += "</select>";
                }

                /* AGREGAMOS EL METODO INSERT */
                tablaFinal += salto + salto;
                tablaFinal += tabular;
                tablaFinal += "<insert id=\"insert\" flushCache=\"true\"";

                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += "parameterType=\"" + entidad + "\"";

                tablaFinal += salto;

                tablaFinal += tabular + tabular;
                tablaFinal += "useGeneratedKeys=\"true\" ";

                // CONFIGURAMOS LOS KEY PROPERTY
                tablaFinal += "keyProperty=\"";

                sw = false;
                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (!itemCampo.isEsCampoClave()) {
                        continue;
                    }

                    if (sw) {
                        tablaFinal += ",";
                    }
                    tablaFinal += generarField(null, itemCampo.getCampoClase());
                    sw = true;
                }

                tablaFinal += "\"";

                // FIN DE LAS CONFIGURACIONES DE CLAVE PRIMARIA
                tablaFinal += ">";

                tablaFinal += salto + salto;
                tablaFinal += tabular + tabular;

                // SELECT
                tablaFinal += "INSERT INTO " + esquema + "." + tablaOracle + " ";
                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += "(";
                // CAMPOS

                sw = false;

                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (itemCampo.isEsCampoClave()) {
                        continue;
                    }

                    if (sw) {
                        tablaFinal += ",";
                    }

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular;
                    tablaFinal += encerrarCampoBD(itemCampo.getCampoTabla());
                    sw = true;
                }

                // VALUES
                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += ")";
                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += "VALUES";
                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += "(";

                sw = false;

                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (itemCampo.isEsCampoClave()) {
                        continue;
                    }

                    if (sw) {
                        tablaFinal += ",";
                    }

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular;

                    tablaFinal += "#{";
                    tablaFinal += generarField(null, itemCampo.getCampoClase());
                    tablaFinal += ",";
                    tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                    tablaFinal += "}";

                    sw = true;
                }

                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += ")";

                // CERRAMOS EL INSERT
                tablaFinal += salto + salto;
                tablaFinal += tabular;
                tablaFinal += "</insert>";

                /* AGREGAMOS EL METODO UPDATE */

                // CONFIGURAMOS LOS KEY PROPERTY

                tablaFinal += salto + salto;
                tablaFinal += tabular;
                tablaFinal += "<update id=\"update\" flushCache=\"true\"";

                tablaFinal += salto;
                tablaFinal += tabular + tabular;
                tablaFinal += "parameterType=\"" + entidad + "\"";

                // FIN DE LAS CONFIGURACIONES DE CLAVE PRIMARIA
                tablaFinal += ">";

                tablaFinal += salto + salto;
                tablaFinal += tabular + tabular;

                // SELECT
                tablaFinal += "UPDATE " + esquema + "." + tablaOracle;

                tablaFinal += salto + tabular + tabular;
                tablaFinal += "<set>";

                // CAMPOS

                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (itemCampo.isEsCampoClave()) {
                        continue;
                    }

                    tablaFinal += salto + tabular + tabular + tabular;
                    tablaFinal += "<if test=\"";
                    tablaFinal += generarField(null, itemCampo.getCampoClase());
                    tablaFinal += " != null\">";

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular + tabular;
                    tablaFinal += encerrarCampoBD(itemCampo.getCampoTabla());

                    tablaFinal += " = ";

                    tablaFinal += "#{";
                    tablaFinal += generarField(null, itemCampo.getCampoClase());
                    tablaFinal += ",";
                    tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                    tablaFinal += "}";

                    // if (sw) {
                    tablaFinal += ",";
                    // }

                    tablaFinal += salto + tabular + tabular + tabular;
                    tablaFinal += "</if>";

                    // sw = true;
                }

                tablaFinal += salto + tabular + tabular;
                tablaFinal += "</set>";

                tablaFinal += salto + tabular + tabular;
                tablaFinal += "WHERE ";

                sw = false;

                listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (!itemCampo.isEsCampoClave()) {
                        continue;
                    }

                    if (sw) {
                        tablaFinal += " AND ";
                    }

                    tablaFinal += salto;
                    tablaFinal += tabular + tabular + tabular;

                    tablaFinal += encerrarCampoBD(itemCampo.getCampoTabla());

                    tablaFinal += " = ";

                    tablaFinal += "#{";
                    tablaFinal += generarField(null, itemCampo.getCampoClase());
                    tablaFinal += ",";
                    tablaFinal += "jdbcType=" + tipoDatoJDBC(itemCampo.getTipo(), itemCampo.getEscala());
                    tablaFinal += "}";

                    sw = true;
                }

                // CERRAMOS EL INSERT
                tablaFinal += salto;
                tablaFinal += tabular;
                tablaFinal += "</update>";
                tablaFinal += salto;

                /* FIN DE LA CLASE */
                tablaFinal += salto;
                tablaFinal += "</mapper>";

                escribir(RUTA_OUT_DAO_IMPL + tabla + "DAO" + ".xml", tablaFinal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String tipoBuscarLikeEqualRango(String tParam, Long escala) {
        String tablaFinal = "";

        if (tParam.equals("decimal")) {
            if (escala == null || escala.intValue() <= 0) {
                tablaFinal += "rango";
            } else {
                tablaFinal += "rango-decimal";
            }
        } else if (tParam.startsWith("double") || tParam.startsWith("float")) {
            tablaFinal += "rango-decimal";
        } else if (tParam.startsWith("varchar") || tParam.startsWith("char") || tParam.startsWith("text")) {
            tablaFinal += "like";
        } else if (tParam.startsWith("date")) {
            tablaFinal += "rango-fecha";
        } else if (tParam.startsWith("datetime") || tParam.startsWith("timestamp")) {
            tablaFinal += "rango-fecha";
        } else if (tParam.equals("int") || tParam.equals("tinyint")) {
            tablaFinal += "rango";
        } else if (tParam.equals("bigint")) {
            tablaFinal += "rango";
        } else if (tParam.equals("blob") || tParam.equals("mediumblob") || tParam.equals("tinyblob")) {
            tablaFinal += "rango";
        }

        return tablaFinal;
    }

    // --------------------- CONFIGURAR LOS DAO
    public void crearDao() {

        String entidad;
        String tabla;
        String tablaFinal;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tablaFinal = "";
                tabla = itemTabla.getClase();

                tablaFinal += "package " + PAQUETE_DAO + ";";

                entidad = tabla + "Entity";

                tablaFinal += salto + salto;
                tablaFinal += "import " + PAQUETE_ENTIDADES + "." + entidad + ";";
                tablaFinal += salto;

                if (INCLUIR_ANNOTATION_MAPPER_EN_LOS_DAOS) {
                    tablaFinal += "import org.apache.ibatis.annotations.Mapper;";
                    tablaFinal += salto + salto;
                }

                tablaFinal += "import java.util.Map;";
                tablaFinal += salto;
                tablaFinal += "import java.util.List;";
                tablaFinal += salto + salto;

                if (INCLUIR_ANNOTATION_MAPPER_EN_LOS_DAOS) {
                    tablaFinal += "@Mapper";
                    tablaFinal += salto;
                }
                tablaFinal += "public interface " + tabla + "DAO" + " {";
                tablaFinal += salto + salto;

                /* CUERPO DE LA INTERFACE */
                // SELECT PRIMARY KEY
                tablaFinal += tabular;
                tablaFinal += entidad + " selectByID(" + entidad + " " + "entityID) throws Exception;";
                tablaFinal += salto + salto;

                // SELECT
                tablaFinal += tabular;
                tablaFinal += "List<" + entidad + "> select(" + entidad + " filter) throws Exception;";
                tablaFinal += salto + salto;

                // SELECT BY MAP
                tablaFinal += tabular;
                tablaFinal += "List<" + entidad + "> selectByMap(Map<String, Object> params) throws Exception;";
                tablaFinal += salto + salto;

                // SELECT BY MAP GRILLA
                tablaFinal += tabular;
                tablaFinal += "List<Map<String, Object>> selectByMapGrilla(Map<String, Object> params) throws Exception;";
                tablaFinal += salto + salto;

                if (INCLUIR_NEXTVAL_SEQUENCE) {

                    // CORRELATIVO
                    tablaFinal += tabular;
                    if (USAR_LONG_COMO_PRIMARY_KEY_PARA_SERVICIOS) {
                        tablaFinal += "Long selectNextID() throws Exception;";
                    } else {
                        tablaFinal += "Integer selectNextID() throws Exception;";
                    }

                    tablaFinal += salto + salto;

                    // CURRENT VALUE
                    tablaFinal += tabular;
                    if (USAR_LONG_COMO_PRIMARY_KEY_PARA_SERVICIOS) {
                        tablaFinal += "Long selectCurrentID() throws Exception;";
                    } else {
                        tablaFinal += "Integer selectCurrentID() throws Exception;";
                    }

                    tablaFinal += salto + salto;
                }

                // INSERT
                tablaFinal += tabular;
                tablaFinal += "void insert(" + entidad + " entity) throws Exception;";
                tablaFinal += salto + salto;

                // UPDATE
                tablaFinal += tabular;
                tablaFinal += "void update(" + entidad + " entity) throws Exception;";
                tablaFinal += salto;

                /* FIN DE LA INTERFACE */
                tablaFinal += "}";

                escribir(RUTA_OUT_DAO + tabla + "DAO" + ".java", tablaFinal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------------- CONFIGURAR LOS NEGOCIO
    public void crearNegocio() {

        String tabla;
        String entidad;
        String tablaFinal;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tablaFinal = "";
                tabla = itemTabla.getClase();

                tablaFinal += "package " + PAQUETE_NEGOCIO + ";";

                entidad = tabla + "Entity";

                tablaFinal += salto + salto;
                tablaFinal += "import " + PAQUETE_ENTIDADES + "." + entidad + ";";
                tablaFinal += salto;
                tablaFinal += "import " + PAQUETE_NEGOCIO + ".base.BaseNegocio;";
                tablaFinal += salto;
                tablaFinal += "import " + PAQUETE_DAO + "." + tabla + "DAO;";
                tablaFinal += salto + salto;
                tablaFinal += "import java.util.Map;";
                tablaFinal += salto;
                tablaFinal += "import java.util.List;";
                tablaFinal += salto;
                tablaFinal += "import org.apache.commons.collections.CollectionUtils;";
                tablaFinal += salto + salto;
                tablaFinal += "import org.springframework.beans.factory.annotation.Autowired;";
                tablaFinal += salto;
                tablaFinal += "import org.springframework.stereotype.Component;";
                tablaFinal += salto;
                tablaFinal += "import org.springframework.transaction.annotation.Transactional;";

                tablaFinal += salto + salto;
                if (INCLUIR_ANOTACION_QUALIFIER) {
                    tablaFinal += "@Component(\"" + toFirstLowerCase(tabla) + "Negocio" + "\")";
                } else {
                    tablaFinal += "@Component";
                }
                tablaFinal += salto;
                tablaFinal += "public class " + tabla + "Negocio" + " extends BaseNegocio {";
                tablaFinal += salto + salto + tabular;

                tablaFinal += "@Autowired";
                tablaFinal += salto + tabular;
                tablaFinal += "private " + tabla + "DAO " + toFirstLowerCase(tabla) + "DAO;";
                tablaFinal += salto + salto;

                /* CUERPO DE LA INTERFACE */

                // SELECT BY PK
                tablaFinal += tabular;
                if (USAR_LONG_COMO_PRIMARY_KEY_PARA_SERVICIOS) {
                    tablaFinal += "public " + entidad + " selectByID(Long " + toFirstLowerCase(tabla) + "ID) throws Exception {";
                } else {
                    tablaFinal += "public " + entidad + " selectByID(Integer " + toFirstLowerCase(tabla) + "ID) throws Exception {";
                }

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += entidad + " " + toFirstLowerCase(tabla) + " = new " + entidad + "();";
                tablaFinal += salto + salto + tabular + tabular;

                List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    if (itemCampo.isEsCampoClave()) {
                        tablaFinal += toFirstLowerCase(tabla) + "." + generarField("set", itemCampo.getCampoTabla()) + "(" + toFirstLowerCase(tabla) + "ID);";
                        tablaFinal += salto + tabular + tabular;
                    }

                }

                tablaFinal += salto + tabular + tabular;
                tablaFinal += "return " + toFirstLowerCase(tabla) + "DAO.selectByID(" + toFirstLowerCase(tabla) + ");";
                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // SELECT
                tablaFinal += tabular;
                tablaFinal += "public List<" + entidad + "> select(" + entidad + " filter) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "return " + toFirstLowerCase(tabla) + "DAO.select(filter);";
                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // SELECT BY MAP
                tablaFinal += tabular;
                tablaFinal += "public List<" + entidad + "> selectByMap(Map<String, Object> params) throw Exception {";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "return " + toFirstLowerCase(tabla) + "DAO.selectByMap(params);";
                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // SELECT BY MAP GRILLA
                tablaFinal += tabular;
                tablaFinal += "public List<Map<String, Object>> selectByMapGrilla(Map<String, Object> params) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "return " + toFirstLowerCase(tabla) + "DAO.selectByMapGrilla(params);";
                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // SELECT KEY OBJECT
                tablaFinal += tabular;
                if (USAR_LONG_COMO_PRIMARY_KEY_PARA_SERVICIOS) {
                    tablaFinal += "public Map<Long, " + entidad + "> selectKeyObject(" + entidad + " filter) throws Exception {";
                } else {
                    tablaFinal += "public Map<Integer, " + entidad + "> selectKeyObject(" + entidad + " filter) throws Exception {";
                }

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "List<" + entidad + "> entityList = " + toFirstLowerCase(tabla) + "DAO.select(filter);";
                tablaFinal += salto + salto + tabular + tabular;
                if (USAR_LONG_COMO_PRIMARY_KEY_PARA_SERVICIOS) {
                    tablaFinal += "HashMap<Long, " + entidad + "> result = new HashMap<>(entityList == null ? 16 : entityList.size());";
                } else {
                    tablaFinal += "HashMap<Integer, " + entidad + "> result = new HashMap<>(entityList == null ? 16 : entityList.size());";
                }
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "if (CollectionUtils.isNotEmpty(entityList)) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "for (" + entidad + " entity : entityList) {";
                tablaFinal += salto + salto + tabular + tabular + tabular + tabular;
                tablaFinal += "result.put(entity.getLocalesID(), entity);";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "}";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "return result;";
                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // INSERT
                tablaFinal += tabular;
                tablaFinal += "@Transactional(rollbackFor = Throwable.class)";
                tablaFinal += salto + tabular;
                tablaFinal += "public void insert(" + entidad + " entity) throws Exception {";

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += toFirstLowerCase(tabla) + "DAO.insert(entity);";
                tablaFinal += salto + tabular;
                tablaFinal += "}";
                tablaFinal += salto + salto;

                // UPDATE
                tablaFinal += tabular;
                tablaFinal += "@Transactional(rollbackFor = Throwable.class)";
                tablaFinal += salto + tabular;
                tablaFinal += "public void update(" + entidad + " entity) throws Exception {";

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += toFirstLowerCase(tabla) + "DAO.update(entity);";
                tablaFinal += salto + tabular;
                tablaFinal += "}";
                tablaFinal += salto + salto;

                // DELETE
                tablaFinal += tabular;
                tablaFinal += "@Transactional(rollbackFor = Throwable.class)";
                tablaFinal += salto + tabular;
                tablaFinal += "public void delete(" + entidad + " entity) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "entity.setIndDel(Constantes.REGISTRO_INACTIVO);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += toFirstLowerCase(tabla) + "DAO.update(entity);";
                tablaFinal += salto + tabular;
                tablaFinal += "}";
                tablaFinal += salto;

                /* FIN DE LA CLASE */
                tablaFinal += "}";

                escribir(RUTA_OUT_NEGOCIO + tabla + "Negocio" + ".java", tablaFinal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generarField(String prefijo, String nombreCampo) {
        String s = nombreCampo == null ? "" : nombreCampo.trim();

        s = customField(s);

        // sufijos
        if (s.startsWith("mto_")) {
            s = s.substring(4);
            s = "monto_" + s;
        }

        if (s.startsWith("fec_")) {
            s = s.substring(4);
            s = "fecha_" + s;
        }

        if (s.startsWith("cod_")) {
            s = s.substring(4);
            s = "codigo_" + s;
        }

        if (s.startsWith("num_")) {
            s = s.substring(4);
            s = "numero_" + s;
        }

        if (s.startsWith("nro_")) {
            s = s.substring(4);
            s = "numero_" + s;
        }

        // posfijos
        if (s.endsWith("_id")) {
            s = s.substring(0, s.length() - 3);
            s = s + "ID";
        }

        if (s.endsWith("igv") && s.length() > 3) {
            s = s.substring(0, s.length() - 3);
            s = s + "IGV";
        }

        while (s.contains("_")) {
            int pos = s.indexOf('_');
            if (pos >= 0) {
                String sub01 = s.substring(0, pos);
                String sub02 = s.substring(pos + 1);
                s = sub01 + toFirstUpperCase(sub02);
            }
        }

        if ("set".equals(prefijo)) {
            return "set" + toFirstUpperCase(s);
        }

        if ("get".equals(prefijo)) {
            return "get" + toFirstUpperCase(s);
        }

        return s;
    }

    // --------------------- CONFIGURAR LOS SERVICIO
    public void crearServicio() {

        String entidad;
        String tabla;
        String tablaFinal;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tablaFinal = "";
                tabla = itemTabla.getClase();

                tablaFinal += "package " + PAQUETE_SERVICE + ";";

                entidad = tabla + "Entity";

                tablaFinal += salto + salto;
                tablaFinal += "import " + PAQUETE_ENTIDADES + "." + entidad + ";";
                tablaFinal += salto;
                tablaFinal += "import java.util.Map;";
                tablaFinal += salto;
                tablaFinal += "import java.util.List;";

                tablaFinal += salto + salto;
                tablaFinal += "public interface " + tabla + "Service {";
                tablaFinal += salto + salto + tabular;

                /* CUERPO DE LA INTERFACE */

                // SELECT BY PK
                if (USAR_LONG_COMO_PRIMARY_KEY_PARA_SERVICIOS) {
                    tablaFinal += entidad + " selectByID(Long " + toFirstLowerCase(tabla) + "ID) throws Exception;";
                } else {
                    tablaFinal += entidad + " selectByID(Integer " + toFirstLowerCase(tabla) + "ID) throws Exception;";
                }

                tablaFinal += salto + salto;

                // SELECT
                tablaFinal += tabular;
                tablaFinal += "List<" + entidad + "> select(" + entidad + " filter) throws Exception;";
                tablaFinal += salto + salto;

                // SELECT MAP
                tablaFinal += tabular;
                tablaFinal += "List<" + entidad + "> selectByMap(Map<String, Object> params) throws Exception;";
                tablaFinal += salto + salto;

                // SELECT MAP GRILLA
                tablaFinal += tabular;
                tablaFinal += "List<Map<String, Object>> selectByMapGrilla(Map<String, Object> params) throws Exception;";
                tablaFinal += salto + salto;

                // INSERT
                tablaFinal += tabular;
                tablaFinal += "void insert(" + entidad + " entity) throws Exception;";
                tablaFinal += salto + salto;

                // UPDATE
                tablaFinal += tabular;
                tablaFinal += "void update(" + entidad + " entity) throws Exception;";
                tablaFinal += salto + salto;

                // DELETE
                tablaFinal += tabular;
                tablaFinal += "void delete(" + entidad + " entity) throws Exception;";
                tablaFinal += salto;

                /* FIN DE LA CLASE */
                tablaFinal += "}";

                escribir(RUTA_OUT_SERVICE + tabla + "Service" + ".java", tablaFinal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------------- CONFIGURAR LOS SERVICIO IMPLEMENT
    public void crearServicioImpl() {

        String tabla;
        String entidad;
        String tablaFinal;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tablaFinal = "";
                tabla = itemTabla.getClase();

                tablaFinal += "package " + PAQUETE_SERVICE_IMPL + ";";

                entidad = tabla + "Entity";

                String firstChar = tabla.substring(0, 1).toUpperCase();

                tablaFinal += salto + salto;
                tablaFinal += "import " + PAQUETE_ENTIDADES + "." + entidad + ";";
                tablaFinal += salto;
                tablaFinal += "import " + PAQUETE_SERVICE + "." + tabla + "Service;";
                tablaFinal += salto;
                tablaFinal += "import " + PAQUETE_NEGOCIO + "." + tabla + "Negocio;";
                tablaFinal += salto;
                tablaFinal += "import " + PAQUETE_SERVICE + ".base.BaseService;";
                tablaFinal += salto + salto;
                tablaFinal += "import java.util.Map;";
                tablaFinal += salto;
                tablaFinal += "import java.util.List;";
                tablaFinal += salto + salto;
                tablaFinal += "import org.apache.logging.log4j.LogManager;";
                tablaFinal += salto;
                tablaFinal += "import org.apache.logging.log4j.Logger;";
                tablaFinal += salto + salto;
                tablaFinal += "import org.springframework.beans.factory.annotation.Autowired;";
                tablaFinal += salto;
                if (INCLUIR_ANOTACION_QUALIFIER) {
                    tablaFinal += "import org.springframework.beans.factory.annotation.Qualifier;";
                    tablaFinal += salto;
                }
                tablaFinal += "import org.springframework.stereotype.Service;";

                tablaFinal += salto + salto;
                if (INCLUIR_ANOTACION_QUALIFIER) {
                    tablaFinal += "@Service(\"" + toFirstLowerCase(tabla) + "Service" + "\")";
                } else {
                    tablaFinal += "@Service";
                }
                //tablaFinal += salto;
                tablaFinal += "public class " + tabla + "ServiceImpl" + " extends BaseService implements " + tabla + "Service {";
                tablaFinal += salto + salto + tabular;

                tablaFinal += "private static final Logger log = LogManager.getLogger(" + tabla + "ServiceImpl.class);";
                tablaFinal += salto + salto + tabular;

                tablaFinal += "@Autowired";
                if (INCLUIR_ANOTACION_QUALIFIER) {
                    tablaFinal += salto + tabular;
                    tablaFinal += "@Qualifier(\"" + toFirstLowerCase(tabla) + "Negocio\")";
                }
                tablaFinal += salto + tabular;
                tablaFinal += "private " + tabla + "Negocio " + toFirstLowerCase(tabla) + "Negocio;";
                tablaFinal += salto + salto;

                /* CUERPO DE LA INTERFACE */

                // SELECT BY PK
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                if (USAR_LONG_COMO_PRIMARY_KEY_PARA_SERVICIOS) {
                    tablaFinal += "public " + entidad + " selectByID(Long " + toFirstLowerCase(tabla) + "ID) throws Exception {";
                } else {
                    tablaFinal += "public " + entidad + " selectByID(Integer " + toFirstLowerCase(tabla) + "ID) throws Exception {";
                }

                tablaFinal += salto + salto + tabular + tabular;

                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;

                tablaFinal += "return " + toFirstLowerCase(tabla) + "Negocio.selectByID(" + toFirstLowerCase(tabla) + "ID);";

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-SBI-" + nextSecError() + "\", " + toFirstLowerCase(tabla) + "ID" + ", sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";

                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // SELECT
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                tablaFinal += "public List<" + entidad + "> select(" + entidad + " filter) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "return " + toFirstLowerCase(tabla) + "Negocio.select(filter);";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-SEL-" + nextSecError() + "\", filter, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";
                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // SELECT MAP
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                tablaFinal += "public List<" + entidad + "> selectByMap(Map<String, Object> params) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "return " + toFirstLowerCase(tabla) + "Negocio.selectByMap(params);";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-SBM-" + nextSecError() + "\", params, params, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";
                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // SELECT MAP GRILLA
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                tablaFinal += "public List<Map<String, Object>> selectByMapGrilla(Map<String, Object> params) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "return " + toFirstLowerCase(tabla) + "Negocio.selectByMapGrilla(params);";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-SBG-" + nextSecError() + "\", params, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";
                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // INSERT
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                tablaFinal += "public void insert(" + entidad + " entity) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;

                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;

                tablaFinal += toFirstLowerCase(tabla) + "Negocio.insert(entity);";

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-INS-" + nextSecError() + "\", entity, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";

                tablaFinal += salto + tabular;
                tablaFinal += "}";
                tablaFinal += salto + salto;

                // UPDATE
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                tablaFinal += "public void update(" + entidad + " entity) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;

                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;

                tablaFinal += toFirstLowerCase(tabla) + "Negocio.update(entity);";

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-UPD-" + nextSecError() + "\", entity, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";

                tablaFinal += salto + tabular;
                tablaFinal += "}";
                tablaFinal += salto + salto;

                // DELETE
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                tablaFinal += "public void delete(" + entidad + " entity) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;

                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;

                tablaFinal += toFirstLowerCase(tabla) + "Negocio.delete(entity);";

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-DEL-" + nextSecError() + "\", entity, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";

                tablaFinal += salto + tabular;
                tablaFinal += "}";
                tablaFinal += salto + salto;

                /* FIN DE LA CLASE */
                tablaFinal += "}";

                escribir(RUTA_OUT_SERVICE_IMPL + tabla + "ServiceImpl" + ".java", tablaFinal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // --------------------- CONFIGURAR LOS SERVICIO IMPLEMENT QUE INCLUYE LO DE LA CAPA NEGOCIO
    public void crearServicioNegocioImpl() {

        String tabla;
        String entidad;
        String tablaFinal;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tablaFinal = "";
                tabla = itemTabla.getClase();

                tablaFinal += "package " + PAQUETE_SERVICE_IMPL + ";";

                entidad = tabla + "Entity";

                String firstChar = tabla.substring(0, 1).toUpperCase();

                tablaFinal += salto + salto;
                tablaFinal += "import " + PAQUETE_ENTIDADES + "." + entidad + ";";
                tablaFinal += salto;
                tablaFinal += "import " + PAQUETE_SERVICE + "." + tabla + "Service;";
                tablaFinal += salto;
                tablaFinal += "import " + PAQUETE_SERVICE + ".base.BaseService;";
                tablaFinal += salto;
                tablaFinal += "import " + PAQUETE_UTILS + ".Constantes;";
                tablaFinal += salto;
                tablaFinal += "import " + PAQUETE_DAO + "." + tabla + "DAO;";
                tablaFinal += salto + salto;
                tablaFinal += "import java.util.Map;";
                tablaFinal += salto;
                tablaFinal += "import java.util.List;";
                tablaFinal += salto + salto;
                tablaFinal += "import org.apache.logging.log4j.LogManager;";
                tablaFinal += salto;
                tablaFinal += "import org.apache.logging.log4j.Logger;";
                tablaFinal += salto + salto;
                tablaFinal += "import org.springframework.beans.factory.annotation.Autowired;";
                tablaFinal += salto;
                if (INCLUIR_ANOTACION_QUALIFIER) {
                    tablaFinal += "import org.springframework.beans.factory.annotation.Qualifier;";
                    tablaFinal += salto;
                }
                tablaFinal += "import org.springframework.stereotype.Service;";
                tablaFinal += salto;
                tablaFinal += "import org.springframework.transaction.annotation.Transactional;";

                tablaFinal += salto + salto;
                if (INCLUIR_ANOTACION_QUALIFIER) {
                    tablaFinal += "@Service(\"" + toFirstLowerCase(tabla) + "Service" + "\")";
                } else {
                    tablaFinal += "@Service";
                }
                tablaFinal += salto;
                tablaFinal += "public class " + tabla + "ServiceImpl" + " extends BaseService implements " + tabla + "Service {";
                tablaFinal += salto + salto + tabular;

                tablaFinal += "private static final Logger log = LogManager.getLogger(" + tabla + "ServiceImpl.class);";
                tablaFinal += salto + salto + tabular;

                if (INYECCION_POR_CONTRUCTOR) {
                    tablaFinal += "private final " + tabla + "DAO " + toFirstLowerCase(tabla) + "DAO;";
                    tablaFinal += salto + salto + tabular;

                    tablaFinal += "@Autowired";
                    tablaFinal += salto + tabular;
                    tablaFinal += "public " + tabla + "ServiceImpl(" + tabla + "DAO " + toFirstLowerCase(tabla) + "DAO) {";
                    tablaFinal += salto + tabular + tabular;
                    tablaFinal += "this." + toFirstLowerCase(tabla) + "DAO = " + toFirstLowerCase(tabla) + "DAO;";
                    tablaFinal += salto + tabular;
                    tablaFinal += "}";
                    tablaFinal += salto + salto;
                } else {
                    tablaFinal += "@Autowired";
                    tablaFinal += salto + tabular;
                    if (INCLUIR_ANOTACION_QUALIFIER) {
                        tablaFinal += "@Qualifier(\"" + toFirstLowerCase(tabla) + "DAO\")";
                        tablaFinal += salto + tabular;
                    }
                    tablaFinal += "private " + tabla + "DAO " + toFirstLowerCase(tabla) + "DAO;";
                    tablaFinal += salto + salto;
                }

                /* CUERPO DE LA INTERFACE */

                // SELECT BY PK
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                if (USAR_LONG_COMO_PRIMARY_KEY_PARA_SERVICIOS) {
                    tablaFinal += "public " + entidad + " selectByID(Long " + toFirstLowerCase(tabla) + "ID) throws Exception {";
                } else {
                    tablaFinal += "public " + entidad + " selectByID(Integer " + toFirstLowerCase(tabla) + "ID) throws Exception {";
                }

                tablaFinal += salto + salto + tabular + tabular;

                tablaFinal += "try {";

                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "if (" + toFirstLowerCase(tabla) + "ID == null) throw new IllegalArgumentException(\"parameter " + toFirstLowerCase(tabla) + "ID cannot be null\");";

                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += entidad + " " + toFirstLowerCase(tabla) + " = new " + entidad + "();";
                tablaFinal += salto + salto + tabular + tabular + tabular;

                List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    if (itemCampo.isEsCampoClave()) {
                        tablaFinal += toFirstLowerCase(tabla) + "." + generarField("set", itemCampo.getCampoTabla()) + "(" + toFirstLowerCase(tabla) + "ID);";
                        tablaFinal += salto + tabular + tabular + tabular;
                    }
                }

                tablaFinal += salto + tabular + tabular + tabular;

                tablaFinal += "return " + toFirstLowerCase(tabla) + "DAO.selectByID(" + toFirstLowerCase(tabla) + ");";

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-SBI-" + nextSecError() + "\", " + toFirstLowerCase(tabla) + "ID, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";

                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // SELECT
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                tablaFinal += "public List<" + entidad + "> select(" + entidad + " filter) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "if (filter == null) throw new IllegalArgumentException(\"parameter filter cannot be null\");";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "return " + toFirstLowerCase(tabla) + "DAO.select(filter);";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-SEL-" + nextSecError() + "\", filter, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";
                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // SELECT MAP
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                tablaFinal += "public List<" + entidad + "> selectByMap(Map<String, Object> params) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "if (params == null) throw new IllegalArgumentException(\"parameter params cannot be null\");";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "return " + toFirstLowerCase(tabla) + "DAO.selectByMap(params);";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-SBM-" + nextSecError() + "\", params, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";
                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // SELECT MAP GRILLA
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                tablaFinal += "public List<Map<String, Object>> selectByMapGrilla(Map<String, Object> params) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "if (params == null) throw new IllegalArgumentException(\"parameter params cannot be null\");";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "return " + toFirstLowerCase(tabla) + "DAO.selectByMapGrilla(params);";
                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-SBG-" + nextSecError() + "\", params, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";
                tablaFinal += salto + tabular;
                tablaFinal += "}";

                tablaFinal += salto + salto;

                // INSERT
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                tablaFinal += "@Transactional(rollbackFor = Throwable.class)";
                tablaFinal += salto + tabular;
                tablaFinal += "public void insert(" + entidad + " entity) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;

                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "if (entity == null) throw new IllegalArgumentException(\"parameter entity cannot be null\");";
                tablaFinal += salto + salto + tabular + tabular + tabular;

                tablaFinal += "entity.setIndDel(Constantes.REGISTRO_ACTIVO);";
                tablaFinal += salto + salto + tabular + tabular + tabular;

                tablaFinal += toFirstLowerCase(tabla) + "DAO.insert(entity);";

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-INS-" + nextSecError() + "\", entity, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";

                tablaFinal += salto + tabular;
                tablaFinal += "}";
                tablaFinal += salto + salto;

                // UPDATE
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                tablaFinal += "@Transactional(rollbackFor = Throwable.class)";
                tablaFinal += salto + tabular;
                tablaFinal += "public void update(" + entidad + " entity) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;

                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "if (entity == null) throw new IllegalArgumentException(\"parameter entity cannot be null\");";
                tablaFinal += salto + tabular + tabular + tabular;

                tablaFinal += "if (entity.get" + tabla + "ID() == null) throw new IllegalArgumentException(\"parameter entity." + toFirstLowerCase(tabla) + "ID cannot be null\");";
                tablaFinal += salto + salto + tabular + tabular + tabular;

                tablaFinal += toFirstLowerCase(tabla) + "DAO.update(entity);";

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-UPD-" + nextSecError() + "\", entity, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";

                tablaFinal += salto + tabular;
                tablaFinal += "}";
                tablaFinal += salto + salto;

                // DELETE
                tablaFinal += tabular;
                tablaFinal += "@Override";
                tablaFinal += salto + tabular;
                tablaFinal += "@Transactional(rollbackFor = Throwable.class)";
                tablaFinal += salto + tabular;
                tablaFinal += "public void delete(" + entidad + " entity) throws Exception {";
                tablaFinal += salto + salto + tabular + tabular;

                tablaFinal += "try {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "if (entity == null) throw new IllegalArgumentException(\"parameter entity cannot be null\");";
                tablaFinal += salto + tabular + tabular + tabular;

                tablaFinal += "if (entity.get" + tabla + "ID() == null) throw new IllegalArgumentException(\"parameter entity." + toFirstLowerCase(tabla) + "ID cannot be null\");";
                tablaFinal += salto + salto + tabular + tabular + tabular;

                tablaFinal += "entity.setIndDel(Constantes.REGISTRO_INACTIVO);";
                tablaFinal += salto + salto + tabular + tabular + tabular;

                tablaFinal += toFirstLowerCase(tabla) + "DAO.update(entity);";

                tablaFinal += salto + salto + tabular + tabular;
                tablaFinal += "} catch (Exception sos) {";
                tablaFinal += salto + salto + tabular + tabular + tabular;
                tablaFinal += "String msgError = handleMsgError(\"" + firstChar + "SI-DEL-" + nextSecError() + "\", entity, sos);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "log.error(msgError);";
                tablaFinal += salto + tabular + tabular + tabular;
                tablaFinal += "throw handleError(sos);";
                tablaFinal += salto + tabular + tabular;
                tablaFinal += "}";

                tablaFinal += salto + tabular;
                tablaFinal += "}";
                tablaFinal += salto;

                /* FIN DE LA CLASE */
                tablaFinal += "}";

                escribir(RUTA_OUT_SERVICE_IMPL + tabla + "ServiceImpl" + ".java", tablaFinal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------------- CONFIGURAR LOS BEAN
    public void crearBean() {

        // nombre de la clase sin el sufijo Entity
        String tabla;
        String tablaFinal;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tablaFinal = "";
                tabla = itemTabla.getClase();

                tablaFinal += "package " + PAQUETE_ENTIDADES + ";";
                tablaFinal += salto;
                tablaFinal += salto;
                tablaFinal += "import " + PAQUETE_ENTIDADES + ".base.AuditoriaFields;";
                tablaFinal += salto;

                if (INCLUIR_VALIDACION_GRUPOS_EN_BEANS) {
                    tablaFinal += "import " + PAQUETE_VALIDATIONS + ".GroupDelete;";
                    tablaFinal += salto;
                    tablaFinal += "$IMPORT_GROUP_INSERT$";
                    tablaFinal += "import " + PAQUETE_VALIDATIONS + ".GroupUpdate;";
                    tablaFinal += salto;
                }

                tablaFinal += "$IMPORT_DATE_SEGMENT$";

                if (UTILIZAR_LOMBOK) {
                    tablaFinal += "import lombok.Data;";
                    tablaFinal += salto + salto;
                }

                if (INCLUIR_VALIDACION_GRUPOS_EN_BEANS) {
                    tablaFinal += "$IMPORT_NOT_EMPTY$";
                    tablaFinal += "import javax.validation.constraints.NotNull;";
                    tablaFinal += salto;
                }


                tablaFinal += salto;

                if (UTILIZAR_LOMBOK) {
                    tablaFinal += "@Data";
                    tablaFinal += salto;
                }

                tablaFinal += "public class " + tabla + "Entity" + " extends AuditoriaFields {";
                tablaFinal += salto;
                tablaFinal += salto;

                List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());

                boolean hayCampoDate = false;
                boolean hayValidacionNotEmpty = false;
                boolean hayGroupInsert = false;

                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (itemCampo.isEsAuditoria()) {
                        continue;
                    }

                    if (INCLUIR_VALIDACION_GRUPOS_EN_BEANS) {

                        if (itemCampo.isEsCampoClave()) {
                            // los primary key, en este esquema siempre son Integer/Double asi que siempre se usar NotNull (en vez de NotEmpty)
                            tablaFinal += tabular;
                            tablaFinal += "@NotNull(message = \"es requerido\", groups = {GroupUpdate.class, GroupDelete.class})";
                            tablaFinal += salto;

                        } else {

                            if (!itemCampo.isEsNullable()) {
                                // si no es primary key y es notnull, utilizar el notEmpty solo si es string, en otro caso es not null
                                String tipo = tipoDato(itemCampo.getTipo(), itemCampo.getEscala());

                                if (StringUtils.equalsIgnoreCase(tipo, "String ")) {
                                    tablaFinal += tabular;
                                    tablaFinal += "@NotEmpty(message = \"no puede ser empty\", groups = {GroupInsert.class, GroupUpdate.class})";
                                    tablaFinal += salto;
                                    hayValidacionNotEmpty = true;
                                    hayGroupInsert = true;
                                } else {
                                    tablaFinal += tabular;
                                    tablaFinal += "@NotNull(message = \"es requerido\", groups = {GroupInsert.class, GroupUpdate.class})";
                                    tablaFinal += salto;
                                    hayGroupInsert = true;
                                }
                            }
                        }
                    }

                    tablaFinal += tabular;
                    tablaFinal += "private ";

                    tablaFinal += tipoDato(itemCampo.getTipo(), itemCampo.getEscala());
                    tablaFinal += itemCampo.getCampoClase();
                    tablaFinal += ";";

                    if (StringUtils.equalsIgnoreCase(tipoDato(itemCampo.getTipo(), itemCampo.getEscala()), "Date ")) {
                        hayCampoDate = true;
                    }

                    tablaFinal += " // ";
                    tablaFinal += itemCampo.getCampoTabla();

                    if (INCLUIR_VALIDACION_GRUPOS_EN_BEANS) {
                        tablaFinal += salto + salto;

                    } else {
                        tablaFinal += salto;
                    }
                }

                /* ENTRE CAMPOS Y METODOS */
                if (!UTILIZAR_LOMBOK) {

                    /* CONSTRUCTOR */
                    tablaFinal += tabular;
                    tablaFinal += "public " + tabla + "Entity() {";
                    tablaFinal += salto + tabular;
                    tablaFinal += "}";
                    tablaFinal += salto + salto;

                    /* METODOS GET/SET DE LA CLASE */

                    for (CRUDCampoTablaClase itemCampo : listaCampos) {

                        // ignorar los campos de auditoria
                        if (itemCampo.isEsAuditoria()) {
                            continue;
                        }

                        tablaFinal += tabular;
                        tablaFinal += "public ";

                        /* METODO GET */
                        tablaFinal += tipoDato(itemCampo.getTipo(), itemCampo.getEscala());
                        tablaFinal += generarField("get", itemCampo.getCampoClase());

                        tablaFinal += "() {";
                        tablaFinal += salto;
                        tablaFinal += tabular + tabular;
                        tablaFinal += "return " + generarField(null, itemCampo.getCampoClase()) + ";";

                        tablaFinal += salto;
                        tablaFinal += tabular;
                        tablaFinal += "}";
                        tablaFinal += salto;
                        tablaFinal += salto;

                        /* METODO SET */
                        tablaFinal += tabular;
                        tablaFinal += "public void ";

                        tablaFinal += generarField("set", itemCampo.getCampoClase());
                        tablaFinal += "(";

                        tablaFinal += tipoDato(itemCampo.getTipo(), itemCampo.getEscala());

                        tablaFinal += generarField(null, itemCampo.getCampoClase());
                        tablaFinal += ") {";

                        tablaFinal += salto;
                        tablaFinal += tabular + tabular;
                        tablaFinal += "this." + generarField(null, itemCampo.getCampoClase()) + " = " + generarField(null, itemCampo.getCampoClase()) + ";";
                        tablaFinal += salto;
                        tablaFinal += tabular;
                        tablaFinal += "}";
                        tablaFinal += salto;
                    }
                }

                if (UTILIZAR_LOMBOK) {
                    if (INCLUIR_VALIDACION_GRUPOS_EN_BEANS) {
                        tablaFinal = tablaFinal.substring(0, tablaFinal.length() - 2);
                    } else {
                        tablaFinal = tablaFinal.substring(0, tablaFinal.length() - 1);
                    }
                }

                /* FIN DE LA CLASE */
                tablaFinal += salto;
                tablaFinal += "}";

                tablaFinal = tablaFinal.replace("$IMPORT_DATE_SEGMENT$", hayCampoDate ? "import java.util.Date;" + salto : StringUtils.EMPTY);
                tablaFinal = tablaFinal.replace("$IMPORT_NOT_EMPTY$", hayValidacionNotEmpty ? "import javax.validation.constraints.NotEmpty;" + salto : StringUtils.EMPTY);
                tablaFinal = tablaFinal.replace("$IMPORT_GROUP_INSERT$", hayGroupInsert ? "import " + PAQUETE_VALIDATIONS + ".GroupInsert;" + salto : StringUtils.EMPTY);

                escribir(RUTA_OUT_ENTIDADES + tabla + "Entity" + ".java", tablaFinal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------------- CONFIGURAR LAS VISTAS
    @SuppressWarnings("unused")
    public void crearVistaNuevo() {

        String contenidoFichero;
        String contenidoPlantilla;

        try {
            contenidoPlantilla = FileUtils.readFileToString(new File(RUTA_ARCHIVO_PLANTILLA_NUEVO));
        } catch (IOException e1) {
            System.out.println("no se pudo leer el archivo... acabando todo");
            return;
        }

        String tabla;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tabla = itemTabla.getClase();

                StringBuilder sb = new StringBuilder();

                // hace una copia de la plantilla para que procede todo denuevo
                // para la nueva tabla
                contenidoFichero = contenidoPlantilla;

                // buscando el bloque donde se deben meter las filas HTML por
                // cada campo
                int posCamposInicio = contenidoFichero.indexOf("**BLOQUE_CAMPOS_INICIO**");
                int posCamposFinal = contenidoFichero.indexOf("**BLOQUE_CAMPOS_FINAL**");

                String bloqueCampos;
                String bloqueCamposCopia = null;

                if (posCamposFinal > posCamposInicio) {

                    bloqueCampos = contenidoFichero.substring(posCamposInicio + "**BLOQUE_CAMPOS_INICIO**".length(), posCamposFinal);

                } else {
                    System.out.println("no se pudo hacer mucho con " + tabla + ", seguimos con la sgte tabla");
                    continue;
                }

                // aqui se guardan todos los rows con la data configurada
                String fragmentos = "";
                List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (itemCampo.isEsAuditoria()) {
                        continue;
                    }

                    String campoBean = generarField(null, itemCampo.getCampoClase());

                    String frag = bloqueCampos;
                    frag = frag.replace("**CAMPO_PRIMERA_MAYUSCULA**", primeraMayusculaTituloCampoHTML(campoBean, true));
                    frag = frag.replace("**CAMPO_BEAN**", campoBean);
                    frag = frag.replace("**TIPO_HTML_CAMPO_BEAN**", prefijoHTML(itemCampo.getTipo(), itemCampo.getEscala()));
                    frag = frag.replace("**MAX_LENGTH_CAMPO**", maxLengthHTML(itemCampo));

                    fragmentos += (tabular + tabular + tabular + frag);

                }

                // reemplazando el bloque por todo las filas (fragmentos)
                contenidoFichero = contenidoFichero.replace(bloqueCampos, fragmentos);

                contenidoFichero = contenidoFichero.replace("**BLOQUE_CAMPOS_INICIO**", "");
                contenidoFichero = contenidoFichero.replace("**BLOQUE_CAMPOS_FINAL**", "");

                contenidoFichero = contenidoFichero.replace("**TABLA_MAYUSCULA**", tabla.toUpperCase());
                contenidoFichero = contenidoFichero.replace("**MODULO_URL**", URL_PREFIJO_MODULO + tabla.toLowerCase() + URL_EXTENSION_MODULO);
                contenidoFichero = contenidoFichero.replace("**MODULO_BASE_URL**", URL_BASE_PARA_LA_APP);

                contenidoFichero = contenidoFichero.replace("**INPUT_MASK_CONTROLES**", generarInputMask(itemTabla));

                escribir(RUTA_OUT_VIEWS + URL_PREFIJO_MODULO + tabla.toLowerCase() + "/" + tabla.toLowerCase() + "-nuevo" + ".jsp", contenidoFichero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void crearVistaEditar() {

        String contenidoFichero;
        String contenidoPlantilla;

        try {
            contenidoPlantilla = FileUtils.readFileToString(new File(RUTA_ARCHIVO_PLANTILLA_EDITAR));
        } catch (IOException e1) {
            System.out.println("no se pudo leer el archivo... acabando todo");
            return;
        }

        String tabla;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tabla = itemTabla.getClase();

                StringBuilder sb = new StringBuilder();

                // hace una copia de la plantilla para que procede todo denuevo
                // para la nueva tabla
                contenidoFichero = contenidoPlantilla;

                // buscando el bloque donde se deben meter las filas HTML por
                // cada campo
                int posCamposInicio = contenidoFichero.indexOf("**BLOQUE_CAMPOS_INICIO**");
                int posCamposFinal = contenidoFichero.indexOf("**BLOQUE_CAMPOS_FINAL**");

                String bloqueCampos;
                String bloqueCamposCopia = null;

                if (posCamposFinal > posCamposInicio) {
                    bloqueCampos = contenidoFichero.substring(posCamposInicio + "**BLOQUE_CAMPOS_INICIO**".length(), posCamposFinal);

                } else {
                    System.out.println("no se pudo hacer mucho con " + tabla + ", seguimos con la sgte tabla");
                    continue;
                }

                // aqui se guardan todos los rows con la data configurada
                String fragmentos = "";

                List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (itemCampo.isEsAuditoria()) {
                        continue;
                    }

                    String campoBean = generarField(null, itemCampo.getCampoClase());

                    String frag = bloqueCampos;
                    frag = frag.replace("**CAMPO_PRIMERA_MAYUSCULA**", primeraMayusculaTituloCampoHTML(campoBean, true));
                    frag = frag.replace("**CAMPO_BEAN**", campoBean);
                    frag = frag.replace("**TIPO_HTML_CAMPO_BEAN**", prefijoHTML(itemCampo.getTipo(), itemCampo.getEscala()));
                    frag = frag.replace("**MAX_LENGTH_CAMPO**", maxLengthHTML(itemCampo));

                    fragmentos += (tabular + tabular + tabular + frag);

                }

                // reemplazando el bloque por todo las filas (fragmentos)
                contenidoFichero = contenidoFichero.replace(bloqueCampos, fragmentos);

                contenidoFichero = contenidoFichero.replace("**BLOQUE_CAMPOS_INICIO**", "");
                contenidoFichero = contenidoFichero.replace("**BLOQUE_CAMPOS_FINAL**", "");

                contenidoFichero = contenidoFichero.replace("**TABLA_MAYUSCULA**", tabla.toUpperCase());
                contenidoFichero = contenidoFichero.replace("**MODULO_URL**", URL_PREFIJO_MODULO + tabla.toLowerCase() + URL_EXTENSION_MODULO);
                contenidoFichero = contenidoFichero.replace("**MODULO_BASE_URL**", URL_BASE_PARA_LA_APP);

                contenidoFichero = contenidoFichero.replace("**INPUT_MASK_CONTROLES**", generarInputMask(itemTabla));

                escribir(RUTA_OUT_VIEWS + URL_PREFIJO_MODULO + tabla.toLowerCase() + "/" + tabla.toLowerCase() + "-editar" + ".jsp", contenidoFichero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void crearVistaVer() {

        String contenidoFichero;
        String contenidoPlantilla;
        try {
            contenidoPlantilla = FileUtils.readFileToString(new File(RUTA_ARCHIVO_PLANTILLA_VER));
        } catch (IOException e1) {
            System.out.println("no se pudo leer el archivo... acabando todo");
            return;
        }

        String tabla;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tabla = itemTabla.getClase();

                StringBuilder sb = new StringBuilder();

                // hace una copia de la plantilla para que procede todo denuevo para la nueva tabla
                contenidoFichero = contenidoPlantilla;

                // buscando el bloque donde se deben meter las filas HTML por cada campo
                int posCamposInicio = contenidoFichero.indexOf("**BLOQUE_CAMPOS_INICIO**");
                int posCamposFinal = contenidoFichero.indexOf("**BLOQUE_CAMPOS_FINAL**");

                String bloqueCampos;
                String bloqueCamposCopia = null;

                if (posCamposFinal > posCamposInicio) {
                    bloqueCampos = contenidoFichero.substring(posCamposInicio + "**BLOQUE_CAMPOS_INICIO**".length(), posCamposFinal);

                } else {
                    System.out.println("no se pudo hacer mucho con " + tabla + ", seguimos con la sgte tabla");
                    continue;
                }

                // aqui se guardan todos los rows con la data configurada
                String fragmentos = "";

                List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (itemCampo.isEsAuditoria()) {
                        continue;
                    }

                    String campoBean = generarField(null, itemCampo.getCampoClase());

                    String frag = bloqueCampos;
                    frag = frag.replace("**CAMPO_PRIMERA_MAYUSCULA**", primeraMayusculaTituloCampoHTML(campoBean, true));
                    frag = frag.replace("**CAMPO_BEAN**", campoBean);
                    frag = frag.replace("**TIPO_HTML_CAMPO_BEAN**", prefijoHTML(itemCampo.getTipo(), itemCampo.getEscala()));
                    frag = frag.replace("**MAX_LENGTH_CAMPO**", maxLengthHTML(itemCampo));

                    fragmentos += (tabular + tabular + tabular + frag);
                }

                // reemplazando el bloque por todo las filas (fragmentos)
                contenidoFichero = contenidoFichero.replace(bloqueCampos, fragmentos);

                contenidoFichero = contenidoFichero.replace("**BLOQUE_CAMPOS_INICIO**", "");
                contenidoFichero = contenidoFichero.replace("**BLOQUE_CAMPOS_FINAL**", "");

                contenidoFichero = contenidoFichero.replace("**TABLA_MAYUSCULA**", tabla.toUpperCase());
                contenidoFichero = contenidoFichero.replace("**MODULO_URL**", URL_PREFIJO_MODULO + tabla.toLowerCase() + URL_EXTENSION_MODULO);
                contenidoFichero = contenidoFichero.replace("**MODULO_BASE_URL**", URL_BASE_PARA_LA_APP);

                escribir(RUTA_OUT_VIEWS + URL_PREFIJO_MODULO + tabla.toLowerCase() + "/" + tabla.toLowerCase() + "-ver" + ".jsp", contenidoFichero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void crearVistaBuscar() {

        String contenidoFichero;
        String contenidoPlantilla;
        try {
            contenidoPlantilla = FileUtils.readFileToString(new File(RUTA_ARCHIVO_PLANTILLA_BUSCAR));
        } catch (IOException e1) {
            System.out.println("no se pudo leer el archivo... acabando todo");
            return;
        }

        String tabla;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tabla = itemTabla.getClase();

                StringBuilder sb = new StringBuilder();

                // hace una copia de la plantilla para que procede todo denuevo para la nueva tabla
                contenidoFichero = contenidoPlantilla;

                // buscando el bloque donde se deben meter las filas HTML por cada campo
                int posCamposInicio = contenidoFichero.indexOf("**BLOQUE_CAMPOS_INICIO**");
                int posCamposFinal = contenidoFichero.indexOf("**BLOQUE_CAMPOS_FINAL**");

                String bloqueCampos;
                String bloqueCamposCopia = null;

                if (posCamposFinal > posCamposInicio) {
                    bloqueCampos = contenidoFichero.substring(posCamposInicio + "**BLOQUE_CAMPOS_INICIO**".length(), posCamposFinal);

                } else {
                    System.out.println("no se pudo hacer mucho con " + tabla + ", seguimos con la sgte tabla");
                    continue;
                }

                // aqui se guardan todos los rows con la data configurada
                String fragmentos = "";

                List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (itemCampo.isEsAuditoria()) {
                        continue;
                    }

                    String campoBean = generarField(null, itemCampo.getCampoClase());

                    String frag = bloqueCampos;
                    frag = frag.replace("**CAMPO_PRIMERA_MAYUSCULA**", primeraMayusculaTituloCampoHTML(campoBean, true));

                    frag = frag.replace("**CAMPO_BEAN**", campoBean);
                    frag = frag.replace("**TIPO_HTML_CAMPO_BEAN**", prefijoHTML(itemCampo.getTipo(), itemCampo.getEscala()));
                    frag = frag.replace("**MAX_LENGTH_CAMPO**", maxLengthHTML(itemCampo));

                    fragmentos += (tabular + tabular + tabular + frag);

                }

                // reemplazando el bloque por todo las filas (fragmentos)
                contenidoFichero = contenidoFichero.replace(bloqueCampos, fragmentos);

                contenidoFichero = contenidoFichero.replace("**BLOQUE_CAMPOS_INICIO**", "");
                contenidoFichero = contenidoFichero.replace("**BLOQUE_CAMPOS_FINAL**", "");

                contenidoFichero = contenidoFichero.replace("**TABLA_MAYUSCULA**", tabla.toUpperCase());
                contenidoFichero = contenidoFichero.replace("**TABLA_MINUSCULA**", tabla.toLowerCase());
                contenidoFichero = contenidoFichero.replace("**TABLA_PRIMERA_MAYUSCULA_SIMPLE**", primeraMayusculaTituloCampoHTML(tabla, false));

                CRUDCampoTablaClase campoClave = buscarPrimerCampoClave(itemTabla);
                if (campoClave != null) {
                    contenidoFichero = contenidoFichero.replace("**CAMPO_CLAVE_PARAMETRO**", campoClave.getCampoClase());
                }

                contenidoFichero = contenidoFichero.replace("**MODULO_URL**", URL_PREFIJO_MODULO + tabla.toLowerCase() + URL_EXTENSION_MODULO);
                contenidoFichero = contenidoFichero.replace("**MODULO_BASE_URL**", URL_BASE_PARA_LA_APP);

                contenidoFichero = contenidoFichero.replace("**INPUT_MASK_CONTROLES**", generarInputMask(itemTabla));

                escribir(RUTA_OUT_VIEWS + URL_PREFIJO_MODULO + tabla.toLowerCase() + "/" + tabla.toLowerCase() + "-buscar" + ".jsp", contenidoFichero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void crearVistaBuscarGrilla() {

        String contenidoFichero = null;
        String contenidoPlantilla;
        try {
            contenidoPlantilla = FileUtils.readFileToString(new File(RUTA_ARCHIVO_PLANTILLA_BUSCAR_GRILLA));
        } catch (IOException e1) {
            System.out.println("no se pudo leer el archivo... acabando todo");
            return;
        }

        String tabla;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tabla = itemTabla.getClase();

                try {

                    StringBuilder sb = new StringBuilder();

                    // hace una copia de la plantilla para que procede todo denuevo para la nueva tabla
                    contenidoFichero = contenidoPlantilla;

                    if (contenidoFichero == null) {
                        throw new Exception("contenidoFichero cannot be null");
                    }

                    // buscando el bloque donde se deben meter las filas HTML por cada campo
                    int posCamposInicio = contenidoFichero.indexOf("**BLOQUE_CAMPOS_INICIO_01**");
                    int posCamposFinal = contenidoFichero.indexOf("**BLOQUE_CAMPOS_FINAL_01**");

                    String bloqueCampos;
                    String bloqueCamposCopia = null;

                    if (posCamposFinal > posCamposInicio) {
                        bloqueCampos = contenidoFichero.substring(posCamposInicio + "**BLOQUE_CAMPOS_INICIO_01**".length(), posCamposFinal);

                    } else {
                        System.out.println("no se pudo hacer mucho con " + tabla + ", seguimos con la sgte tabla");
                        continue;
                    }

                    // aqui se guardan todos los rows con la data configurada
                    String fragmentos = "";

                    List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());
                    for (CRUDCampoTablaClase itemCampo : listaCampos) {

                        // ignorar los campos de auditoria
                        if (itemCampo.isEsAuditoria()) {
                            continue;
                        }

                        String campoBean = generarField(null, itemCampo.getCampoClase());

                        String frag = bloqueCampos;
                        frag = frag.replace("**CAMPO_PRIMERA_MAYUSCULA**", primeraMayusculaTituloCampoHTML(campoBean, true));

                        frag = frag.replace("**CAMPO_BEAN**", campoBean);
                        frag = frag.replace("**TIPO_HTML_CAMPO_BEAN**", prefijoHTML(itemCampo.getTipo(), itemCampo.getEscala()));
                        frag = frag.replace("**MAX_LENGTH_CAMPO**", maxLengthHTML(itemCampo));

                        fragmentos += (tabular + tabular + tabular + frag + salto);

                    }

                    // reemplazando el bloque por todo las filas (fragmentos)
                    contenidoFichero = contenidoFichero.replace(bloqueCampos, fragmentos);

                    contenidoFichero = contenidoFichero.replace("**TABLA_MAYUSCULA**", tabla.toUpperCase());
                    contenidoFichero = contenidoFichero.replace("**TABLA_MINUSCULA**", tabla.toLowerCase());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                String primerCampo = null;

                try {

                    StringBuilder sb = new StringBuilder();

                    // buscando el bloque donde se deben meter las filas HTML por cada campo
                    int posCamposInicio = contenidoFichero.indexOf("**BLOQUE_CAMPOS_INICIO_02**");
                    int posCamposFinal = contenidoFichero.indexOf("**BLOQUE_CAMPOS_FINAL_02**");

                    String bloqueCampos;
                    String bloqueCamposCopia = null;

                    if (posCamposFinal > posCamposInicio) {
                        bloqueCampos = contenidoFichero.substring(posCamposInicio + "**BLOQUE_CAMPOS_INICIO_02**".length(), posCamposFinal);

                    } else {
                        System.out.println("no se pudo hacer mucho con " + tabla + ", seguimos con la sgte tabla");
                        continue;
                    }

                    // aqui se guardan todos los rows con la data configurada
                    String fragmentos = "";

                    primerCampo = null;

                    List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());
                    for (CRUDCampoTablaClase itemCampo : listaCampos) {

                        // ignorar los campos de auditoria
                        if (itemCampo.isEsAuditoria()) {
                            continue;
                        }

                        String campoBean = generarField(null, itemCampo.getCampoClase());

                        if (primerCampo == null) {
                            primerCampo = campoBean;
                        }

                        String frag = bloqueCampos;
                        frag = frag.replace("**CAMPO_PRIMERA_MAYUSCULA**", primeraMayusculaTituloCampoHTML(campoBean, true));

                        frag = frag.replace("**CAMPO_BEAN**", campoBean);
                        frag = frag.replace("**TIPO_HTML_CAMPO_BEAN**", prefijoHTML(itemCampo.getTipo(), itemCampo.getEscala()));
                        frag = frag.replace("**MAX_LENGTH_CAMPO**", maxLengthHTML(itemCampo));

                        fragmentos += (tabular + tabular + tabular + tabular + frag + salto);

                    }

                    // reemplazando el bloque por todo las filas (fragmentos)
                    contenidoFichero = contenidoFichero.replace(bloqueCampos, fragmentos);

                    if (contenidoFichero == null) {
                        throw new Exception("contenidoFichero cannot be null");
                    }

                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }

                contenidoFichero = contenidoFichero.replace("**TABLA_PRIMER_CAMPO**", StringUtils.trimToEmpty(primerCampo));
                contenidoFichero = contenidoFichero.replace("**BLOQUE_CAMPOS_INICIO_01**", "");
                contenidoFichero = contenidoFichero.replace("**BLOQUE_CAMPOS_FINAL_01**", "");
                contenidoFichero = contenidoFichero.replace("**BLOQUE_CAMPOS_INICIO_02**", "");
                contenidoFichero = contenidoFichero.replace("**BLOQUE_CAMPOS_FINAL_02**", "");

                contenidoFichero = contenidoFichero.replace("**TABLA_MAYUSCULA**", tabla.toUpperCase());
                contenidoFichero = contenidoFichero.replace("**TABLA_MINUSCULA**", tabla.toLowerCase());
                contenidoFichero = contenidoFichero.replace("**TABLA_PRIMERA_MAYUSCULA_SIMPLE**", primeraMayusculaTituloCampoHTML(tabla, false));

                contenidoFichero = contenidoFichero.replace("**MODULO_URL**", URL_PREFIJO_MODULO + tabla.toLowerCase() + URL_EXTENSION_MODULO);
                contenidoFichero = contenidoFichero.replace("**MODULO_BASE_URL**", URL_BASE_PARA_LA_APP);

                escribir(RUTA_OUT_VIEWS + URL_PREFIJO_MODULO + tabla.toLowerCase() + "/" + tabla.toLowerCase() + "-buscar-grilla" + ".jsp", contenidoFichero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void crearRestControllers() {

        String contenidoFichero;
        String contenidoPlantilla;
        try {
            contenidoPlantilla = FileUtils.readFileToString(new File(RUTA_ARCHIVO_PLANTILLA_CONTROLLER_REST));
        } catch (IOException e1) {
            System.out.println("no se pudo leer el archivo... acabando todo");
            return;
        }

        String tabla;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tabla = itemTabla.getClase();

                String primerCampo = null;

                StringBuilder sb = new StringBuilder();

                // hace una copia de la plantilla para que procede todo denuevo para la nueva tabla
                contenidoFichero = contenidoPlantilla;

                // aqui se guardan todos los rows con la data configurada
                String fragmentos = "";

                List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (itemCampo.isEsAuditoria()) {
                        continue;
                    }

                    String campoBean = generarField(null, itemCampo.getCampoClase());

                    if (primerCampo == null) {
                        primerCampo = campoBean;
                    }
                }

                // nota: el ** trae problemas con el replaceFirst
                while (contenidoFichero.contains("--CONTADOR_ERROR--")) {
                    contenidoFichero = contenidoFichero.replaceFirst("--CONTADOR_ERROR--", nextSecError());
                }

                contenidoFichero = contenidoFichero.replace("**TABLA**", tabla);
                contenidoFichero = contenidoFichero.replace("**PRIMERDIGITO_ERROR**", tabla.substring(0, 1).toUpperCase());
                contenidoFichero = contenidoFichero.replace("**TABLA_PRIMER_CAMPO**", StringUtils.trimToEmpty(primerCampo));
                contenidoFichero = contenidoFichero.replace("**TABLA_MAYUSCULA**", tabla.toUpperCase());
                contenidoFichero = contenidoFichero.replace("**TABLA_MINUSCULA**", tabla.toLowerCase());
                contenidoFichero = contenidoFichero.replace("**TABLA_PRIMERA_MAYUSCULA**", primeraMayusculaTituloCampoHTML(tabla, true));
                contenidoFichero = contenidoFichero.replace("**TABLA_PRIMERA_MAYUSCULA_SIMPLE**", primeraMayusculaTituloCampoHTML(tabla, false));

                contenidoFichero = contenidoFichero.replace("**MODULO_URL**", URL_PREFIJO_MODULO + tabla.toLowerCase() + URL_EXTENSION_MODULO);
                contenidoFichero = contenidoFichero.replace("**MODULO_BASE_URL**", URL_BASE_PARA_LA_APP);

                contenidoFichero = contenidoFichero.replace("**PAQUETE_BASE**", PAQUETE_BASE);

                escribir(RUTA_OUT_CONTROLLERS_REST + "Registro" + tabla + "RestController.java", contenidoFichero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void crearWebControllers() {

        String contenidoFichero;
        String contenidoPlantilla;
        try {
            contenidoPlantilla = FileUtils.readFileToString(new File(RUTA_ARCHIVO_PLANTILLA_CONTROLLER_WEB));
        } catch (IOException e1) {
            System.out.println("no se pudo leer el archivo... acabando todo");
            return;
        }

        String tabla;

        try {

            List<CRUDTablaClase> listaTablas = listarTablas();
            for (CRUDTablaClase itemTabla : listaTablas) {

                tabla = itemTabla.getClase();

                String primerCampo = null;

                StringBuilder sb = new StringBuilder();

                // hace una copia de la plantilla para que procede todo denuevo para la nueva tabla
                contenidoFichero = contenidoPlantilla;

                // aqui se guardan todos los rows con la data configurada
                String fragmentos = "";

                List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());
                for (CRUDCampoTablaClase itemCampo : listaCampos) {

                    // ignorar los campos de auditoria
                    if (itemCampo.isEsAuditoria()) {
                        continue;
                    }

                    String campoBean = generarField(null, itemCampo.getCampoClase());

                    if (primerCampo == null) {
                        primerCampo = campoBean;
                    }
                }

                // nota: el ** trae problemas con el replaceFirst
                while (contenidoFichero.contains("--CONTADOR_ERROR--")) {
                    contenidoFichero = contenidoFichero.replaceFirst("--CONTADOR_ERROR--", nextSecError());
                }

                contenidoFichero = contenidoFichero.replace("**TABLA**", tabla);
                contenidoFichero = contenidoFichero.replace("**PRIMERDIGITO_ERROR**", tabla.substring(0, 1).toUpperCase());
                contenidoFichero = contenidoFichero.replace("**TABLA_PRIMER_CAMPO**", StringUtils.trimToEmpty(primerCampo));
                contenidoFichero = contenidoFichero.replace("**TABLA_MAYUSCULA**", tabla.toUpperCase());
                contenidoFichero = contenidoFichero.replace("**TABLA_MINUSCULA**", tabla.toLowerCase());
                contenidoFichero = contenidoFichero.replace("**TABLA_PRIMERA_MAYUSCULA**", primeraMayusculaTituloCampoHTML(tabla, true));
                contenidoFichero = contenidoFichero.replace("**TABLA_PRIMERA_MAYUSCULA_SIMPLE**", primeraMayusculaTituloCampoHTML(tabla, false));

                contenidoFichero = contenidoFichero.replace("**MODULO_URL**", URL_PREFIJO_MODULO + tabla.toLowerCase() + URL_EXTENSION_MODULO);
                contenidoFichero = contenidoFichero.replace("**MODULO_BASE_URL**", URL_BASE_PARA_LA_APP);

                contenidoFichero = contenidoFichero.replace("**PAQUETE_BASE**", PAQUETE_BASE);

                escribir(RUTA_OUT_CONTROLLERS_WEB + "Registro" + tabla.substring(0, 1).toUpperCase() + tabla.substring(1).toLowerCase() + "Controller.java", contenidoFichero);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String primeraMayusculaTituloCampoHTML(String input, boolean mejorar) {
        String res = input.substring(0, 1).toUpperCase() + input.substring(1);

        if (mejorar) {
            if (res.endsWith("on")) {
                res = res.substring(0, res.length() - 2) + "&oacute;n";
            }

            if (res.endsWith("ia")) {
                res = res.substring(0, res.length() - 2) + "&iacute;a";
            }
        }

        return res;
    }

    public String prefijoHTML(String tParam, Long escala) {

        String result = "";

        if (tParam.equals("decimal")) {

            if (escala == null || escala.intValue() <= 0) {
                result += "int";
            } else {
                result += "dbl";
            }

        } else if (tParam.startsWith("double") || tParam.startsWith("float")) {
            result += "dbl";
        } else if (tParam.equals("varchar") || tParam.equals("char") || tParam.equals("text")) {
            result += "txt";
        } else if (tParam.startsWith("datetime") || tParam.startsWith("date")) { // ok
            result += "fec";
        } else if (tParam.equals("int") || tParam.equals("tinyint")) { // ok
            result += "int";
        } else if (tParam.equals("bigint")) {
            result += "int";
        } else if (tParam.equals("blob") || tParam.equals("mediumblob") || tParam.equals("tinyblob")) {
            result += "byte";
        }

        return result;
    }

    private String generarInputMask(CRUDTablaClase itemTabla) throws Exception {

        StringBuilder sb = new StringBuilder();

        List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());
        for (CRUDCampoTablaClase itemCampo : listaCampos) {

            // ignorar los campos de auditoria
            if (itemCampo.isEsAuditoria()) {
                continue;
            }

            // tipo de dato
            String tParam = itemCampo.getTipo();

            String campoBean = generarField(null, itemCampo.getCampoClase());
            String prefijo = prefijoHTML(itemCampo.getTipo(), itemCampo.getEscala());
            String nameCampo = prefijo + "_" + campoBean;

            // cuando son numeros
            Long escala = itemCampo.getEscala();
            Long precision = itemCampo.getPrecision();

            // control.prop('maxlength', maxlength);

            if (tParam.equals("decimal")) {

                if (escala == null || escala.intValue() <= 0) {

                    if (precision != null) {

                        // es entero
                        String maxLength = maxLengthHTML(itemCampo);

                        sb.append(tabular).append(tabular);
                        sb.append("control = div.find( 'input[name=\"").append(nameCampo).append("\"]' );").append(salto).append(tabular).append(tabular);
                        sb.append("control.inputmask( \"9{0,").append(maxLength).append("}\", {placeholder: ''});").append(salto).append(tabular).append(tabular);
                        sb.append("control.prop('maxlength', ").append(maxLength).append(");");
                        sb.append(salto);
                        sb.append(salto);
                    }

                } else {

                    String maxLength = maxLengthHTML(itemCampo);

                    sb.append(tabular).append(tabular);
                    sb.append("control = div.find( 'input[name=\"").append(nameCampo).append("\"]' );").append(salto).append(tabular).append(tabular);
                    sb.append("control.inputmask( \"decimal\", {placeholder: ''});").append(salto).append(tabular).append(tabular);
                    sb.append("control.prop('maxlength', ").append(maxLength).append(");");
                    sb.append(salto);
                    sb.append(salto);

                }

            } else if (tParam.startsWith("double") || tParam.startsWith("float")) {

                String maxLength = maxLengthHTML(itemCampo);

                sb.append(tabular).append(tabular);
                sb.append("control = div.find( 'input[name=\"").append(nameCampo).append("\"]' );").append(salto).append(tabular).append(tabular);
                sb.append("control.inputmask( \"decimal\", {placeholder: ''});").append(salto).append(tabular).append(tabular);
                sb.append("control.prop('maxlength', ").append(maxLength).append(");");
                sb.append(salto);
                sb.append(salto);

            } else if (tParam.equals("varchar") || tParam.equals("char") || tParam.equals("text")) {

                String maxLength = maxLengthHTML(itemCampo);

                sb.append(tabular).append(tabular);
                sb.append("control = div.find( 'input[name=\"").append(nameCampo).append("\"]' );").append(salto).append(tabular).append(tabular);
                sb.append("control.inputmask( \"[*|| ]{0,").append(maxLength).append("}\", {placeholder: ''});").append(salto).append(tabular).append(tabular);
                sb.append("control.prop( 'maxlength', ").append(maxLength).append(" );");
                sb.append(salto);
                sb.append(salto);

            } else if (tParam.startsWith("datetime") || tParam.startsWith("date")) { // ok

                String maxLength = maxLengthHTML(itemCampo);

                sb.append(tabular).append(tabular);
                sb.append("control = div.find( 'input[name=\"").append(nameCampo).append("\"]' );").append(salto).append(tabular).append(tabular);
                sb.append("control.inputmask( \"9{2}/9{2}/{1}9{4}\", {placeholder: ''});").append(salto).append(tabular).append(tabular);
                sb.append("control.prop( 'maxlength', ").append(maxLength).append(" );");
                sb.append(salto);
                sb.append(salto);

            } else if (tParam.equals("int") || tParam.equals("tinyint")) { // ok

                String maxLength = maxLengthHTML(itemCampo);

                sb.append(tabular).append(tabular);
                sb.append("control = div.find( 'input[name=\"").append(nameCampo).append("\"]' );").append(salto).append(tabular).append(tabular);
                sb.append("control.inputmask( \"9{0,").append(maxLength).append("}\", {placeholder: ''});").append(salto).append(tabular).append(tabular);
                sb.append("control.prop( 'maxlength', ").append(maxLength).append(" );");
                sb.append(salto);
                sb.append(salto);

            } else if (tParam.equals("bigint")) {

                String maxLength = maxLengthHTML(itemCampo);

                sb.append(tabular).append(tabular);
                sb.append("control = div.find( 'input[name=\"").append(nameCampo).append("\"]' );").append(salto).append(tabular).append(tabular);
                sb.append("control.inputmask( \"9{0,").append(maxLength).append("}\", {placeholder: ''});").append(salto).append(tabular).append(tabular);
                sb.append("control.prop( 'maxlength', ").append(maxLength).append(" );");
                sb.append(salto);
                sb.append(salto);
            } else if (tParam.equals("blob")) {

                String maxLength = maxLengthHTML(itemCampo);

                sb.append(tabular).append(tabular);
                sb.append("control = div.find( 'input[name=\"").append(nameCampo).append("\"]' );").append(salto).append(tabular).append(tabular);
                sb.append("control.inputmask( \"9{0,").append(maxLength).append("}\", {placeholder: ''});").append(salto).append(tabular).append(tabular);
                sb.append("control.prop( 'maxlength', ").append(maxLength).append(" );");
                sb.append(salto);
                sb.append(salto);
            }

        }

        return sb.toString();
    }

    public String maxLengthHTML(CRUDCampoTablaClase itemCampo) {

        String tParam = itemCampo.getTipo();

        // cuando es cadena
        Long longitud = itemCampo.getLongitud();

        // cuando son numeros
        Long escala = itemCampo.getEscala();
        Long precision = itemCampo.getPrecision();

        String result = "";

        if (tParam.equals("decimal")) {

            if (escala == null || escala.intValue() <= 0) {

                if (precision != null) {
                    result = String.valueOf(precision.intValue());
                }

            } else {

                int size = 1;
                if (precision != null) {
                    size += precision.intValue();
                }
                if (escala != null) {
                    size += escala.intValue();
                }
                result = String.valueOf(size);
            }

        } else if (tParam.startsWith("double") || tParam.startsWith("float")) {

            int size = 1;
            if (precision != null) {
                size += precision.intValue();
            }
            if (escala != null) {
                size += escala.intValue();
            }
            result = String.valueOf(size);

        } else if (tParam.equals("varchar") || tParam.equals("char") || tParam.equals("text")) { // TODO: revisar si text tiene un ancho fijo

            if (longitud != null) {
                result = String.valueOf(longitud.intValue());
            }

        } else if (tParam.startsWith("datetime") || tParam.startsWith("date")) { // ok

            result = "10";

        } else if (tParam.equals("int") || tParam.equals("tinyint")) { // ok

            int size = 0;
            if (precision != null) {
                size += precision.intValue();
            }
            result = String.valueOf(size);

        } else if (tParam.equals("bigint")) {

            int size = 1;
            if (precision != null) {
                size += precision.intValue();
            }
            if (escala != null) {
                size += escala.intValue();
            }
            result = String.valueOf(size);
        }

        if (StringUtils.isBlank(result)) {
            result = "50"; // tamaño por default
        }

        return result;
    }

    public String tipoDato(String tParam, Long escala) {

        String result = "";

        if (tParam.equals("decimal")) { // ok

            if (escala == null || escala.intValue() <= 0) {
                result += "Long ";
            } else {
                result += "Double ";
            }

        } else if (tParam.startsWith("double") || tParam.startsWith("float")) { // ok
            result += "Double ";
        } else if (tParam.equals("varchar") || tParam.equals("char") || tParam.equals("text")) { // ok
            result += "String ";
        } else if (tParam.startsWith("datetime") || tParam.startsWith("date")) { // ok
            result += "Date ";
        } else if (tParam.equals("int") || tParam.equals("tinyint")) { // ok
            result += "Integer ";
        } else if (tParam.equals("bigint")) {
            result += "Long ";
        } else if (tParam.equals("mediumblob") || tParam.equals("blob")) {
            result += "byte[] ";
        } else {
            System.out.println("tipo de dato no conocido: " + tParam);
        }

        return result;
    }

    public String tipoDatoJDBC(String tParam, Long escala) {
        String result = "";

        if (tParam.equals("decimal")) {
            if (escala == null || escala.intValue() <= 0) {
                result += "BIGINT";
            } else {
                result += "DECIMAL";
            }
        }

        if (tParam.startsWith("double") || tParam.startsWith("float")) {
            result += "DECIMAL"; // ok
        } else if (tParam.startsWith("varchar") || tParam.startsWith("char") || tParam.startsWith("text")) {
            result += "VARCHAR"; // ok
        } else if (tParam.equals("date")) {
            result += "DATE"; // ok
        } else if (tParam.equals("datetime") || tParam.equals("timestamp")) {
            result += "TIMESTAMP"; // ok
        } else if (tParam.equals("int") || tParam.equals("tinyint")) {
            result += "INTEGER"; // ok
        } else if (tParam.equals("bigint")) {
            result += "BIGINT";
        } else if (tParam.equals("blob")) {
            result += "BLOB";
        } else if (tParam.equals("mediumblob")) {
            result += "MEDIUMBLOB";
        } else if (tParam.equals("tinyblob")) {
            result += "TINYBLOB";
        }

        return result;
    }

    public void escribir(String name, String contenido) {
        try {
            FileUtils.writeStringToFile(new File(name), contenido);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<CRUDTablaClase> listarTablas() throws Exception {

        // para hacer mas eficiente se usa una memoria cache, mismo GAE
        if (cache.get("listaTablas") == null) {

            Connection cnx = getCnx();
            Statement cmd = cnx.createStatement();
            ResultSet rs = cmd.executeQuery("SELECT table_name FROM information_schema.tables where table_schema = '" + NOMBRE_BASE_DATOS + "'");

            CRUDTablaClase tablaClase;
            ArrayList<CRUDTablaClase> tablas = new ArrayList<>();

            String clase;
            String tabla;
            String[] trozos;

            int contadorTablas = 0;
            while (rs.next()) {

                tablaClase = new CRUDTablaClase();

                tabla = StringUtils.trimToEmpty(rs.getString(1));
                trozos = StringUtils.lowerCase(tabla).split("_");

                clase = "";
                for (String trozo : trozos) {
                    clase += toFirstUpperCase(StringUtils.trimToEmpty(trozo));
                }

                tablaClase.setTabla(tabla);
                tablaClase.setClase(clase);

                tablas.add(tablaClase);

                contadorTablas++;

            }

            rs.close();
            cmd.close();
            cnx.close();

            if (contadorTablas == 0) {
                System.out.println("No se encontraron tablas");
            } else {
                System.out.println("Se encontraron " + contadorTablas + " tablas");
            }

            System.out.println("procesando...");

            cache.put("listaTablas", tablas);
        }

        return (List<CRUDTablaClase>) cache.get("listaTablas");
    }

    @SuppressWarnings("unchecked")
    public List<CRUDCampoTablaClase> listarCampos(String nombreTabla) throws Exception {

        // para hacer mas eficiente se usa una memoria cache, mismo GAE
        if (cache.get("camposTabla" + nombreTabla) == null) {

            CRUDCampoTablaClase campoTablaClase;
            ArrayList<CRUDCampoTablaClase> campos = new ArrayList<>();

            String campoTabla;
            String campoClase;

            Statement cmd;
            ResultSet rs;

            Connection cnx = getCnx();

            cmd = cnx.createStatement();
            rs = cmd.executeQuery(" SELECT COLUMN_NAME, DATA_TYPE, NUMERIC_SCALE, NUMERIC_PRECISION, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE FROM information_schema.columns WHERE TABLE_SCHEMA = '"
                    + NOMBRE_BASE_DATOS + "' AND TABLE_NAME = '" + nombreTabla + "' ORDER BY ORDINAL_POSITION ");

            while (rs.next()) {

                campoTablaClase = new CRUDCampoTablaClase();

                // nombre del campo en la tabla
                campoTabla = StringUtils.trimToEmpty(rs.getString("COLUMN_NAME"));

                // nombre del atributo en la clase java
                if (PASAR_A_MINUSCULAS_CAMPOS_DE_BD) {
                    campoClase = generarField(null, campoTabla.toLowerCase());
                } else {
                    campoClase = generarField(null, campoTabla);
                }

                campoTablaClase.setCampoTabla(campoTabla);
                campoTablaClase.setCampoClase(campoClase);
                campoTablaClase.setTipo(StringUtils.trimToEmpty(rs.getString("DATA_TYPE")));
                campoTablaClase.setEscala(rs.getLong("NUMERIC_SCALE"));
                // campos adicionales: precision cuando es numero, y longitud
                // cuando es cadena
                campoTablaClase.setPrecision(rs.getLong("NUMERIC_PRECISION"));
                campoTablaClase.setLongitud(rs.getLong("CHARACTER_MAXIMUM_LENGTH"));
                campoTablaClase.setEsNullable(StringUtils.endsWithIgnoreCase(rs.getString("IS_NULLABLE"), "YES"));

                campos.add(campoTablaClase);

            }

            // recorriendo campos de auditoria
            for (String s : campos_auditoria) {

                // buscando en la lista de campos si alguno coincide para
                // actualizar como campo auditoria
                for (CRUDCampoTablaClase itemCampo : campos) {

                    if (s.equalsIgnoreCase(itemCampo.getCampoTabla())) {
                        itemCampo.setEsAuditoria(true);
                        break;
                    }

                }

            }

            rs.close();
            cmd.close();

            // buscando todos los campos de la clave de la tabla
            cmd = cnx.createStatement();
            rs = cmd.executeQuery(" SELECT COLUMN_NAME FROM information_schema.columns WHERE TABLE_SCHEMA = '" + NOMBRE_BASE_DATOS + "' AND TABLE_NAME = '" + nombreTabla
                    + "' AND COLUMN_KEY = 'PRI' ORDER BY ORDINAL_POSITION");

            // recorriendo campos clave de la tabla
            while (rs.next()) {

                // buscando en la lista de campos si alguno coincide para
                // actualizar como campo clave
                for (CRUDCampoTablaClase itemCampo : campos) {

                    if (StringUtils.trimToEmpty(rs.getString(1)).equalsIgnoreCase(itemCampo.getCampoTabla())) {
                        itemCampo.setEsCampoClave(true);
                        break;
                    }

                }

            }

            rs.close();
            cmd.close();

            cnx.close();

            cache.put("camposTabla" + nombreTabla, campos);
        }

        return (List<CRUDCampoTablaClase>) cache.get("camposTabla" + nombreTabla);
    }

    public List<CRUDCampoTablaClase> buscarCamposClave(CRUDTablaClase itemTabla) throws Exception {

        List<CRUDCampoTablaClase> listaCampos = listarCampos(itemTabla.getTabla());

        if (CollectionUtils.isNotEmpty(listaCampos)) {

            List<CRUDCampoTablaClase> listaCamposKey = new ArrayList<>();

            for (CRUDCampoTablaClase campo : listaCampos) {

                if (campo.isEsAuditoria()) {
                    continue;
                }

                if (campo.isEsCampoClave()) {

                    listaCamposKey.add(campo);
                }

            }

            return listaCamposKey;
        }

        return null;
    }

    public CRUDCampoTablaClase buscarPrimerCampoClave(CRUDTablaClase itemTabla) throws Exception {

        List<CRUDCampoTablaClase> listaCamposKey = buscarCamposClave(itemTabla);

        if (CollectionUtils.isNotEmpty(listaCamposKey)) {
            return listaCamposKey.get(0);
        }

        return null;
    }

    public Connection getCnx() throws Exception {
        return Cnx.getConexionInventario();
//        return Cnx.getConexionPediche();
    }

}
