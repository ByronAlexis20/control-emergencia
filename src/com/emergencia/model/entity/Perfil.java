package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="perfil")
@NamedQueries({
	@NamedQuery(name="Perfil.buscarPorPatron", query="SELECT p FROM Perfil p where lower(p.nombre) like lower(:patron)"),
	@NamedQuery(name="Perfil.buscarPerfilPorId", query="SELECT p FROM Perfil p where p.idPerfil = :idPerfil"),
	@NamedQuery(name="Perfil.buscarPorNombre", query="SELECT p FROM Perfil p where lower(p.nombre) = lower(:patron)"),
	@NamedQuery(name="Perfil.buscarPorNombreDiferenteId", query="SELECT p FROM Perfil p where lower(p.nombre) = lower(:patron) and p.idPerfil <> :id"),
})
public class Perfil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_perfil")
	private Integer idPerfil;

	private String estado;

	private String nombre;

	//bi-directional many-to-one association to Permiso
	@OneToMany(mappedBy="perfil")
	private List<Permiso> permisos;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="perfil")
	private List<Usuario> usuarios;

	public Perfil() {
	}

	public Integer getIdPerfil() {
		return this.idPerfil;
	}

	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Permiso> getPermisos() {
		return this.permisos;
	}

	public void setPermisos(List<Permiso> permisos) {
		this.permisos = permisos;
	}

	public Permiso addPermiso(Permiso permiso) {
		getPermisos().add(permiso);
		permiso.setPerfil(this);

		return permiso;
	}

	public Permiso removePermiso(Permiso permiso) {
		getPermisos().remove(permiso);
		permiso.setPerfil(null);

		return permiso;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setPerfil(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setPerfil(null);

		return usuario;
	}

}