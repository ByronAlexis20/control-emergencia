package com.emergencia.control.registros;

import java.util.ArrayList;
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
import org.zkoss.zul.Window;

import com.emergencia.model.dao.EstadoCivilDAO;
import com.emergencia.model.dao.PerfilDAO;
import com.emergencia.model.dao.TipoSangreDAO;
import com.emergencia.model.dao.UsuarioDAO;
import com.emergencia.model.entity.EstadoCivil;
import com.emergencia.model.entity.Perfil;
import com.emergencia.model.entity.Persona;
import com.emergencia.model.entity.TipoSangre;
import com.emergencia.model.entity.Usuario;
import com.emergencia.util.ControllerHelper;
import com.emergencia.util.Globales;

public class BomberoEditar {
	@Wire private Window winBomberoEditar;
	@Wire private Combobox cboPerfil;
	@Wire private Combobox cboEstadoCivil;
	@Wire private Combobox cboTipoSangre;
	@Wire private Textbox txtNombres;
	@Wire private Textbox txtApellidos;
	@Wire private Textbox txtNoDocumento;
	@Wire private Textbox txtTelefono;
	@Wire private Textbox txtDireccion;
	@Wire private Textbox txtCorreo;
	@Wire private Datebox dtpFechaNacimiento;
	
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
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		usuario = (Usuario) Executions.getCurrent().getArg().get("Bombero");
		if (usuario == null) {
			usuario = new Usuario();
			usuario.setEstado("A");
			persona = new Persona();
			persona.setEstado("A");
			usuario.setPersona(persona);
			perfilSeleccionado = null;
			estadoCivilSeleccionado = null;
		}else {
			persona = usuario.getPersona();
			perfilSeleccionado = usuario.getPerfil();
			estadoCivilSeleccionado = usuario.getPersona().getEstadoCivil();
			cboPerfil.setText(usuario.getPerfil().getNombre());
			cboEstadoCivil.setText(usuario.getPersona().getEstadoCivil().getEstadoCivil());
			dtpFechaNacimiento.setValue(usuario.getPersona().getFechaNacimiento());
			tipoSangreSeleccionado = usuario.getPersona().getTipoSangre();
			cboTipoSangre.setText(usuario.getPersona().getTipoSangre().getTipoSangre());
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
						usuario.setPerfil((Perfil)cboPerfil.getSelectedItem().getValue());
						usuarioDAO.getEntityManager().getTransaction().begin();			
						if (usuario.getIdUsuario() == null) {
							persona.setFechaNacimiento(dtpFechaNacimiento.getValue());
							persona.setEstadoCivil((EstadoCivil)cboEstadoCivil.getSelectedItem().getValue());
							persona.setTipoSangre((TipoSangre)cboTipoSangre.getSelectedItem().getValue());
							usuario.setUsuario(persona.getCedula());
							usuario.setClave(helper.encriptar(persona.getCedula()));
							usuario.setPersona(persona);
							List<Usuario> lista = new ArrayList<>();
							lista.add(usuario);
							persona.setUsuarios(lista);
							usuarioDAO.getEntityManager().persist(persona);
						}else{
							usuario.setUsuario(usuario.getPersona().getCedula());
							usuario.setClave(helper.encriptar(usuario.getPersona().getCedula()));
							usuario.getPersona().setEstadoCivil((EstadoCivil)cboEstadoCivil.getSelectedItem().getValue());
							usuario.getPersona().setTipoSangre((TipoSangre)cboTipoSangre.getSelectedItem().getValue());
							usuario.getPersona().setFechaNacimiento(dtpFechaNacimiento.getValue());
							usuario = (Usuario) usuarioDAO.getEntityManager().merge(usuario);
							usuarioDAO.getEntityManager().merge(persona);
						}
						usuarioDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						BindUtils.postGlobalCommand(null, null, "Usuario.buscarBomberoPorPatron", null);
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
		int idUsuario;
		if(usuario.getIdUsuario() != null)
			idUsuario = usuario.getIdUsuario();
		else
			idUsuario = 0;
		if(usuarioDAO.getBuscarUsuario(persona.getCedula(),idUsuario).size() > 0) {
			Clients.showNotification("Usuario ya existe");
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
			if(ls.getIdPerfil() == Globales.codigoBomberoRentado)
				listaRetornar.add(ls);
			else if(ls.getIdPerfil() == Globales.codigoBomberoVoluntario)
				listaRetornar.add(ls);
			else if(ls.getIdPerfil() == Globales.codigoJefeGuardia){
				listaRetornar.add(ls);
			}
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
		BindUtils.postGlobalCommand(null, null, "Usuario.buscarBomberoPorPatron", null);
		winBomberoEditar.detach();
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
