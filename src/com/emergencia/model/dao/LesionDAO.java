package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Lesion;

public class LesionDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Lesion> buscarLesiones() {
		List<Lesion> resultado; 
		Query query = getEntityManager().createNamedQuery("Lesion.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Lesion>) query.getResultList();
		return resultado;
	}
}
