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
}
