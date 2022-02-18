package com.emergencia.control.controlVehiculo;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.emergencia.model.dao.PersonalEmergenciaDAO;
import com.emergencia.model.dao.UsuarioDAO;
import com.emergencia.model.dao.VehiculoDAO;
import com.emergencia.model.entity.ControlVehiculo;
import com.emergencia.model.entity.Emergencia;
import com.emergencia.model.entity.PersonalEmergencia;
import com.emergencia.model.entity.Usuario;
import com.emergencia.model.entity.Vehiculo;
import com.emergencia.util.PrintReport;

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
	
	PersonalEmergenciaDAO personalEmergenciaDAO = new PersonalEmergenciaDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException, ParseException{
		Selectors.wireComponents(view, this, false);
		emergencia = (Emergencia) Executions.getCurrent().getArg().get("Emergencia");
		cargarDatos();
		if(emergencia != null) {
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Date fechaDate = null;
			String fecha = "";
			if(emergencia.getDia() < 10) 
				fecha = fecha + "0" + emergencia.getDia() + "/";
			else
				fecha = fecha  + emergencia.getDia() + "/";
			
			if(emergencia.getMe().getIdMes() < 10)
				fecha = fecha + "0" + emergencia.getMe().getIdMes();
			else
				fecha = fecha  + emergencia.getMe().getIdMes();
			
			fecha = fecha + "/" + emergencia.getAnio();
			
			fechaDate = formato.parse(fecha);
			
			dtpFecha.setValue(fechaDate);
			dtpFecha.setConstraint("after " + new SimpleDateFormat("yyyyMMdd").format(fechaDate));
		}
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
			dtpFecha.setValue(control.getFecha());
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
							BindUtils.postGlobalCommand(null, null, "ControlVehiculo.findAll", null);
							mostrarInforme(control);
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
	
	private void mostrarInforme(ControlVehiculo con) {
		try {
			String tipoEmergencia = "";
			if(con.getEmergencia().getTipoEmergencia().getGrupo().equals("APH")) {
				tipoEmergencia = "prehospitalaria";
			}else if(con.getEmergencia().getTipoEmergencia().getGrupo().equals("CI")) {
				tipoEmergencia = "contra incendio";
			}else if(con.getEmergencia().getTipoEmergencia().getGrupo().equals("LS")) {
				tipoEmergencia = "labor social";
			}
			String personal = "";
			List<PersonalEmergencia> listaPersonalEmergencia = personalEmergenciaDAO.buscarPorEmergencia(con.getEmergencia().getIdEmergencia());
			for(PersonalEmergencia per : listaPersonalEmergencia) {
				personal = personal + "   - <b>" + per.getBombero().getGrado() + " " + per.getBombero().getPersona().getNombres() + " " + per.getBombero().getPersona().getApellidos() + "</b><br/>";
			}
			int milisegundos = (int)(con.getHoraLlegadaCentral().getTime() - con.getHoraLlegadaEmergencia().getTime());
			
			int minutos = (int) ((milisegundos / (1000*60)) % 60); 
			int horas = (int) ((milisegundos / (1000*60*60)) % 24);

			
			String informe = "INFORME No. " + con.getnReporte() + " DE EVENTO POR EMERGENCIA";
			String resumen = "En el dia <b>" + con.getEmergencia().getDia() + "-" + con.getEmergencia().getMe().getMes() + "-" + con.getEmergencia().getAnio() + "</b> a las <b>" + 
					con.getHoraSalidaBase() + "</b> se reportó por <b>" + con.getEmergencia().getFormaAviso().getFormaAviso() + "</b> la emergencia <b>" + tipoEmergencia + 
					"</b> de tipo <b>" + con.getEmergencia().getTipoEmergencia().getTipoEmergencia() + "</b>, sucedido en el <b>" + con.getEmergencia().getBarrio().getBarrio() + 
					"</b> al mismo que acudió el vehículo <b>" + con.getVehiculo().getTipoVehiculo().getTipoVehiculo() + " " + con.getVehiculo().getCodigo() + 
					"</b> y asistieron los siguientes bomberos: <br/><br/><br/>" +
					"<b>Personal que asiste a emergencia:</b> <br/><br/>" + 
					personal + "<br/><br/>" + 
					"<b>Chofer a cargo:</b> <br/><br/>" 
					+ "   - <b>" + con.getChofer().getPersona().getNombres() + " " + con.getChofer().getPersona().getApellidos() + "</b><br/><br/>" + 
					"A las <b>" + con.getHoraLlegadaEmergencia() + "</b> se llegó al sitio del incidente y a las <b>" + con.getHoraLlegadaCentral() + "</b> se " + 
					"retornó a la compañía cubriendo un total de <b>" + horas + "</b> horas <b>" + minutos + "</b> minutos de atención. Después de la jornada los " + 
					"bomberos estarán atentos en la base para acudir al llamado de alguna atencion de emergencia.";
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("INFORME", informe);
			params.put("RESUMEN", resumen);
			PrintReport report = new PrintReport();
			report.crearDescargarReporte("/reportes/informe/rptInformeEvento.jasper",usuarioDAO, params);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
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
		//el numero de reporte si es un nuevo registro, se calcula
		if(control.getIdControl() == null) {
			List<ControlVehiculo> lista = this.controlDAO.buscarTodosOrdenados();
			if(lista.size() > 0) {
				Integer nRepor = Integer.parseInt(lista.get(0).getnReporte()) + 1;
				control.setnReporte(String.valueOf(nRepor));
			}
			else
				control.setnReporte("1");
		}
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