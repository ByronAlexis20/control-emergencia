package com.emergencia.control.emergencia;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.EmergenciaDAO;
import com.emergencia.model.entity.Emergencia;

public class RegistroEmergencia {
	@Wire private Listbox lstEmergencias;
	List<Emergencia> listaEmergencia;
	String textoBuscar;
	
	EmergenciaDAO emergenciaDAO = new EmergenciaDAO();
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		buscar();
	}
	
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/forms/emergencias/nuevaEmergencia.zul", null, null);
		ventanaCargar.doModal();
	}
	
	@GlobalCommand("Emergencia.findAll")
	@Command
	@NotifyChange({"listaEmergencia"})
	public void buscar(){
		if (listaEmergencia != null) {
			listaEmergencia = null; 
		}
		listaEmergencia = emergenciaDAO.obtenerEmergencias();
		if(listaEmergencia.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@Command
	public void editar(@BindingParam("emergencia") Emergencia em){
		if(em == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Emergencia", em);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/emergencias/nuevaEmergencia.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("emergencia") Emergencia em){
		if (em == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						emergenciaDAO.getEntityManager().getTransaction().begin();
						em.setEstado("I");
						emergenciaDAO.getEntityManager().merge(em);
						emergenciaDAO.getEntityManager().getTransaction().commit();
						buscar();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						emergenciaDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}
	public List<Emergencia> getListaEmergencia() {
		return listaEmergencia;
	}
	public void setListaEmergencia(List<Emergencia> listaEmergencia) {
		this.listaEmergencia = listaEmergencia;
	}
	public String getTextoBuscar() {
		return textoBuscar;
	}
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
	
}
