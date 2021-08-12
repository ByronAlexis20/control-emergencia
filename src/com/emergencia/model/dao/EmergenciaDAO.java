package com.emergencia.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Emergencia;

public class EmergenciaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Emergencia> obtenerEmergencias() {
		List<Emergencia> resultado = new ArrayList<Emergencia>(); 
		Query query = getEntityManager().createNamedQuery("Emergencia.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Emergencia>) query.getResultList();
		return resultado;
	}
}