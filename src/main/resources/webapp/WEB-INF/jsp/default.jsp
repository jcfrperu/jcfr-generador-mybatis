<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- definir el folder de la plantilla --%>
<c:choose>
	<c:when test="${empty _template}">
		<c:set var="_folder" value="default" />
	</c:when> 
	<c:otherwise>
		<c:set var="_folder" value="${_template}" />
	</c:otherwise>
</c:choose>

<!DOCTYPE html>
<html lang="es">
<head>

	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<c:choose>
		<c:when test="${empty _meta}">
			<c:import url="${_folder}/default-meta.jsp" />
		</c:when>
		<c:otherwise>
			<c:import url="${_folder}/${_meta}.jsp" />
		</c:otherwise>
	</c:choose>

    <title>Sistema de Administraci&oacute;n - SIMIO</title>

    <!-- Bootstrap Core CSS -->
    <link href="libs/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="libs/metisMenu/css/metisMenu.min.css" rel="stylesheet">
    
    <!-- Boostrap 3 DateTimepicker CSS -->
    <link href="libs/bootstrap3-datepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet">

    <!-- App Custom CSS -->
    <link href="css/simioapp-template.css" rel="stylesheet">
    
    <!-- Custom Fonts CSS -->
    <link href="libs/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

	<!-- DataTables CSS -->
	<link rel="stylesheet" type="text/css" href="libs/datatables/DataTables-1.10.12/css/dataTables.bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="libs/datatables/Responsive-2.1.0/css/responsive.bootstrap.min.css"/>	

	<c:choose>
		<c:when test="${empty _css}">
			<c:import url="${_folder}/default-css.jsp" />
		</c:when>
		<c:otherwise>
			<c:import url="${_folder}/${_css}.jsp" />
		</c:otherwise>
	</c:choose>

	<!-- jQuery -->
	<script src="libs/jquery/1.12.1/jquery.min.js"></script>
	<script src="libs/jquery-inputmask/jquery.inputmask.bundle.min.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="libs/bootstrap/3.3.6/js/bootstrap.min.js"></script>

	<!-- Metis Menu Plugin JavaScript -->
	<script src="libs/metisMenu/js/metisMenu.min.js"></script>

	<!-- Boostrap 3 DateTimepicker JavaScript -->
	<script src="libs/momentjs/moment-with-locales.min.js"></script>
	<script src="libs/bootstrap3-datepicker/js/bootstrap-datetimepicker.min.js"></script>

	<!-- App Custom JavaScript -->
	<script src="js/simioapp-template.js"></script>
	<script src="js/simioapp-default.js"></script>
	
	<!-- DataTables JavaScript -->
	<script type="text/javascript" src="libs/datatables/DataTables-1.10.12/js/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="libs/datatables/DataTables-1.10.12/js/dataTables.bootstrap.min.js"></script>
	<script type="text/javascript" src="libs/datatables/Responsive-2.1.0/js/dataTables.responsive.min.js"></script>
	<script type="text/javascript" src="libs/datatables/Responsive-2.1.0/js/responsive.bootstrap.min.js"></script>    

	<c:choose>
		<c:when test="${empty _js}">
			<c:import url="${_folder}/default-js.jsp" />
		</c:when>
		<c:otherwise>
			<c:import url="${_folder}/${_js}.jsp" />
		</c:otherwise>
	</c:choose>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="libs/adaptative/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="libs/adaptative/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <div id="wrapper">

        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">

			<div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
				<h4>&nbsp;&nbsp;
					<%--
					<img src="image/simioapp-logo-medium.png" alt="" class="img-rounded">
					 --%>
					<span class="naranja-simioapp">INVENTARIO</span> &nbsp;<small>SIMIOAPP</small>
				</h4>
				<!--
				<a class="navbar-brand naranja-simioapp" href="#"><span><img src="image/logo.png" width='31' height='27' ></span> Registro de corredores de Aseguramiento Universal de Salud</a>
				-->
            </div>

			<%-- icono para acciones de usuario (superior - derecha). pero tenia un bug al achicar pantalla, baja y se pega a la izquierda no mostraando todo el texto de 'cerrar session'
            <ul class="nav navbar-top-links navbar-right latizator">
				<li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-user fa-fw"></i>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
						<!--
                        <li>
							<a href="contenido-cambio-clave.html"><i class="fa fa-user fa-fw"></i> Cambiar contrase&ntilde;a</a>
						</li>
                        <li class="divider"></li>
						-->
                        <li>
							<a href="admin.htm?action=cerrarSesion"><i class="fa fa-sign-out fa-fw"></i> Cerrar sesi&oacute;n</a>
                        </li>
                    </ul>
                </li>
            </ul>
            --%>

            <div class="navbar-default sidebar latizator" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                    	<li>
                    		<a href="#"><i class="fa fa-wrench fa-fw"></i> Mantenimientos<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                 <li>
		                            <a href="registro-catalogo.htm?action=mostrarBuscar" title="Registro de Cat&aacute;logos">Registro de Cat&aacute;logos </a>
		                        </li>
		                        <li>
		                            <a href="registro-parametro.htm?action=mostrarBuscar" title="Registro de Par&aacute;metros">Registro de Par&aacute;metros </a>
		                        </li>
		                        <li>
		                            <a href="registro-catalogobien.htm?action=mostrarBuscar" title="Registro de Cat&aacute;logo Bien">Registro de Cat&aacute;logo Bien </a>
		                        </li>
								<li>
									<a href="registro-ubigeo.htm?action=mostrarBuscar" title="Registro de Ubigeos">Registro de Ubigeos</a>
								</li>
								<li>
									<a href="registro-dependencia.htm?action=mostrarBuscar" title="Registro de Dependencias">Registro de Dependencias</a>
								</li>
								<li>
									<a href="registro-entidad.htm?action=mostrarBuscar" title="Registro de Entidades">Registro de Entidades</a>
								</li>
		                        <li>
		                            <a href="registro-locales.htm?action=mostrarBuscar" title="Registro de Locales">Registro de Locales</a>
		                        </li>
								<li>
									<a href="registro-area.htm?action=mostrarBuscar" title="Registro de &Aacute;reas">Registro de &Aacute;reas</a>
								</li>
								<li>
									<a href="registro-oficina.htm?action=mostrarBuscar" title="Registro de Oficinas">Registro de Oficinas</a>
								</li>
								<li>
		                            <a href="registro-empleado.htm?action=mostrarBuscar" title="Registro de Empleados">Registro de Empleados</a>
		                        </li>
								<li>
									<a href="registro-ubicacion-empleado.htm?action=mostrarBuscar" title="Registro Ubicaci&oacute;n de Empleados">Registro Ubicaci&oacute;n Empleados</a>
								</li>
		                        <li>
		                            <a href="registro-clase.htm?action=mostrarBuscar" title="Registro de Clases">Registro de Clases</a>
		                        </li>
								<li>
		                            <a href="registro-grupo.htm?action=mostrarBuscar" title="Registro de Grupos">Registro de Grupos</a>
		                        </li>
		                        <li>
		                            <a href="registro-grupoclase.htm?action=mostrarBuscar" title="Registro de Grupo/Clase">Registro de Grupo/Clase</a>
		                        </li>
		                        <li>
		                            <a href="registro-cuenta.htm?action=mostrarBuscar" title="Registro de Cuentas">Registro de Cuentas</a>
		                        </li>
		                        <li>
                                    <a href="registro-inventario.htm?action=mostrarBuscar" title="Registro de Inventarios">Registro de Inventarios</a>
                                </li>
                            </ul>
                    	</li>
                    	<li>
                    		<a href="#"><i class="fa fa-tasks fa-fw"></i> Sincronizaci&oacute;n<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                            	<li>
		                            <a href="generacion-token.htm?action=mostrarPanelToken" title="Ir al Panel Tokens"><i class="fa fa-database fa-fw"></i> Panel Token </a>
		                        </li>
                            </ul>
                            <ul class="nav nav-second-level">
                            	<li>
		                            <a href="importacion-zip.htm?action=mostrarPanelImportarZIP" title="Ir a Importar Archivo ZIP"><i class="fa fa-file-archive-o"></i> Importar Archivo ZIP </a>
		                        </li>
                            </ul>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="sincronizar.htm?action=mostrarSincronizarInventario" title="Ir a Sincronizar Inventario"><i class="fa fa-cloud-upload"></i> Sincronizar Inventario </a>
                                </li>
                            </ul>
                    	</li>
                    	<li>
                            <a href="#"><i class="fa fa-tasks fa-fw"></i> Inventario <span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="apertura-inventario.htm?action=mostrarPanelApertura" title="Mostrar Panel Apertura de Inventario"> Apertura de Inventario </a>
                                </li>
                            </ul>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="asignacion-inventario.htm?action=mostrarPanelAsignacion" title="Mostrar Panel Asignaci&oacute;n de Inventario"> Asignaci&oacute;n de Inventario </a>
                                </li>
                            </ul>
							<ul class="nav nav-second-level">
								<li>
									<a href="sobrante-faltante-inventario.htm?action=mostrarPanelSobranteFaltante" title="Mostrar Panel Sobrantes / Faltantes de Inventario"> Sobrantes y Faltantes </a>
								</li>
							</ul>
							<ul class="nav nav-second-level">
								<li>
									<a href="estado-subida-inventario.htm?action=mostrarPanelEstadoSubida" title="Mostrar Panel Estado Subida de Inventario"> Estado de Subidas </a>
								</li>
							</ul>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="cierre-inventario.htm?action=mostrarPanelCierre" title="Mostrar Panel Cierre de Inventario"> Cierre de Inventario </a>
                                </li>
                            </ul>
                        </li>
                    	<li>
                            <a href="#"><i class="fa fa-tasks fa-fw"></i> Exportaci&oacute;n<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="exportar-excel.htm?action=mostrarExportarFormatoExcel" title="Ir a exportar Formato Excel 2015"><i class="fa fa-database fa-fw"></i> Formato Excel 2015 </a>
                                </li>
                            </ul>
                        </li>
						<li>
							<a href="admin.htm?action=cerrarSesion"><i class="fa fa-sign-out fa-fw"></i> Cerrar sesi&oacute;n</a>
						</li>

                    </ul>
                </div>
            </div>
        </nav>

		<div id="page-wrapper" class="contenedor-app">
			
			<%-- en este punto, _template nunca viene vacio --%>
			<c:choose>
				<c:when test="${empty _module}">
					<c:choose>
						<c:when test="${empty _page}">
							<c:import url="${_folder}/default-page.jsp" />	
						</c:when>
						<c:otherwise>
							<c:import url="${_folder}/${_page}.jsp" />
						</c:otherwise>
					</c:choose> 
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${empty _page}">
							<c:import url="${_folder}/${_module}/default-page.jsp" />
						</c:when>
						<c:otherwise>
							<c:import url="${_folder}/${_module}/${_page}.jsp" />
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose> 

		</div>

	</div>

</body>

</html>
