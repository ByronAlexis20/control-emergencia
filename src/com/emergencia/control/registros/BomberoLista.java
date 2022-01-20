package com.emergencia.control.registros;

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

import com.emergencia.model.dao.UsuarioDAO;
import com.emergencia.model.entity.Usuario;

public class BomberoLista {
	public String textoBuscar;
	List<Usuario> listaBombero;
	@Wire private Listbox lstBomberos;
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("Usuario.buscarBomberoPorPatron")
	@Command
	@NotifyChange({"listaBombero"})
	public void buscar(){
		if (listaBombero != null) {
			listaBombero = null; 
		}
		listaBombero = usuarioDAO.getListaBomberosBuscar(textoBuscar);
		lstBomberos.setModel(new ListModelList(listaBombero));
		if(listaBombero.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/forms/registros/bomberoEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editar(@BindingParam("bombero") Usuario us){
		if(us == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Bombero", us);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/registros/bomberoEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("bombero") Usuario us){
		if (us == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Usuario usu = usuarioDAO.buscarPorId(us.getIdUsuario());
		if(usu != null) {
			if(usu.getControlvehiculoChofer().size() > 0 || usu.getEmergencias().size() > 0 || usu.getPrehospitalaria().size() > 0) {
				Clients.showNotification("No se puede eliminar el registro, hay registros que dependen de éste.");
				return;
			}
		}
		
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						usuarioDAO.getEntityManager().getTransaction().begin();
						us.setEstado("I");
						usuarioDAO.getEntityManager().merge(us);
						usuarioDAO.getEntityManager().getTransaction().commit();
						BindUtils.postGlobalCommand(null, null, "Usuario.buscarBomberoPorPatron", null);
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						usuarioDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}
	public List<Usuario> getListaBombero() {
		return listaBombero;
	}
	public void setListaBombero(List<Usuario> listaBombero) {
		this.listaBombero = listaBombero;
	}
	public String getTextoBuscar() {
		return textoBuscar;
	}
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
}