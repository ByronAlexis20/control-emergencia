package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipo_emergencia database table.
 * 
 */
@Entity
@Table(name="tipo_emergencia")
@NamedQuery(name="TipoEmergencia.findAll", query="SELECT t FROM TipoEmergencia t")
public class TipoEmergencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_emergencia")
	private int idTipoEmergencia;

	private String descripcion;

	private String estado;

	@Column(name="tipo_emergencia")
	private String tipoEmergencia;

	//bi-directional many-to-one association to Emergencia
	@OneToMany(mappedBy="tipoEmergencia")
	private List<Emergencia> emergencias;

	public TipoEmergencia() {
	}

	public int getIdTipoEmergencia() {
		return this.idTipoEmergencia;
	}

	public void setIdTipoEmergencia(int idTipoEmergencia) {
		this.idTipoEmergencia = idTipoEmergencia;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipoEmergencia() {
		return this.tipoEmergencia;
	}

	public void setTipoEmergencia(String tipoEmergencia) {
		this.tipoEmergencia = tipoEmergencia;
	}

	public List<Emergencia> getEmergencias() {
		return this.emergencias;
	}

	public void setEmergencias(List<Emergencia> emergencias) {
		this.emergencias = emergencias;
	}

	public Emergencia addEmergencia(Emergencia emergencia) {
		getEmergencias().add(emergencia);
		emergencia.setTipoEmergencia(this);

		return emergencia;
	}

	public Emergencia removeEmergencia(Emergencia emergencia) {
		getEmergencias().remove(emergencia);
		emergencia.setTipoEmergencia(null);

		return emergencia;
	}

}