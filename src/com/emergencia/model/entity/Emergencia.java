package com.emergencia.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="emergencia")
@NamedQueries({
	@NamedQuery(name="Emergencia.findAll", query="SELECT e FROM Emergencia e where e.estado = 'A'"),
	@NamedQuery(name="Emergencia.buscarSinControlVehiculo", query="SELECT e FROM Emergencia e "
			+ "left join ControlVehiculo c on e.idEmergencia = c.emergencia.idEmergencia and c.emergencia.estado = 'A' where c.idControl is null and  e.estado = 'A'")
})
public class Emergencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_emergencia")
	private Integer idEmergencia;

	private int anio;

	private String avenida;

	private String calle;

	@Column(name="confirmacion_llamada")
	private String confirmacionLlamada;

	@Column(name="descripcion_operaciones")
	private String descripcionOperaciones;

	private int dia;

	@Column(name="direccion_evento")
	private String direccionEvento;

	private String estado;

	@Column(name="id_jefe_guardia")
	private int idJefeGuardia;

	@Column(name="id_usuario_elaborador")
	private int idUsuarioElaborador;

	private String novedades;

	private String referencias;

	private String telefono;

	//bi-directional many-to-one association to Barrio
	@ManyToOne
	@JoinColumn(name="id_barrio")
	private Barrio barrio;

	//bi-directional many-to-one association to FormaAviso
	@ManyToOne
	@JoinColumn(name="id_forma_aviso")
	private FormaAviso formaAviso;

	//bi-directional many-to-one association to Me
	@ManyToOne
	@JoinColumn(name="id_mes")
	private Me me;

	//bi-directional many-to-one association to Parroquia
	@ManyToOne
	@JoinColumn(name="id_parroquia")
	private Parroquia parroquia;

	//bi-directional many-to-one association to TipoEmergencia
	@ManyToOne
	@JoinColumn(name="id_tipo_emergencia")
	private TipoEmergencia tipoEmergencia;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="id_bombero")
	private Usuario usuario;

	//bi-directional many-to-one association to SignoVitalEmergencia
	@OneToMany(mappedBy="emergencia", cascade = CascadeType.ALL)
	private List<SignoVitalEmergencia> signoVitalEmergencias;

	@OneToMany(mappedBy="emergencia", cascade = CascadeType.ALL)
	private List<ControlVehiculo> controlVehiculos;
	
	public Emergencia() {
	}

	public Integer getIdEmergencia() {
		return this.idEmergencia;
	}

	public void setIdEmergencia(Integer idEmergencia) {
		this.idEmergencia = idEmergencia;
	}

	public int getAnio() {
		return this.anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public String getAvenida() {
		return this.avenida;
	}

	public void setAvenida(String avenida) {
		this.avenida = avenida;
	}

	public String getCalle() {
		return this.calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getConfirmacionLlamada() {
		return this.confirmacionLlamada;
	}

	public void setConfirmacionLlamada(String confirmacionLlamada) {
		this.confirmacionLlamada = confirmacionLlamada;
	}

	public String getDescripcionOperaciones() {
		return this.descripcionOperaciones;
	}

	public void setDescripcionOperaciones(String descripcionOperaciones) {
		this.descripcionOperaciones = descripcionOperaciones;
	}

	public int getDia() {
		return this.dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public String getDireccionEvento() {
		return this.direccionEvento;
	}

	public void setDireccionEvento(String direccionEvento) {
		this.direccionEvento = direccionEvento;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getIdJefeGuardia() {
		return this.idJefeGuardia;
	}

	public void setIdJefeGuardia(int idJefeGuardia) {
		this.idJefeGuardia = idJefeGuardia;
	}

	public int getIdUsuarioElaborador() {
		return this.idUsuarioElaborador;
	}

	public void setIdUsuarioElaborador(int idUsuarioElaborador) {
		this.idUsuarioElaborador = idUsuarioElaborador;
	}

	public String getNovedades() {
		return this.novedades;
	}

	public void setNovedades(String novedades) {
		this.novedades = novedades;
	}

	public String getReferencias() {
		return this.referencias;
	}

	public void setReferencias(String referencias) {
		this.referencias = referencias;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Barrio getBarrio() {
		return this.barrio;
	}

	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}

	public FormaAviso getFormaAviso() {
		return this.formaAviso;
	}

	public void setFormaAviso(FormaAviso formaAviso) {
		this.formaAviso = formaAviso;
	}

	public Me getMe() {
		return this.me;
	}

	public void setMe(Me me) {
		this.me = me;
	}

	public Parroquia getParroquia() {
		return this.parroquia;
	}

	public void setParroquia(Parroquia parroquia) {
		this.parroquia = parroquia;
	}

	public TipoEmergencia getTipoEmergencia() {
		return this.tipoEmergencia;
	}

	public void setTipoEmergencia(TipoEmergencia tipoEmergencia) {
		this.tipoEmergencia = tipoEmergencia;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<SignoVitalEmergencia> getSignoVitalEmergencias() {
		return this.signoVitalEmergencias;
	}

	public void setSignoVitalEmergencias(List<SignoVitalEmergencia> signoVitalEmergencias) {
		this.signoVitalEmergencias = signoVitalEmergencias;
	}

	public SignoVitalEmergencia addSignoVitalEmergencia(SignoVitalEmergencia signoVitalEmergencia) {
		getSignoVitalEmergencias().add(signoVitalEmergencia);
		signoVitalEmergencia.setEmergencia(this);

		return signoVitalEmergencia;
	}

	public SignoVitalEmergencia removeSignoVitalEmergencia(SignoVitalEmergencia signoVitalEmergencia) {
		getSignoVitalEmergencias().remove(signoVitalEmergencia);
		signoVitalEmergencia.setEmergencia(null);

		return signoVitalEmergencia;
	}

	public List<ControlVehiculo> getControlVehiculos() {
		return controlVehiculos;
	}

	public void setControlVehiculos(List<ControlVehiculo> controlVehiculos) {
		this.controlVehiculos = controlVehiculos;
	}

	public ControlVehiculo addControlVehiculo(ControlVehiculo controlVehiculo) {
		getControlVehiculos().add(controlVehiculo);
		controlVehiculo.setEmergencia(this);

		return controlVehiculo;
	}

	public ControlVehiculo removeControlVehiculo(ControlVehiculo controlVehiculo) {
		getControlVehiculos().remove(controlVehiculo);
		controlVehiculo.setEmergencia(null);

		return controlVehiculo;
	}
}