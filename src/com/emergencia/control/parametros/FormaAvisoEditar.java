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

import com.emergencia.model.dao.FormaAvisoDAO;
import com.emergencia.model.entity.FormaAviso;

public class FormaAvisoEditar {
	@Wire private Window winFormaAvisoEditar;
	@Wire private Textbox txtForma;
	FormaAviso formaAviso;
	FormaAvisoDAO formaDAO = new FormaAvisoDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		formaAviso = (FormaAviso) Executions.getCurrent().getArg().get("FormaAviso");
		if (formaAviso == null) {
			formaAviso = new FormaAviso();
			formaAviso.setEstado("A");
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
						formaDAO.getEntityManager().getTransaction().begin();			
						if (formaAviso.getIdFormaAviso() == null) {
							formaDAO.getEntityManager().persist(formaAviso);
						}else{
							formaAviso = (FormaAviso) formaDAO.getEntityManager().merge(formaAviso);
						}			
						formaDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						formaDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "FormaAviso.buscarPorPatron", null);
		winFormaAvisoEditar.detach();
	}
	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtForma.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar forma de aviso","info",txtForma,"end_center",2000);
				txtForma.setFocus(true);
				return retorna;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public FormaAviso getFormaAviso() {
		return formaAviso;
	}
	public void setFormaAviso(FormaAviso formaAviso) {
		this.formaAviso = formaAviso;
	}
}
