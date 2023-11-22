package com.tresjotas.restobar.controladores.base;

import com.jcfr.utiles.DateTime;
import com.jcfr.utiles.ListaItem;
import com.jcfr.utiles.enums.JExceptionEnum;
import com.jcfr.utiles.files.JFUtil;
import com.jcfr.utiles.listas.JLUtil;
import com.jcfr.utiles.math.Alea;
import com.jcfr.utiles.string.JSUtil;
import com.jcfr.utiles.web.ComboWeb;
import com.tresjotas.restobar.comunes.beans.UploadParams;
import com.tresjotas.restobar.comunes.exceptions.RestobarException;
import com.tresjotas.restobar.servicios.CatalogoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// import java.util.logging.Logger;

public class BaseAdminController extends MultiActionController {

    // para java util logging
    // private static final Logger log = Logger.getLogger(BaseController.class.getName());

    private static final Logger log = LogManager.getLogger(BaseAdminController.class);

    protected static final JSUtil JS = JSUtil.JSUtil;
    protected static final JFUtil JF = JFUtil.JFUtil;
    protected static final JLUtil JL = JLUtil.JLUtil;

    @Autowired
    @Qualifier("catalogoService")
    protected CatalogoService catalogoService;

    protected HttpSession invalidarSession(HttpServletRequest request, boolean create) {

        // operacion safely y quietly
        HttpSession session = null;

        try {

            // con solo llamar a esta url crea una session si no la hay, e invalida la existente
            session = request == null ? null : request.getSession(create);
            if (session != null) {

                try {
                    session.setAttribute(Constantes.USUARIO_SESSION_NAME, null);
                } catch (Exception sos) {
                }

                try {
                    session.removeAttribute(Constantes.USUARIO_SESSION_NAME);
                } catch (Exception sos) {
                }

                try {
                    session.invalidate();
                } catch (Exception sos) {
                }

            }

        } catch (Exception sos) {

        }

        return session;
    }

    protected UsuarioSession getUsuarioSession(HttpServletRequest request) throws Exception {

        UsuarioSession usuarioSession = (UsuarioSession) WebUtils.getSessionAttribute(request, Constantes.USUARIO_SESSION_NAME);

        // FIXME: esperar al sistema de autenticacion/autorizacion que va hacer luna
        if (usuarioSession == null) {

            // TODO/FIXME: aqui se implementa un dummy para recoger el usuario
            usuarioSession = UsuarioSessionImpl.getDefaultUser();

            WebUtils.setSessionAttribute(request, Constantes.USUARIO_SESSION_NAME, usuarioSession);
        }

        if (usuarioSession == null) {
            throw new SimioException("BC-GUS-918", "Usuario no tiene credenciales válidas");
        }

        return usuarioSession;
    }

    public ModelAndView cargarTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return handleCargar(request, true);
    }

    public ModelAndView cargarPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return handleCargar(request, false);
    }

    protected Object getSessionAttribute(HttpServletRequest request, String name) {
        return WebUtils.getSessionAttribute(request, name);
    }

    protected void setSessionAttribute(HttpServletRequest request, String name, Object value) {
        WebUtils.setSessionAttribute(request, name, value);
    }

    protected String handleMsgError(String codigoError, Exception sos) {

        return handleMsgError(true, codigoError, sos);
    }

    protected String handleMsgError(boolean incluirCodigoError, String codigoError, Exception sos) {

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

        return msgError;
    }


    protected String handleJSONError(String codigoError, DataJsonBean dataJsonBean, Exception sos) {

        boolean esValidacion = sos instanceof SimioException
                && JExceptionEnum.VALIDACION.equals(((SimioException) sos).getTipo());

        String msgError = handleMsgError(!esValidacion, codigoError, sos);

        if (esValidacion) {
            // error validacion
            dataJsonBean.setRespuesta("errorValidacion", msgError, ((SimioException) sos).getInfo());
        } else {
            // error caso normal
            dataJsonBean.setRespuesta("error", msgError, null);
        }

        return msgError;
    }

    protected String getHeaderAcceso(HttpServletResponse response) {

        // TODO/FIXME: REVISAR MUY BIEN ESTO, PERMITIRIA ACCEDER POR AJAX DESDE UN DOMINIO QUE NO ES EL NUESTRO
        // headers
        // "Access-Control-Allow-Origin"
        // "Access-Control-Allow-Methods"
        // "Access-Control-Allow-Headers"
        // "Access-Control-Max-Age"

        String header = response.getHeader("Access-Control-Allow-Origin");

        return StringUtils.trimToEmpty(header);
    }

    protected String getOrigenCliente(HttpServletRequest request) {
        String clientOrigin = request.getHeader("origin");

        return StringUtils.trimToEmpty(clientOrigin);
    }

    protected String getIPCliente(HttpServletRequest request) {

        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr(); // 0:0:0:0:0:0:0:1
        }

        return StringUtils.trimToEmpty(ipAddress);
    }

    protected ModelAndView handleJSONResponse(DataJsonBean dataJsonBean, HttpServletResponse response) throws Exception {

        response.setContentType("text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");

        String dataJsonString = new ObjectMapper().writeValueAsString(dataJsonBean);

        PrintWriter writer = response.getWriter();

        if (writer != null) {
            writer.write(dataJsonString);
            // writer.close();
        }

        return null;
    }

    protected ModelAndView handleModelAndView(Map<String, Object> model) {
        // PRE: model cannot be null and must contain _next attribute
        String _nextView = MapUtils.getString(model, "_view");

        return new ModelAndView(_nextView, model);
    }

    protected ModelAndView handleErrorModelAndView(Map<String, Object> model, String msgError, String plantilla) {

        Map<String, Object> result = model;

        if (result == null) {
            result = new HashMap<>();
            // model.put("_view", plantilla);
        }

        result.put("_module", null);
        result.put("_msgError", msgError);

        setVistaTemplate(result, "error", null, plantilla);

        return handleModelAndView(result);
    }

    protected void setVistaTemplate(Map<String, Object> model, String pagina, String modulo, String plantilla) {
        setNavigationParams(model, pagina, modulo, plantilla, true);
    }

    protected void setVistaPage(Map<String, Object> model, String pagina, String modulo, String plantilla) {
        setNavigationParams(model, pagina, modulo, plantilla, false);
    }

    private void setNavigationParams(Map<String, Object> model, String pagina, String modulo, String plantilla, boolean useTemplateAsView) {

        // PRE: pagina no puede ser null, plantilla puede ser null pero solo si
        // useTemplateAsView es false
        if (JS._vacio(pagina)) throw new IllegalArgumentException("pagina no puede ser null");
        if (useTemplateAsView && JS._vacio(plantilla)) {
            throw new IllegalArgumentException("plantilla no puede ser null");
        }

        String _view;
        String _page = JS.toBlank(pagina);
        String _module = JS.toBlank(modulo);
        String _template = JS.toBlank(plantilla);

        if (useTemplateAsView) {
            _view = _template;
        } else {
            if (!JS._vacio(_template)) {
                if (!JS._vacio(_module)) {
                    _view = _template + "/" + _module + "/" + _page;
                } else {
                    _view = _template + "/" + _page;
                }
            } else {
                // template puede ser vacio
                if (!JS._vacio(_module)) {
                    _view = _module + "/" + _page;
                } else {
                    _view = _page;
                }
            }
        }

        // setear atributos de navegacion, vista es la siguiente vista
        if (!JS._vacio(_page)) model.put("_page", _page);
        if (!JS._vacio(_module)) model.put("_module", _module);
        if (!JS._vacio(_template)) model.put("_template", _template);
        if (!JS._vacio(_view)) model.put("_view", _view);

    }

    private ModelAndView handleCargar(HttpServletRequest request, boolean useTemplateAsView) throws Exception {

        // INFO: solo busca los atributos en el request y armar el _nextView
        String _page = JS.toBlank(request.getParameter("_page"));
        String _module = JS.toBlank(request.getParameter("_module"));
        String _template = JS.toBlank(request.getParameter("_template"));

        HashMap<String, Object> model = new HashMap<>();

        // usar page
        setNavigationParams(model, _page, _module, _template, useTemplateAsView);

        return handleModelAndView(model);
    }

    protected DateTime setCamposAuditoria(HttpServletRequest request, AuditoriaFields entity, boolean insert) throws Exception {

        DateTime now = DateTime.getNow();

        UsuarioSession usuarioSession = getUsuarioSession(request);

        entity.setFechaAct(JS.toDate(now));
        entity.setUsuAct(usuarioSession.getUsuarioSessionID());

        if (insert) {
            entity.setFechaReg(JS.toDate(now));
            entity.setUsuReg(usuarioSession.getUsuarioSessionID());
        }

        return now;
    }

    private ComboWeb crearCombo(List<ListaItem> items, boolean agregerItemSeleccione, boolean agregarItemTodos) throws Exception {

        ComboWeb comboWeb = new ComboWeb(items);

        if (CollectionUtils.isNotEmpty(items)) {

            if (agregerItemSeleccione) {
                comboWeb.addItemSelIni(Constantes.COMBO_SELECCIONE_VALUE, Constantes.COMBO_SELECCIONE_LABEL);
            }

            if (agregarItemTodos) {
                comboWeb.addItemSelIni(Constantes.COMBO_TODOS_VALUE, Constantes.COMBO_TODOS_LABEL);
            }

        }

        return comboWeb;
    }

    protected ComboWeb cargarCombo(HttpServletRequest request, String nombreCombo, List<ListaItem> items, boolean agregerItemSeleccione, boolean agregarItemTodos) throws Exception {

        ComboWeb comboWeb = crearCombo(items, agregerItemSeleccione, agregarItemTodos);

        request.setAttribute(nombreCombo, comboWeb);

        return comboWeb;
    }

    protected ComboWeb cargarCombo(HttpServletRequest request, String nombreCombo, List<ListaItem> items, boolean agregerItemSeleccione, boolean agregarItemTodos, Object valorSeleccionado) throws Exception {

        ComboWeb comboWeb = crearCombo(items, agregerItemSeleccione, agregarItemTodos);

        comboWeb.setSelID(valorSeleccionado);

        request.setAttribute(nombreCombo, comboWeb);

        return comboWeb;
    }

    protected ComboWeb cargarComboSeleccione(HttpServletRequest request, String nombreCombo, List<ListaItem> items) throws Exception {

        ComboWeb comboWeb = crearCombo(items, true, false);

        request.setAttribute(nombreCombo, comboWeb);

        return comboWeb;
    }

    protected ComboWeb cargarComboSeleccione(HttpServletRequest request, String nombreCombo, List<ListaItem> items, Object valorSeleccionado) throws Exception {

        ComboWeb comboWeb = crearCombo(items, true, false);

        comboWeb.setSelID(valorSeleccionado);

        request.setAttribute(nombreCombo, comboWeb);

        return comboWeb;
    }

    protected ComboWeb cargarComboTodos(HttpServletRequest request, String nombreCombo, List<ListaItem> items) throws Exception {

        ComboWeb comboWeb = crearCombo(items, false, true);

        request.setAttribute(nombreCombo, comboWeb);

        return comboWeb;
    }

    protected ComboWeb cargarComboTodos(HttpServletRequest request, String nombreCombo, List<ListaItem> items, Object valorSeleccionado) throws Exception {

        ComboWeb comboWeb = crearCombo(items, false, true);

        comboWeb.setSelID(valorSeleccionado);

        request.setAttribute(nombreCombo, comboWeb);

        return comboWeb;
    }

    protected ComboWeb cargarComboCatalogo(HttpServletRequest request, String nombreCombo, String catalogo, boolean agregerItemSeleccione, boolean agregarItemTodos) throws Exception {

        // trae los items de un catalogo
        List<ListaItem> items = catalogoService.selectCatalogoCombo(catalogo, true, true);

        // y crea el combo segun los items adicionales
        ComboWeb comboWeb = crearCombo(items, agregerItemSeleccione, agregarItemTodos);

        request.setAttribute(nombreCombo, comboWeb);

        return comboWeb;
    }

    protected ComboWeb cargarComboCatalogoSeleccione(HttpServletRequest request, String nombreCombo, String catalogo) throws Exception {

        // trae los items de un catalogo
        List<ListaItem> items = catalogoService.selectCatalogoCombo(catalogo, true, true);

        // y crea el combo segun los items adicionales
        ComboWeb comboWeb = crearCombo(items, true, false);

        request.setAttribute(nombreCombo, comboWeb);

        return comboWeb;
    }

    protected ComboWeb cargarComboCatalogoSeleccione(HttpServletRequest request, String nombreCombo, String catalogo, Object valorSeleccionado) throws Exception {

        // trae los items de un catalogo
        List<ListaItem> items = catalogoService.selectCatalogoCombo(catalogo, true, true);

        // y crea el combo segun los items adicionales
        ComboWeb comboWeb = crearCombo(items, true, false);

        // seleccionar un valor
        comboWeb.setSelID(valorSeleccionado);

        request.setAttribute(nombreCombo, comboWeb);

        return comboWeb;
    }

    protected ComboWeb cargarComboCatalogoTodos(HttpServletRequest request, String nombreCombo, String catalogo) throws Exception {

        // trae los items de un catalogo
        List<ListaItem> items = catalogoService.selectCatalogoCombo(catalogo, true, true);

        // y crea el combo segun los items adicionales
        ComboWeb comboWeb = crearCombo(items, false, true);

        request.setAttribute(nombreCombo, comboWeb);

        return comboWeb;
    }

    protected ComboWeb cargarComboCatalogoTodos(HttpServletRequest request, String nombreCombo, String catalogo, Object valorSeleccionado) throws Exception {

        // trae los items de un catalogo
        List<ListaItem> items = catalogoService.selectCatalogoCombo(catalogo, true, true);

        // y crea el combo segun los items adicionales
        ComboWeb comboWeb = crearCombo(items, false, true);

        comboWeb.setSelID(valorSeleccionado);

        request.setAttribute(nombreCombo, comboWeb);

        return comboWeb;
    }

    protected ComboWeb cargarCombo(String catalogo) throws Exception {

        List<ListaItem> items = catalogoService.selectCatalogoCombo(catalogo, true, true);

        // y crea el combo segun los items adicionales
        return crearCombo(items, false, false);
    }


    protected Map<String, Object> handleMultipartForm(HttpServletRequest request, UploadParams params) throws Exception {
        return handleMultipartForm(request, params, true, false);
    }

    protected Map<String, Object> handleMultipartForm(HttpServletRequest request, UploadParams params, boolean escribirEnDisco, boolean incluirBytesEnResult) throws Exception {

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) throw new RestobarException("RCC-001", "Formulario no es multipart");

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // maximum size that will be stored in memory
        factory.setSizeThreshold(params.getMaxMemSize());

        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File(params.getRutaUploadTemp()));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // maximum file size to be uploaded.
        upload.setSizeMax(params.getMaxFileSize());

        // Parse the request to get file items.
        List<FileItem> fileItems = upload.parseRequest(request);

        // Process the uploaded file items
        Iterator<FileItem> iterator = fileItems.iterator();

        // ruta donde se guardaran los ficheros adjuntos, debe terminar en /
        String filePath = params.getRutaUploadFiles();

        // mapa resultado con los parametros del request
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        while (iterator.hasNext()) {

            FileItem fi = (FileItem) iterator.next();
            if (fi.isFormField()) {
                // en campo normal de formulario
                result.put(fi.getFieldName(), fi.getString("UTF-8"));

            } else {

                // atributo name del control
                String fieldName = fi.getFieldName();

                // nombre del archivo, puede ser vacio si no subio nada
                String fileNameOriginal = new String(fi.getName().getBytes(), "UTF-8");

                // String contentType = fi.getContentType(); // application/octet-stream
                // boolean isInMemory = fi.isInMemory(); // siempre es true
                // long sizeInBytes = fi.getSize();

                // si no hay nombre de archivo es que no adjunto nada en el <input type="file"
                if (!JS._vacio(fileNameOriginal)) {

                    if (escribirEnDisco) {

                        String ext = FilenameUtils.getExtension(fileNameOriginal);

                        // crea un nombre de archivo unico: nombre campo para tipar archivo + NRO_UNICO_TIEMPO_Y_RANDOM
                        String fileNameGenerado = Alea.newNumerosID(fieldName + "-");

                        if (!JS._vacio(ext)) {
                            fileNameGenerado = fileNameGenerado + "." + ext;
                        }

                        fi.write(new File(filePath + fileNameGenerado));

                        // incluir en el result el filename original
                        result.put(fieldName + "_fileNameORI", fileNameOriginal);

                        // incluir en el result el filename generado
                        result.put(fieldName + "_fileNameGEN", fileNameGenerado);

                        // incluir en el result la ruta donde se guardo el archivo
                        result.put(fieldName + "_pathFileGEN", filePath + fileNameGenerado);

                        // incluir en el result como nombre de campo la ruta del archivo generado
                        result.put(fieldName, result.get(fieldName + "_pathFileGEN"));
                    }

                    if (incluirBytesEnResult) {
                        // incluir en el result el inputstream
                        result.put(fieldName + "_inputStream", IOUtils.toByteArray(fi.getInputStream()));
                    }

                }

            }

        }

        return result;
    }
}
