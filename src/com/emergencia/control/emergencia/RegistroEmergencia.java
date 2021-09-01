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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.EmergenciaDAO;
import com.emergencia.model.dao.MesDAO;
import com.emergencia.model.entity.Emergencia;
import com.emergencia.model.entity.Me;

public class RegistroEmergencia {
	@Wire Listbox lstEmergencias;
	@Wire Textbox txtDia;
	@Wire Textbox txtAnio;
	@Wire Combobox cboMes;
	List<Emergencia> listaEmergencia;
	MesDAO mesDAO = new MesDAO();
	Me mesSeleccionado;
	
	EmergenciaDAO emergenciaDAO = new EmergenciaDAO();
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		cargarTodasEmergencias();
	}
	
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/forms/emergencias/nuevaEmergencia.zul", null, null);
		ventanaCargar.doModal();
	}
	
	@GlobalCommand("Emergencia.findAll")
	@Command
	@NotifyChange({"listaEmergencia"})
	public void cargarTodasEmergencias(){
		if (listaEmergencia != null) {
			listaEmergencia = null; 
		}
		listaEmergencia = emergenciaDAO.obtenerEmergencias();
		if(listaEmergencia.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@GlobalCommand("Emergencia.buscarPorFecha")
	@Command
	@NotifyChange({"listaEmergencia"})
	public void buscar(){
		if(txtDia.getText().isEmpty()) {
			Clients.showNotification("Escriba día","info",txtDia,"end_center",2000);
			txtDia.focus();
			return;
		}
		if(cboMes.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar Mes","info",cboMes,"end_center",2000);
			return;
		}
		if(txtAnio.getText().isEmpty()) {
			Clients.showNotification("Escriba el año","info",txtAnio,"end_center",2000);
			txtAnio.focus();
			return;
		}
		if (listaEmergencia != null) {
			listaEmergencia = null; 
		}
		listaEmergencia = emergenciaDAO.buscarPorFecha(Integer.parseInt(txtDia.getText()), mesSeleccionado.getIdMes(), Integer.parseInt(txtAnio.getText()));
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
	public List<Me> getListaMeses(){
		return mesDAO.obtenerMeses();
	}
	public List<Emergencia> getListaEmergencia() {
		return listaEmergencia;
	}
	public void setListaEmergencia(List<Emergencia> listaEmergencia) {
		this.listaEmergencia = listaEmergencia;
	}
	public Me getMesSeleccionado() {
		return mesSeleccionado;
	}
	public void setMesSeleccionado(Me mesSeleccionado) {
		this.mesSeleccionado = mesSeleccionado;
	}	
}