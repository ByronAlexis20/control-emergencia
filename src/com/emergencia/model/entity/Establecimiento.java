package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@NamedQuery(name="Establecimiento.findAll", query="SELECT e FROM Establecimiento e")
public class Establecimiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_establecimiento")
	private Integer idEstablecimiento;

	private String direccion;

	private String establecimiento;

	private String estado;

	//bi-directional many-to-one association to Prehospitalaria
	@OneToMany(mappedBy="establecimiento")
	private List<Prehospitalaria> prehospitalarias;

	public Establecimiento() {
	}

	public Integer getIdEstablecimiento() {
		return this.idEstablecimiento;
	}

	public void setIdEstablecimiento(Integer idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEstablecimiento() {
		return this.establecimiento;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
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
		prehospitalaria.setEstablecimiento(this);

		return prehospitalaria;
	}

	public Prehospitalaria removePrehospitalaria(Prehospitalaria prehospitalaria) {
		getPrehospitalarias().remove(prehospitalaria);
		prehospitalaria.setEstablecimiento(null);

		return prehospitalaria;
	}

}