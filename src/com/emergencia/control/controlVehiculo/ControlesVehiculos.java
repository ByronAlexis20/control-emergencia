package com.emergencia.control.controlVehiculo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.ControlVehiculoDAO;
import com.emergencia.model.entity.ControlVehiculo;

public class ControlesVehiculos {
	@Wire Listbox lstControlVehiculo;
	@Wire Datebox dtpFecha;
	List<ControlVehiculo> listaControlVehiculo;
	ControlVehiculoDAO controlDAO = new ControlVehiculoDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		cargarTodosControles();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("ControlVehiculo.findAll")
	@NotifyChange({"listaControlVehiculo"})
	public void cargarTodosControles(){
		if (listaControlVehiculo != null) {
			listaControlVehiculo = null; 
		}
		listaControlVehiculo = controlDAO.buscarTodosControles();
		lstControlVehiculo.setModel(new ListModelList(listaControlVehiculo));
		if(listaControlVehiculo.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("ControlVehiculo.buscarPorFecha")
	@Command
	@NotifyChange({"listaControlVehiculo"})
	public void buscar(){
		if(dtpFecha.getValue() == null) {
			Clients.showNotification("Debe ingresar una fecha");
			return;
		}
		if (listaControlVehiculo != null) {
			listaControlVehiculo = null; 
		}
		listaControlVehiculo = controlDAO.buscarPorFecha(dtpFecha.getValue());
		lstControlVehiculo.setModel(new ListModelList(listaControlVehiculo));
		if(listaControlVehiculo.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@Command
	public void editar(@BindingParam("control") ControlVehiculo control){
		if(control == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if(control.getEmergencia() != null) {
			params.put("Emergencia", control.getEmergencia());
			Window ventanaCargar = (Window) Executions.createComponents("/forms/control-vehiculos/registrarEmergencia.zul", null, params);
			ventanaCargar.doModal();
		}else {
			params.put("Prehospitalaria", control.getPrehospitalaria());
			Window ventanaCargar = (Window) Executions.createComponents("/forms/control-vehiculos/registrarPrehospitalaria.zul", null, params);
			ventanaCargar.doModal();
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("control") ControlVehiculo control){
		if (control == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						controlDAO.getEntityManager().getTransaction().begin();
						control.setEstado("I");
						controlDAO.getEntityManager().merge(control);
						controlDAO.getEntityManager().getTransaction().commit();
						BindUtils.postGlobalCommand(null, null, "ControlVehiculo.findAll", null);
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						controlDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}
	public List<ControlVehiculo> getListaControlVehiculo() {
		return listaControlVehiculo;
	}
	public void setListaControlVehiculo(List<ControlVehiculo> listaControlVehiculo) {
		this.listaControlVehiculo = listaControlVehiculo;
	}
	
}