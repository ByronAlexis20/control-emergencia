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
	
	@SuppressWarnings("unchecked")
	public List<TipoEmergencia> getTipoEmergenciaPorDescripcion(String value) {
		List<TipoEmergencia> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("TipoEmergencia.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<TipoEmergencia>) query.getResultList();
		return resultado;
	}
	
	public TipoEmergencia buscarPorId(Integer id) {
		TipoEmergencia dato; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("TipoEmergencia.buscarPorId");
		consulta.setParameter("id", id);
		dato = (TipoEmergencia) consulta.getSingleResult();
		return dato;
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoEmergencia> buscarPorNombre(String value) {
		List<TipoEmergencia> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("TipoEmergencia.buscarPorNombre");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		resultado = (List<TipoEmergencia>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoEmergencia> buscarPorNombreDiferenteId(String value, Integer id) {
		List<TipoEmergencia> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("TipoEmergencia.buscarPorNombreDiferenteId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		query.setParameter("id", id);
		resultado = (List<TipoEmergencia>) query.getResultList();
		return resultado;
	}
}