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
	
	@SuppressWarnings("unchecked")
	public List<Genero> getGeneroPorDescripcion(String value) {
		List<Genero> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Genero.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Genero>) query.getResultList();
		return resultado;
	}
}
