package com.emergencia.control.seguridad;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class UsuarioLista {
	public String textoBuscar;
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	List<Usuario> usuarioLista;
	@Wire private Listbox lstUsuarios;
	
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("Usuario.buscarPorPatron")
	@Command
	@NotifyChange({"usuarioLista"})
	public void buscar(){
		if (usuarioLista != null) {
			usuarioLista = null; 
		}
		usuarioLista = usuarioDAO.getListaUsuariosBuscar(textoBuscar);
		lstUsuarios.setModel(new ListModelList(usuarioLista));
		if(usuarioLista.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/forms/seguridad/usuarioEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editar(@BindingParam("usuario") Usuario usuarioSeleccionado){
		if(usuarioSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		usuarioDAO.getEntityManager().refresh(usuarioSeleccionado);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Usuario", usuarioSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/seguridad/usuarioEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("usuario") Usuario usuarioSeleccionada){

		if (usuarioSeleccionada == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}

		Usuario usu = usuarioDAO.buscarPorId(usuarioSeleccionada.getIdUsuario());
		if(usu != null) {
			if(usu.getControlvehiculoChofer().size() > 0 || usu.getEmergencias().size() > 0 || usu.getPrehospitalaria().size() > 0) {
				Clients.showNotification("No se puede eliminar el registro, hay registros que dependen de éste.");
				return;
			}
		}
		
		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						usuarioDAO.getEntityManager().getTransaction().begin();
						usuarioSeleccionada.setEstado("I");
						usuarioDAO.getEntityManager().merge(usuarioSeleccionada);
						usuarioDAO.getEntityManager().getTransaction().commit();;
						buscar();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						usuarioDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	
	public List<Usuario> getUsuarioLista() {
		return usuarioLista;
	}
	public void setUsuarioLista(List<Usuario> usuarioLista) {
		this.usuarioLista = usuarioLista;
	}
	public String getTextoBuscar() {
		return textoBuscar;
	}
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
}
