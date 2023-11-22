<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="divModalPopup" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
    <div id="divPopupContainerClass" class="container appMsgConfirmContainer verticalAlignmentHelper">
        <div class="row verticalAlignCenter">
            <div class="col-xs-12">
                <div id="divPopupPanelClass" class="panel panel-info">
                    <div class="panel-heading" id="divPopupMensaje">Mensaje</div>
                    <div class="panel-body">
                        <div class="text-center">
                            <button type="button" id="btnPopupAceptar" class="btn btn-primary btn-md" data-dismiss="modal">Aceptar</button>
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
                                <button type="button" id="btnPopupAceptarSINO" class="btn btn-primary btn-md btn-block" data-dismiss="modal" title="Si">SI</button>
                            </div>
                            <div class="col-xs-5">
                                <button type="button" id="btnPopupCancelarSINO" class="btn btn-primary btn-md btn-block" data-dismiss="modal" title="No">NO</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
