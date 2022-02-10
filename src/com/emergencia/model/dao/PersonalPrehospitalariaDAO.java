package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.PersonalPrehospitalaria;

public class PersonalPrehospitalariaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<PersonalPrehospitalaria> buscarPorEmergencia(Integer idEmergencia) {
		List<PersonalPrehospitalaria> resultado; 
		Query query = getEntityManager().createNamedQuery("PersonalPrehospitalaria.buscarPorEmergencia");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idEmergencia", idEmergencia);
		resultado = (List<PersonalPrehospitalaria>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<PersonalPrehospitalaria> buscarPorBombero(Integer idBombero) {
		List<PersonalPrehospitalaria> resultado; 
		Query query = getEntityManager().createNamedQuery("PersonalPrehospitalaria.buscarPorBombero");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idBombero", idBombero);
		resultado = (List<PersonalPrehospitalaria>) query.getResultList();
		return resultado;
	}
}
