package com.emergencia.control.parametros;

import java.util.List;

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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.FormaAvisoDAO;
import com.emergencia.model.entity.FormaAviso;

public class FormaAvisoEditar {
	@Wire private Window winFormaAvisoEditar;
	@Wire private Textbox txtForma;
	@Wire private Checkbox chkEstado;
	@Wire private Row rowEstado;
	FormaAviso formaAviso;
	FormaAvisoDAO formaDAO = new FormaAvisoDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		chkEstado.setChecked(true);
		// Recupera el objeto pasado como parametro. 
		formaAviso = (FormaAviso) Executions.getCurrent().getArg().get("FormaAviso");
		if (formaAviso == null) {
			formaAviso = new FormaAviso();
			formaAviso.setEstado("A");
			rowEstado.setVisible(false);
		}else {
			if(formaAviso.getEstado().equals("A")) {
				chkEstado.setChecked(true);
			}else {
				chkEstado.setChecked(false);
			}
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
						if(chkEstado.isChecked())
							formaAviso.setEstado("A");
						else
							formaAviso.setEstado("I");
						
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
			if(formaAviso != null) {
				if(formaAviso.getIdFormaAviso() == null) {
					List<FormaAviso> lista = this.formaDAO.buscarPorNombre(txtForma.getValue());
					if(lista.size() > 0) {
						Clients.showNotification("Nombre de forma de aviso ya existe","info",txtForma,"end_center",2000);
						return retorna;
					}
				}else {
					List<FormaAviso> lista = this.formaDAO.buscarPorNombreDiferenteId(txtForma.getValue(), formaAviso.getIdFormaAviso());
					if(lista.size() > 0) {
						Clients.showNotification("Nombre de forma de aviso ya existe","info",txtForma,"end_center",2000);
						return retorna;
					}
				}
			}
			
			if(formaAviso.getIdFormaAviso() != null) {
				if(!chkEstado.isChecked()) {
					FormaAviso forma = formaDAO.buscarPorId(formaAviso.getIdFormaAviso());
					if(forma != null) {
						if(forma.getEmergencias().size() > 0) {
							Clients.showNotification("No se puede eliminar el registro, hay registros que dependen de éste.");
							return retorna;
						}
					}
				}
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
