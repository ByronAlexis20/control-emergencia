package com.emergencia.control;

import java.io.IOException;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;

public class Index {
	
	//private UsuarioDAO usuarioDAO = new UsuarioDAO();

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		principal();
	}

	private void principal() {
		//Usuario objeto  = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername().trim()); 		
		//if(objeto.isCambioClave() == false) {
			Executions.getCurrent().sendRedirect("/menuPrincipal.zul");
		//}else {
		//	Executions.sendRedirect("/Mantenimiento/usuarios/cambioClave.zul");
		//}
	}
}