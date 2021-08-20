package com.emergencia.control.registros;

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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.VehiculoDAO;
import com.emergencia.model.entity.Vehiculo;

public class VehiculoLista {
	public String textoBuscar;
	List<Vehiculo> listaVehiculos;
	@Wire private Listbox lstVehiculos;
	VehiculoDAO vehiculoDAO = new VehiculoDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("Vehiculo.buscarPorPatron")
	@Command
	@NotifyChange({"listaVehiculos"})
	public void buscar(){
		if (listaVehiculos != null) {
			listaVehiculos = null; 
		}
		listaVehiculos = vehiculoDAO.getVehiculoPorDescripcion(textoBuscar);
		lstVehiculos.setModel(new ListModelList(listaVehiculos));
		if(listaVehiculos.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/forms/registros/vehiculoEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editar(@BindingParam("vehiculo") Vehiculo ve){
		if(ve == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Vehiculo", ve);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/registros/vehiculoEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("vehiculo") Vehiculo ve){
		if (ve == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						vehiculoDAO.getEntityManager().getTransaction().begin();
						ve.setEstado("I");
						vehiculoDAO.getEntityManager().merge(ve);
						vehiculoDAO.getEntityManager().getTransaction().commit();
						BindUtils.postGlobalCommand(null, null, "Vehiculo.buscarPorPatron", null);
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						vehiculoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}
	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
	public List<Vehiculo> getListaVehiculos() {
		return listaVehiculos;
	}
	public void setListaVehiculos(List<Vehiculo> listaVehiculos) {
		this.listaVehiculos = listaVehiculos;
	}

}
