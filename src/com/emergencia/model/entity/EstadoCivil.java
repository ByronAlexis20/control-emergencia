package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="estado_civil")
@NamedQuery(name="EstadoCivil.findAll", query="SELECT e FROM EstadoCivil e")
public class EstadoCivil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_estado_civil")
	private Integer idEstadoCivil;

	private String estado;

	@Column(name="estado_civil")
	private String estadoCivil;

	//bi-directional many-to-one association to Bombero
	@OneToMany(mappedBy="estadoCivil")
	private List<Bombero> bomberos;

	public EstadoCivil() {
	}

	public Integer getIdEstadoCivil() {
		return this.idEstadoCivil;
	}

	public void setIdEstadoCivil(Integer idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoCivil() {
		return this.estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public List<Bombero> getBomberos() {
		return this.bomberos;
	}

	public void setBomberos(List<Bombero> bomberos) {
		this.bomberos = bomberos;
	}

	public Bombero addBombero(Bombero bombero) {
		getBomberos().add(bombero);
		bombero.setEstadoCivil(this);

		return bombero;
	}

	public Bombero removeBombero(Bombero bombero) {
		getBomberos().remove(bombero);
		bombero.setEstadoCivil(null);

		return bombero;
	}

}