package com.emergencia.control.controlVehiculo;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.ControlVehiculoDAO;
import com.emergencia.model.dao.UsuarioDAO;
import com.emergencia.model.dao.VehiculoDAO;
import com.emergencia.model.entity.ControlVehiculo;
import com.emergencia.model.entity.Emergencia;
import com.emergencia.model.entity.Usuario;
import com.emergencia.model.entity.Vehiculo;

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
	@Wire Datebox dtpFecha;
	
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
	
	VehiculoDAO vehiculoDAO = new VehiculoDAO();
	Vehiculo vehiculoSeleccionado;
	
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	Usuario choferSeleccionado;
	Usuario cuarteleroSeleccionado;
	
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
			cboVehiculo.setText(control.getVehiculo().getTipoVehiculo().getTipoVehiculo() + " - " + control.getVehiculo().getCodigo());
			vehiculoSeleccionado = control.getVehiculo();
			cboChofer.setText(control.getChofer().getPersona().getNombres() + " " + control.getChofer().getPersona().getApellidos());
			choferSeleccionado = control.getChofer();
			cboCuartelero.setText(control.getCuartelero().getPersona().getNombres() + " " + control.getCuartelero().getPersona().getApellidos());
			cuarteleroSeleccionado = control.getCuartelero();
		}else {
			control = new ControlVehiculo();
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar() {
		try {
			if (validarDatos() == false) {
				return;
			}
			Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onYes")) {		
						try {						
							controlDAO.getEntityManager().getTransaction().begin();
							copiarDatos();
							if(control.getIdControl() == null) {
								control.setEmergencia(emergencia);
								controlDAO.getEntityManager().persist(control);
							}else {
								controlDAO.getEntityManager().merge(control);
							}
							controlDAO.getEntityManager().getTransaction().commit();
							Clients.showNotification("Proceso Ejecutado con exito.");
							BindUtils.postGlobalCommand(null, null, "Emergencia.buscarSinControlVehiculo", null);
							salir();						
						} catch (Exception e) {
							e.printStackTrace();
							controlDAO.getEntityManager().getTransaction().rollback();
						}
					}
				}
			});
		}catch(Exception ex) {
		}
	}
	private void copiarDatos() {
		control.setChofer(choferSeleccionado);
		control.setCuartelero(cuarteleroSeleccionado);
		control.setVehiculo(vehiculoSeleccionado);
		control.setEstado("A");
		control.setFecha(dtpFecha.getValue());
		control.setHoraLlegadaCentral(new Time(tmHoraLlegadaBase.getValue().getTime()));
		control.setHoraLlegadaEmergencia(new Time(tmHoraLlegada.getValue().getTime()));
		Date horaActual = new Date();
		control.setHoraReporte(new Time(horaActual.getTime()));
		control.setHoraSalidaBase(new Time(tmHorasalida.getValue().getTime()));
		control.setNovedades(txtNovedades.getText());
		control.setnReporte(txtNoReporte.getText());
	}
	private boolean validarDatos() {
		try {
			boolean band = true;
			if(cboVehiculo.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Vehículo","info",cboVehiculo,"end_center",2000);
				return false;
			}
			if(cboChofer.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Chofer","info",cboChofer,"end_center",2000);
				return false;
			}
			if(cboCuartelero.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Cuartelero","info",cboCuartelero,"end_center",2000);
				return false;
			}
			if(dtpFecha.getValue() == null) {
				Clients.showNotification("Seleccione fecha","info",dtpFecha,"end_center",2000);
				return false;
			}
			if(txtNoReporte.getText().isEmpty()) {
				Clients.showNotification("Debe registrar No. de Reporte","info",txtNoReporte,"end_center",2000);
				txtNoReporte.focus();
				return false;
			}
			if(tmHorasalida.getValue() == null) {
				Clients.showNotification("Debe registrar Hora de salida","info",tmHorasalida,"end_center",2000);
				return false;
			}
			if(tmHoraLlegada.getValue() == null) {
				Clients.showNotification("Debe registrar Hora de llegada a la emergencia","info",tmHoraLlegada,"end_center",2000);
				return false;
			}
			if(tmHoraLlegadaBase.getValue() == null) {
				Clients.showNotification("Debe registrar Hora de llegada a la central","info",tmHoraLlegadaBase,"end_center",2000);
				return false;
			}
			return band;
		}catch(Exception ex) {
			return false;
		}
	}
	@Command
	public void salir() {
		winRegistroEmergencia.detach();
	}
	public List<Vehiculo> getListaVehiculos(){
		return vehiculoDAO.getVehiculoPorDescripcion("");
	}
	public List<Usuario> getChoferes(){
		return usuarioDAO.getListaChoferBuscar("");
	}
	public List<Usuario> getListaCuarteleros(){
		return usuarioDAO.getListaBomberosBuscar("");
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
	public Vehiculo getVehiculoSeleccionado() {
		return vehiculoSeleccionado;
	}
	public void setVehiculoSeleccionado(Vehiculo vehiculoSeleccionado) {
		this.vehiculoSeleccionado = vehiculoSeleccionado;
	}
	public Usuario getChoferSeleccionado() {
		return choferSeleccionado;
	}
	public void setChoferSeleccionado(Usuario choferSeleccionado) {
		this.choferSeleccionado = choferSeleccionado;
	}
	public Usuario getCuarteleroSeleccionado() {
		return cuarteleroSeleccionado;
	}
	public void setCuarteleroSeleccionado(Usuario cuarteleroSeleccionado) {
		this.cuarteleroSeleccionado = cuarteleroSeleccionado;
	}
	
}