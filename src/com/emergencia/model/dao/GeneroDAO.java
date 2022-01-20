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
	
	public Genero buscarPorId(Integer id) {
		Genero dato; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("Genero.buscarPorId");
		consulta.setParameter("id", id);
		dato = (Genero) consulta.getSingleResult();
		return dato;
	}
	
	@SuppressWarnings("unchecked")
	public List<Genero> buscarPorNombre(String value) {
		List<Genero> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("Genero.buscarPorNombre");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		resultado = (List<Genero>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Genero> buscarPorNombreDiferenteId(String value, Integer id) {
		List<Genero> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("Genero.buscarPorNombreDiferenteId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		query.setParameter("id", id);
		resultado = (List<Genero>) query.getResultList();
		return resultado;
	}
}
