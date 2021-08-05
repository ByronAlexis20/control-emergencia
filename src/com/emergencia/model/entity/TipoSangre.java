package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipo_sangre database table.
 * 
 */
@Entity
@Table(name="tipo_sangre")
@NamedQuery(name="TipoSangre.findAll", query="SELECT t FROM TipoSangre t")
public class TipoSangre implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_sangre")
	private int idTipoSangre;

	private String estado;

	@Column(name="tipo_sangre")
	private String tipoSangre;

	//bi-directional many-to-one association to Bombero
	@OneToMany(mappedBy="tipoSangre")
	private List<Bombero> bomberos;

	public TipoSangre() {
	}

	public int getIdTipoSangre() {
		return this.idTipoSangre;
	}

	public void setIdTipoSangre(int idTipoSangre) {
		this.idTipoSangre = idTipoSangre;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipoSangre() {
		return this.tipoSangre;
	}

	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public List<Bombero> getBomberos() {
		return this.bomberos;
	}

	public void setBomberos(List<Bombero> bomberos) {
		this.bomberos = bomberos;
	}

	public Bombero addBombero(Bombero bombero) {
		getBomberos().add(bombero);
		bombero.setTipoSangre(this);

		return bombero;
	}

	public Bombero removeBombero(Bombero bombero) {
		getBomberos().remove(bombero);
		bombero.setTipoSangre(null);

		return bombero;
	}

}