package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;


/**
 * The persistent class for the signo_vital_emergencia database table.
 * 
 */
@Entity
@Table(name="signo_vital_emergencia")
@NamedQuery(name="SignoVitalEmergencia.findAll", query="SELECT s FROM SignoVitalEmergencia s")
public class SignoVitalEmergencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_signo_vital")
	private Integer idSignoVital;

	@Column(name="escala_glasgow")
	private int escalaGlasgow;

	private String estado;

	@Column(name="frecuencia_respiratoria")
	private int frecuenciaRespiratoria;

	private Time hora;

	@Column(name="llenado_capilar")
	private int llenadoCapilar;

	@Column(name="presion_arterial")
	private String presionArterial;

	@Column(name="pulso_minimo")
	private int pulsoMinimo;

	@Column(name="saturacion_oxigeno")
	private int saturacionOxigeno;

	@Column(name="temperatura_corporal")
	private float temperaturaCorporal;

	//bi-directional many-to-one association to Emergencia
	@ManyToOne
	@JoinColumn(name="id_emergencia")
	private Emergencia emergencia;

	public SignoVitalEmergencia() {
	}

	public Integer getIdSignoVital() {
		return this.idSignoVital;
	}

	public void setIdSignoVital(Integer idSignoVital) {
		this.idSignoVital = idSignoVital;
	}

	public int getEscalaGlasgow() {
		return this.escalaGlasgow;
	}

	public void setEscalaGlasgow(int escalaGlasgow) {
		this.escalaGlasgow = escalaGlasgow;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getFrecuenciaRespiratoria() {
		return this.frecuenciaRespiratoria;
	}

	public void setFrecuenciaRespiratoria(int frecuenciaRespiratoria) {
		this.frecuenciaRespiratoria = frecuenciaRespiratoria;
	}

	public Time getHora() {
		return this.hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public int getLlenadoCapilar() {
		return this.llenadoCapilar;
	}

	public void setLlenadoCapilar(int llenadoCapilar) {
		this.llenadoCapilar = llenadoCapilar;
	}

	public String getPresionArterial() {
		return this.presionArterial;
	}

	public void setPresionArterial(String presionArterial) {
		this.presionArterial = presionArterial;
	}

	public int getPulsoMinimo() {
		return this.pulsoMinimo;
	}

	public void setPulsoMinimo(int pulsoMinimo) {
		this.pulsoMinimo = pulsoMinimo;
	}

	public int getSaturacionOxigeno() {
		return this.saturacionOxigeno;
	}

	public void setSaturacionOxigeno(int saturacionOxigeno) {
		this.saturacionOxigeno = saturacionOxigeno;
	}

	public float getTemperaturaCorporal() {
		return this.temperaturaCorporal;
	}

	public void setTemperaturaCorporal(float temperaturaCorporal) {
		this.temperaturaCorporal = temperaturaCorporal;
	}

	public Emergencia getEmergencia() {
		return this.emergencia;
	}

	public void setEmergencia(Emergencia emergencia) {
		this.emergencia = emergencia;
	}

}