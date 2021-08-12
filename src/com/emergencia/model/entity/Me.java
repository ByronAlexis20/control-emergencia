package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="mes")
@NamedQuery(name="Me.findAll", query="SELECT m FROM Me m")
public class Me implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_mes")
	private int idMes;

	private String estado;

	private String mes;

	//bi-directional many-to-one association to Emergencia
	@OneToMany(mappedBy="me")
	private List<Emergencia> emergencias;

	public Me() {
	}

	public int getIdMes() {
		return this.idMes;
	}

	public void setIdMes(int idMes) {
		this.idMes = idMes;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMes() {
		return this.mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public List<Emergencia> getEmergencias() {
		return this.emergencias;
	}

	public void setEmergencias(List<Emergencia> emergencias) {
		this.emergencias = emergencias;
	}

	public Emergencia addEmergencia(Emergencia emergencia) {
		getEmergencias().add(emergencia);
		emergencia.setMe(this);

		return emergencia;
	}

	public Emergencia removeEmergencia(Emergencia emergencia) {
		getEmergencias().remove(emergencia);
		emergencia.setMe(null);

		return emergencia;
	}

}