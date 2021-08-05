package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the responsable_vehiculo database table.
 * 
 */
@Entity
@Table(name="responsable_vehiculo")
@NamedQuery(name="ResponsableVehiculo.findAll", query="SELECT r FROM ResponsableVehiculo r")
public class ResponsableVehiculo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_responsable")
	private int idResponsable;

	private String estado;

	//bi-directional many-to-one association to Chofer
	@ManyToOne
	@JoinColumn(name="id_chofer")
	private Chofer chofer;

	//bi-directional many-to-one association to Vehiculo
	@ManyToOne
	@JoinColumn(name="id_vehiculo")
	private Vehiculo vehiculo;

	public ResponsableVehiculo() {
	}

	public int getIdResponsable() {
		return this.idResponsable;
	}

	public void setIdResponsable(int idResponsable) {
		this.idResponsable = idResponsable;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Chofer getChofer() {
		return this.chofer;
	}

	public void setChofer(Chofer chofer) {
		this.chofer = chofer;
	}

	public Vehiculo getVehiculo() {
		return this.vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

}