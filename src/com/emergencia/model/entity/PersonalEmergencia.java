package com.emergencia.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="personal_emergencia")
@NamedQueries({
	@NamedQuery(name="PersonalEmergencia.buscarPorEmergencia", query="SELECT p FROM PersonalEmergencia p where p.emergencia.idEmergencia = :idEmergencia and p.estado = 'A'"),
	@NamedQuery(name="PersonalEmergencia.buscarPorBombero", query="SELECT p FROM PersonalEmergencia p where p.bombero.idUsuario = :idBombero and p.estado = 'A'"),
})
public class PersonalEmergencia implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_personal_emergencia")
	private Integer idPersonalEmergencia;
	
	@ManyToOne
	@JoinColumn(name="id_emergencia")
	private Emergencia emergencia;
	
	@ManyToOne
	@JoinColumn(name="id_bombero")
	private Usuario bombero;
	
	private String estado;

	public PersonalEmergencia() {
		super();
	}

	public PersonalEmergencia(Integer idPersonalEmergencia, Emergencia emergencia, Usuario bombero, String estado) {
		super();
		this.idPersonalEmergencia = idPersonalEmergencia;
		this.emergencia = emergencia;
		this.bombero = bombero;
		this.estado = estado;
	}

	public Integer getIdPersonalEmergencia() {
		return idPersonalEmergencia;
	}

	public void setIdPersonalEmergencia(Integer idPersonalEmergencia) {
		this.idPersonalEmergencia = idPersonalEmergencia;
	}

	public Emergencia getEmergencia() {
		return emergencia;
	}

	public void setEmergencia(Emergencia emergencia) {
		this.emergencia = emergencia;
	}

	public Usuario getBombero() {
		return bombero;
	}

	public void setBombero(Usuario bombero) {
		this.bombero = bombero;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
		
}