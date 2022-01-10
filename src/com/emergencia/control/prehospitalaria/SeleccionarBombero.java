package com.emergencia.control.prehospitalaria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
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
import com.emergencia.model.entity.PersonalPrehospitalaria;
import com.emergencia.model.entity.Prehospitalaria;
import com.emergencia.model.entity.Usuario;

public class SeleccionarBombero {
	List<Usuario> listaBombero;
	@Wire private Listbox lstBomberos;
	@Wire private Window winSeleccionarBombero;
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	Prehospitalaria prehospitalario;
	RegistroPrehospitalario registroPrehospitalario;
	List<Usuario> listaBomberosAgregados = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		prehospitalario = (Prehospitalaria) Executions.getCurrent().getArg().get("Prehospitalaria");
		registroPrehospitalario = (RegistroPrehospitalario) Executions.getCurrent().getArg().get("RegistroPrehospitalario");
		listaBomberosAgregados = (List<Usuario>) Executions.getCurrent().getArg().get("Bomberos");
		buscar();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("Usuario.buscarBomberoEmergencias")
	@Command
	@NotifyChange({"listaBombero"})
	public void buscar(){
		if (listaBombero != null) {
			listaBombero = null; 
		}
		boolean bandera = false;
		List<Usuario> lista = usuarioDAO.buscarBomberoEmergencias();
		List<Usuario> listaAgg = new ArrayList<>();
		for(Usuario us : lista) {
			bandera = false;
			if(listaBomberosAgregados != null) {
				for(Usuario usAgg : listaBomberosAgregados) {
					if(usAgg.getIdUsuario() == us.getIdUsuario())
						bandera = true;
				}
			}
			if(bandera == false){
				listaAgg.add(us);
			}
		}
		
		listaBombero = listaAgg;
		lstBomberos.setModel(new ListModelList(listaBombero));
		if(listaBombero.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void agregar(){
		if(lstBomberos.getSelectedItem() == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {
						Usuario usu = (Usuario) lstBomberos.getSelectedItem().getValue();
						PersonalPrehospitalaria per = new PersonalPrehospitalaria();
						per.setBombero(usu);
						per.setEstado("A");
						per.setIdPersonalPrehospitalaria(null);
						per.setPrehospitalaria(prehospitalario);
						registroPrehospitalario.agregarBombero(per);
						salir();
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
		});
	}
	
	@Command
	public void salir() {
		winSeleccionarBombero.detach();
	}
	public List<Usuario> getListaBombero() {
		return listaBombero;
	}
	
	public void setListaBombero(List<Usuario> listaBombero) {
		this.listaBombero = listaBombero;
	}
	
}