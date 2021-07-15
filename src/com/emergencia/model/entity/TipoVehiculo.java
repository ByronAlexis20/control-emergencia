package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipo_vehiculo database table.
 * 
 */
@Entity
@Table(name="tipo_vehiculo")
@NamedQuery(name="TipoVehiculo.findAll", query="SELECT t FROM TipoVehiculo t")
public class TipoVehiculo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_vehiculo")
	private int idTipoVehiculo;

	private String estado;

	@Column(name="tipo_vehiculo")
	private String tipoVehiculo;

	//bi-directional many-to-one association to Vehiculo
	@OneToMany(mappedBy="tipoVehiculo")
	private List<Vehiculo> vehiculos;

	public TipoVehiculo() {
	}

	public int getIdTipoVehiculo() {
		return this.idTipoVehiculo;
	}

	public void setIdTipoVehiculo(int idTipoVehiculo) {
		this.idTipoVehiculo = idTipoVehiculo;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipoVehiculo() {
		return this.tipoVehiculo;
	}

	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public List<Vehiculo> getVehiculos() {
		return this.vehiculos;
	}

	public void setVehiculos(List<Vehiculo> vehiculos) {
		this.vehiculos = vehiculos;
	}

	public Vehiculo addVehiculo(Vehiculo vehiculo) {
		getVehiculos().add(vehiculo);
		vehiculo.setTipoVehiculo(this);

		return vehiculo;
	}

	public Vehiculo removeVehiculo(Vehiculo vehiculo) {
		getVehiculos().remove(vehiculo);
		vehiculo.setTipoVehiculo(null);

		return vehiculo;
	}

}