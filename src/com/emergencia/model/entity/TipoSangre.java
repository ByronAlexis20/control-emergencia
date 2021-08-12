package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="tipo_sangre")
@NamedQuery(name="TipoSangre.findAll", query="SELECT t FROM TipoSangre t where t.estado = 'A'")
public class TipoSangre implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_sangre")
	private int idTipoSangre;

	private String estado;

	@Column(name="tipo_sangre")
	private String tipoSangre;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="tipoSangre")
	private List<Persona> personas;

	public TipoSangre() {
	}

	public int getIdTipoSangre() {
		return this.idTipoSangre;
	}

	public void setIdTipoSangre(int idTipoSangre) {
		this.idTipoSangre = idTipoSangre;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipoSangre() {
		return this.tipoSangre;
	}

	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public List<Persona> getPersonas() {
		return this.personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	public Persona addPersona(Persona persona) {
		getPersonas().add(persona);
		persona.setTipoSangre(this);

		return persona;
	}

	public Persona removePersona(Persona persona) {
		getPersonas().remove(persona);
		persona.setTipoSangre(null);

		return persona;
	}

}