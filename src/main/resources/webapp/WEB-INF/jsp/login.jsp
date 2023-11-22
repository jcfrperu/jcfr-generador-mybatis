<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="es">
<head>

	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Sistema de Inventario SimiOS - Administrador</title>

    <!-- Bootstrap Core CSS -->
    <link href="libs/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">

    <!-- App Custom CSS -->
    <link href="css/simioapp-template.css" rel="stylesheet">
    
    <!-- Custom Fonts CSS -->
    <link href="libs/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

	<!-- jQuery -->
	<script src="libs/jquery/1.12.1/jquery.min.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="libs/bootstrap/3.3.6/js/bootstrap.min.js"></script>

	<!-- App Custom JavaScript -->
	<script src="js/simioapp-template.js"></script>
	<script src="js/simioapp-default.js"></script>
	
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="libs/adaptative/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="libs/adaptative/respond/1.4.2/respond.min.js"></script>
    <![endif]-->    

</head>

<body>
 
    <div class="container">
        <div class="row">
            <div class="col-sm-8 col-md-6 col-sm-offset-2 col-md-offset-3">
                <div class="login-panel panel panel-info">
                    <div class="panel-heading">
                        <h2 class="panel-title"><strong>Sistema de Inventario</strong> &nbsp;&nbsp;<small>INGRESO AL SISTEMA</small></h2>
                    </div>
                    <div class="panel-body">
                    
                        <form role="form" action="admin.htm?action=login" method="POST">

								<div class="form-group">
									<label>EMPRESA</label>
                                    <input class="form-control input-sm" title="C&oacute;digo Empresa" name="empresa" type="text" autofocus>
									<span class="help-block"></span>
                                </div>

                                <div class="form-group">
									<label>USUARIO</label>
                                    <input class="form-control input-sm" title="C&oacute;digo Usuario" name="usuario" type="text">
									<span class="help-block"></span>
                                </div>
                               
                                <div class="form-group">
									<label>PASSWORD</label>
                                    <input class="form-control input-sm" title="Password" name="password" type="password">
									<span class="help-block"></span>
                                </div>
                                
                                <div class="form-group">
	                                <div class="row">
		                                <div class="col-sm-4 col-md-4 col-sm-offset-4 col-md-offset-4">
	                           	    		<button class="btn btn-md btn-primary btn-block" title="Ingresar">INGRESAR</button>
	                                	</div>
	                                </div>
                                </div>                                
                                
                                <c:if test="${requestScope.hayValidacion == 'si'}">
	                                <div class="form-group">
										 <div class="alert alert-danger alert-dismissible" role="alert">
										  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
										  <strong>${requestScope.msgGeneral}</strong> ${requestScope.msgDetalle}
										</div>
									</div>
                                </c:if>
                                
                        </form>
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- a la simple visualizacion de esta pagina elimina los datos de la session -->
    <c:remove var="usuario_session" scope="session" />
 
</body>

</html>
