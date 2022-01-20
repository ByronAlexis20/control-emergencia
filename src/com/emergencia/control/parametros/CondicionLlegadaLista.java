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

import com.emergencia.model.dao.CondicionLlegadaDAO;
import com.emergencia.model.entity.CondicionLlegada;

public class CondicionLlegadaLista {
	public String textoBuscar;
	List<CondicionLlegada> listaCondicion;
	@Wire private Listbox lstCondicion;
	CondicionLlegadaDAO condicionDAO = new CondicionLlegadaDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("CondicionLlegada.buscarPorPatron")
	@Command
	@NotifyChange({"listaCondicion"})
	public void buscar(){
		if (listaCondicion != null) {
			listaCondicion = null; 
		}
		listaCondicion = condicionDAO.getCondicionPorDescripcion(textoBuscar);
		lstCondicion.setModel(new ListModelList(listaCondicion));
		if(listaCondicion.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/forms/parametros/condicionLlegadaEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editar(@BindingParam("condicion") CondicionLlegada con){
		if(con == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Condicion", con);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/parametros/condicionLlegadaEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("condicion") CondicionLlegada con){
		if (con == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		CondicionLlegada c = condicionDAO.buscarPorId(con.getIdCondicionLlegada());
		if(c != null) {
			if(c.getPrehospitalarias().size() > 0) {
				Clients.showNotification("No se puede eliminar el registro, hay registros que dependen de éste.");
				return;
			}
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						condicionDAO.getEntityManager().getTransaction().begin();
						con.setEstado("I");
						condicionDAO.getEntityManager().merge(con);
						condicionDAO.getEntityManager().getTransaction().commit();
						BindUtils.postGlobalCommand(null, null, "CondicionLlegada.buscarPorPatron", null);
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						condicionDAO.getEntityManager().getTransaction().rollback();
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
	public List<CondicionLlegada> getListaCondicion() {
		return listaCondicion;
	}
	public void setListaCondicion(List<CondicionLlegada> listaCondicion) {
		this.listaCondicion = listaCondicion;
	}

}
