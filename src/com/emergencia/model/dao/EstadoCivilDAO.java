package com.emergencia.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.EstadoCivil;

public class EstadoCivilDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<EstadoCivil> obtenerEstadosCiviles() {
		List<EstadoCivil> resultado = new ArrayList<EstadoCivil>(); 
		Query query = getEntityManager().createNamedQuery("EstadoCivil.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<EstadoCivil>) query.getResultList();
		return resultado;
	}
}
