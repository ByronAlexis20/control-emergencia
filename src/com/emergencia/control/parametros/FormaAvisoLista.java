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

import com.emergencia.model.dao.FormaAvisoDAO;
import com.emergencia.model.entity.FormaAviso;

public class FormaAvisoLista {
	public String textoBuscar;
	List<FormaAviso> listaFormaAviso;
	@Wire private Listbox lstFormaAviso;
	FormaAvisoDAO formaDAO = new FormaAvisoDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("FormaAviso.buscarPorPatron")
	@Command
	@NotifyChange({"listaFormaAviso"})
	public void buscar(){
		if (listaFormaAviso != null) {
			listaFormaAviso = null; 
		}
		listaFormaAviso = formaDAO.getFormaAvisoPorDescripcion(textoBuscar);
		lstFormaAviso.setModel(new ListModelList(listaFormaAviso));
		if(listaFormaAviso.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/forms/parametros/formaAvisoEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editar(@BindingParam("forma") FormaAviso fo){
		if(fo == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("FormaAviso", fo);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/parametros/formaAvisoEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("forma") FormaAviso fo){
		if (fo == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						formaDAO.getEntityManager().getTransaction().begin();
						fo.setEstado("I");
						formaDAO.getEntityManager().merge(fo);
						formaDAO.getEntityManager().getTransaction().commit();
						BindUtils.postGlobalCommand(null, null, "FormaAviso.buscarPorPatron", null);
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						formaDAO.getEntityManager().getTransaction().rollback();
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
	public List<FormaAviso> getListaFormaAviso() {
		return listaFormaAviso;
	}
	public void setListaFormaAviso(List<FormaAviso> listaFormaAviso) {
		this.listaFormaAviso = listaFormaAviso;
	}
}