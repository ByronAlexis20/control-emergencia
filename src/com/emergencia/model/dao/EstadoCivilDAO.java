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
	
	public EstadoCivil buscarPorId(Integer id) {
		EstadoCivil dato; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("EstadoCivil.buscarPorId");
		consulta.setParameter("id", id);
		dato = (EstadoCivil) consulta.getSingleResult();
		return dato;
	}
	
	@SuppressWarnings("unchecked")
	public List<EstadoCivil> buscarPorNombre(String value) {
		List<EstadoCivil> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("EstadoCivil.buscarPorNombre");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		resultado = (List<EstadoCivil>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<EstadoCivil> buscarPorNombreDiferenteId(String value, Integer id) {
		List<EstadoCivil> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("EstadoCivil.buscarPorNombreDiferenteId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		query.setParameter("id", id);
		resultado = (List<EstadoCivil>) query.getResultList();
		return resultado;
	}
}
