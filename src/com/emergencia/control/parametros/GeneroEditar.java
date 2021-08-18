package com.emergencia.control.parametros;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.GeneroDAO;
import com.emergencia.model.entity.Genero;

public class GeneroEditar {
	@Wire private Window winGeneroEditar;
	@Wire private Textbox txtGenero;
	Genero genero;
	GeneroDAO generoDAO = new GeneroDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		genero = (Genero) Executions.getCurrent().getArg().get("Genero");
		if (genero == null) {
			genero = new Genero();
			genero.setEstado("A");
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar(){
		if(isValidarDatos() == true) {
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {						
						generoDAO.getEntityManager().getTransaction().begin();			
						if (genero.getIdGenero() == null) {
							generoDAO.getEntityManager().persist(genero);
						}else{
							genero = (Genero) generoDAO.getEntityManager().merge(genero);
						}			
						generoDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						generoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Genero.buscarPorPatron", null);
		winGeneroEditar.detach();
	}
	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtGenero.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el nombre del genero","info",txtGenero,"end_center",2000);
				txtGenero.setFocus(true);
				return retorna;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public Genero getGenero() {
		return genero;
	}
	public void setGenero(Genero genero) {
		this.genero = genero;
	}

}
