package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.PersonalEmergencia;

public class PersonalEmergenciaDAO extends ClaseDAO {

	@SuppressWarnings("unchecked")
	public List<PersonalEmergencia> buscarPorEmergencia(Integer idEmergencia) {
		List<PersonalEmergencia> resultado; 
		Query query = getEntityManager().createNamedQuery("PersonalEmergencia.buscarPorEmergencia");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idEmergencia", idEmergencia);
		resultado = (List<PersonalEmergencia>) query.getResultList();
		return resultado;
	}
	
}
