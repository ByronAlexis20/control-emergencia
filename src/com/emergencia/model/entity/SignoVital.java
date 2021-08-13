package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;


/**
 * The persistent class for the signo_vital database table.
 * 
 */
@Entity
@Table(name="signo_vital")
@NamedQuery(name="SignoVital.buscarPorPrehospitalario", query="SELECT s FROM SignoVital s where s.prehospitalaria.idPrehospitalaria = :id and s.estado = 'A'")
public class SignoVital implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_signo_vital")
	private Integer idSignoVital;

	private String estado;

	@Column(name="frecuencia_respiratoria")
	private int frecuenciaRespiratoria;

	private Time hora;

	@Column(name="llenado_capilar")
	private int llenadoCapilar;

	@Column(name="presion_arterial")
	private int presionArterial;

	@Column(name="pulso_min")
	private int pulsoMin;

	@Column(name="saturacion_oxigeno")
	private int saturacionOxigeno;

	private float temperatura;

	//bi-directional many-to-one association to Prehospitalaria
	@ManyToOne
	@JoinColumn(name="id_prehospitalaria")
	private Prehospitalaria prehospitalaria;

	public SignoVital() {
	}

	public Integer getIdSignoVital() {
		return this.idSignoVital;
	}

	public void setIdSignoVital(Integer idSignoVital) {
		this.idSignoVital = idSignoVital;
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

	public int getPresionArterial() {
		return this.presionArterial;
	}

	public void setPresionArterial(int presionArterial) {
		this.presionArterial = presionArterial;
	}

	public int getPulsoMin() {
		return this.pulsoMin;
	}

	public void setPulsoMin(int pulsoMin) {
		this.pulsoMin = pulsoMin;
	}

	public int getSaturacionOxigeno() {
		return this.saturacionOxigeno;
	}

	public void setSaturacionOxigeno(int saturacionOxigeno) {
		this.saturacionOxigeno = saturacionOxigeno;
	}

	public float getTemperatura() {
		return this.temperatura;
	}

	public void setTemperatura(float temperatura) {
		this.temperatura = temperatura;
	}

	public Prehospitalaria getPrehospitalaria() {
		return this.prehospitalaria;
	}

	public void setPrehospitalaria(Prehospitalaria prehospitalaria) {
		this.prehospitalaria = prehospitalaria;
	}

}