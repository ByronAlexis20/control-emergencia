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

import com.emergencia.model.dao.GeneroDAO;
import com.emergencia.model.entity.Genero;

public class GeneroLista {
	public String textoBuscar;
	List<Genero> listaGeneros;
	@Wire private Listbox lstGeneros;
	GeneroDAO generoDAO = new GeneroDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("Genero.buscarPorPatron")
	@Command
	@NotifyChange({"listaGeneros"})
	public void buscar(){
		if (listaGeneros != null) {
			listaGeneros = null; 
		}
		listaGeneros = generoDAO.getGeneroPorDescripcion(textoBuscar);
		lstGeneros.setModel(new ListModelList(listaGeneros));
		if(listaGeneros.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/forms/parametros/generoEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editar(@BindingParam("genero") Genero gen){
		if(gen == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Genero", gen);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/parametros/generoEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("genero") Genero gen){
		if (gen == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						generoDAO.getEntityManager().getTransaction().begin();
						gen.setEstado("I");
						generoDAO.getEntityManager().merge(gen);
						generoDAO.getEntityManager().getTransaction().commit();
						BindUtils.postGlobalCommand(null, null, "Genero.buscarPorPatron", null);
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						generoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}
	public List<Genero> getListaGeneros() {
		return listaGeneros;
	}
	public void setListaGeneros(List<Genero> listaGeneros) {
		this.listaGeneros = listaGeneros;
	}
	
}