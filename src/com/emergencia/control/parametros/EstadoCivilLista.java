package com.emergencia.control.parametros;

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

import com.emergencia.model.dao.EstadoCivilDAO;
import com.emergencia.model.entity.EstadoCivil;

public class EstadoCivilLista {
	public String textoBuscar;
	List<EstadoCivil> listaEstadoCivil;
	@Wire private Listbox lstEstadoCivil;
	EstadoCivilDAO estadoCivilDAO = new EstadoCivilDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("EstadoCivil.buscarPorPatron")
	@Command
	@NotifyChange({"listaEstadoCivil"})
	public void buscar(){
		if (listaEstadoCivil != null) {
			listaEstadoCivil = null; 
		}
		listaEstadoCivil = estadoCivilDAO.getEstadoCivilPorDescripcion(textoBuscar);
		lstEstadoCivil.setModel(new ListModelList(listaEstadoCivil));
		if(listaEstadoCivil.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/forms/parametros/estadoCivilEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editar(@BindingParam("estado") EstadoCivil es){
		if(es == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("EstadoCivil", es);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/parametros/estadoCivilEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("estado") EstadoCivil es){
		if (es == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						estadoCivilDAO.getEntityManager().getTransaction().begin();
						es.setEstado("I");
						estadoCivilDAO.getEntityManager().merge(es);
						estadoCivilDAO.getEntityManager().getTransaction().commit();
						BindUtils.postGlobalCommand(null, null, "EstadoCivil.buscarPorPatron", null);
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						estadoCivilDAO.getEntityManager().getTransaction().rollback();
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
	public List<EstadoCivil> getListaEstadoCivil() {
		return listaEstadoCivil;
	}
	public void setListaEstadoCivil(List<EstadoCivil> listaEstadoCivil) {
		this.listaEstadoCivil = listaEstadoCivil;
	}

}
