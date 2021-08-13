package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.SignoVital;

public class SignoVitalDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<SignoVital> buscarPorPrehospitalario(Integer id) {
		List<SignoVital> resultado; 
		Query query = getEntityManager().createNamedQuery("SignoVital.buscarPorPrehospitalario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("id", id);
		resultado = (List<SignoVital>) query.getResultList();
		return resultado;
	}
}
