package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Canton;

public class CantonDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Canton> buscarPorIdProvincia(Integer id) {
		List<Canton> resultado; 
		Query query = getEntityManager().createNamedQuery("Canton.buscarPorProvincia");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idProvincia", id);
		resultado = (List<Canton>) query.getResultList();
		return resultado;
	}
}
