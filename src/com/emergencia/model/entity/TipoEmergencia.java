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
@NamedQueries({
	@NamedQuery(name="TipoEmergencia.findAll", query="SELECT t FROM TipoEmergencia t where t.estado = 'A'"),
	@NamedQuery(name="TipoEmergencia.buscarPorPatron", query="SELECT t FROM TipoEmergencia t where lower(t.descripcion) like lower(:patron) and t.estado = 'A'")
})
public class TipoEmergencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_emergencia")
	private Integer idTipoEmergencia;

	private String descripcion;

	private String estado;
	
	private String grupo;

	@Column(name="tipo_emergencia")
	private String tipoEmergencia;

	//bi-directional many-to-one association to Emergencia
	@OneToMany(mappedBy="tipoEmergencia")
	private List<Emergencia> emergencias;

	//bi-directional many-to-one association to PreHospitalaria
	@OneToMany(mappedBy="tipoEmergencia")
	private List<Prehospitalaria> prehospitalarias;
		
	public TipoEmergencia() {
	}

	public Integer getIdTipoEmergencia() {
		return this.idTipoEmergencia;
	}

	public void setIdTipoEmergencia(Integer idTipoEmergencia) {
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

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public List<Prehospitalaria> getPrehospitalarias() {
		return prehospitalarias;
	}

	public void setPrehospitalarias(List<Prehospitalaria> prehospitalarias) {
		this.prehospitalarias = prehospitalarias;
	}

	public Prehospitalaria addPrehospitalaria(Prehospitalaria prehospitalaria) {
		getPrehospitalarias().add(prehospitalaria);
		prehospitalaria.setTipoEmergencia(this);

		return prehospitalaria;
	}

	public Prehospitalaria removePrehospitalaria(Prehospitalaria prehodpitalaria) {
		getPrehospitalarias().remove(prehodpitalaria);
		prehodpitalaria.setTipoEmergencia(null);

		return prehodpitalaria;
	}
}