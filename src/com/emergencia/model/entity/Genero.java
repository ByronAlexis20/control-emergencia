package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@NamedQuery(name="Genero.findAll", query="SELECT g FROM Genero g")
public class Genero implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_genero")
	private Integer idGenero;

	private String estado;

	private String genero;

	//bi-directional many-to-one association to Prehospitalaria
	@OneToMany(mappedBy="genero")
	private List<Prehospitalaria> prehospitalarias;

	public Genero() {
	}

	public Integer getIdGenero() {
		return this.idGenero;
	}

	public void setIdGenero(Integer idGenero) {
		this.idGenero = idGenero;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getGenero() {
		return this.genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public List<Prehospitalaria> getPrehospitalarias() {
		return this.prehospitalarias;
	}

	public void setPrehospitalarias(List<Prehospitalaria> prehospitalarias) {
		this.prehospitalarias = prehospitalarias;
	}

	public Prehospitalaria addPrehospitalaria(Prehospitalaria prehospitalaria) {
		getPrehospitalarias().add(prehospitalaria);
		prehospitalaria.setGenero(this);

		return prehospitalaria;
	}

	public Prehospitalaria removePrehospitalaria(Prehospitalaria prehospitalaria) {
		getPrehospitalarias().remove(prehospitalaria);
		prehospitalaria.setGenero(null);

		return prehospitalaria;
	}

}