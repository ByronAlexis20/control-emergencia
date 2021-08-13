package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Procedimiento;

public class ProcedimientoDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Procedimiento> buscarPorPrehospitalario(Integer id) {
		List<Procedimiento> resultado; 
		Query query = getEntityManager().createNamedQuery("Procedimiento.buscarPorPrehospitalario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("id", id);
		resultado = (List<Procedimiento>) query.getResultList();
		return resultado;
	}
}
