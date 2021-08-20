package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Vehiculo;

public class VehiculoDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Vehiculo> getVehiculoPorDescripcion(String value) {
		List<Vehiculo> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Vehiculo.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Vehiculo>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Vehiculo> buscarPorCodigo(String codigo) {
		List<Vehiculo> resultado; 
		Query query = getEntityManager().createNamedQuery("Vehiculo.buscarPorCodigo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("codigo", codigo);
		resultado = (List<Vehiculo>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Vehiculo> buscarPorCodigo(String codigo, Integer id) {
		List<Vehiculo> resultado; 
		Query query = getEntityManager().createNamedQuery("Vehiculo.buscarPorCodigoDiferenteActual");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("codigo", codigo);
		query.setParameter("id", id);
		resultado = (List<Vehiculo>) query.getResultList();
		return resultado;
	}
}
