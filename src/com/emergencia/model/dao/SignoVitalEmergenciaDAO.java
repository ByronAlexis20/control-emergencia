package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.SignoVitalEmergencia;

public class SignoVitalEmergenciaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<SignoVitalEmergencia> buscarPorEmergencia(Integer id) {
		List<SignoVitalEmergencia> resultado; 
		Query query = getEntityManager().createNamedQuery("SignoVitalEmergencia.buscarPorEmergencia");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idEmergencia", id);
		resultado = (List<SignoVitalEmergencia>) query.getResultList();
		return resultado;
	}
}
