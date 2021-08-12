package com.emergencia.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.TipoSangre;

public class TipoSangreDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<TipoSangre> obtenerTipoSangre() {
		List<TipoSangre> resultado = new ArrayList<TipoSangre>(); 
		Query query = getEntityManager().createNamedQuery("TipoSangre.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<TipoSangre>) query.getResultList();
		return resultado;
	}
}
