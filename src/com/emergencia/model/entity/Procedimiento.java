package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the procedimiento database table.
 * 
 */
@Entity
@NamedQuery(name="Procedimiento.buscarPorPrehospitalario", query="SELECT p FROM Procedimiento p where p.prehospitalaria.idPrehospitalaria = :id and p.estado = 'A'")
public class Procedimiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_procedimiento")
	private Integer idProcedimiento;

	private String estado;

	//bi-directional many-to-one association to Prehospitalaria
	@ManyToOne
	@JoinColumn(name="id_prehospitalaria")
	private Prehospitalaria prehospitalaria;

	//bi-directional many-to-one association to TipoProcedimiento
	@ManyToOne
	@JoinColumn(name="id_tipo_procedimiento")
	private TipoProcedimiento tipoProcedimiento;

	public Procedimiento() {
	}

	public Integer getIdProcedimiento() {
		return this.idProcedimiento;
	}

	public void setIdProcedimiento(Integer idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Prehospitalaria getPrehospitalaria() {
		return this.prehospitalaria;
	}

	public void setPrehospitalaria(Prehospitalaria prehospitalaria) {
		this.prehospitalaria = prehospitalaria;
	}

	public TipoProcedimiento getTipoProcedimiento() {
		return this.tipoProcedimiento;
	}

	public void setTipoProcedimiento(TipoProcedimiento tipoProcedimiento) {
		this.tipoProcedimiento = tipoProcedimiento;
	}

}