package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="usuario")
@NamedQueries({
	@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u"),
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

	//bi-directional many-to-one association to ResponsableVehiculo
	@OneToMany(mappedBy="usuario")
	private List<ResponsableVehiculo> responsableVehiculos;

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

	public List<ResponsableVehiculo> getResponsableVehiculos() {
		return this.responsableVehiculos;
	}

	public void setResponsableVehiculos(List<ResponsableVehiculo> responsableVehiculos) {
		this.responsableVehiculos = responsableVehiculos;
	}

	public ResponsableVehiculo addResponsableVehiculo(ResponsableVehiculo responsableVehiculo) {
		getResponsableVehiculos().add(responsableVehiculo);
		responsableVehiculo.setUsuario(this);

		return responsableVehiculo;
	}

	public ResponsableVehiculo removeResponsableVehiculo(ResponsableVehiculo responsableVehiculo) {
		getResponsableVehiculos().remove(responsableVehiculo);
		responsableVehiculo.setUsuario(null);

		return responsableVehiculo;
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