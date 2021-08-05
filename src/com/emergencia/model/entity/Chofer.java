package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the chofer database table.
 * 
 */
@Entity
@NamedQuery(name="Chofer.findAll", query="SELECT c FROM Chofer c")
public class Chofer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_chofer")
	private int idChofer;

	private String cargo;

	private String estado;

	private String puesto;

	//bi-directional many-to-one association to ResponsableVehiculo
	@OneToMany(mappedBy="chofer")
	private List<ResponsableVehiculo> responsableVehiculos;

	//bi-directional many-to-one association to Persona
	@ManyToOne
	@JoinColumn(name="id_persona")
	private Persona persona;

	public Chofer() {
	}

	public int getIdChofer() {
		return this.idChofer;
	}

	public void setIdChofer(int idChofer) {
		this.idChofer = idChofer;
	}

	public String getCargo() {
		return this.cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPuesto() {
		return this.puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public List<ResponsableVehiculo> getResponsableVehiculos() {
		return this.responsableVehiculos;
	}

	public void setResponsableVehiculos(List<ResponsableVehiculo> responsableVehiculos) {
		this.responsableVehiculos = responsableVehiculos;
	}

	public ResponsableVehiculo addResponsableVehiculo(ResponsableVehiculo responsableVehiculo) {
		getResponsableVehiculos().add(responsableVehiculo);
		responsableVehiculo.setChofer(this);

		return responsableVehiculo;
	}

	public ResponsableVehiculo removeResponsableVehiculo(ResponsableVehiculo responsableVehiculo) {
		getResponsableVehiculos().remove(responsableVehiculo);
		responsableVehiculo.setChofer(null);

		return responsableVehiculo;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}