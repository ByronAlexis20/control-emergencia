package com.emergencia.control.prehospitalaria;

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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.PrehospitalariaDAO;
import com.emergencia.model.entity.Prehospitalaria;

public class CondicionPrehospitalaria {
	@Wire Listbox lstPrehospitalario;
	@Wire Datebox dtpFecha;
	List<Prehospitalaria> listaPrehospitalaria;
	Prehospitalaria prehospitalariaSeleccionado;
	PrehospitalariaDAO prehospitalariaDAO = new PrehospitalariaDAO(); 
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		cargarEmergencias();
	}
	@GlobalCommand("Prehospitalaria.findAll")
	@NotifyChange({"listaPrehospitalaria"})
	public void cargarEmergencias(){
		if (listaPrehospitalaria != null) {
			listaPrehospitalaria = null; 
		}
		listaPrehospitalaria = prehospitalariaDAO.obtenerPrehospitalarias();
		if(listaPrehospitalaria.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@GlobalCommand("Prehospitalaria.findAll")
	@Command
	@NotifyChange({"listaPrehospitalaria"})
	public void buscar(){
		if(dtpFecha.getValue() == null) {
			Clients.showNotification("Debe seleccionar fecha para realizar la busqueda");
			return;
		}
		if (listaPrehospitalaria != null) {
			listaPrehospitalaria = null; 
		}
		listaPrehospitalaria = prehospitalariaDAO.buscarPorFechaAtencion(dtpFecha.getValue());
		if(listaPrehospitalaria.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/forms/prehospitalario/registroPrehospitalario.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editar(@BindingParam("prehospitalarioo") Prehospitalaria pre){
		if(pre == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Prehospitalaria", pre);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/prehospitalario/registroPrehospitalario.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("prehospitalarioo") Prehospitalaria pre){
		if (pre == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						prehospitalariaDAO.getEntityManager().getTransaction().begin();
						pre.setEstado("I");
						prehospitalariaDAO.getEntityManager().merge(pre);
						prehospitalariaDAO.getEntityManager().getTransaction().commit();
						buscar();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						prehospitalariaDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}
	public List<Prehospitalaria> getListaPrehospitalaria() {
		return listaPrehospitalaria;
	}
	public void setListaPrehospitalaria(List<Prehospitalaria> listaPrehospitalaria) {
		this.listaPrehospitalaria = listaPrehospitalaria;
	}
	public Prehospitalaria getPrehospitalariaSeleccionado() {
		return prehospitalariaSeleccionado;
	}
	public void setPrehospitalariaSeleccionado(Prehospitalaria prehospitalariaSeleccionado) {
		this.prehospitalariaSeleccionado = prehospitalariaSeleccionado;
	}
	
}