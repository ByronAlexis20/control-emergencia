package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the prehospitalaria database table.
 * 
 */
@Entity
@NamedQuery(name = "Prehospitalaria.findAll", query = "SELECT p FROM Prehospitalaria p where p.estado = 'A'")
public class Prehospitalaria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_prehospitalaria")
	private Integer idPrehospitalaria;

	@Column(name = "cedula_informante")
	private String cedulaInformante;

	@Column(name = "cedula_usuario")
	private String cedulaUsuario;

	@Column(name = "direccion_evento")
	private String direccionEvento;

	private int edad;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_atencion")
	private Date fechaAtencion;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_evento")
	private Date fechaEvento;

	@Column(name = "hora_atencion")
	private Time horaAtencion;

	@Column(name = "hora_evanto")
	private Time horaEvanto;

	private String interrogatorio;

	@Column(name = "lugar_evento")
	private String lugarEvento;

	@Column(name = "nombre_informante")
	private String nombreInformante;

	@Column(name = "nombre_usuario")
	private String nombreUsuario;

	// bi-directional many-to-one association to LocalizacionLesion
	@OneToMany(mappedBy = "prehospitalaria", cascade = CascadeType.ALL)
	private List<LocalizacionLesion> localizacionLesions;

	// bi-directional many-to-one association to CondicionLlegada
	@ManyToOne
	@JoinColumn(name = "id_condicion_llegada")
	private CondicionLlegada condicionLlegada;

	// bi-directional many-to-one association to Emergencia
	@ManyToOne
	@JoinColumn(name = "id_tipo_emergencia")
	private TipoEmergencia tipoEmergencia;

	// bi-directional many-to-one association to Establecimiento
	@ManyToOne
	@JoinColumn(name = "id_establecimiento")
	private Establecimiento establecimiento;

	// bi-directional many-to-one association to Genero
	@ManyToOne
	@JoinColumn(name = "id_genero")
	private Genero genero;

	// bi-directional many-to-one association to Procedimiento
	@OneToMany(mappedBy = "prehospitalaria", cascade = CascadeType.ALL)
	private List<Procedimiento> procedimientos;

	// bi-directional many-to-one association to SignoVital
	@OneToMany(mappedBy = "prehospitalaria", cascade = CascadeType.ALL)
	private List<SignoVital> signoVitals;

	public Prehospitalaria() {
	}

	public Integer getIdPrehospitalaria() {
		return this.idPrehospitalaria;
	}

	public void setIdPrehospitalaria(Integer idPrehospitalaria) {
		this.idPrehospitalaria = idPrehospitalaria;
	}

	public String getCedulaInformante() {
		return this.cedulaInformante;
	}

	public void setCedulaInformante(String cedulaInformante) {
		this.cedulaInformante = cedulaInformante;
	}

	public String getCedulaUsuario() {
		return this.cedulaUsuario;
	}

	public void setCedulaUsuario(String cedulaUsuario) {
		this.cedulaUsuario = cedulaUsuario;
	}

	public String getDireccionEvento() {
		return this.direccionEvento;
	}

	public void setDireccionEvento(String direccionEvento) {
		this.direccionEvento = direccionEvento;
	}

	public int getEdad() {
		return this.edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaAtencion() {
		return this.fechaAtencion;
	}

	public void setFechaAtencion(Date fechaAtencion) {
		this.fechaAtencion = fechaAtencion;
	}

	public Date getFechaEvento() {
		return this.fechaEvento;
	}

	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public Time getHoraAtencion() {
		return this.horaAtencion;
	}

	public void setHoraAtencion(Time horaAtencion) {
		this.horaAtencion = horaAtencion;
	}

	public Time getHoraEvanto() {
		return this.horaEvanto;
	}

	public void setHoraEvanto(Time horaEvanto) {
		this.horaEvanto = horaEvanto;
	}

	public String getInterrogatorio() {
		return this.interrogatorio;
	}

	public void setInterrogatorio(String interrogatorio) {
		this.interrogatorio = interrogatorio;
	}

	public String getLugarEvento() {
		return this.lugarEvento;
	}

	public void setLugarEvento(String lugarEvento) {
		this.lugarEvento = lugarEvento;
	}

	public String getNombreInformante() {
		return this.nombreInformante;
	}

	public void setNombreInformante(String nombreInformante) {
		this.nombreInformante = nombreInformante;
	}

	public String getNombreUsuario() {
		return this.nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public List<LocalizacionLesion> getLocalizacionLesions() {
		return this.localizacionLesions;
	}

	public void setLocalizacionLesions(List<LocalizacionLesion> localizacionLesions) {
		this.localizacionLesions = localizacionLesions;
	}

	public LocalizacionLesion addLocalizacionLesion(LocalizacionLesion localizacionLesion) {
		getLocalizacionLesions().add(localizacionLesion);
		localizacionLesion.setPrehospitalaria(this);

		return localizacionLesion;
	}

	public LocalizacionLesion removeLocalizacionLesion(LocalizacionLesion localizacionLesion) {
		getLocalizacionLesions().remove(localizacionLesion);
		localizacionLesion.setPrehospitalaria(null);

		return localizacionLesion;
	}

	public CondicionLlegada getCondicionLlegada() {
		return this.condicionLlegada;
	}

	public void setCondicionLlegada(CondicionLlegada condicionLlegada) {
		this.condicionLlegada = condicionLlegada;
	}

	public TipoEmergencia getTipoEmergencia() {
		return tipoEmergencia;
	}

	public void setTipoEmergencia(TipoEmergencia tipoEmergencia) {
		this.tipoEmergencia = tipoEmergencia;
	}

	public Establecimiento getEstablecimiento() {
		return this.establecimiento;
	}

	public void setEstablecimiento(Establecimiento establecimiento) {
		this.establecimiento = establecimiento;
	}

	public Genero getGenero() {
		return this.genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public List<Procedimiento> getProcedimientos() {
		return this.procedimientos;
	}

	public void setProcedimientos(List<Procedimiento> procedimientos) {
		this.procedimientos = procedimientos;
	}

	public Procedimiento addProcedimiento(Procedimiento procedimiento) {
		getProcedimientos().add(procedimiento);
		procedimiento.setPrehospitalaria(this);

		return procedimiento;
	}

	public Procedimiento removeProcedimiento(Procedimiento procedimiento) {
		getProcedimientos().remove(procedimiento);
		procedimiento.setPrehospitalaria(null);

		return procedimiento;
	}

	public List<SignoVital> getSignoVitals() {
		return this.signoVitals;
	}

	public void setSignoVitals(List<SignoVital> signoVitals) {
		this.signoVitals = signoVitals;
	}

	public SignoVital addSignoVital(SignoVital signoVital) {
		getSignoVitals().add(signoVital);
		signoVital.setPrehospitalaria(this);

		return signoVital;
	}

	public SignoVital removeSignoVital(SignoVital signoVital) {
		getSignoVitals().remove(signoVital);
		signoVital.setPrehospitalaria(null);

		return signoVital;
	}

}