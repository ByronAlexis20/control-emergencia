package com.emergencia.model.dao;

import java.util.Date;
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
	public List<ControlVehiculo> buscarTodosOrdenados() {
		List<ControlVehiculo> resultado; 
		Query query = getEntityManager().createNamedQuery("ControlVehiculo.buscarTodos");
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
	
	@SuppressWarnings("unchecked")
	public List<ControlVehiculo> buscarPorPrehospitalaria(Integer idPrehospitalaria) {
		List<ControlVehiculo> resultado; 
		Query query = getEntityManager().createNamedQuery("ControlVehiculo.buscarPorPrehospitalaria");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPrehospitalaria", idPrehospitalaria);
		resultado = (List<ControlVehiculo>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<ControlVehiculo> buscarPorFecha(Date fecha) {
		List<ControlVehiculo> resultado; 
		Query query = getEntityManager().createNamedQuery("ControlVehiculo.buscarPorFecha");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("fecha", fecha);
		resultado = (List<ControlVehiculo>) query.getResultList();
		return resultado;
	}
}
