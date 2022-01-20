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
	
	@SuppressWarnings("unchecked")
	public List<TipoSangre> getTipoPorDescripcion(String value) {
		List<TipoSangre> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("TipoSangre.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<TipoSangre>) query.getResultList();
		return resultado;
	}
	
	public TipoSangre buscarPorId(Integer id) {
		TipoSangre dato; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("TipoSangre.buscarPorId");
		consulta.setParameter("id", id);
		dato = (TipoSangre) consulta.getSingleResult();
		return dato;
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoSangre> buscarPorNombre(String value) {
		List<TipoSangre> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("TipoSangre.buscarPorNombre");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		resultado = (List<TipoSangre>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoSangre> buscarPorNombreDiferenteId(String value, Integer id) {
		List<TipoSangre> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("TipoSangre.buscarPorNombreDiferenteId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		query.setParameter("id", id);
		resultado = (List<TipoSangre>) query.getResultList();
		return resultado;
	}
}