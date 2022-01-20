package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.TipoVehiculo;

public class TipoVehiculoDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<TipoVehiculo> getTipoVehiculoPorDescripcion(String value) {
		List<TipoVehiculo> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("TipoVehiculo.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<TipoVehiculo>) query.getResultList();
		return resultado;
	}
	
	public TipoVehiculo buscarPorId(Integer id) {
		TipoVehiculo dato; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("TipoVehiculo.buscarPorId");
		consulta.setParameter("id", id);
		dato = (TipoVehiculo) consulta.getSingleResult();
		return dato;
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoVehiculo> buscarPorNombre(String value) {
		List<TipoVehiculo> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("TipoVehiculo.buscarPorNombre");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		resultado = (List<TipoVehiculo>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoVehiculo> buscarPorNombreDiferenteId(String value, Integer id) {
		List<TipoVehiculo> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("TipoVehiculo.buscarPorNombreDiferenteId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		query.setParameter("id", id);
		resultado = (List<TipoVehiculo>) query.getResultList();
		return resultado;
	}
}
