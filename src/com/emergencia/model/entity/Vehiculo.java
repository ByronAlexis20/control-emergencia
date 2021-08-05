package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the vehiculo database table.
 * 
 */
@Entity
@NamedQuery(name="Vehiculo.findAll", query="SELECT v FROM Vehiculo v")
public class Vehiculo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_vehiculo")
	private int idVehiculo;

	private String codigo;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Emergencia
	@OneToMany(mappedBy="vehiculo")
	private List<Emergencia> emergencias;

	//bi-directional many-to-one association to ResponsableVehiculo
	@OneToMany(mappedBy="vehiculo")
	private List<ResponsableVehiculo> responsableVehiculos;

	//bi-directional many-to-one association to TipoVehiculo
	@ManyToOne
	@JoinColumn(name="id_tipo_vehiculo")
	private TipoVehiculo tipoVehiculo;

	public Vehiculo() {
	}

	public int getIdVehiculo() {
		return this.idVehiculo;
	}

	public void setIdVehiculo(int idVehiculo) {
		this.idVehiculo = idVehiculo;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
		emergencia.setVehiculo(this);

		return emergencia;
	}

	public Emergencia removeEmergencia(Emergencia emergencia) {
		getEmergencias().remove(emergencia);
		emergencia.setVehiculo(null);

		return emergencia;
	}

	public List<ResponsableVehiculo> getResponsableVehiculos() {
		return this.responsableVehiculos;
	}

	public void setResponsableVehiculos(List<ResponsableVehiculo> responsableVehiculos) {
		this.responsableVehiculos = responsableVehiculos;
	}

	public ResponsableVehiculo addResponsableVehiculo(ResponsableVehiculo responsableVehiculo) {
		getResponsableVehiculos().add(responsableVehiculo);
		responsableVehiculo.setVehiculo(this);

		return responsableVehiculo;
	}

	public ResponsableVehiculo removeResponsableVehiculo(ResponsableVehiculo responsableVehiculo) {
		getResponsableVehiculos().remove(responsableVehiculo);
		responsableVehiculo.setVehiculo(null);

		return responsableVehiculo;
	}

	public TipoVehiculo getTipoVehiculo() {
		return this.tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

}