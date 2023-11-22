package com.jcfr.generador;

import com.jcfr.utiles.Constantes;
import com.jcfr.utiles.string.JSUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.omg.SendingContext.RunTime;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes"})
public class SimpleGenerador {

    // parametros obligatorios para configurar
    private final static String CUSTOM_ID = "plan_viaje_concepto";

    private final static String RUTA_IN_BASE = "D://jcfr/temp/generador/";
    private final static String RUTA_ARCHIVO_DDL_SCRIPT_BD = CUSTOM_ID + ".txt";

    private final static boolean INCLUIR_NOMBRE_CAMPO_COMO_COMENTARIO = true;

    // escoger el motor, aparte se tiene la opcion de personalizar la generacion
    // de ciertos campos -> customField()
    private final static String MOTOR_DE_BD = "oracle"; // oracle, mysql,
    // postgre

    // ademas se genera un ID especial para cada ejecucion/BD, para organizar
    // esa personalizacion

    // private final static String CUSTOM_ID =
    // "siga-viaticos-plan-viaje-concepto";
    // private final static String CUSTOM_ID = "plan_viaje_destino";

    private final static String RUTA_OUT_BASE = "D://jcfr/temp/generador/out/";
    private final static String RUTA_ARCHIVO_BEAN = CUSTOM_ID + ".java";
    private final static String RUTA_ARCHIVO_IBATIS = CUSTOM_ID + ".xml";

    private final static String COMILLAS = "\"";

    private static final JSUtil JS = JSUtil.JSUtil;

    public static void main(String[] args) {

        SimpleGenerador instance = new SimpleGenerador();

        try {

            List<Map<String, String>> metadata = instance.armarListaMetaData();

            instance.generarFileBean(metadata);
            instance.generarFileIbatisXML(metadata);

            System.out.println("fin del programa");

        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }

    }

    // USAR ESTE METODO PARA CUSTOMIZAR LA CREACION DE LOS CAMPOS DE LOS BEANS
    // SE INVOCA ANTES DE TOCA CONVERSION AUTOMATICA
    private String customField(String nombreCampoLowCase) {

        String s = nombreCampoLowCase;

        if (CUSTOM_ID.equals("plan_viaje_concepto") || CUSTOM_ID.equals("plan_viaje_destino")) {

            // personalizando los equals
            if (s.equals("user_crea")) {
                return "usuarioCreacion";
            }

            if (s.equals("fech_crea")) {
                return "fechaCreacion";
            }

            if (s.equals("user_modi")) {
                return "usuarioModificacion";
            }

            if (s.equals("fech_modi")) {
                return "fechaModificacion";
            }

            // prefijos
            if (s.startsWith("fech_")) {
                s = s.substring(5);
                s = "fecha_" + s;
            }

            if (s.startsWith("codi_")) {
                s = s.substring(5);
                s = "codigo_" + s;
            }

            if (s.startsWith("obs_")) {
                s = s.substring(4);
                s = "observacion_" + s;
            }

            if (s.startsWith("ind_")) {
                s = s.substring(4);
                s = "indicador_" + s;
            }

            if (s.startsWith("des_")) {
                s = s.substring(4);
                s = "descripcion_" + s;
            }

            if (s.startsWith("tip_")) {
                s = s.substring(4);
                s = "tipo_" + s;
            }

        }

        if (CUSTOM_ID.equals("plan_viaje")) {

            // personalizando los equals
            if (s.equals("plan_viaje_id")) {
                return "codPlanViaje";
            }
            if (s.equals("trabajador")) {
                return "codTrabajador";
            }
            if (s.equals("cod_planilla_viaje")) {
                return "codPlanilla";
            }

            if (s.equals("tip_mot_reem")) {
                return "tipoMotReembolso";
            }

            if (s.equals("mto_tope_viat_inter")) {
                return "montoTopeViaticoInternacional";
            }

            if (s.equals("user_crea")) {
                return "usuarioCreacion";
            }

            if (s.equals("fech_crea")) {
                return "fechaCreacion";
            }

            if (s.equals("user_modi")) {
                return "usuarioModificacion";
            }

            if (s.equals("fech_modi")) {
                return "fechaModificacion";
            }

            if (s.equals("cod_tip_mot_reem")) {
                return "codigoTipoMotReembolso";
            }

            if (s.equals("cod_estpago")) {
                return "codigoEstadoPago";
            }

            if (s.equals("cod_cargo_emp")) {
                return "codigoCargoEmpleado";
            }

            // prefijos
            if (s.startsWith("fech_")) {
                s = s.substring(5);
                s = "fecha_" + s;
            }

            if (s.startsWith("codi_")) {
                s = s.substring(5);
                s = "codigo_" + s;
            }

            if (s.startsWith("obs_")) {
                s = s.substring(4);
                s = "observacion_" + s;
            }

            if (s.startsWith("ind_")) {
                s = s.substring(4);
                s = "indicador_" + s;
            }

            if (s.startsWith("des_")) {
                s = s.substring(4);
                s = "descripcion_" + s;
            }

            if (s.startsWith("tip_")) {
                s = s.substring(4);
                s = "tipo_" + s;
            }

        }

        return s;
    }

    private String extraerCampoJava(String campoBD) {
        String s = StringUtils.trimToEmpty(campoBD).toLowerCase();

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

        return s;
    }

    private List<Map<String, String>> armarListaMetaData() throws Exception {

        List<String> campos = FileUtils.readLines(new File(RUTA_IN_BASE + RUTA_ARCHIVO_DDL_SCRIPT_BD));

        return procesar(campos);

    }

    private void generarFileBean(List<Map<String, String>> camposMap) throws Exception {

        if (camposMap == null) throw new Exception("camposMap cannot be null");

        StringBuilder sb = new StringBuilder(camposMap.size() * 20);

        // ARMAR LOS CAMPOS DEL BEAN
        int max = 0;
        if (INCLUIR_NOMBRE_CAMPO_COMO_COMENTARIO) {
            // moneria, solo para que el // quede igual espaciado
            for (Map<String, String> map : camposMap) {
                int actual = MapUtils.getString(map, "tipoJava", StringUtils.EMPTY).length() + MapUtils.getString(map, "campoJava", StringUtils.EMPTY).length();
                if (actual > max) {
                    max = actual;
                }
            }

            max = max + 5;
        }

        for (Map<String, String> map : camposMap) {

            sb.append("    private ");
            sb.append(MapUtils.getString(map, "tipoJava"));
            sb.append(" ");
            sb.append(MapUtils.getString(map, "campoJava"));
            sb.append(";");

            if (INCLUIR_NOMBRE_CAMPO_COMO_COMENTARIO) {

                int actual = MapUtils.getString(map, "tipoJava", StringUtils.EMPTY).length() + MapUtils.getString(map, "campoJava", StringUtils.EMPTY).length();
                sb.append(JS.REPLICATE(' ', Math.abs(max - actual)));
                sb.append("// ");
                sb.append(MapUtils.getString(map, "campoBD"));
            }

            sb.append(Constantes.ENTER_LINUX);

        }

        sb.append(Constantes.ENTER_LINUX);
        sb.append(Constantes.ENTER_LINUX);
        sb.append(Constantes.ENTER_LINUX);

        sb.append("     ");
        sb.append("SimpleDateFormat sdf = new SimpleDateFormat(");
        sb.append(COMILLAS);
        sb.append("dd/MM/yyyy");
        sb.append(COMILLAS);
        sb.append(");");
        sb.append(Constantes.ENTER_LINUX);
        sb.append(Constantes.ENTER_LINUX);

        // ARMAR seters
        for (Map<String, String> map : camposMap) {

            sb.append("     ");
            sb.append("obj.set");
            sb.append(MapUtils.getString(map, "campoJava").substring(0, 1).toUpperCase()).append(MapUtils.getString(map, "campoJava").substring(1));
            sb.append("(");

            String tipo = MapUtils.getString(map, "tipoJava", StringUtils.EMPTY).toLowerCase();
            switch (tipo) {
                case "double":
                    sb.append("Double.parseDouble(");
                    sb.append(MapUtils.getString(map, "campoJava"));
                    sb.append(")");
                    break;
                case "date":
                    sb.append("sdf.parse(");
                    sb.append(MapUtils.getString(map, "campoJava"));
                    sb.append(")");
                    break;
                case "long":
                    sb.append("Long.parseLong(");
                    sb.append(MapUtils.getString(map, "campoJava"));
                    sb.append(")");
                    break;
                default:
                    sb.append(MapUtils.getString(map, "campoJava"));
                    break;
            }

            sb.append(");");

            sb.append(Constantes.ENTER_LINUX);
        }

        FileUtils.write(new File(RUTA_OUT_BASE + RUTA_ARCHIVO_BEAN), sb.toString());

    }

    private void generarFileIbatisXML(List<Map<String, String>> camposMap) throws Exception {
        StringBuilder sb = new StringBuilder(camposMap == null ? 8 : camposMap.size() * 10);

        // RESULT MAP
        sb.append("<resultMap id=");
        sb.append(COMILLAS);
        sb.append("resultMapID");
        sb.append(COMILLAS);
        sb.append(" class=");
        sb.append(COMILLAS);
        sb.append("typeAliasClass");
        sb.append(COMILLAS);
        sb.append(">");
        sb.append(Constantes.ENTER_LINUX);

        for (Map<String, String> map : camposMap) {

            sb.append("    ");
            sb.append("<result property=");
            sb.append(COMILLAS);
            sb.append(MapUtils.getString(map, "campoJava"));
            sb.append(COMILLAS);
            sb.append(" column=");
            sb.append(COMILLAS);
            sb.append(MapUtils.getString(map, "campoBD"));
            sb.append(COMILLAS);
            sb.append(" />");

            sb.append(Constantes.ENTER_LINUX);
        }

        sb.append("</resultMap>");

        sb.append(Constantes.ENTER_LINUX);
        sb.append(Constantes.ENTER_LINUX);
        sb.append(Constantes.ENTER_LINUX);

        // UPDATE
        sb.append("<update id=");
        sb.append(COMILLAS);
        sb.append("updateNombreTabla");
        sb.append(COMILLAS);
        sb.append(" parameterClass=");
        sb.append(COMILLAS);
        sb.append("typeAliasClass");
        sb.append(COMILLAS);
        sb.append(">");
        sb.append(Constantes.ENTER_LINUX);

        sb.append("    ");
        sb.append("UPDATE").append(Constantes.ENTER_LINUX);
        sb.append("        ");
        sb.append("nombreTabla").append(Constantes.ENTER_LINUX);
        sb.append("    ");
        sb.append("SET").append(Constantes.ENTER_LINUX);

        for (Map<String, String> map : camposMap) {

            sb.append("        ");
            sb.append(MapUtils.getString(map, "campoBD"));
            sb.append(" = ");
            sb.append("#");
            sb.append(MapUtils.getString(map, "campoJava"));
            sb.append("#");
            sb.append(",");

            sb.append(Constantes.ENTER_LINUX);
        }
        sb.replace(sb.length() - 2, sb.length(), ""); // remover la coma final
        sb.append(Constantes.ENTER_LINUX);
        sb.append("</update>");

        sb.append(Constantes.ENTER_LINUX);
        sb.append(Constantes.ENTER_LINUX);
        sb.append(Constantes.ENTER_LINUX);

        // UPDATE SELECTIVE
        sb.append("<update id=");
        sb.append(COMILLAS);
        sb.append("updateNombreTablaSelective");
        sb.append(COMILLAS);
        sb.append(" parameterClass=");
        sb.append(COMILLAS);
        sb.append("typeAliasClass");
        sb.append(COMILLAS);
        sb.append(">");
        sb.append(Constantes.ENTER_LINUX);

        sb.append(" ");
        sb.append("UPDATE").append(Constantes.ENTER_LINUX);
        sb.append(" ");
        sb.append("nombreTabla").append(Constantes.ENTER_LINUX);
        sb.append(" ");
        sb.append("SET").append(Constantes.ENTER_LINUX);

        for (Map<String, String> map : camposMap) {

            sb.append("<isNotNull prepend=");
            sb.append(COMILLAS);
            sb.append(",");
            sb.append(COMILLAS);
            sb.append(" ");
            sb.append("property=");
            sb.append(COMILLAS);
            sb.append(MapUtils.getString(map, "campoJava"));
            sb.append(COMILLAS);
            sb.append(">");
            sb.append(" ");
            sb.append(MapUtils.getString(map, "campoBD"));
            sb.append(" ");
            sb.append("=");
            sb.append(" ");
            sb.append("#");
            sb.append(MapUtils.getString(map, "campoJava"));
            sb.append("#");
            sb.append(" ");
            sb.append("</isNotNull>");

            sb.append(Constantes.ENTER_LINUX);
        }
        sb.replace(sb.length() - 2, sb.length(), ""); // remover la coma final
        sb.append(Constantes.ENTER_LINUX);
        sb.append("</update>");

        sb.append(Constantes.ENTER_LINUX);
        sb.append(Constantes.ENTER_LINUX);
        sb.append(Constantes.ENTER_LINUX);

        // INSERT
        sb.append("<insert id=");
        sb.append(COMILLAS);
        sb.append("insertNombreTabla");
        sb.append(COMILLAS);
        sb.append(" parameterClass=");
        sb.append(COMILLAS);
        sb.append("typeAliasClass");
        sb.append(COMILLAS);
        sb.append(">");
        sb.append(Constantes.ENTER_LINUX);

        sb.append("    ");
        sb.append("INSERT INTO nombreTabla").append("(").append(Constantes.ENTER_LINUX);

        for (Map<String, String> map : camposMap) {

            sb.append("        ");
            sb.append(MapUtils.getString(map, "campoBD"));
            sb.append(",");

            sb.append(Constantes.ENTER_LINUX);
        }

        sb.replace(sb.length() - 2, sb.length(), ""); // remover la coma final

        sb.append(Constantes.ENTER_LINUX);
        sb.append("    ");
        sb.append(") VALUES (").append(Constantes.ENTER_LINUX);

        for (Map<String, String> map : camposMap) {

            sb.append("        ");
            sb.append("#");
            sb.append(MapUtils.getString(map, "campoJava"));
            sb.append("#");
            sb.append(",");

            sb.append(Constantes.ENTER_LINUX);
        }

        sb.replace(sb.length() - 2, sb.length(), ""); // remover la coma final
        sb.append(Constantes.ENTER_LINUX);

        sb.append("    ");
        sb.append(")");

        sb.append(Constantes.ENTER_LINUX);
        sb.append("</insert>");

        FileUtils.write(new File(RUTA_OUT_BASE + RUTA_ARCHIVO_IBATIS), sb.toString());

    }

    // CRUDCampoTablaClase
    private List<Map<String, String>> procesar(List<String> campos) {

        List<Map<String, String>> result = new ArrayList<>(campos == null ? 0 : campos.size());

        Map<String, String> itemResult;

        String linea;
        String campoBD;
        String campoJava;
        String tipoBD;
        String tipoJava;

        if (campos == null) throw new RuntimeException("campos cannot be null");

        for (String campo : campos) {

            itemResult = new LinkedHashMap<>();

            // procesar en este orden
            linea = preprocesarLinea(campo);

            if (StringUtils.isBlank(linea) || linea.endsWith("--ignorar")) continue;

            campoBD = extraerCampoBD(linea);
            tipoBD = extraetTipoBD(linea);
            tipoJava = extraerTipoJava(tipoBD, linea);

            campoJava = extraerCampoJava(campoBD);

            itemResult.put("campoBD", campoBD);
            itemResult.put("tipoBD", tipoBD);

            itemResult.put("campoJava", campoJava);
            itemResult.put("tipoJava", tipoJava);

            itemResult.put("linea", linea);

            result.add(itemResult);
        }

        return result;
    }

    private String preprocesarLinea(String linea) {
        return JS.quitarEspacios(StringUtils.trimToEmpty(linea));
    }

    private String extraerCampoBD(String linea) {
        // SE ASUME QUE LA LINEA SIEMPRE TIENE DATA Y ES EL NOMBRE DEL CAMPO
        return StringUtils.trimToEmpty(linea).split(" ")[0].replace(COMILLAS, "");
    }

    private String extraetTipoBD(String linea) {
        String lineaClean = StringUtils.trimToEmpty(linea).toUpperCase(); // recien
        // aqui lo pasa a upper
        // SE ASUME QUE LA LINEA SIEMPRE TIENE DATA
        String tipoBD = StringUtils.trimToEmpty(lineaClean).split(" ")[1];

        tipoBD = StringUtils.trimToEmpty(tipoBD);

        int posPar = tipoBD.indexOf('(');
        if (posPar >= 0) {
            tipoBD = StringUtils.substring(tipoBD, 0, posPar);
        }

        int posComa = tipoBD.indexOf(',');
        if (posComa >= 0) {
            tipoBD = StringUtils.substring(tipoBD, 0, posComa);
        }

        return StringUtils.trimToEmpty(tipoBD);
    }

    private String extraerTipoJava(String tipoBD, String linea) {
        String tipoJava = "";

        if (MOTOR_DE_BD.equals("oracle")) {

            if (tipoBD.equalsIgnoreCase("DATE")) {
                return "Date";
            }

            if (tipoBD.equalsIgnoreCase("VARCHAR2")) {
                return "String";
            }

            if (tipoBD.equalsIgnoreCase("CHAR")) {
                return "String";
            }

            if (tipoBD.equalsIgnoreCase("NUMBER")) {

                String tipoBDCompleto = StringUtils.trimToEmpty(linea).toUpperCase();
                // SE ASUME QUE LA LINEA SIEMPRE TIENE DATA
                tipoBDCompleto = StringUtils.trimToEmpty(tipoBDCompleto).split(" ")[1];

                if (tipoBDCompleto.contains(",")) { // sera sufi con esto? es
                    // double si tiene coma
                    // NUMBER(10,0)

                    String clean = tipoBDCompleto;
                    clean = clean.replace("NUMBER", "");
                    clean = clean.replace("(", "");
                    clean = clean.replace(")", "");

                    String[] cleanTrozo = clean.split(",");

                    if (cleanTrozo.length == 2) {
                        String digitoIzquierda = StringUtils.trimToEmpty(cleanTrozo[0]);
                        String digitoDerecha = StringUtils.trimToEmpty(cleanTrozo[1]);

                        if (NumberUtils.isNumber(digitoDerecha)) {
                            int digitoDerechaInt = JS.toInt(digitoDerecha);
                            // el ultimo digito no es cero, tiene decimales
                            if (digitoDerechaInt > 0) return "Double";

                            // examina el digito de la izquierda para ver si le
                            // pone Integer o Long
                            if (NumberUtils.isNumber(digitoIzquierda)) {
                                int digitoIzquierdaInt = JS.toInt(digitoIzquierda);

                                if (digitoIzquierdaInt <= 8) return "Integer";

                                return "Long";
                            }
                        }

                    }

                    return "Double";
                }

                return "Long";
            }

        }

        return tipoJava;
    }

    private String toFirstUpperCase(String cadena) {
        if (cadena == null || cadena.length() == 0) return cadena;
        if (cadena.length() == 1) return cadena.toUpperCase();

        return cadena.substring(0, 1).toUpperCase() + cadena.substring(1);
    }

    protected void print(Object ref) {
        if (ref instanceof List) {

            List refList = (List) ref;
            for (Object item : refList) {
                System.out.println(item);
            }
        }

        System.out.println(ref);
    }

}
