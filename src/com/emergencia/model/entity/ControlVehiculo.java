package com.emergencia.model.entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="control_vehiculo")
@NamedQueries({
	@NamedQuery(name="ControlVehiculo.findAll", query="SELECT c FROM ControlVehiculo c"),
	@NamedQuery(name="ControlVehiculo.buscarPorEmergencia", query="SELECT c FROM ControlVehiculo c where c.emergencia.idEmergencia = :idEmergencia"),
	@NamedQuery(name="ControlVehiculo.buscarPorPrehospitalaria", query="SELECT c FROM ControlVehiculo c where c.prehospitalaria.idPrehospitalaria = :idPrehospitalaria"),
	@NamedQuery(name="ControlVehiculo.buscarPorFecha", query="SELECT c FROM ControlVehiculo c where c.fecha = :fecha and c.estado = 'A'"),
	
})
public class ControlVehiculo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_control")
	private Integer idControl;
	
	@ManyToOne
	@JoinColumn(name="id_vehiculo")
	private Vehiculo vehiculo;

	@ManyToOne
	@JoinColumn(name="id_chofer")
	private Usuario chofer;
	
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name="id_emergencia")
	private Emergencia emergencia;
	
	@ManyToOne
	@JoinColumn(name="id_prehospitalaria")
	private Prehospitalaria prehospitalaria;
	
	@ManyToOne
	@JoinColumn(name="id_cuartelero")
	private Usuario cuartelero;
	
	@ManyToOne
	@JoinColumn(name="id_bombero_recibe")
	private Usuario bomberoRecibe;
	
	@Column(name="n_reporte")
	private String nReporte;
	
	@Column(name="hora_reporte")
	private Time horaReporte;
	
	@Column(name="hora_salida_base")
	private Time horaSalidaBase;
	
	@Column(name="hora_llegada_emergencia")
	private Time horaLlegadaEmergencia;
	
	@Column(name="hora_salida_emergencia")
	private Time horaSalidaEmergencia;
	
	@Column(name="hora_llegada_hospital")
	private Time horaLlegadaHospital;
	
	@Column(name="hora_salida_rayos_x")
	private Time horaSalidaRayosX;
	
	@Column(name="hora_llegada_rayos_x")
	private Time horaLlegadaRayosX;
	
	@Column(name="hora_retorno_de_rayos_x")
	private Time horaRetornoDeRayosX;
	
	@Column(name="hora_llegada_del_rayos_x")
	private Time horaLlegadaDeRayosX;
	
	@Column(name="hora_salida_del_hospital")
	private Time horaSalidaDelHospital;
	
	@Column(name="hora_llegada_central")
	private Time horaLlegadaCentral;
	
	private String estado;
	
	private String novedades;
	
	public Integer getIdControl() {
		return idControl;
	}

	public void setIdControl(Integer idControl) {
		this.idControl = idControl;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public Usuario getChofer() {
		return chofer;
	}

	public void setChofer(Usuario chofer) {
		this.chofer = chofer;
	}

	public Emergencia getEmergencia() {
		return emergencia;
	}

	public void setEmergencia(Emergencia emergencia) {
		this.emergencia = emergencia;
	}

	public Prehospitalaria getPrehospitalaria() {
		return prehospitalaria;
	}

	public void setPrehospitalaria(Prehospitalaria prehospitalaria) {
		this.prehospitalaria = prehospitalaria;
	}

	public Usuario getCuartelero() {
		return cuartelero;
	}

	public void setCuartelero(Usuario cuartelero) {
		this.cuartelero = cuartelero;
	}

	public Usuario getBomberoRecibe() {
		return bomberoRecibe;
	}

	public void setBomberoRecibe(Usuario bomberoRecibe) {
		this.bomberoRecibe = bomberoRecibe;
	}

	public String getnReporte() {
		return nReporte;
	}

	public void setnReporte(String nReporte) {
		this.nReporte = nReporte;
	}

	public Time getHoraReporte() {
		return horaReporte;
	}

	public void setHoraReporte(Time horaReporte) {
		this.horaReporte = horaReporte;
	}

	public Time getHoraSalidaBase() {
		return horaSalidaBase;
	}

	public void setHoraSalidaBase(Time horaSalidaBase) {
		this.horaSalidaBase = horaSalidaBase;
	}

	public Time getHoraLlegadaEmergencia() {
		return horaLlegadaEmergencia;
	}

	public void setHoraLlegadaEmergencia(Time horaLlegadaEmergencia) {
		this.horaLlegadaEmergencia = horaLlegadaEmergencia;
	}

	public Time getHoraSalidaEmergencia() {
		return horaSalidaEmergencia;
	}

	public void setHoraSalidaEmergencia(Time horaSalidaEmergencia) {
		this.horaSalidaEmergencia = horaSalidaEmergencia;
	}

	public Time getHoraLlegadaHospital() {
		return horaLlegadaHospital;
	}

	public void setHoraLlegadaHospital(Time horaLlegadaHospital) {
		this.horaLlegadaHospital = horaLlegadaHospital;
	}

	public Time getHoraSalidaRayosX() {
		return horaSalidaRayosX;
	}

	public void setHoraSalidaRayosX(Time horaSalidaRayosX) {
		this.horaSalidaRayosX = horaSalidaRayosX;
	}

	public Time getHoraLlegadaRayosX() {
		return horaLlegadaRayosX;
	}

	public void setHoraLlegadaRayosX(Time horaLlegadaRayosX) {
		this.horaLlegadaRayosX = horaLlegadaRayosX;
	}

	public Time getHoraRetornoDeRayosX() {
		return horaRetornoDeRayosX;
	}

	public void setHoraRetornoDeRayosX(Time horaRetornoDeRayosX) {
		this.horaRetornoDeRayosX = horaRetornoDeRayosX;
	}

	public Time getHoraLlegadaDeRayosX() {
		return horaLlegadaDeRayosX;
	}

	public void setHoraLlegadaDeRayosX(Time horaLlegadaDeRayosX) {
		this.horaLlegadaDeRayosX = horaLlegadaDeRayosX;
	}

	public Time getHoraSalidaDelHospital() {
		return horaSalidaDelHospital;
	}

	public void setHoraSalidaDelHospital(Time horaSalidaDelHospital) {
		this.horaSalidaDelHospital = horaSalidaDelHospital;
	}

	public Time getHoraLlegadaCentral() {
		return horaLlegadaCentral;
	}

	public void setHoraLlegadaCentral(Time horaLlegadaCentral) {
		this.horaLlegadaCentral = horaLlegadaCentral;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNovedades() {
		return novedades;
	}

	public void setNovedades(String novedades) {
		this.novedades = novedades;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
}