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
}
