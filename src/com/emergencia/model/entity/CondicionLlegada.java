package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="condicion_llegada")
@NamedQuery(name="CondicionLlegada.findAll", query="SELECT c FROM CondicionLlegada c")
public class CondicionLlegada implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_condicion_llegada")
	private Integer idCondicionLlegada;

	@Column(name="condicion_llegada")
	private String condicionLlegada;

	private String estado;

	//bi-directional many-to-one association to Prehospitalaria
	@OneToMany(mappedBy="condicionLlegada")
	private List<Prehospitalaria> prehospitalarias;

	public CondicionLlegada() {
	}

	public Integer getIdCondicionLlegada() {
		return this.idCondicionLlegada;
	}

	public void setIdCondicionLlegada(Integer idCondicionLlegada) {
		this.idCondicionLlegada = idCondicionLlegada;
	}

	public String getCondicionLlegada() {
		return this.condicionLlegada;
	}

	public void setCondicionLlegada(String condicionLlegada) {
		this.condicionLlegada = condicionLlegada;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Prehospitalaria> getPrehospitalarias() {
		return this.prehospitalarias;
	}

	public void setPrehospitalarias(List<Prehospitalaria> prehospitalarias) {
		this.prehospitalarias = prehospitalarias;
	}

	public Prehospitalaria addPrehospitalaria(Prehospitalaria prehospitalaria) {
		getPrehospitalarias().add(prehospitalaria);
		prehospitalaria.setCondicionLlegada(this);

		return prehospitalaria;
	}

	public Prehospitalaria removePrehospitalaria(Prehospitalaria prehospitalaria) {
		getPrehospitalarias().remove(prehospitalaria);
		prehospitalaria.setCondicionLlegada(null);

		return prehospitalaria;
	}

}