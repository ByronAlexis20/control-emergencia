package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="forma_aviso")
@NamedQuery(name="FormaAviso.findAll", query="SELECT f FROM FormaAviso f")
public class FormaAviso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_forma_aviso")
	private Integer idFormaAviso;

	private String estado;

	@Column(name="forma_aviso")
	private String formaAviso;

	//bi-directional many-to-one association to Emergencia
	@OneToMany(mappedBy="formaAviso")
	private List<Emergencia> emergencias;

	public FormaAviso() {
	}

	public Integer getIdFormaAviso() {
		return this.idFormaAviso;
	}

	public void setIdFormaAviso(Integer idFormaAviso) {
		this.idFormaAviso = idFormaAviso;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFormaAviso() {
		return this.formaAviso;
	}

	public void setFormaAviso(String formaAviso) {
		this.formaAviso = formaAviso;
	}

	public List<Emergencia> getEmergencias() {
		return this.emergencias;
	}

	public void setEmergencias(List<Emergencia> emergencias) {
		this.emergencias = emergencias;
	}

	public Emergencia addEmergencia(Emergencia emergencia) {
		getEmergencias().add(emergencia);
		emergencia.setFormaAviso(this);

		return emergencia;
	}

	public Emergencia removeEmergencia(Emergencia emergencia) {
		getEmergencias().remove(emergencia);
		emergencia.setFormaAviso(null);

		return emergencia;
	}

}