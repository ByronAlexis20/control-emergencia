package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the vehiculo database table.
 * 
 */
@Entity
@Table(name="vehiculo")
@NamedQueries({
	@NamedQuery(name="Vehiculo.buscarPorPatron", query="SELECT v FROM Vehiculo v where v.estado = 'A' and (lower(v.codigo) like lower(:patron) or lower(v.descripcion) like lower(:patron))"),
	@NamedQuery(name="Vehiculo.buscarPorCodigo", query="SELECT v FROM Vehiculo v where v.estado = 'A' and v.codigo = :codigo"),
	@NamedQuery(name="Vehiculo.buscarPorId", query="SELECT v FROM Vehiculo v where v.idVehiculo = :id"),
	@NamedQuery(name="Vehiculo.buscarPorCodigoDiferenteActual", query="SELECT v FROM Vehiculo v where v.estado = 'A' and v.codigo = :codigo and v.idVehiculo <> :id")
})
public class Vehiculo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_vehiculo")
	private Integer idVehiculo;

	private String codigo;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Emergencia
	@OneToMany(mappedBy="vehiculo")
	private List<ControlVehiculo> controlVehiculos;

	//bi-directional many-to-one association to TipoVehiculo
	@ManyToOne
	@JoinColumn(name="id_tipo_vehiculo")
	private TipoVehiculo tipoVehiculo;

	public Vehiculo() {
	}

	public Integer getIdVehiculo() {
		return this.idVehiculo;
	}

	public void setIdVehiculo(Integer idVehiculo) {
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

	public List<ControlVehiculo> getControlVehiculos() {
		return controlVehiculos;
	}

	public void setControlVehiculos(List<ControlVehiculo> controlVehiculos) {
		this.controlVehiculos = controlVehiculos;
	}

	public ControlVehiculo addControlVehiculo(ControlVehiculo controlVehiculo) {
		getControlVehiculos().add(controlVehiculo);
		controlVehiculo.setVehiculo(this);

		return controlVehiculo;
	}

	public ControlVehiculo removeEmergencia(ControlVehiculo controlVehiculo) {
		getControlVehiculos().remove(controlVehiculo);
		controlVehiculo.setVehiculo(null);

		return controlVehiculo;
	}

	public TipoVehiculo getTipoVehiculo() {
		return this.tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

}