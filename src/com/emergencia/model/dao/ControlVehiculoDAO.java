package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.ControlVehiculo;

public class ControlVehiculoDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<ControlVehiculo> buscarTodosControles() {
		List<ControlVehiculo> resultado; 
		Query query = getEntityManager().createNamedQuery("ControlVehiculo.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<ControlVehiculo>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<ControlVehiculo> buscarPorEmergencia(Integer idEmergencia) {
		List<ControlVehiculo> resultado; 
		Query query = getEntityManager().createNamedQuery("ControlVehiculo.buscarPorEmergencia");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idEmergencia", idEmergencia);
		resultado = (List<ControlVehiculo>) query.getResultList();
		return resultado;
	}
}
