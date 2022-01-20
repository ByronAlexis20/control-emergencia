package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the estado_civil database table.
 * 
 */
@Entity
@Table(name="estado_civil")
@NamedQueries({
	@NamedQuery(name="EstadoCivil.findAll", query="SELECT e FROM EstadoCivil e where e.estado = 'A'"),
	@NamedQuery(name="EstadoCivil.buscarPorPatron", query="SELECT e FROM EstadoCivil e where lower(e.estadoCivil) like lower(:patron)"),
	@NamedQuery(name="EstadoCivil.buscarPorId", query="SELECT e FROM EstadoCivil e where e.idEstadoCivil = :id"),
	@NamedQuery(name="EstadoCivil.buscarPorNombre", query="SELECT e FROM EstadoCivil e where lower(e.estadoCivil) = lower(:patron)"),
	@NamedQuery(name="EstadoCivil.buscarPorNombreDiferenteId", query="SELECT e FROM EstadoCivil e where lower(e.estadoCivil) = lower(:patron) and e.idEstadoCivil <> :id"),
})
public class EstadoCivil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_estado_civil")
	private Integer idEstadoCivil;

	private String estado;

	@Column(name="estado_civil")
	private String estadoCivil;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="estadoCivil")
	private List<Persona> personas;

	public EstadoCivil() {
	}

	public Integer getIdEstadoCivil() {
		return this.idEstadoCivil;
	}

	public void setIdEstadoCivil(Integer idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoCivil() {
		return this.estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public List<Persona> getPersonas() {
		return this.personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	public Persona addPersona(Persona persona) {
		getPersonas().add(persona);
		persona.setEstadoCivil(this);

		return persona;
	}

	public Persona removePersona(Persona persona) {
		getPersonas().remove(persona);
		persona.setEstadoCivil(null);

		return persona;
	}

}