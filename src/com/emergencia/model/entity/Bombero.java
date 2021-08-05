package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the bombero database table.
 * 
 */
@Entity
@NamedQuery(name="Bombero.findAll", query="SELECT b FROM Bombero b")
public class Bombero implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_bombero")
	private Integer idBombero;

	private String cargo;

	private String estado;

	private String grado;

	//bi-directional many-to-one association to EstadoCivil
	@ManyToOne
	@JoinColumn(name="id_estado_civil")
	private EstadoCivil estadoCivil;

	//bi-directional many-to-one association to TipoSangre
	@ManyToOne
	@JoinColumn(name="id_tipo_sangre")
	private TipoSangre tipoSangre;

	//bi-directional many-to-one association to Emergencia
	@OneToMany(mappedBy="bombero")
	private List<Emergencia> emergencias;

	//bi-directional many-to-one association to Persona
	@ManyToOne
	@JoinColumn(name="id_persona")
	private Persona persona;

	public Bombero() {
	}

	public Integer getIdBombero() {
		return this.idBombero;
	}

	public void setIdBombero(Integer idBombero) {
		this.idBombero = idBombero;
	}

	public String getCargo() {
		return this.cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getGrado() {
		return this.grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public EstadoCivil getEstadoCivil() {
		return this.estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public TipoSangre getTipoSangre() {
		return this.tipoSangre;
	}

	public void setTipoSangre(TipoSangre tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public List<Emergencia> getEmergencias() {
		return this.emergencias;
	}

	public void setEmergencias(List<Emergencia> emergencias) {
		this.emergencias = emergencias;
	}

	public Emergencia addEmergencia(Emergencia emergencia) {
		getEmergencias().add(emergencia);
		emergencia.setBombero(this);

		return emergencia;
	}

	public Emergencia removeEmergencia(Emergencia emergencia) {
		getEmergencias().remove(emergencia);
		emergencia.setBombero(null);

		return emergencia;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}