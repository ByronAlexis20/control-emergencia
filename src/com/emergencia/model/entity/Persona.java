package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the persona database table.
 * 
 */
@Entity
@NamedQuery(name="Persona.findAll", query="SELECT p FROM Persona p")
public class Persona implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_persona")
	private int idPersona;

	private String apellidos;

	private String cargo;

	private String cedula;

	private String correo;

	@Column(name="direccion_domiciliaria")
	private String direccionDomiciliaria;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_nacimiento")
	private Date fechaNacimiento;

	private String nombres;

	@Column(name="referencia_domiciliaria")
	private String referenciaDomiciliaria;

	private String telefono;

	//bi-directional many-to-one association to Bombero
	@OneToMany(mappedBy="persona", cascade = CascadeType.ALL)
	private List<Bombero> bomberos;

	//bi-directional many-to-one association to Chofer
	@OneToMany(mappedBy="persona", cascade = CascadeType.ALL)
	private List<Chofer> chofers;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="persona", cascade = CascadeType.ALL)
	private List<Usuario> usuarios;

	public Persona() {
	}

	public int getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(int idPersona) {
		this.idPersona = idPersona;
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

	public List<Bombero> getBomberos() {
		return this.bomberos;
	}

	public void setBomberos(List<Bombero> bomberos) {
		this.bomberos = bomberos;
	}

	public Bombero addBombero(Bombero bombero) {
		getBomberos().add(bombero);
		bombero.setPersona(this);

		return bombero;
	}

	public Bombero removeBombero(Bombero bombero) {
		getBomberos().remove(bombero);
		bombero.setPersona(null);

		return bombero;
	}

	public List<Chofer> getChofers() {
		return this.chofers;
	}

	public void setChofers(List<Chofer> chofers) {
		this.chofers = chofers;
	}

	public Chofer addChofer(Chofer chofer) {
		getChofers().add(chofer);
		chofer.setPersona(this);

		return chofer;
	}

	public Chofer removeChofer(Chofer chofer) {
		getChofers().remove(chofer);
		chofer.setPersona(null);

		return chofer;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setPersona(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setPersona(null);

		return usuario;
	}

}