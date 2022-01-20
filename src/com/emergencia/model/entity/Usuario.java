package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="usuario")
@NamedQueries({
	@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u"),
	@NamedQuery(name="Usuario.buscarPorId", query="SELECT u FROM Usuario u where u.idUsuario = :id"),
	@NamedQuery(name="Usuario.buscaUsuario", 
	query="SELECT u FROM Usuario u WHERE u.usuario = :nombreUsuario and u.estado = 'A'"),
	@NamedQuery(name="Usuario.buscarPorPatron", query="SELECT u FROM Usuario u where (lower(u.persona.nombres) "
			+ "like(:patron) or lower(u.persona.apellidos) like(:patron)) and u.estado = 'A' and u.perfil.idPerfil IN (1,2,3)"),
	@NamedQuery(name="Usuario.buscarPorCedula", query="SELECT u FROM Usuario u where u.persona.cedula = :cedula and u.estado = 'A'"),
	@NamedQuery(name="Usuario.buscarPorCedulaDiferenteAlUsuarioActual", query="SELECT u FROM Usuario u where u.persona.cedula = :cedula and u.estado = 'A' "
			+ "and u.idUsuario <> :idUsuario"),
	@NamedQuery(name="Usuario.buscarPorUsuario", query="SELECT s FROM Usuario s where s.usuario = :patron and s.idUsuario <> :idUsuario and s.estado = 'A'"),
	@NamedQuery(name="Usuario.buscarUsuarioPorCedula", query="SELECT s FROM Usuario s where s.persona.cedula = :cedula and s.estado = 'A'"),
	@NamedQuery(name="Usuario.buscarBomberoPorPatron", query="SELECT u FROM Usuario u where (lower(u.persona.nombres) "
			+ "like(:patron) or lower(u.persona.apellidos) like(:patron)) and u.estado = 'A' and u.perfil.idPerfil IN (4,6)"),
	@NamedQuery(name="Usuario.buscarChoferPorPatron", query="SELECT u FROM Usuario u where (lower(u.persona.nombres) "
			+ "like(:patron) or lower(u.persona.apellidos) like(:patron)) and u.estado = 'A' and u.perfil.idPerfil = 5"),
	@NamedQuery(name="Usuario.buscarBomberoEmergencias", query="SELECT u FROM Usuario u where u.estado = 'A' and u.perfil.idPerfil IN (3,4,6)"),
})
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_usuario")
	private Integer idUsuario;

	private String clave;

	private String estado;

	private String foto;

	private String grado;

	private String usuario;

	//bi-directional many-to-one association to Emergencia
	@OneToMany(mappedBy="usuario")
	private List<Emergencia> emergencias;

	@OneToMany(mappedBy="informante")
	private List<Prehospitalaria> prehospitalaria;
	
	@OneToMany(mappedBy="chofer")
	private List<ControlVehiculo> controlvehiculoChofer;

	@OneToMany(mappedBy="cuartelero")
	private List<ControlVehiculo> controlvehiculoCuartelero;
	
	@OneToMany(mappedBy="bomberoRecibe")
	private List<ControlVehiculo> controlvehiculoBomberoRecibe;
	
	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="id_perfil")
	private Perfil perfil;

	//bi-directional many-to-one association to Persona
	@ManyToOne
	@JoinColumn(name="id_persona")
	private Persona persona;

	public Usuario() {
	}

	public Integer getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getGrado() {
		return this.grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<Emergencia> getEmergencias() {
		return this.emergencias;
	}

	public void setEmergencias(List<Emergencia> emergencias) {
		this.emergencias = emergencias;
	}

	public Emergencia addEmergencia(Emergencia emergencia) {
		getEmergencias().add(emergencia);
		emergencia.setUsuario(this);

		return emergencia;
	}

	public Emergencia removeEmergencia(Emergencia emergencia) {
		getEmergencias().remove(emergencia);
		emergencia.setUsuario(null);

		return emergencia;
	}

	public List<ControlVehiculo> getControlvehiculoChofer() {
		return controlvehiculoChofer;
	}

	public void setControlvehiculoChofer(List<ControlVehiculo> controlvehiculoChofer) {
		this.controlvehiculoChofer = controlvehiculoChofer;
	}

	public ControlVehiculo addControlVehiculoChofer(ControlVehiculo controlVehiculo) {
		getControlvehiculoChofer().add(controlVehiculo);
		controlVehiculo.setChofer(this);

		return controlVehiculo;
	}

	public ControlVehiculo removeControlVehiculoChofer(ControlVehiculo controlVehiculo) {
		getControlvehiculoChofer().remove(controlVehiculo);
		controlVehiculo.setChofer(null);

		return controlVehiculo;
	}

	public List<ControlVehiculo> getControlvehiculoCuartelero() {
		return controlvehiculoCuartelero;
	}

	public void setControlvehiculoCuartelero(List<ControlVehiculo> controlvehiculoCuartelero) {
		this.controlvehiculoCuartelero = controlvehiculoCuartelero;
	}

	public ControlVehiculo addControlVehiculoCuartelero(ControlVehiculo controlVehiculo) {
		getControlvehiculoCuartelero().add(controlVehiculo);
		controlVehiculo.setCuartelero(this);

		return controlVehiculo;
	}

	public ControlVehiculo removeControlVehiculoCuartelero(ControlVehiculo controlVehiculo) {
		getControlvehiculoCuartelero().remove(controlVehiculo);
		controlVehiculo.setCuartelero(null);

		return controlVehiculo;
	}
	
	public List<ControlVehiculo> getControlvehiculoBomberoRecibe() {
		return controlvehiculoBomberoRecibe;
	}

	public void setControlvehiculoBomberoRecibe(List<ControlVehiculo> controlvehiculoBomberoRecibe) {
		this.controlvehiculoBomberoRecibe = controlvehiculoBomberoRecibe;
	}

	public ControlVehiculo addControlVehiculoBomberoRecibe(ControlVehiculo controlVehiculo) {
		getControlvehiculoBomberoRecibe().add(controlVehiculo);
		controlVehiculo.setBomberoRecibe(this);

		return controlVehiculo;
	}

	public ControlVehiculo removeControlVehiculoBomberoRecibe(ControlVehiculo controlVehiculo) {
		getControlvehiculoBomberoRecibe().remove(controlVehiculo);
		controlVehiculo.setBomberoRecibe(null);

		return controlVehiculo;
	}
	
	public List<Prehospitalaria> getPrehospitalaria() {
		return prehospitalaria;
	}

	public void setPrehospitalaria(List<Prehospitalaria> prehospitalaria) {
		this.prehospitalaria = prehospitalaria;
	}
	
	public Prehospitalaria addPrehospitalaria(Prehospitalaria prehospitalaria) {
		getPrehospitalaria().add(prehospitalaria);
		prehospitalaria.setInformante(this);

		return prehospitalaria;
	}

	public Prehospitalaria removePrehospitalaria(Prehospitalaria prehospitalaria) {
		getPrehospitalaria().remove(prehospitalaria);
		prehospitalaria.setInformante(null);

		return prehospitalaria;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}