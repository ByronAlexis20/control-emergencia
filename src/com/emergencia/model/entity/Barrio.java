package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@NamedQuery(name="Barrio.findAll", query="SELECT b FROM Barrio b")
public class Barrio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_barrio")
	private Integer idBarrio;

	private String barrio;

	private String estado;

	//bi-directional many-to-one association to Emergencia
	@OneToMany(mappedBy="barrio")
	private List<Emergencia> emergencias;

	public Barrio() {
	}

	public Integer getIdBarrio() {
		return this.idBarrio;
	}

	public void setIdBarrio(Integer idBarrio) {
		this.idBarrio = idBarrio;
	}

	public String getBarrio() {
		return this.barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Emergencia> getEmergencias() {
		return this.emergencias;
	}

	public void setEmergencias(List<Emergencia> emergencias) {
		this.emergencias = emergencias;
	}

	public Emergencia addEmergencia(Emergencia emergencia) {
		getEmergencias().add(emergencia);
		emergencia.setBarrio(this);

		return emergencia;
	}

	public Emergencia removeEmergencia(Emergencia emergencia) {
		getEmergencias().remove(emergencia);
		emergencia.setBarrio(null);

		return emergencia;
	}

}