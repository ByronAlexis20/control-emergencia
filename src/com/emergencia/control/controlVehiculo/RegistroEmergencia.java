package com.emergencia.control.controlVehiculo;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.ControlVehiculoDAO;
import com.emergencia.model.entity.ControlVehiculo;
import com.emergencia.model.entity.Emergencia;

public class RegistroEmergencia {
	@Wire Window winRegistroEmergencia;
	@Wire Textbox txtFecha;
	@Wire Textbox txtFormaAviso;
	@Wire Textbox txtTipoEmergencia;
	@Wire Textbox txtProvincia;
	@Wire Textbox txtCanton;
	@Wire Textbox txtParroquia;
	@Wire Textbox txtReportadoPor;
	@Wire Textbox txtBarrio;
	@Wire Textbox txtConfirmacion;
	@Wire Textbox txtInformante;
	@Wire Textbox txtTelefono;
	@Wire Textbox txtDescripcionOperaciones;
	
	//datos de los vehiculos
	@Wire Combobox cboVehiculo;
	@Wire Combobox cboChofer;
	@Wire Combobox cboCuartelero;
	@Wire Textbox txtNoReporte;
	@Wire Timebox tmHorasalida;
	@Wire Timebox tmHoraLlegada;
	@Wire Timebox tmHoraLlegadaBase;
	@Wire Textbox txtNovedades;
	
	Emergencia emergencia;
	ControlVehiculo control;
	ControlVehiculoDAO controlDAO = new ControlVehiculoDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		emergencia = (Emergencia) Executions.getCurrent().getArg().get("Emergencia");
		cargarDatos();
	}
	private void cargarDatos() {
		txtFecha.setText(emergencia.getDia() + "/" + emergencia.getMe().getMes() + "/" + emergencia.getAnio());
		txtFormaAviso.setText(emergencia.getFormaAviso().getFormaAviso());
		txtTipoEmergencia.setText(emergencia.getTipoEmergencia().getTipoEmergencia());
		txtProvincia.setText(emergencia.getParroquia().getCanton().getProvincia().getProvincia());
		txtCanton.setText(emergencia.getParroquia().getCanton().getCanton());
		txtParroquia.setText(emergencia.getParroquia().getParroquia());
		txtReportadoPor.setText(emergencia.getFormaAviso().getFormaAviso());
		txtBarrio.setText(emergencia.getBarrio().getBarrio());
		txtConfirmacion.setText(emergencia.getConfirmacionLlamada());
		txtInformante.setText(emergencia.getUsuario().getPersona().getNombres() + " " + emergencia.getUsuario().getPersona().getApellidos());
		txtTelefono.setText(emergencia.getTelefono());
		txtDescripcionOperaciones.setText(emergencia.getDescripcionOperaciones());
		List<ControlVehiculo> lista = controlDAO.buscarPorEmergencia(emergencia.getIdEmergencia());
		if(lista.size() > 0) {//tiene datos y solo se modifica
			control = lista.get(0);
			txtNoReporte.setText(control.getnReporte());
			tmHorasalida.setValue(control.getHoraSalidaBase());
			tmHoraLlegada.setValue(control.getHoraLlegadaEmergencia());
			tmHoraLlegadaBase.setValue(control.getHoraLlegadaCentral());
			txtNovedades.setText(control.getNovedades());
		}else {
			control = new ControlVehiculo();
		}
	}
	@Command
	public void salir() {
		winRegistroEmergencia.detach();
	}
	public Emergencia getEmergencia() {
		return emergencia;
	}
	public void setEmergencia(Emergencia emergencia) {
		this.emergencia = emergencia;
	}
	public ControlVehiculo getControl() {
		return control;
	}
	public void setControl(ControlVehiculo control) {
		this.control = control;
	}
}