package com.emergencia.model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Prehospitalaria;

public class PrehospitalariaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Prehospitalaria> obtenerPrehospitalarias() {
		List<Prehospitalaria> resultado = new ArrayList<Prehospitalaria>(); 
		Query query = getEntityManager().createNamedQuery("Prehospitalaria.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Prehospitalaria>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Prehospitalaria> buscarSinControlVehiculo() {
		List<Prehospitalaria> resultado = new ArrayList<Prehospitalaria>(); 
		Query query = getEntityManager().createNamedQuery("Prehospitalaria.buscarSinControlVehiculo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Prehospitalaria>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Prehospitalaria> buscarPorFechaAtencion(Date fecha) {
		List<Prehospitalaria> resultado; 
		Query query = getEntityManager().createNamedQuery("Prehospitalaria.buscarPorFecha");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("fecha", fecha);
		resultado = (List<Prehospitalaria>) query.getResultList();
		return resultado;
	}
}
