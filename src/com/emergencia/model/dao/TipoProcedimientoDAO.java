package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.TipoProcedimiento;

public class TipoProcedimientoDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<TipoProcedimiento> buscarTiposProcedimiento() {
		List<TipoProcedimiento> resultado; 
		Query query = getEntityManager().createNamedQuery("TipoProcedimiento.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<TipoProcedimiento>) query.getResultList();
		return resultado;
	}
}
