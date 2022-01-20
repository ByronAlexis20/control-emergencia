package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.CondicionLlegada;

public class CondicionLlegadaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<CondicionLlegada> buscarCondicionLlegada() {
		List<CondicionLlegada> resultado; 
		Query query = getEntityManager().createNamedQuery("CondicionLlegada.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<CondicionLlegada>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<CondicionLlegada> getCondicionPorDescripcion(String value) {
		List<CondicionLlegada> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("CondicionLlegada.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<CondicionLlegada>) query.getResultList();
		return resultado;
	}
	
	public CondicionLlegada buscarPorId(Integer id) {
		CondicionLlegada dato; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("CondicionLlegada.buscarPorId");
		consulta.setParameter("id", id);
		dato = (CondicionLlegada) consulta.getSingleResult();
		return dato;
	}
	
	@SuppressWarnings("unchecked")
	public List<CondicionLlegada> buscarPorNombre(String value) {
		List<CondicionLlegada> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("CondicionLlegada.buscarPorNombre");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		resultado = (List<CondicionLlegada>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<CondicionLlegada> buscarPorNombreDiferenteId(String value, Integer id) {
		List<CondicionLlegada> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("CondicionLlegada.buscarPorNombreDiferenteId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		query.setParameter("id", id);
		resultado = (List<CondicionLlegada>) query.getResultList();
		return resultado;
	}
}
