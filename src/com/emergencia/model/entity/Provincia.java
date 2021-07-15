package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the provincia database table.
 * 
 */
@Entity
@NamedQuery(name="Provincia.findAll", query="SELECT p FROM Provincia p")
public class Provincia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_provincia")
	private int idProvincia;

	private String estado;

	private String provincia;

	//bi-directional many-to-one association to Canton
	@OneToMany(mappedBy="provincia")
	private List<Canton> cantons;

	public Provincia() {
	}

	public int getIdProvincia() {
		return this.idProvincia;
	}

	public void setIdProvincia(int idProvincia) {
		this.idProvincia = idProvincia;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public List<Canton> getCantons() {
		return this.cantons;
	}

	public void setCantons(List<Canton> cantons) {
		this.cantons = cantons;
	}

	public Canton addCanton(Canton canton) {
		getCantons().add(canton);
		canton.setProvincia(this);

		return canton;
	}

	public Canton removeCanton(Canton canton) {
		getCantons().remove(canton);
		canton.setProvincia(null);

		return canton;
	}

}