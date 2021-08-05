package com.emergencia.control.emergencia;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
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
import org.zkoss.zul.Listbox;
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
