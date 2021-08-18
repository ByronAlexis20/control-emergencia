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

import com.emergencia.model.dao.CondicionLlegadaDAO;
import com.emergencia.model.entity.CondicionLlegada;

public class CondicionLlegadaEditar {
	@Wire private Window winCondicionEditar;
	@Wire private Textbox txtCondicion;
	CondicionLlegada condicion;
	CondicionLlegadaDAO condicionDAO = new CondicionLlegadaDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		condicion = (CondicionLlegada) Executions.getCurrent().getArg().get("Condicion");
		if (condicion == null) {
			condicion = new CondicionLlegada();
			condicion.setEstado("A");
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
						condicionDAO.getEntityManager().getTransaction().begin();			
						if (condicion.getIdCondicionLlegada() == null) {
							condicionDAO.getEntityManager().persist(condicion);
						}else{
							condicion = (CondicionLlegada) condicionDAO.getEntityManager().merge(condicion);
						}			
						condicionDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						condicionDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "CondicionLlegada.buscarPorPatron", null);
		winCondicionEditar.detach();
	}
	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtCondicion.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar condicion de llegada","info",txtCondicion,"end_center",2000);
				txtCondicion.setFocus(true);
				return retorna;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public CondicionLlegada getCondicion() {
		return condicion;
	}
	public void setCondicion(CondicionLlegada condicion) {
		this.condicion = condicion;
	}

}
