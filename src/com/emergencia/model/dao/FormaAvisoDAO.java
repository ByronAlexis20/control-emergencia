package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.FormaAviso;

public class FormaAvisoDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<FormaAviso> obtenerTodos() {
		List<FormaAviso> resultado; 
		Query query = getEntityManager().createNamedQuery("FormaAviso.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<FormaAviso>) query.getResultList();
		return resultado;
	}
}
