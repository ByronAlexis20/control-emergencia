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

import com.emergencia.model.dao.BarrioDAO;
import com.emergencia.model.entity.Barrio;

public class BarrioEditar {
	@Wire private Window winBarrioEditar;
	@Wire private Textbox txtBarrio;
	@Wire private Checkbox chkEstado;
	@Wire private Row rowEstado;
	Barrio barrio;
	BarrioDAO barrioDAO = new BarrioDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		chkEstado.setChecked(true);
		// Recupera el objeto pasado como parametro. 
		barrio = (Barrio) Executions.getCurrent().getArg().get("Barrio");
		if (barrio == null) {
			barrio = new Barrio();
			barrio.setEstado("A");
			rowEstado.setVisible(false);
		}else {
			if(barrio.getEstado().equals("A")) {
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
						barrioDAO.getEntityManager().getTransaction().begin();
						if(chkEstado.isChecked())
							barrio.setEstado("A");
						else
							barrio.setEstado("I");
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
			if(barrio != null) {
				if(barrio.getIdBarrio() == null) {
					List<Barrio> lista = this.barrioDAO.buscarPorNombre(txtBarrio.getValue());
					if(lista.size() > 0) {
						Clients.showNotification("Nombre de barrio ya existe 1","info",txtBarrio,"end_center",2000);
						return retorna;
					}
				}else {
					List<Barrio> lista = this.barrioDAO.buscarPorNombreDiferenteId(txtBarrio.getValue(), barrio.getIdBarrio());
					System.out.println(barrio.getIdBarrio());
					if(lista.size() > 0) {
						Clients.showNotification("Nombre de barrio ya existe 2","info",txtBarrio,"end_center",2000);
						return retorna;
					}
				}
			}
			if(barrio.getIdBarrio() != null) {
				if(!chkEstado.isChecked()) {
					Barrio br = barrioDAO.buscarPorId(barrio.getIdBarrio());
					if(br != null) {
						if(br.getEmergencias().size() > 0) {
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
	public Barrio getBarrio() {
		return barrio;
	}

	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}
	
}
