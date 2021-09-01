package com.emergencia.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Emergencia;

public class EmergenciaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Emergencia> obtenerEmergencias() {
		List<Emergencia> resultado = new ArrayList<Emergencia>(); 
		Query query = getEntityManager().createNamedQuery("Emergencia.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Emergencia>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Emergencia> buscarSinControlVehiculo() {
		List<Emergencia> resultado = new ArrayList<Emergencia>(); 
		Query query = getEntityManager().createNamedQuery("Emergencia.buscarSinControlVehiculo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Emergencia>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Emergencia> buscarPorFecha(Integer dia, Integer mes, Integer anio) {
		List<Emergencia> resultado; 
		Query query = getEntityManager().createNamedQuery("Emergencia.buscarPorFecha");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("dia", dia);
		query.setParameter("mes", mes);
		query.setParameter("anio", anio);
		resultado = (List<Emergencia>) query.getResultList();
		return resultado;
	}
}