<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html manifest="app.manifest">
<head>
    <title>Toma de Inventario - SIMIO</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- frontend-meta.jsp - inicio -->
    <meta name="description" content="Toma de Inventario">
    <meta name="author" content="SimiOSPeru">
    <!-- frontend-meta.jsp - fin -->

    <!-- Bootstrap Core CSS -->
    <link href="libs/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" type="text/css">

    <!-- MetisMenu CSS -->
    <link href="libs/metisMenu/css/metisMenu.min.css" rel="stylesheet" type="text/css">

    <!-- Boostrap 3 DateTimepicker CSS -->
    <link href="libs/bootstrap3-datepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css">

    <!-- Custom Fonts CSS -->
    <link href="libs/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- DataTables CSS -->
    <link href="libs/datatables/DataTables-1.10.12/css/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="libs/datatables/Responsive-2.1.0/css/responsive.bootstrap.min.css" rel="stylesheet" type="text/css">

    <!-- SideBar CSS -->
    <link href="css/sidebar.css" rel="stylesheet" type="text/css">
    <link href="css/sidebar-style.css" rel="stylesheet" type="text/css">

    <!-- Custom CSS -->
    <link href="css/comunes.css" rel="stylesheet" type="text/css">

</head>

<body>
    <!-- barra de navegacion superior fija -->
    <div class="navbar navbar-static navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">

                <!-- boton que aparece en la izquierda -->
                <button type="button" class="navbar-toggle toggle-left hidden-md hidden-lg" data-toggle="sidebar" data-target=".sidebar-left">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>

                <!-- front-header-main.jsp - inicio -->
                <a class="navbar-brand" href="#" title="Sistema de Inventario"><strong>Sistema de Inventario <em>SimiOS</em></strong></a>
                <!-- front-header-main.jsp - fin -->

            </div>

            <button type="button" class="navbar-toggle toggle-right" data-toggle="sidebar" data-target=".sidebar-right">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>

        </div>
    </div>

    <!-- inicio del contenido -->
    <div class="container-fluid">
        <div class="row">

            <!-- menu de la izquierda - inicio -->
            <div class="col-xs-6 col-sm-3 col-md-2 sidebar sidebar-left sidebar-animate sidebar-md-show">
                <ul class="nav navbar-stacked">
                    <li class="active">
                        <button id="btn-menu-izq-listado-bienes" class="btn btn-md btn-info btn-block botonMenu" title="Mesas"><i class="fa fa-table fa-3x"></i><br />Listado Bienes</button>
                    </li>
                    <li>
                        <button id="btn-menu-izq-nuevo-bien" class="btn btn-md btn-primary btn-block botonMenu" title="Nuevo Bien"><i class="fa fa-plus-circle fa-3x"></i><br />Nuevo Bien</button>
                    </li>
                    <li>
                        <button id="btn-menu-izq-editar-bien" class="btn btn-md btn-warning btn-block botonMenu" title="Editar Bien"><i class="fa fa-pencil fa-3x"></i><br />Editar Bien</button>
                    </li>
                    <li>
                        <button id="btn-menu-izq-quitar-bien" class="btn btn-md btn-danger btn-block botonMenu" title="Quitar Bien"><i class="fa fa-remove fa-3x"></i><br />Quitar Bien</button>
                    </li>
                    <li>
                        <button id="btn-menu-izq-ver-bien" class="btn btn-md btn-info btn-block botonMenu" title="Ver bien"><i class="fa fa-columns fa-3x"></i><br />Ver Bien</button>
                    </li>
                    <li>
                        <button id="btn-menu-izq-resumen-toma" class="btn btn-md btn-success btn-block botonMenu" title="Resumen Toma de Inventario"><i class="fa fa-list-alt fa-3x"></i><br />Resumen</button>
                    </li>

                </ul>
            </div>
            <!-- menu de la izquierda - fin -->

            <!-- contenido del centro: header-second + contenido de paginas - inicio -->
            <div class="col-md-10 col-md-offset-2">

                <!-- frontend-header-second.jsp - inicio -->
                <div class="row">
                    <div class="col-md-8 col-md-offset-4">
                        <h4>
                            <span id="div-header-inventariador" class="label label-primary" title="Inventariador">usuario</span>
                            <span id="div-header-empresa" class="label label-danger" title="Empresa">entidad</span>
                            <span id="div-header-inventario" class="label label-warning" title="Modo offline">Modo Offline</span>
                        </h4>
                    </div>
                </div>

                <div>&nbsp;</div>
                <!-- frontend-header-second.jsp - fin -->

                <div class="row">
                    <div class="col-md-12">

                        <!-- contenido de paginas -->
                        <div id="page-wrapper" class="contenedor-app">

                            <div id="div-main-app">

                                <!-- div-vista-listado INICIO -->
                                <div id="div-vista-listado">
                                    <!-- TODO/FIXME: aun sin vista -->
                                    llenar vista div-vista-listado
                                </div>
                                <!-- div-vista-listado FIN -->


                                <!-- div-vista-registro INICIO -->
                                <div id="div-vista-registro">

                                    <!-- seleccion del tipo de bien - inicio -->
                                    <div class="row">
                                        <div class="col-sm-12 col-md-12">
                                            <fieldset>
                                                <legend>SELECCI&Oacute;N DEL TIPO DE BIEN</legend>

                                                <div class="row">
                                                    <div class="col-sm-6 col-md-6">
                                                        <div class="form-group">
                                                            <label class="control-label">Escriba el nombre del bien a buscar</label>
                                                            <input id="txt_xxxx" name="txt_xxxx" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-6 col-md-6">
                                                        <div class="form-group">
                                                            <label class="control-label">&nbsp;</label>
                                                            <select multiple class="form-control input-sm" id="cbo_catalogo_bien" name="cbo_catalogo_bien">

                                                            </select>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </fieldset>
                                        </div>
                                    </div>
                                    <!-- seleccion del tipo de bien - fin -->

                                    <!-- datos del bien - inicio -->
                                    <div class="row">
                                        <div class="col-sm-12 col-md-12">
                                            <fieldset>
                                                <legend>DATOS DEL BIEN</legend>

                                                <div class="row">
                                                    <div class="col-sm-12 col-md-12">
                                                        <div class="form-group">
                                                            <label class="control-label">Denominaci&oacute;n</label>
                                                            <input readOnly id="txt_denominacion" name="txt_denominacion" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-8 col-md-8">
                                                        <div class="form-group">
                                                            <label class="control-label">Grupo Gen&eacute;rico</label>
                                                            <input readOnly id="txt_grupo_generico" name="txt_grupo_generico" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Clase</label>
                                                            <input readOnly id="txt_clase" name="txt_clase" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">C&oacute;digo Patrimonial</label>
                                                            <input readOnly id="txt_codigo_patrimonial" name="txt_codigo_patrimonial" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">C&oacute;digo Interno</label>
                                                            <input readOnly id="txt_codigo_interno" name="txt_codigo_interno" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Ult. Correlativo</label>
                                                            <input readOnly id="txt_ult_correlativo" name="txt_ult_correlativo" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Tipo de cuenta</label>
                                                            <select readOnly id="cbo_tipo_cuenta" name="cbo_tipo_cuenta" class="form-control input-sm">
                                                                <option value="P">DE USO PRIVADO</option>
                                                                <option value="E">DE USO ESTATAL</option>
                                                            </select>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">&nbsp;</label>
                                                            <div class="radio">
                                                                <label>
                                                                    <input id="rb_activo_fijo" type="radio" name="rb_tipo_cuenta" value="AF" checked>
                                                                    <strong>Activo Fijo</strong>
                                                                </label>
                                                            </div>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">&nbsp;</label>
                                                            <div class="radio">
                                                                <label>
                                                                    <input id="rb_cuenta_orden" type="radio" name="rb_tipo_cuenta" value="CO" checked>
                                                                    <strong>Cuenta de Orden</strong>
                                                                </label>
                                                            </div>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Cuenta contable</label>
                                                            <select readOnly id="cbo_cuenta_contable" name="cbo_cuenta_contable" class="form-control input-sm">
                                                            </select>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-8 col-md-8">
                                                        <div class="form-group">
                                                            <label class="control-label">&nbsp;</label>
                                                            <input readOnly id="txt_des_cuenta_contable" name="txt_des_cuenta_contable" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Forma de Adquisici&oacute;n</label>
                                                            <select readOnly id="cbo_forma_adquisicion" name="cbo_forma_adquisicion" class="form-control input-sm">
                                                            </select>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Fecha</label>
                                                            <input readOnly id="txt_fecha_adquisicion" name="txt_fecha_adquisicion" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Resol. Alta</label>
                                                            <input readOnly id="txt_resol_alta" name="txt_resol_alta" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Valor de Adquisici&oacute;n</label>
                                                            <input readOnly id="txt_valor_adquisicion" name="txt_valor_adquisicion" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Valor Neto</label>
                                                            <input readOnly id="txt_valor_neto" name="txt_valor_neto" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">&nbsp;</label>
                                                            <div class="checkbox">
                                                                <label>
                                                                    <input id="chk_asegurado" name="chk_asegurado" type="checkbox" value="S">
                                                                    <strong>Asegurado</strong>
                                                                </label>
                                                            </div>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Estado del Bien</label>
                                                            <select readOnly id="cbo_estado_bien" name="cbo_estado_bien" class="form-control input-sm">
                                                                <option value="#">SELECCIONE ESTADO DEL BIEN</option>
                                                                <option value="N">MUY BUENO</option>
                                                                <option value="B">BUENO</option>
                                                                <option value="R">REGULAR</option>
                                                                <option value="M">MALO</option>
                                                            </select>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-8 col-md-8">
                                                        <div class="form-group">
                                                            <label class="control-label">Usuario</label>
                                                            <select readOnly id="cbo_usuario" name="cbo_usuario" class="form-control input-sm">
                                                            </select>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-8 col-md-8">
                                                        <div class="form-group">
                                                            <label class="control-label">Local</label>
                                                            <select readOnly id="cbo_local" name="cbo_local" class="form-control input-sm">
                                                            </select>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-8 col-md-8">
                                                        <div class="form-group">
                                                            <label class="control-label">&Aacute;rea</label>
                                                            <select readOnly id="cbo_area" name="cbo_area" class="form-control input-sm">
                                                            </select>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Oficina</label>
                                                            <select readOnly id="cbo_oficina" name="cbo_oficina" class="form-control input-sm">
                                                            </select>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                            </fieldset>
                                        </div>
                                    </div>
                                    <!-- datos del bien - fin -->

                                    <!-- detalle tecnico y otros - inicio -->
                                    <div class="row">
                                        <div class="col-sm-10 col-md-10">
                                            <fieldset>
                                                <legend>DETALLE T&Eacute;CNICO</legend>

                                                <div class="row">
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Marca</label>
                                                            <input readOnly id="txt_marca" name="txt_marca" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Modelo</label>
                                                            <input readOnly id="txt_modelo" name="txt_modelo" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Tipo</label>
                                                            <input readOnly id="txt_tipo" name="txt_tipo" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Color</label>
                                                            <input readOnly id="txt_color" name="txt_color" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Serie</label>
                                                            <input readOnly id="txt_serie" name="txt_serie" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Placa</label>
                                                            <input readOnly id="txt_placa" name="txt_placa" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">N&deg; Motor</label>
                                                            <input readOnly id="txt_numero_motor" name="txt_numero_motor" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">N&deg; Chasis</label>
                                                            <input readOnly id="txt_numero_chasis" name="txt_numero_chasis" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Dimension</label>
                                                            <input readOnly id="txt_dimension" name="txt_dimension" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Año</label>
                                                            <input readOnly id="txt_anyo" name="txt_anyo" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Raza</label>
                                                            <input readOnly id="txt_raza" name="txt_raza" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Nombre</label>
                                                            <input readOnly id="txt_nombre" name="txt_nombre" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-4 col-md-4">
                                                        <div class="form-group">
                                                            <label class="control-label">Edad</label>
                                                            <input readOnly id="txt_edad" name="txt_edad" type="text" maxlength="50" class="form-control input-sm">
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-sm-12 col-md-12">
                                                        <div class="form-group">
                                                            <label class="control-label">Otros</label>
                                                            <textarea readOnly id="txt_otros" name="txt_otros" maxlength="200" class="form-control input-sm" rows="4"></textarea>
                                                            <span class="help-block"></span>
                                                        </div>
                                                    </div>
                                                </div>

                                            </fieldset>
                                        </div>

                                        <div class="col-sm-2 col-md-2">
                                            <fieldset>
                                                <legend>OTROS</legend>
                                                <button disabled="true" id="btn_otros" class="btn btn-sm btn-primary btn-block" title="Otros" data-toggle="modal" data-target="#divModalOtros"><i class="fa fa-file-o fa-4x"></i></button>
                                            </fieldset>
                                        </div>
                                    </div>
                                    <!--detalle tecnico y otros - fin -->

                                    <!-- botones - inicio -->
                                    <div class="row">
                                        <div class="col-sm-12 col-md-12">
                                            <div class="panel panel-default">

                                                <div class="panel-body">

                                                    <div class="row">

                                                        <div class="col-sm-3 col-md-2 col-sm-offset-1 col-md-offset-3">
                                                            <button id="bnt_grabar" class="btn btn-primary btn-block" title="Grabar">Grabar</button>
                                                        </div>

                                                        <div class="col-sm-3 col-md-2">
                                                            <button id="btn_cancelar" class="btn btn-primary btn-block" title="Cancelar">Cancelar</button>
                                                        </div>

                                                        <div class="col-sm-3 col-md-2">
                                                            <button id="btn_salir" class="btn btn-primary btn-block" title="Salir">Salir</button>
                                                        </div>

                                                    </div>

                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                    <!-- botones - fin -->

                                    <div class="row"> &nbsp; </div>

                                    <!-- popups - inicio -->
                                    <div id="divModalOtros" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
                                        <div class="modal-dialog modal-lg" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <strong>Datos T&eacute;cnicos - Otros</strong>
                                                </div>
                                                <div class="modal-body">
                                                    <!-- contenido del modal - inicio -->
                                                    <fieldset>
                                                        <legend>DATOS T&Eacute;CNICOS - DATOS GENERALES</legend>

                                                        <div class="row">
                                                            <div class="col-sm-12 col-md-12">
                                                                <div class="form-group">
                                                                    <label class="control-label">Marca/Fabricante</label>
                                                                    <input id="txt_DatosTecnicos_marcaFabricante" name="txt_marcaFabricante" type="text" maxlength="50" class="form-control input-sm">
                                                                    <span class="help-block"></span>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-sm-6 col-md-6">
                                                                <div class="form-group">
                                                                    <label class="control-label">Modelo</label>
                                                                    <input id="txt_DatosTecnicos_modelo" name="txt_DatosTecnicos_modelo" type="text" maxlength="50" class="form-control input-sm">
                                                                    <span class="help-block"></span>
                                                                </div>
                                                            </div>
                                                            <div class="col-sm-6 col-md-6">
                                                                <div class="form-group">
                                                                    <label class="control-label">Tipo</label>
                                                                    <input id="txt_DatosTecnicos_Tipo" name="txt_DatosTecnicos_Tipo" type="text" maxlength="50" class="form-control input-sm">
                                                                    <span class="help-block"></span>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-sm-6 col-md-6">
                                                                <div class="form-group">
                                                                    <label class="control-label">N&uacute;mero de Matr&iacute;cula</label>
                                                                    <input id="txt_DatosTecnicos_NumeroMatricula" name="txt_DatosTecnicos_NumeroMatricula" type="text" maxlength="50" class="form-control input-sm">
                                                                    <span class="help-block"></span>
                                                                </div>
                                                            </div>
                                                            <div class="col-sm-6 col-md-6">
                                                                <div class="form-group">
                                                                    <label class="control-label">A&ntilde;o de Fabricaci&oacute;n</label>
                                                                    <input id="txt_DatosTecnicos_AnyoFabricacion" name="txt_DatosTecnicos_AnyoFabricacion" type="text" maxlength="50" class="form-control input-sm">
                                                                    <span class="help-block"></span>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-sm-6 col-md-6">
                                                                <div class="form-group">
                                                                    <label class="control-label">N&uacute;mero de Serie</label>
                                                                    <input id="txt_DatosTecnicos_NumeroSerie" name="txt_DatosTecnicos_NumeroSerie" type="text" maxlength="50" class="form-control input-sm">
                                                                    <span class="help-block"></span>
                                                                </div>
                                                            </div>
                                                            <div class="col-sm-6 col-md-6">
                                                                <div class="form-group">
                                                                    <label class="control-label">Dimensiones</label>
                                                                    <input id="txt_DatosTecnicos_Dimensiones" name="txt_DatosTecnicos_Dimensiones" type="text" maxlength="50" class="form-control input-sm">
                                                                    <span class="help-block"></span>
                                                                </div>
                                                            </div>
                                                        </div>


                                                    </fieldset>
                                                    <!-- contenido del modal - final -->
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-primary" data-dismiss="modal">Aceptar</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- popups - fin -->

                                </div>
                                <!-- div-vista-registro FIN -->


                                <!-- div-vista-ver INICIO -->
                                <div id="div-vista-ver">
                                    <!-- TODO/FIXME: aun sin vista -->
                                    llenar vista div-vista-ver
                                </div>
                                <!-- div-vista-ver FIN -->


                                <!-- div-vista-resumen INICIO -->
                                <div id="div-vista-resumen">
                                    <!-- TODO/FIXME: aun sin vista -->
                                    llenar vista div-vista-resumen
                                </div>
                                <!-- div-vista-resumen FIN -->

                                <!-- div-vista-conexion INICIO -->
                                <div id="div-vista-conexion">
                                    <!-- TODO/FIXME: aun sin vista -->
                                    llenar vista div-vista-conexion
                                </div>
                                <!-- div-vista-conexion FIN -->

                            </div>

                        </div>

                    </div>
                </div>

            </div>
            <!-- contenido del centro: header-second + contenido de paginas - fin -->

            <!-- menu de la derecha - inicio -->
            <div class="col-xs-6 col-sm-3 col-md-2 sidebar sidebar-right sidebar-animate">
                <ul class="nav navbar-stacked">
                    <li class="active">
                        <button id="btn-menu-der-ir-modo-online" class="btn btn-md btn-primary btn-block botonMenu" title="Ir a Modo Online"><i class="fa fa-toggle-on fa-3x"></i><br />Ir a Modo Online</button>
                    </li>
                    <li>
                        <button id="btn-menu-der-revisar-conexion" class="btn btn-md btn-warning btn-block botonMenu" title="Revisar Conexi&oacute;n a Internet"><i class="fa fa-wifi fa-3x"></i><br />Revisar Conexi&oacute;n</button>
                    </li>
                </ul>
            </div>
            <!-- menu de la derecha - fin -->

            <!-- forms ocultos -->
            <div id="hiddenForms">
                <form id="form-ir-modo-online" action="sincronizar.htm" method="POST" role="form">
                    <input type="hidden" name="action" value="mostrarSincronizarInventario">
                    <input type="hidden" name="usuario" value="">
                    <input type="hidden" name="entidad" value="">
                    <input type="hidden" name="inventario" value="">
                    <input type="hidden" name="token" value="">
                </form>
            </div>

            <!-- popups generales: mensajes y confirmacion -->
            <div id="divModalPopup" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
                <div id="divPopupContainerClass" class="container appMsgConfirmContainer verticalAlignmentHelper">
                    <div class="row verticalAlignCenter">
                        <div class="col-xs-12">
                            <div id="divPopupPanelClass" class="panel panel-info">
                                <div class="panel-heading" id="divPopupMensaje">Mensaje</div>
                                <div class="panel-body">
                                    <div class="text-center">
                                        <button type="button" id="btnPopupAceptar" class="btn btn-primary btn-sm" data-dismiss="modal">Aceptar</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id="divModalPopupSINO" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
                <div id="divPopupContainerClassSINO" class="container appMsgConfirmContainer verticalAlignmentHelper">
                    <div class="row verticalAlignCenter">
                        <div class="col-xs-12">
                            <div id="divPopupPanelClassSINO" class="panel panel-info">
                                <div class="panel-heading" id="divPopupMensajeSINO">Mensaje</div>
                                <div class="panel-body">
                                    <div class="row">
                                        <div class="col-xs-5 col-xs-offset-1">
                                            <button type="button" id="btnPopupAceptarSINO" class="btn btn-primary btn-sm btn-block" data-dismiss="modal" title="Si">SI</button>
                                        </div>
                                        <div class="col-xs-5">
                                            <button type="button" id="btnPopupCancelarSINO" class="btn btn-primary btn-sm btn-block" data-dismiss="modal" title="No">NO</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <!-- jQuery -->
    <script src="libs/jquery/1.12.1/jquery.min.js"></script>
    <script src="libs/jquery-inputmask/jquery.inputmask.bundle.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="libs/bootstrap/3.3.6/js/bootstrap.min.js"></script>

    <!-- Side Bar JavaScript -->
    <script src="js/sidebar.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="libs/metisMenu/js/metisMenu.min.js"></script>

    <!-- Boostrap 3 DateTimepicker JavaScript -->
    <script src="libs/momentjs/moment-with-locales.min.js"></script>
    <script src="libs/bootstrap3-datepicker/js/bootstrap-datetimepicker.min.js"></script>

    <!-- App Custom JavaScript -->
	<script src="js/simioapp-default.js"></script>
    <script src="js/jquery-simiosdb.js"></script>
    <script src="js/simiosdb.js"></script>
    <script src="js/simioapp-global.js"></script>
    <script src="js/simioapp-config.js"></script>
    <script src="js/simioapp-dao.js"></script>
    <script src="js/simioapp-view.js"></script>
	<script src="js/simioapp.js"></script>
    <script src="js/simioapp-listado.js"></script>
    <script src="js/simioapp-registro.js"></script>
    <script src="js/simioapp-listado.js"></script>
    <script src="js/simioapp-listado.js"></script>
    <script src="js/simioapp-listado.js"></script>
    <script src="js/simioapp-listado.js"></script>

    <!-- DataTables JavaScript -->
    <script type="text/javascript" src="libs/datatables/DataTables-1.10.12/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="libs/datatables/DataTables-1.10.12/js/dataTables.bootstrap.min.js"></script>
    <script type="text/javascript" src="libs/datatables/Responsive-2.1.0/js/dataTables.responsive.min.js"></script>
    <script type="text/javascript" src="libs/datatables/Responsive-2.1.0/js/responsive.bootstrap.min.js"></script>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="libs/adaptative/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="libs/adaptative/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script type="text/javascript">
    window.addEventListener('load', function(e) {
		
    	window.applicationCache.addEventListener('updateready', function(e) {
    		//Si hubo cambios en el manifiesto
        	if (window.applicationCache.status == window.applicationCache.UPDATEREADY) {
    			//Sustituira la antigua cache por la nueva
                window.applicationCache.swapCache();
                //if (confirm('A new version of this site is available. Load it?')) {
                    window.location.reload();
                //}
        	} else {
            // Si no hubo cambios en el manifiesto
        	}
    	}, false);
    }, false);
    
	$(document).ready(function() {
		initApp();
	});

</script>

</body>

</html>