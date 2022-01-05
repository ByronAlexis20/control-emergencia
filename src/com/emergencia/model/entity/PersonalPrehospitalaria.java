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
@Table(name="personal_prehospitalaria")
@NamedQueries({
	@NamedQuery(name="PersonalPrehospitalaria.buscarPorEmergencia", query="SELECT p FROM PersonalPrehospitalaria p where p.estado = 'A' and p.prehospitalaria.idPrehospitalaria = :idEmergencia")
})
public class PersonalPrehospitalaria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_personal_prehospitalaria")
	private Integer idPersonalPrehospitalaria;
	
	@ManyToOne
	@JoinColumn(name="id_prehospitalaria")
	private Prehospitalaria prehospitalaria;
	
	@ManyToOne
	@JoinColumn(name="id_bombero")
	private Usuario bombero;
	
	private String estado;

	public PersonalPrehospitalaria() {
		super();
	}

	public PersonalPrehospitalaria(Integer idPersonalPrehospitalaria, Prehospitalaria prehospitalaria, Usuario bombero,
			String estado) {
		super();
		this.idPersonalPrehospitalaria = idPersonalPrehospitalaria;
		this.prehospitalaria = prehospitalaria;
		this.bombero = bombero;
		this.estado = estado;
	}

	public Integer getIdPersonalPrehospitalaria() {
		return idPersonalPrehospitalaria;
	}

	public void setIdPersonalPrehospitalaria(Integer idPersonalPrehospitalaria) {
		this.idPersonalPrehospitalaria = idPersonalPrehospitalaria;
	}

	public Prehospitalaria getPrehospitalaria() {
		return prehospitalaria;
	}

	public void setPrehospitalaria(Prehospitalaria prehospitalaria) {
		this.prehospitalaria = prehospitalaria;
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