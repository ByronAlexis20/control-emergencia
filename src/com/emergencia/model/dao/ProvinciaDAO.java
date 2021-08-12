package com.emergencia.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Provincia;

public class ProvinciaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Provincia> obtenerProvincias() {
		List<Provincia> resultado = new ArrayList<Provincia>(); 
		Query query = getEntityManager().createNamedQuery("Provincia.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Provincia>) query.getResultList();
		return resultado;
	}
}