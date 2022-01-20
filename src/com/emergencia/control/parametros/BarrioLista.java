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

import com.emergencia.model.dao.BarrioDAO;
import com.emergencia.model.entity.Barrio;

public class BarrioLista {
	public String textoBuscar;
	List<Barrio> listaBarrios;
	@Wire private Listbox lstBarrios;
	BarrioDAO barrioDAO = new BarrioDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("Barrio.buscarPorPatron")
	@Command
	@NotifyChange({"listaBarrios"})
	public void buscar(){
		if (listaBarrios != null) {
			listaBarrios = null; 
		}
		listaBarrios = barrioDAO.getBarrioPorDescripcion(textoBuscar);
		lstBarrios.setModel(new ListModelList(listaBarrios));
		if(listaBarrios.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/forms/parametros/barrioEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editar(@BindingParam("barrio") Barrio bar){
		if(bar == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Barrio", bar);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/parametros/barrioEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("barrio") Barrio bar){
		if (bar == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Barrio br = barrioDAO.buscarPorId(bar.getIdBarrio());
		if(br != null) {
			if(br.getEmergencias().size() > 0) {
				Clients.showNotification("No se puede eliminar el registro, hay registros que dependen de éste.");
				return;
			}
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						barrioDAO.getEntityManager().getTransaction().begin();
						bar.setEstado("I");
						barrioDAO.getEntityManager().merge(bar);
						barrioDAO.getEntityManager().getTransaction().commit();
						BindUtils.postGlobalCommand(null, null, "Barrio.buscarPorPatron", null);
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						barrioDAO.getEntityManager().getTransaction().rollback();
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

	public List<Barrio> getListaBarrios() {
		return listaBarrios;
	}

	public void setListaBarrios(List<Barrio> listaBarrios) {
		this.listaBarrios = listaBarrios;
	}
	
}
