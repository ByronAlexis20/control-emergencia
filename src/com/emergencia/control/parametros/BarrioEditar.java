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

import com.emergencia.model.dao.BarrioDAO;
import com.emergencia.model.entity.Barrio;

public class BarrioEditar {
	@Wire private Window winBarrioEditar;
	@Wire private Textbox txtBarrio;
	Barrio barrio;
	BarrioDAO barrioDAO = new BarrioDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		barrio = (Barrio) Executions.getCurrent().getArg().get("Barrio");
		if (barrio == null) {
			barrio = new Barrio();
			barrio.setEstado("A");
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
						barrioDAO.getEntityManager().getTransaction().begin();			
						if (barrio.getIdBarrio() == null) {
							barrioDAO.getEntityManager().persist(barrio);
						}else{
							barrio = (Barrio) barrioDAO.getEntityManager().merge(barrio);
						}			
						barrioDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						barrioDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Barrio.buscarPorPatron", null);
		winBarrioEditar.detach();
	}
	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtBarrio.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el nombre del barrio","info",txtBarrio,"end_center",2000);
				txtBarrio.setFocus(true);
				return retorna;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public Barrio getBarrio() {
		return barrio;
	}

	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}
	
}
