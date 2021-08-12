package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.TipoEmergencia;

public class TipoEmergenciaDAO extends ClaseDAO{

	@SuppressWarnings("unchecked")
	public List<TipoEmergencia> obtenerTodos() {
		List<TipoEmergencia> resultado; 
		Query query = getEntityManager().createNamedQuery("TipoEmergencia.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<TipoEmergencia>) query.getResultList();
		return resultado;
	}
	
}
