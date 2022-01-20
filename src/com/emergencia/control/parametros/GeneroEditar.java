package com.emergencia.control.parametros;

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

import com.emergencia.model.dao.GeneroDAO;
import com.emergencia.model.entity.Genero;

public class GeneroEditar {
	@Wire private Window winGeneroEditar;
	@Wire private Textbox txtGenero;
	@Wire private Checkbox chkEstado;
	@Wire private Row rowEstado;
	Genero genero;
	GeneroDAO generoDAO = new GeneroDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		chkEstado.setChecked(true);
		// Recupera el objeto pasado como parametro. 
		genero = (Genero) Executions.getCurrent().getArg().get("Genero");
		if (genero == null) {
			genero = new Genero();
			genero.setEstado("A");
			rowEstado.setVisible(false);
		}else {
			if(genero.getEstado().equals("A")) {
				chkEstado.setChecked(true);
			}else {
				chkEstado.setChecked(false);
			}
		}
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
							genero.setEstado("A");
						else
							genero.setEstado("I");
						
						generoDAO.getEntityManager().getTransaction().begin();			
						if (genero.getIdGenero() == null) {
							generoDAO.getEntityManager().persist(genero);
						}else{
							genero = (Genero) generoDAO.getEntityManager().merge(genero);
						}			
						generoDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						generoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Genero.buscarPorPatron", null);
		winGeneroEditar.detach();
	}
	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtGenero.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el nombre del genero","info",txtGenero,"end_center",2000);
				txtGenero.setFocus(true);
				return retorna;
			}
			if(genero != null) {
				if(genero.getIdGenero() == null) {
					List<Genero> lista = this.generoDAO.buscarPorNombre(txtGenero.getValue());
					if(lista.size() > 0) {
						Clients.showNotification("Nombre de género ya existe","info",txtGenero,"end_center",2000);
						return retorna;
					}
				}else {
					List<Genero> lista = this.generoDAO.buscarPorNombreDiferenteId(txtGenero.getValue(), genero.getIdGenero());
					if(lista.size() > 0) {
						Clients.showNotification("Nombre de género ya existe","info",txtGenero,"end_center",2000);
						return retorna;
					}
				}
			}
			if(genero.getIdGenero() != null) {
				if(!chkEstado.isChecked()) {
					Genero gene = generoDAO.buscarPorId(genero.getIdGenero());
					if(gene != null) {
						if(gene.getPrehospitalarias().size() > 0) {
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
	public Genero getGenero() {
		return genero;
	}
	public void setGenero(Genero genero) {
		this.genero = genero;
	}

}
