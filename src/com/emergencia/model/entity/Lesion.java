package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the lesion database table.
 * 
 */
@Entity
@NamedQuery(name="Lesion.findAll", query="SELECT l FROM Lesion l where l.estado = 'A'")
public class Lesion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_lesion")
	private int idLesion;

	private String estado;

	private String lesion;

	//bi-directional many-to-one association to LocalizacionLesion
	@OneToMany(mappedBy="lesion")
	private List<LocalizacionLesion> localizacionLesions;

	public Lesion() {
	}

	public int getIdLesion() {
		return this.idLesion;
	}

	public void setIdLesion(int idLesion) {
		this.idLesion = idLesion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getLesion() {
		return this.lesion;
	}

	public void setLesion(String lesion) {
		this.lesion = lesion;
	}

	public List<LocalizacionLesion> getLocalizacionLesions() {
		return this.localizacionLesions;
	}

	public void setLocalizacionLesions(List<LocalizacionLesion> localizacionLesions) {
		this.localizacionLesions = localizacionLesions;
	}

	public LocalizacionLesion addLocalizacionLesion(LocalizacionLesion localizacionLesion) {
		getLocalizacionLesions().add(localizacionLesion);
		localizacionLesion.setLesion(this);

		return localizacionLesion;
	}

	public LocalizacionLesion removeLocalizacionLesion(LocalizacionLesion localizacionLesion) {
		getLocalizacionLesions().remove(localizacionLesion);
		localizacionLesion.setLesion(null);

		return localizacionLesion;
	}

}