package com.emergencia.control.controlVehiculo;

import java.io.IOException;
import java.util.ArrayList;
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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.ControlVehiculoDAO;
import com.emergencia.model.dao.EmergenciaDAO;
import com.emergencia.model.dao.PrehospitalariaDAO;
import com.emergencia.model.entity.ControlVehiculo;
import com.emergencia.model.entity.Emergencia;
import com.emergencia.model.entity.Prehospitalaria;

public class MovimientoVehiculo {
	@Wire Window winSeleccionarTipoEmergencia;
	@Wire Window winRegistrarEmergencia;
	@Wire Window winRegistrarPrehospitalaria;
	@Wire Combobox cboTipoEmergenciaGeneral;
	
	EmergenciaDAO emergenciaDAO = new EmergenciaDAO();
	PrehospitalariaDAO prehospitalariaDAO = new PrehospitalariaDAO();
	ControlVehiculoDAO controlVehiculoDAO = new ControlVehiculoDAO();
	
	TipoEmergenciaGeneral tipoEmergenciaGeneralSeleccionado;
	List<Emergencia> listaEmergencias;
	List<Prehospitalaria> listaPrehospitalaria;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		winSeleccionarTipoEmergencia.setMaximized(true);
		winRegistrarEmergencia.setMinimized(true);
		winRegistrarPrehospitalaria.setMinimized(true);
	}
	@Command
	public void continuar() {
		if(tipoEmergenciaGeneralSeleccionado == null) {
			Clients.showNotification("Debe seleccionar el tipo de emergencia");
			return;
		}
		if(tipoEmergenciaGeneralSeleccionado.getCodigo().equals("C")) {
			winSeleccionarTipoEmergencia.setVisible(false);
			winSeleccionarTipoEmergencia.setMinimized(true);
			winRegistrarEmergencia.setVisible(true);
			winRegistrarEmergencia.setMaximized(true);
			BindUtils.postGlobalCommand(null, null, "Emergencia.buscarSinControlVehiculo", null);
		}else {
			winSeleccionarTipoEmergencia.setVisible(false);
			winSeleccionarTipoEmergencia.setMinimized(true);
			winRegistrarPrehospitalaria.setVisible(true);
			winRegistrarPrehospitalaria.setMaximized(true);
			BindUtils.postGlobalCommand(null, null, "Prehospitalaria.buscarSinControlVehiculo", null);
		}
	}
	@GlobalCommand("Emergencia.buscarSinControlVehiculo")
	@Command
	@NotifyChange({"listaEmergencias"})
	public void cargarEmergenciasSinControlvehiculo() {
		if (listaEmergencias != null) {
			listaEmergencias = null; 
		}
		List<Emergencia> lista = emergenciaDAO.obtenerEmergencias();
		List<ControlVehiculo> listaVehiculo = controlVehiculoDAO.buscarTodosControles();
		List<Emergencia> listaPresentar = new ArrayList<>();
		boolean bandera = false;
		for(Emergencia p : lista) {
			bandera = false;
			for(ControlVehiculo c : listaVehiculo) {
				if(c.getEmergencia() != null) {
					if(p.getIdEmergencia() == c.getEmergencia().getIdEmergencia())
						bandera = true;
				}
			}
			if(bandera == false)
				listaPresentar.add(p);
		}
		listaEmergencias = listaPresentar;
		if(listaEmergencias.size() == 0) {
			Clients.showNotification("No hay emergencias.!!");
		}
	}
	@GlobalCommand("Prehospitalaria.buscarSinControlVehiculo")
	@Command
	@NotifyChange({"listaPrehospitalaria"})
	public void cargarPrehospitalariaSinControlvehiculo() {
		if (listaPrehospitalaria != null) {
			listaPrehospitalaria = null; 
		}
		List<Prehospitalaria> lista = prehospitalariaDAO.obtenerPrehospitalarias();
		List<ControlVehiculo> listaVehiculo = controlVehiculoDAO.buscarTodosControles();
		List<Prehospitalaria> listaPresentar = new ArrayList<>();
		boolean bandera = false;
		for(Prehospitalaria p : lista) {
			bandera = false;
			for(ControlVehiculo c : listaVehiculo) {
				if(c.getPrehospitalaria() != null) {
					if(p.getIdPrehospitalaria() == c.getPrehospitalaria().getIdPrehospitalaria())
						bandera = true;
				}
			}
			if(bandera == false)
				listaPresentar.add(p);
		}
		listaPrehospitalaria = listaPresentar;
		if(listaPrehospitalaria.size() == 0) {
			Clients.showNotification("No hay emergencias.!!");
		}
	}
	@Command
	public void regresarEmergencia() {
		winRegistrarEmergencia.setMinimized(true);
		winSeleccionarTipoEmergencia.setVisible(true);
		winSeleccionarTipoEmergencia.setMaximized(true);
		winRegistrarEmergencia.setVisible(false);
	}
	@Command
	public void regresarPrehospitalaria() {
		winRegistrarPrehospitalaria.setMinimized(true);
		winSeleccionarTipoEmergencia.setVisible(true);
		winSeleccionarTipoEmergencia.setMaximized(true);
		winRegistrarPrehospitalaria.setVisible(false);
	}
	@Command
	public void registrarVehiculoEmergencia(@BindingParam("emergencia") Emergencia em){
		if(em == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Emergencia", em);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/control-vehiculos/registrarEmergencia.zul", null, params);
		ventanaCargar.doModal();
	}
	@Command
	public void registrarVehiculoPrehospitalaria(@BindingParam("prehospitalaria") Prehospitalaria pr){
		if(pr == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Prehospitalaria", pr);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/control-vehiculos/registrarPrehospitalaria.zul", null, params);
		ventanaCargar.doModal();
	}
	public List<TipoEmergenciaGeneral> getListaTipoEmergenciaGeneral(){
		List<TipoEmergenciaGeneral> lista = new ArrayList<>();
		TipoEmergenciaGeneral t1 = new TipoEmergenciaGeneral("APH", "Prehospitalaria");
		lista.add(t1);
		TipoEmergenciaGeneral t2 = new TipoEmergenciaGeneral("C", "Control de Incendio o Labor social");
		lista.add(t2);
		return lista;
	}
	public TipoEmergenciaGeneral getTipoEmergenciaGeneralSeleccionado() {
		return tipoEmergenciaGeneralSeleccionado;
	}
	public void setTipoEmergenciaGeneralSeleccionado(TipoEmergenciaGeneral tipoEmergenciaGeneralSeleccionado) {
		this.tipoEmergenciaGeneralSeleccionado = tipoEmergenciaGeneralSeleccionado;
	}
	public List<Emergencia> getListaEmergencias() {
		return listaEmergencias;
	}
	public void setListaEmergencias(List<Emergencia> listaEmergencias) {
		this.listaEmergencias = listaEmergencias;
	}
	public List<Prehospitalaria> getListaPrehospitalaria() {
		return listaPrehospitalaria;
	}
	public void setListaPrehospitalaria(List<Prehospitalaria> listaPrehospitalaria) {
		this.listaPrehospitalaria = listaPrehospitalaria;
	}

	public class TipoEmergenciaGeneral {
		private String codigo;
		private String nombre;
		public TipoEmergenciaGeneral() {
			super();
		}
		public TipoEmergenciaGeneral(String codigo, String nombre) {
			super();
			this.codigo = codigo;
			this.nombre = nombre;
		}
		public String getCodigo() {
			return codigo;
		}
		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
	}
}