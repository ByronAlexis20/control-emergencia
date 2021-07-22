package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="localizacion_lesion")
@NamedQuery(name="LocalizacionLesion.findAll", query="SELECT l FROM LocalizacionLesion l")
public class LocalizacionLesion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_localizacion_lesion")
	private Integer idLocalizacionLesion;

	private String estado;

	@Column(name="lugar_involucrada")
	private String lugarInvolucrada;

	//bi-directional many-to-one association to Lesion
	@ManyToOne
	@JoinColumn(name="id_lesion")
	private Lesion lesion;

	//bi-directional many-to-one association to Prehospitalaria
	@ManyToOne
	@JoinColumn(name="id_prehospitalaria")
	private Prehospitalaria prehospitalaria;

	public LocalizacionLesion() {
	}

	public Integer getIdLocalizacionLesion() {
		return this.idLocalizacionLesion;
	}

	public void setIdLocalizacionLesion(Integer idLocalizacionLesion) {
		this.idLocalizacionLesion = idLocalizacionLesion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getLugarInvolucrada() {
		return this.lugarInvolucrada;
	}

	public void setLugarInvolucrada(String lugarInvolucrada) {
		this.lugarInvolucrada = lugarInvolucrada;
	}

	public Lesion getLesion() {
		return this.lesion;
	}

	public void setLesion(Lesion lesion) {
		this.lesion = lesion;
	}

	public Prehospitalaria getPrehospitalaria() {
		return this.prehospitalaria;
	}

	public void setPrehospitalaria(Prehospitalaria prehospitalaria) {
		this.prehospitalaria = prehospitalaria;
	}

}