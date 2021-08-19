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
	
	@SuppressWarnings("unchecked")
	public List<EstadoCivil> getEstadoCivilPorDescripcion(String value) {
		List<EstadoCivil> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("EstadoCivil.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<EstadoCivil>) query.getResultList();
		return resultado;
	}
}
