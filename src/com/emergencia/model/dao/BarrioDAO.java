package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Barrio;

public class BarrioDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Barrio> getBarrioPorDescripcion(String value) {
		List<Barrio> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Barrio.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Barrio>) query.getResultList();
		return resultado;
	}
	
	public Barrio buscarPorId(Integer id) {
		Barrio usuario; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("Barrio.buscarPorId");
		consulta.setParameter("id", id);
		usuario = (Barrio) consulta.getSingleResult();
		return usuario;
	}
	
	@SuppressWarnings("unchecked")
	public List<Barrio> buscarPorNombre(String value) {
		List<Barrio> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("Barrio.buscarPorNombre");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		resultado = (List<Barrio>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Barrio> buscarPorNombreDiferenteId(String value, Integer id) {
		List<Barrio> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("Barrio.buscarPorNombreDiferenteId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		query.setParameter("id", id);
		resultado = (List<Barrio>) query.getResultList();
		return resultado;
	}
}
