package com.emergencia.control.seguridad;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.EstadoCivilDAO;
import com.emergencia.model.dao.PerfilDAO;
import com.emergencia.model.dao.PersonaDAO;
import com.emergencia.model.dao.TipoSangreDAO;
import com.emergencia.model.dao.UsuarioDAO;
import com.emergencia.model.entity.EstadoCivil;
import com.emergencia.model.entity.Perfil;
import com.emergencia.model.entity.Persona;
import com.emergencia.model.entity.TipoSangre;
import com.emergencia.model.entity.Usuario;
import com.emergencia.util.ControllerHelper;
import com.emergencia.util.Globales;


public class UsuarioEditar {
	@Wire private Window winUsuarioEditar;
	@Wire private Combobox cboPerfil;
	@Wire private Combobox cboEstadoCivil;
	@Wire private Combobox cboTipoSangre;
	@Wire private Textbox txtNombres;
	@Wire private Textbox txtApellidos;
	@Wire private Textbox txtNoDocumento;
	@Wire private Textbox txtTelefono;
	@Wire private Textbox txtUsuario;
	@Wire private Textbox txtDireccion;
	@Wire private Textbox txtClave;
	@Wire private Textbox txtCorreo;
	@Wire private Datebox dtpFechaNacimiento;
	@Wire private Checkbox chkEstado;
	@Wire private Row rowEstado;
	
	PerfilDAO perfilDAO = new PerfilDAO();
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	Usuario usuario;
	Perfil perfilSeleccionado;
	ControllerHelper helper = new ControllerHelper();
	Persona persona;
	EstadoCivilDAO estadoCivilDAO = new EstadoCivilDAO();
	EstadoCivil estadoCivilSeleccionado;
	TipoSangreDAO tipoSangreDAO = new TipoSangreDAO();
	TipoSangre tipoSangreSeleccionado;
	PersonaDAO personaDAO = new PersonaDAO();
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		chkEstado.setChecked(true);
		// Recupera el objeto pasado como parametro. 
		usuario = (Usuario) Executions.getCurrent().getArg().get("Usuario");
		if (usuario == null) {
			usuario = new Usuario();
			usuario.setEstado("A");
			persona = new Persona();
			persona.setEstado("A");
			usuario.setPersona(persona);
			perfilSeleccionado = null;
			estadoCivilSeleccionado = null;
			rowEstado.setVisible(false);
		}else {
			persona = usuario.getPersona();
			perfilSeleccionado = usuario.getPerfil();
			estadoCivilSeleccionado = usuario.getPersona().getEstadoCivil();
			cboPerfil.setText(usuario.getPerfil().getNombre());
			cboEstadoCivil.setText(usuario.getPersona().getEstadoCivil().getEstadoCivil());
			dtpFechaNacimiento.setValue(usuario.getPersona().getFechaNacimiento());
			tipoSangreSeleccionado = usuario.getPersona().getTipoSangre();
			cboTipoSangre.setText(usuario.getPersona().getTipoSangre().getTipoSangre());
			if(usuario.getEstado().equals("A")) {
				chkEstado.setChecked(true);
			}else {
				chkEstado.setChecked(false);
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar(){
		if(validarDatos() == false) {
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {
						if(chkEstado.isChecked())
							usuario.setEstado("A");
						else
							usuario.setEstado("I");
						
						usuario.setPerfil((Perfil)cboPerfil.getSelectedItem().getValue());
						usuario.setClave(helper.encriptar(txtClave.getText()));
						usuarioDAO.getEntityManager().getTransaction().begin();			
						if (usuario.getIdUsuario() == null) {
							persona.setFechaNacimiento(dtpFechaNacimiento.getValue());
							persona.setEstadoCivil((EstadoCivil)cboEstadoCivil.getSelectedItem().getValue());
							persona.setTipoSangre((TipoSangre)cboTipoSangre.getSelectedItem().getValue());
							usuario.setPersona(persona);
							List<Usuario> lista = new ArrayList<>();
							lista.add(usuario);
							persona.setUsuarios(lista);
							usuarioDAO.getEntityManager().persist(persona);
						}else{
							usuario.getPersona().setEstadoCivil((EstadoCivil)cboEstadoCivil.getSelectedItem().getValue());
							usuario.getPersona().setTipoSangre((TipoSangre)cboTipoSangre.getSelectedItem().getValue());
							usuario.getPersona().setFechaNacimiento(dtpFechaNacimiento.getValue());
							usuario = (Usuario) usuarioDAO.getEntityManager().merge(usuario);
							usuarioDAO.getEntityManager().merge(usuario.getPersona());
						}
						usuarioDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						BindUtils.postGlobalCommand(null, null, "Usuario.buscarPorPatron", null);
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						usuarioDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	
	public boolean validarDatos() {
		if(txtNoDocumento.getText().isEmpty()) {
			Clients.showNotification("Debe registrar el número de documento","info",txtNoDocumento,"end_center",2000);
			txtNoDocumento.focus();
			return false;
		}
		if(txtNoDocumento.getText().length() < 10) {
			Clients.showNotification("El número de cédula no tiene la cantidad de digitos obligatorios [10]","info",txtNoDocumento,"end_center",2000);
			txtNoDocumento.focus();
			return false;
		}
		if(!helper.validarDeCedula(txtNoDocumento.getText())) {
			Clients.showNotification("Número de CÉDULA NO VÁLIDA!","info",txtNoDocumento,"end_center",2000);
			txtNoDocumento.focus();
			return false;
		}
		if(usuario.getIdUsuario() != null) {
			if(!chkEstado.isChecked()) {
				Usuario us = usuarioDAO.buscarPorId(usuario.getIdUsuario());
				if(us != null) {
					if(us.getControlvehiculoChofer().size() > 0 || us.getEmergencias().size() > 0 || us.getPrehospitalaria().size() > 0) {
						Clients.showNotification("No se puede eliminar el registro, hay registros que dependen de éste.");
						return false;
					}
				}
			}
		}
		//validar que la cedula no la tenga otra persona
		if(persona != null) {
			if(persona.getIdPersona() != null) {
				List<Persona> listaPersona = this.personaDAO.buscarPorCedulaDiferenteId(txtNoDocumento.getText(), persona.getIdPersona());
				if(listaPersona.size() > 0) {
					Clients.showNotification("Número de cédula ya existe entre los registros","info",txtNoDocumento,"end_center",2000);
					txtNoDocumento.focus();
					return false;
				}
			}else {
				List<Persona> listaPersona = this.personaDAO.buscarPorCedula(txtNoDocumento.getText());
				if(listaPersona.size() > 0) {
					Clients.showNotification("Número de cédula ya existe entre los registros","info",txtNoDocumento,"end_center",2000);
					txtNoDocumento.focus();
					return false;
				}
			}
		}
		if(cboEstadoCivil.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar estado civil","info",cboEstadoCivil,"end_center",2000);
			return false;
		}
		if(cboTipoSangre.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar tipo de sangre","info",cboTipoSangre,"end_center",2000);
			return false;
		}
		if(cboPerfil.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar el Perfil de usuario","info",cboPerfil,"end_center",2000);
			return false;
		}
		if(dtpFechaNacimiento.getValue() == null) {
			Clients.showNotification("Debe registrar fecha de nacimiento","info",dtpFechaNacimiento,"end_center",2000);
			return false;
		}
		//validar edad entre 18 a 30 años
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
		Date fechaNacimiento = dtpFechaNacimiento.getValue();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate fechaNac = LocalDate.parse(formatoFecha.format(fechaNacimiento), fmt);
		LocalDate ahora = LocalDate.now();
		Period periodo = Period.between(fechaNac, ahora);
		int anio = periodo.getYears();
		if(anio < 18 || anio > 30) {
			Clients.showNotification("Solo puede registrar personas entre [18 - 30] años de edad","info",dtpFechaNacimiento,"end_center",2000);
			return false;
		}
		
		//luego preguntar si el numero de documento ya se encuentra sobre los registros
		if(validarUsuarioExistente() == true) {
			Clients.showNotification("Ya hay un Usuario con el número de documento " + txtNoDocumento.getText() + "!","info",txtNoDocumento,"end_center",2000);
			txtNoDocumento.focus();
			return false;
		}
		
		if(txtNombres.getText().isEmpty()) {
			Clients.showNotification("Debe registrar nombre del usuario","info",txtNombres,"end_center",2000);
			txtNombres.setFocus(true);
			return false;
		}
		if(txtApellidos.getText().isEmpty()) {
			Clients.showNotification("Debe registrar apellidos del usuario","info",txtApellidos,"end_center",2000);
			txtApellidos.setFocus(true);
			return false;
		}
		if(txtTelefono.getText().isEmpty()) {
			Clients.showNotification("Debe registrar Telefono del usuario","info",txtTelefono,"end_center",2000);
			txtTelefono.setFocus(true);
			return false;
		}
		//validar el email, el email es opcional, solo se valida si esta lleno
		if(!txtCorreo.getText().isEmpty()) {
			if(!ControllerHelper.validarEmail(txtCorreo.getText())) {
				Clients.showNotification("El correo ingresado no es valido","info",txtCorreo,"end_center",2000);
				txtCorreo.setFocus(true);
				return false;
			}
		}
		if(txtUsuario.getText().isEmpty()) {
			Clients.showNotification("Debe registrar Usuario del usuario","info",txtUsuario,"end_center",2000);
			txtUsuario.setFocus(true);
			return false;
		}
		int idUsuario;
		if(usuario.getIdUsuario() != null)
			idUsuario = usuario.getIdUsuario();
		else
			idUsuario = 0;
		if(usuarioDAO.getBuscarUsuario(txtUsuario.getText(),idUsuario).size() > 0) {
			Clients.showNotification("Usuario ya existe","info",txtUsuario,"end_center",2000);
			return false;
		}
		if(txtClave.getText().isEmpty()) {
			Clients.showNotification("Debe registrar Clave del usuario","info",txtClave,"end_center",2000);
			txtClave.setFocus(true);
			return false;
		}
		return true;
	}
	/** Validar si el usuario existe a traves de la cedula */
	private boolean validarUsuarioExistente() {
		try {
			boolean bandera = false;
			List<Usuario> listaUsuario;
			if (usuario.getIdUsuario() == null) {
				listaUsuario = usuarioDAO.getValidarUsuarioExistente(txtNoDocumento.getText().toString());
			}else {
				listaUsuario = usuarioDAO.getValidarUsuarioExistenteDiferente(txtNoDocumento.getText().toString(),usuario.getIdUsuario());
			}
			
			if(listaUsuario.size() != 0)
				bandera = true;
			else
				bandera = false;
			return bandera;
		}catch(Exception ex) {
			return false;
		}
	}

	public List<Perfil> getPerfiles(){
		List<Perfil> lista = perfilDAO.getPerfilesPorDescripcion("");
		List<Perfil> listaRetornar = new ArrayList<>();
		for(Perfil ls : lista) {
			if(ls.getIdPerfil() == Globales.codigoAdministrador)
				listaRetornar.add(ls);
			else if(ls.getIdPerfil() == Globales.codigoSecretaria)
				listaRetornar.add(ls);
			else if(ls.getIdPerfil() == Globales.codigoJefeGuardia)
				listaRetornar.add(ls);
		}
		return listaRetornar;
	}
	
	public List<EstadoCivil> getEstadosCiviles(){
		return estadoCivilDAO.obtenerEstadosCiviles();
	}
	
	public List<TipoSangre> getTiposSangres(){
		return tipoSangreDAO.obtenerTipoSangre();
	}
	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Usuario.buscarPorPatron", null);
		winUsuarioEditar.detach();
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Perfil getPerfilSeleccionado() {
		return perfilSeleccionado;
	}

	public void setPerfilSeleccionado(Perfil perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public EstadoCivil getEstadoCivilSeleccionado() {
		return estadoCivilSeleccionado;
	}

	public void setEstadoCivilSeleccionado(EstadoCivil estadoCivilSeleccionado) {
		this.estadoCivilSeleccionado = estadoCivilSeleccionado;
	}

	public TipoSangre getTipoSangreSeleccionado() {
		return tipoSangreSeleccionado;
	}

	public void setTipoSangreSeleccionado(TipoSangre tipoSangreSeleccionado) {
		this.tipoSangreSeleccionado = tipoSangreSeleccionado;
	}
	
}