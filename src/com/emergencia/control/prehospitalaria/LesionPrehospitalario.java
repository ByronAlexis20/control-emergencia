package com.emergencia.control.prehospitalaria;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.LesionDAO;
import com.emergencia.model.entity.Lesion;
import com.emergencia.model.entity.LocalizacionLesion;

public class LesionPrehospitalario {
	@Wire Window winLesion;
	@Wire Combobox cboLesion;
	@Wire Textbox txtLugar;
	Lesion lesionSeleccionado;
	LesionDAO lesionDAO = new LesionDAO();
	
	RegistroPrehospitalario registroPrehospitalario;
	LocalizacionLesion localizacionLesion;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		registroPrehospitalario = (RegistroPrehospitalario) Executions.getCurrent().getArg().get("RegistroPrehospitalario");
		localizacionLesion = new LocalizacionLesion();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void agregar() {
		if(cboLesion.getSelectedItem().getValue() == null) {
			Clients.showNotification("Debe seleccionar Lesión");
			return;
		}
		if(txtLugar.getText().toString().isEmpty()) {
			Clients.showNotification("Debe registrar lugar de la lesión");
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {
						localizacionLesion.setEstado("A");
						localizacionLesion.setLesion((Lesion) cboLesion.getSelectedItem().getValue());
						localizacionLesion.setLugarInvolucrada(txtLugar.getText());
						registroPrehospitalario.agregarLocalizacionLesion(localizacionLesion);
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
		winLesion.detach();
	}
	public List<Lesion> getListaLesiones(){
		return lesionDAO.buscarLesiones();
	}
	public Lesion getLesionSeleccionado() {
		return lesionSeleccionado;
	}
	public void setLesionSeleccionado(Lesion lesionSeleccionado) {
		this.lesionSeleccionado = lesionSeleccionado;
	}

	public LocalizacionLesion getLocalizacionLesion() {
		return localizacionLesion;
	}

	public void setLocalizacionLesion(LocalizacionLesion localizacionLesion) {
		this.localizacionLesion = localizacionLesion;
	}
}