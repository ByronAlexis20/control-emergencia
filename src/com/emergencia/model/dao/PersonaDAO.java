package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Persona;

public class PersonaDAO extends ClaseDAO {
	
	@SuppressWarnings("unchecked")
	public List<Persona> buscarPorCedula(String cedula) {
		List<Persona> resultado; 
		Query query = getEntityManager().createNamedQuery("Persona.buscarPorCedula");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula", cedula);
		resultado = (List<Persona>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Persona> buscarPorCedulaDiferenteId(String cedula, Integer idPersona) {
		List<Persona> resultado; 
		Query query = getEntityManager().createNamedQuery("Persona.buscarPorCedulaDiferenteID");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula", cedula);
		query.setParameter("id", idPersona);
		resultado = (List<Persona>) query.getResultList();
		return resultado;
	}
}