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
}
