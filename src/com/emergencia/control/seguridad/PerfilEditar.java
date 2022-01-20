package com.emergencia.control.seguridad;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.PerfilDAO;
import com.emergencia.model.entity.Perfil;

public class PerfilEditar {
	@Wire private Window winPerfilEditar;
	@Wire private Textbox txtPerfil;

	@Wire private Checkbox chkEstado;
	@Wire private Row rowEstado;
	
	private PerfilDAO perfilDAO = new PerfilDAO();
	private Perfil perfil;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		chkEstado.setChecked(true);
		// Recupera el objeto pasado como parametro. 
		perfil = (Perfil) Executions.getCurrent().getArg().get("Perfil");
		if (perfil == null) {
			perfil = new Perfil();
			perfil.setEstado("A");
			rowEstado.setVisible(false);
		}else {
			if(perfil.getEstado().equals("A")) {
				chkEstado.setChecked(true);
			}else {
				chkEstado.setChecked(false);
			}
		}
	}

	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtPerfil.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el Perfil","info",txtPerfil,"end_center",2000);
				txtPerfil.setFocus(true);
				return retorna;
			}
			if(perfil != null) {
				if(perfil.getIdPerfil() == null) {
					List<Perfil> listaPerfil = this.perfilDAO.buscarPorNombre(txtPerfil.getValue());
					if(listaPerfil.size() > 0) {
						Clients.showNotification("Nombre de Perfil ya existe","info",txtPerfil,"end_center",2000);
						return retorna;
					}
				}else {
					List<Perfil> listaPerfil = this.perfilDAO.buscarPorNombreDiferenteId(txtPerfil.getValue(), perfil.getIdPerfil());
					if(listaPerfil.size() > 0) {
						Clients.showNotification("Nombre de Perfil ya existe","info",txtPerfil,"end_center",2000);
						return retorna;
					}
				}
			}
			if(perfil.getIdPerfil() != null) {
				if(!chkEstado.isChecked()) {
					Perfil per = perfilDAO.getPerfilPorId(perfil.getIdPerfil());
					if(per != null) {
						if(per.getPermisos().size() > 0 || per.getUsuarios().size() > 0) {
							Clients.showNotification("No se puede eliminar el registro, hay registros que dependen de éste.");
							return retorna;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar(){
		if(isValidarDatos() == true) {
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {
						if(chkEstado.isChecked())
							perfil.setEstado("A");
						else
							perfil.setEstado("I");
						
						perfilDAO.getEntityManager().getTransaction().begin();			
						if (perfil.getIdPerfil() == null) {
							perfilDAO.getEntityManager().persist(perfil);
						}else{
							perfil = (Perfil) perfilDAO.getEntityManager().merge(perfil);
						}			
						perfilDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						perfilDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Perfil.buscarPorPatron", null);
		winPerfilEditar.detach();
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
}
