package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.FormaAviso;

public class FormaAvisoDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<FormaAviso> obtenerTodos() {
		List<FormaAviso> resultado; 
		Query query = getEntityManager().createNamedQuery("FormaAviso.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<FormaAviso>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<FormaAviso> getFormaAvisoPorDescripcion(String value) {
		List<FormaAviso> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("FormaAviso.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<FormaAviso>) query.getResultList();
		return resultado;
	}
	
	public FormaAviso buscarPorId(Integer id) {
		FormaAviso dato; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("FormaAviso.buscarPorId");
		consulta.setParameter("id", id);
		dato = (FormaAviso) consulta.getSingleResult();
		return dato;
	}
	
	@SuppressWarnings("unchecked")
	public List<FormaAviso> buscarPorNombre(String value) {
		List<FormaAviso> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("FormaAviso.buscarPorNombre");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		resultado = (List<FormaAviso>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<FormaAviso> buscarPorNombreDiferenteId(String value, Integer id) {
		List<FormaAviso> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("FormaAviso.buscarPorNombreDiferenteId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		query.setParameter("id", id);
		resultado = (List<FormaAviso>) query.getResultList();
		return resultado;
	}
}
