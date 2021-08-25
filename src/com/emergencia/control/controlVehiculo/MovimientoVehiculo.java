package com.emergencia.control.controlVehiculo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.EmergenciaDAO;
import com.emergencia.model.entity.Emergencia;

public class MovimientoVehiculo {
	@Wire Window winSeleccionarTipoEmergencia;
	@Wire Window winRegistrarEmergencia;
	@Wire Window winRegistrarPrehospitalaria;
	@Wire Combobox cboTipoEmergenciaGeneral;
	
	EmergenciaDAO emergenciaDAO = new EmergenciaDAO();
	
	TipoEmergenciaGeneral tipoEmergenciaGeneralSeleccionado;
	List<Emergencia> listaEmergencias;
	
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
			
		}
	}
	
	@GlobalCommand("Emergencia.buscarSinControlVehiculo")
	@Command
	@NotifyChange({"listaEmergencias"})
	public void cargarEmergenciasSinControlvehiculo() {
		if (listaEmergencias != null) {
			listaEmergencias = null; 
		}
		listaEmergencias = emergenciaDAO.buscarSinControlVehiculo();
		if(listaEmergencias.size() == 0) {
			Clients.showNotification("No hay emergencias.!!");
		}
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