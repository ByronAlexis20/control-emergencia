package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.LocalizacionLesion;

public class LocalizacionLesionDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<LocalizacionLesion> buscarPorPrehospitalario(Integer id) {
		List<LocalizacionLesion> resultado; 
		Query query = getEntityManager().createNamedQuery("LocalizacionLesion.buscarPorPrehospitalario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("id", id);
		resultado = (List<LocalizacionLesion>) query.getResultList();
		return resultado;
	}
}
