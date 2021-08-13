package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Genero;

public class GeneroDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Genero> buscarGeneros() {
		List<Genero> resultado; 
		Query query = getEntityManager().createNamedQuery("Genero.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Genero>) query.getResultList();
		return resultado;
	}
}
