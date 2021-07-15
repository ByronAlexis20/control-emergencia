package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the bombero database table.
 * 
 */
@Entity
@NamedQuery(name="Bombero.findAll", query="SELECT b FROM Bombero b")
public class Bombero implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_bombero")
	private int idBombero;

	private String apellidos;

	private String cargo;

	private String cedula;

	private String celular;

	private String correo;

	@Column(name="direccion_domiciliaria")
	private String direccionDomiciliaria;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_nacimiento")
	private Date fechaNacimiento;

	private String grado;

	private String nombres;

	@Column(name="referencia_domiciliaria")
	private String referenciaDomiciliaria;

	private String telefono;

	//bi-directional many-to-one association to EstadoCivil
	@ManyToOne
	@JoinColumn(name="id_estado_civil")
	private EstadoCivil estadoCivil;

	//bi-directional many-to-one association to TipoSangre
	@ManyToOne
	@JoinColumn(name="id_tipo_sangre")
	private TipoSangre tipoSangre;

	//bi-directional many-to-one association to Emergencia
	@OneToMany(mappedBy="bombero")
	private List<Emergencia> emergencias;

	public Bombero() {
	}

	public int getIdBombero() {
		return this.idBombero;
	}

	public void setIdBombero(int idBombero) {
		this.idBombero = idBombero;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCargo() {
		return this.cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccionDomiciliaria() {
		return this.direccionDomiciliaria;
	}

	public void setDireccionDomiciliaria(String direccionDomiciliaria) {
		this.direccionDomiciliaria = direccionDomiciliaria;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getGrado() {
		return this.grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getReferenciaDomiciliaria() {
		return this.referenciaDomiciliaria;
	}

	public void setReferenciaDomiciliaria(String referenciaDomiciliaria) {
		this.referenciaDomiciliaria = referenciaDomiciliaria;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public EstadoCivil getEstadoCivil() {
		return this.estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public TipoSangre getTipoSangre() {
		return this.tipoSangre;
	}

	public void setTipoSangre(TipoSangre tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public List<Emergencia> getEmergencias() {
		return this.emergencias;
	}

	public void setEmergencias(List<Emergencia> emergencias) {
		this.emergencias = emergencias;
	}

	public Emergencia addEmergencia(Emergencia emergencia) {
		getEmergencias().add(emergencia);
		emergencia.setBombero(this);

		return emergencia;
	}

	public Emergencia removeEmergencia(Emergencia emergencia) {
		getEmergencias().remove(emergencia);
		emergencia.setBombero(null);

		return emergencia;
	}

}