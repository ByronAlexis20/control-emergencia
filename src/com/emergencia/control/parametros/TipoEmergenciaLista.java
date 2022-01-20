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

import com.emergencia.model.dao.TipoEmergenciaDAO;
import com.emergencia.model.entity.TipoEmergencia;

public class TipoEmergenciaLista {
	public String textoBuscar;
	List<TipoEmergencia> listaTipoEmergencia;
	@Wire private Listbox lstTipoEmergencia;
	TipoEmergenciaDAO tipoEmergenciaDAO = new TipoEmergenciaDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("TipoEmergencia.buscarPorPatron")
	@Command
	@NotifyChange({"listaTipoEmergencia"})
	public void buscar(){
		if (listaTipoEmergencia != null) {
			listaTipoEmergencia = null; 
		}
		listaTipoEmergencia = tipoEmergenciaDAO.getTipoEmergenciaPorDescripcion(textoBuscar);
		lstTipoEmergencia.setModel(new ListModelList(listaTipoEmergencia));
		if(listaTipoEmergencia.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/forms/parametros/tipoEmergenciaEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editar(@BindingParam("tipo") TipoEmergencia tip){
		if(tip == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TipoEmergencia", tip);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/parametros/tipoEmergenciaEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("tipo") TipoEmergencia tip){
		if (tip == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		TipoEmergencia te = tipoEmergenciaDAO.buscarPorId(tip.getIdTipoEmergencia());
		if(te != null) {
			if(te.getEmergencias().size() > 0 || te.getPrehospitalarias().size() > 0) {
				Clients.showNotification("No se puede eliminar el registro, hay registros que dependen de éste.");
				return;
			}
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						tipoEmergenciaDAO.getEntityManager().getTransaction().begin();
						tip.setEstado("I");
						tipoEmergenciaDAO.getEntityManager().merge(tip);
						tipoEmergenciaDAO.getEntityManager().getTransaction().commit();
						BindUtils.postGlobalCommand(null, null, "TipoEmergencia.buscarPorPatron", null);
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						tipoEmergenciaDAO.getEntityManager().getTransaction().rollback();
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
	public List<TipoEmergencia> getListaTipoEmergencia() {
		return listaTipoEmergencia;
	}
	public void setListaTipoEmergencia(List<TipoEmergencia> listaTipoEmergencia) {
		this.listaTipoEmergencia = listaTipoEmergencia;
	}
}