package com.emergencia.model.dao;

import java.util.ArrayList;
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
}
