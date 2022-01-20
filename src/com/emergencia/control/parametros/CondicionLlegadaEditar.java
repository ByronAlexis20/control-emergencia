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

import com.emergencia.model.dao.CondicionLlegadaDAO;
import com.emergencia.model.entity.CondicionLlegada;

public class CondicionLlegadaEditar {
	@Wire private Window winCondicionEditar;
	@Wire private Textbox txtCondicion;
	@Wire private Checkbox chkEstado;
	@Wire private Row rowEstado;
	CondicionLlegada condicion;
	CondicionLlegadaDAO condicionDAO = new CondicionLlegadaDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		chkEstado.setChecked(true);
		// Recupera el objeto pasado como parametro. 
		condicion = (CondicionLlegada) Executions.getCurrent().getArg().get("Condicion");
		if (condicion == null) {
			condicion = new CondicionLlegada();
			condicion.setEstado("A");
			rowEstado.setVisible(false);
		}else {
			if(condicion.getEstado().equals("A")) {
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
							condicion.setEstado("A");
						else
							condicion.setEstado("I");
						
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
			
			if(condicion != null) {
				if(condicion.getIdCondicionLlegada() == null) {
					List<CondicionLlegada> lista = this.condicionDAO.buscarPorNombre(txtCondicion.getValue());
					if(lista.size() > 0) {
						Clients.showNotification("Nombre de condición ya existe","info",txtCondicion,"end_center",2000);
						return retorna;
					}
				}else {
					List<CondicionLlegada> lista = this.condicionDAO.buscarPorNombreDiferenteId(txtCondicion.getValue(), condicion.getIdCondicionLlegada());
					if(lista.size() > 0) {
						Clients.showNotification("Nombre de condición ya existe","info",txtCondicion,"end_center",2000);
						return retorna;
					}
				}
			}
			
			if(condicion.getIdCondicionLlegada() != null) {
				if(!chkEstado.isChecked()) {
					CondicionLlegada c = condicionDAO.buscarPorId(condicion.getIdCondicionLlegada());
					if(c != null) {
						if(c.getPrehospitalarias().size() > 0) {
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
	public CondicionLlegada getCondicion() {
		return condicion;
	}
	public void setCondicion(CondicionLlegada condicion) {
		this.condicion = condicion;
	}

}
