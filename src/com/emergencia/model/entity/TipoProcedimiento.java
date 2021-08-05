package com.emergencia.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipo_procedimiento database table.
 * 
 */
@Entity
@Table(name="tipo_procedimiento")
@NamedQuery(name="TipoProcedimiento.findAll", query="SELECT t FROM TipoProcedimiento t")
public class TipoProcedimiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_procedimiento")
	private int idTipoProcedimiento;

	private String estado;

	@Column(name="tipo_procedimiento")
	private String tipoProcedimiento;

	//bi-directional many-to-one association to Procedimiento
	@OneToMany(mappedBy="tipoProcedimiento")
	private List<Procedimiento> procedimientos;

	public TipoProcedimiento() {
	}

	public int getIdTipoProcedimiento() {
		return this.idTipoProcedimiento;
	}

	public void setIdTipoProcedimiento(int idTipoProcedimiento) {
		this.idTipoProcedimiento = idTipoProcedimiento;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipoProcedimiento() {
		return this.tipoProcedimiento;
	}

	public void setTipoProcedimiento(String tipoProcedimiento) {
		this.tipoProcedimiento = tipoProcedimiento;
	}

	public List<Procedimiento> getProcedimientos() {
		return this.procedimientos;
	}

	public void setProcedimientos(List<Procedimiento> procedimientos) {
		this.procedimientos = procedimientos;
	}

	public Procedimiento addProcedimiento(Procedimiento procedimiento) {
		getProcedimientos().add(procedimiento);
		procedimiento.setTipoProcedimiento(this);

		return procedimiento;
	}

	public Procedimiento removeProcedimiento(Procedimiento procedimiento) {
		getProcedimientos().remove(procedimiento);
		procedimiento.setTipoProcedimiento(null);

		return procedimiento;
	}

}