package com.emergencia.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Me;

public class MesDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Me> obtenerMeses() {
		List<Me> resultado = new ArrayList<Me>(); 
		Query query = getEntityManager().createNamedQuery("Me.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Me>) query.getResultList();
		return resultado;
	}
}
