package com.emergencia.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.emergencia.model.entity.Perfil;

public class PerfilDAO extends ClaseDAO {
	
	public Perfil getPerfilPorId(Integer id) {
		Perfil perfil; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("Perfil.buscarPerfilPorId");
		consulta.setParameter("idPerfil", id);
		perfil = (Perfil) consulta.getSingleResult();
		return perfil;
	}
	
	@SuppressWarnings("unchecked")
	public List<Perfil> getPerfilesPorDescripcion(String value) {
		List<Perfil> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Perfil.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Perfil>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Perfil> buscarPorNombre(String value) {
		List<Perfil> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("Perfil.buscarPorNombre");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		resultado = (List<Perfil>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Perfil> buscarPorNombreDiferenteId(String value, Integer idPerfil) {
		List<Perfil> resultado; 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("Perfil.buscarPorNombreDiferenteId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron.trim());
		query.setParameter("id", idPerfil);
		resultado = (List<Perfil>) query.getResultList();
		return resultado;
	}
}