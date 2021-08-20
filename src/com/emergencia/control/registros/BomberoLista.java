package com.emergencia.control.registros;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

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
	@GlobalCommand("Barrio.buscarPorPatron")
	@Command
	@NotifyChange({"listaBombero"})
	public void buscar(){
		if (listaBombero != null) {
			listaBombero = null; 
		}
		listaBombero = barrioDAO.getBarrioPorDescripcion(textoBuscar);
		lstBomberos.setModel(new ListModelList(listaBombero));
		if(listaBombero.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
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