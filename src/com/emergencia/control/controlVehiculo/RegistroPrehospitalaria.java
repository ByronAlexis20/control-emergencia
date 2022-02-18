package com.emergencia.control.controlVehiculo;

import java.io.IOException;
import java.sql.Time;
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
import com.emergencia.model.dao.PersonalPrehospitalariaDAO;
import com.emergencia.model.dao.UsuarioDAO;
import com.emergencia.model.dao.VehiculoDAO;
import com.emergencia.model.entity.ControlVehiculo;
import com.emergencia.model.entity.PersonalPrehospitalaria;
import com.emergencia.model.entity.Prehospitalaria;
import com.emergencia.model.entity.Usuario;
import com.emergencia.model.entity.Vehiculo;
import com.emergencia.util.PrintReport;

public class RegistroPrehospitalaria {
	@Wire Window winRegistroPrehospitalario;
	@Wire Textbox txtFechaAtencion;
	@Wire Textbox txtFechaEvento;
	@Wire Textbox txtTipoEmergencia;
	@Wire Textbox txtCedulaUsuario;
	@Wire Textbox txtNombreUsuario;
	@Wire Textbox txtEdad;
	@Wire Textbox txtCedulaInformante;
	@Wire Textbox txtNombreInformante;
	@Wire Textbox txtCondicionLlegada;
	@Wire Textbox txtDireccionEvento;
	@Wire Textbox txtLugarEvento;
	@Wire Textbox txtInterrogatorio;
	@Wire Datebox dtpFecha;
	
	//datos de los vehiculos
	@Wire Combobox cboVehiculo;
	@Wire Combobox cboChofer;
	@Wire Combobox cboCuartelero;
	@Wire Textbox txtNoReporte;
	@Wire Timebox tmHorasalida;
	@Wire Timebox tmHoraLlegada;
	@Wire Timebox tmHoraSalidaDeEmergencia;
	@Wire Timebox tmHoraLlegadaHospital;
	@Wire Timebox tmHoraSalidaRayosX;
	@Wire Timebox tmHoraLlegadaRayosX;
	@Wire Timebox tmHoraRetornoRayosX;
	@Wire Timebox tmHoraLlegadaDeRayosX;
	@Wire Timebox tmHoraSalidaHospital;
	@Wire Timebox tmHoraLlegadaCentral;
	
	@Wire Textbox txtNovedades;
	
	Prehospitalaria prehospitalaria;
	ControlVehiculo control;
	ControlVehiculoDAO controlDAO = new ControlVehiculoDAO();
	
	VehiculoDAO vehiculoDAO = new VehiculoDAO();
	Vehiculo vehiculoSeleccionado;
	
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	Usuario choferSeleccionado;
	Usuario cuarteleroSeleccionado;
	
	PersonalPrehospitalariaDAO personalPrehospitalariaDAO = new PersonalPrehospitalariaDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		prehospitalaria = (Prehospitalaria) Executions.getCurrent().getArg().get("Prehospitalaria");
		cargarDatos();
	}
	private void cargarDatos() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		txtFechaAtencion.setText(formato.format(prehospitalaria.getFechaAtencion()));
		txtFechaEvento.setText(formato.format(prehospitalaria.getFechaEvento()));
		txtTipoEmergencia.setText(prehospitalaria.getTipoEmergencia().getTipoEmergencia());
		txtCedulaUsuario.setText(prehospitalaria.getCedulaUsuario());
		txtNombreUsuario.setText(prehospitalaria.getNombreUsuario());
		txtEdad.setText(String.valueOf(prehospitalaria.getEdad()));
		txtCedulaInformante.setText(prehospitalaria.getInformante().getPersona().getCedula());
		txtNombreInformante.setText(prehospitalaria.getInformante().getPersona().getNombres() + " " + prehospitalaria.getInformante().getPersona().getApellidos());
		txtCondicionLlegada.setText(prehospitalaria.getCondicionLlegada().getCondicionLlegada());
		txtDireccionEvento.setText(prehospitalaria.getDireccionEvento());
		txtLugarEvento.setText(prehospitalaria.getLugarEvento());
		txtInterrogatorio.setText(prehospitalaria.getInterrogatorio());
		List<ControlVehiculo> lista = controlDAO.buscarPorPrehospitalaria(prehospitalaria.getIdPrehospitalaria());
		if(lista.size() > 0) {//tiene datos y solo se modifica
			control = lista.get(0);
			txtNoReporte.setText(control.getnReporte());
			tmHorasalida.setValue(control.getHoraSalidaBase());
			tmHoraLlegada.setValue(control.getHoraLlegadaEmergencia());
			tmHoraSalidaDeEmergencia.setValue(control.getHoraSalidaEmergencia());
			tmHoraLlegadaHospital.setValue(control.getHoraLlegadaHospital());
			tmHoraSalidaRayosX.setValue(control.getHoraSalidaRayosX());
			tmHoraLlegadaRayosX.setValue(control.getHoraLlegadaDeRayosX());
			tmHoraRetornoRayosX.setValue(control.getHoraRetornoDeRayosX());
			tmHoraLlegadaDeRayosX.setValue(control.getHoraLlegadaDeRayosX());
			tmHoraSalidaHospital.setValue(control.getHoraSalidaDelHospital());
			tmHoraLlegadaCentral.setValue(control.getHoraLlegadaCentral());
			txtNovedades.setText(control.getNovedades());
			cboVehiculo.setText(control.getVehiculo().getTipoVehiculo().getTipoVehiculo() + " " + control.getVehiculo().getCodigo());
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
								control.setPrehospitalaria(prehospitalaria);
								controlDAO.getEntityManager().persist(control);
							}else {
								controlDAO.getEntityManager().merge(control);
							}
							controlDAO.getEntityManager().getTransaction().commit();
							Clients.showNotification("Proceso Ejecutado con exito.");
							BindUtils.postGlobalCommand(null, null, "Prehospitalaria.buscarSinControlVehiculo", null);
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
			List<PersonalPrehospitalaria> listaPersonalPrehospitalario = personalPrehospitalariaDAO.buscarPorEmergencia(con.getPrehospitalaria().getIdPrehospitalaria());
			for(PersonalPrehospitalaria per : listaPersonalPrehospitalario) {
				personal = personal + "   - <b>" + per.getBombero().getGrado() + " " + per.getBombero().getPersona().getNombres() + " " + per.getBombero().getPersona().getApellidos() + "</b><br/>";
			}
			int milisegundos = (int)(con.getHoraLlegadaCentral().getTime() - con.getHoraLlegadaEmergencia().getTime());
			
			int minutos = (int) ((milisegundos / (1000*60)) % 60); 
			int horas = (int) ((milisegundos / (1000*60*60)) % 24);
			
			SimpleDateFormat formatFecha = new SimpleDateFormat("dd-MMMMMMM-yyyy");
			
			String informe = "INFORME No. " + con.getnReporte() + " DE EVENTO POR EMERGENCIA";
			String resumen = "En el dia <b>" + formatFecha.format(con.getPrehospitalaria().getFechaEvento()) + "</b> a las <b>" + 
					con.getHoraSalidaBase() + "</b> se reportó la emergencia <b>" + tipoEmergencia + 
					"</b> de tipo <b>" + con.getPrehospitalaria().getTipoEmergencia().getTipoEmergencia() + "</b>, sucedido en <b>" + con.getPrehospitalaria().getDireccionEvento() + 
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
		control.setHoraLlegadaCentral(new Time(tmHoraLlegadaCentral.getValue().getTime()));
		control.setHoraLlegadaEmergencia(new Time(tmHoraLlegada.getValue().getTime()));
		Date horaActual = new Date();
		control.setHoraReporte(new Time(horaActual.getTime()));
		control.setHoraSalidaBase(new Time(tmHorasalida.getValue().getTime()));
		control.setNovedades(txtNovedades.getText());
		if(tmHoraLlegadaDeRayosX.getValue() != null)
			control.setHoraLlegadaDeRayosX(new Time(tmHoraLlegadaDeRayosX.getValue().getTime()));
		if(tmHoraLlegadaHospital.getValue() != null)
			control.setHoraLlegadaHospital(new Time(tmHoraLlegadaHospital.getValue().getTime()));
		if(tmHoraLlegadaRayosX.getValue() != null)
			control.setHoraLlegadaRayosX(new Time(tmHoraLlegadaRayosX.getValue().getTime()));
		if(tmHoraRetornoRayosX.getValue() != null)
			control.setHoraRetornoDeRayosX(new Time(tmHoraRetornoRayosX.getValue().getTime()));
		if(tmHoraSalidaHospital.getValue() != null)
			control.setHoraSalidaDelHospital(new Time(tmHoraSalidaHospital.getValue().getTime()));
		if(tmHoraSalidaDeEmergencia.getValue() != null)
			control.setHoraSalidaEmergencia(new Time(tmHoraSalidaDeEmergencia.getValue().getTime()));
		if(tmHoraSalidaRayosX.getValue() != null)
			control.setHoraSalidaRayosX(new Time(tmHoraSalidaRayosX.getValue().getTime()));
		
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
			if(tmHoraLlegadaCentral.getValue() == null) {
				Clients.showNotification("Debe registrar Hora de llegada a la central","info",tmHoraLlegadaCentral,"end_center",2000);
				return false;
			}
			return band;
		}catch(Exception ex) {
			return false;
		}
	}
	@Command
	public void salir() {
		winRegistroPrehospitalario.detach();
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
	public Prehospitalaria getPrehospitalaria() {
		return prehospitalaria;
	}
	public void setPrehospitalaria(Prehospitalaria prehospitalaria) {
		this.prehospitalaria = prehospitalaria;
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
