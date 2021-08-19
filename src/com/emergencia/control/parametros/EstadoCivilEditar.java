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

import com.emergencia.model.dao.EstadoCivilDAO;
import com.emergencia.model.entity.EstadoCivil;

public class EstadoCivilEditar {
	@Wire private Window winEstadoEditar;
	@Wire private Textbox txtEstado;
	EstadoCivil estadoCivil;
	EstadoCivilDAO estadoDAO = new EstadoCivilDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		estadoCivil = (EstadoCivil) Executions.getCurrent().getArg().get("EstadoCivil");
		if (estadoCivil == null) {
			estadoCivil = new EstadoCivil();
			estadoCivil.setEstado("A");
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
						estadoDAO.getEntityManager().getTransaction().begin();			
						if (estadoCivil.getIdEstadoCivil() == null) {
							estadoDAO.getEntityManager().persist(estadoCivil);
						}else{
							estadoCivil = (EstadoCivil) estadoDAO.getEntityManager().merge(estadoCivil);
						}			
						estadoDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						estadoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "EstadoCivil.buscarPorPatron", null);
		winEstadoEditar.detach();
	}
	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtEstado.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el nombre del estado civil","info",txtEstado,"end_center",2000);
				txtEstado.setFocus(true);
				return retorna;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}
	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
}