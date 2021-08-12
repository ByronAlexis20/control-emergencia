package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Parroquia;

public class ParroquiaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Parroquia> buscarPorIdCanton(Integer id) {
		List<Parroquia> resultado; 
		Query query = getEntityManager().createNamedQuery("Parroquia.buscarPorCanton");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCanton", id);
		resultado = (List<Parroquia>) query.getResultList();
		return resultado;
	}
}
