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

import com.emergencia.model.dao.TipoSangreDAO;
import com.emergencia.model.entity.TipoSangre;

public class TipoSangreEditar {
	@Wire private Window winTipoSangreEditar;
	@Wire private Textbox txtTipo;
	@Wire private Checkbox chkEstado;
	@Wire private Row rowEstado;
	TipoSangre tipoSangre;
	TipoSangreDAO tipoSangreDAO = new TipoSangreDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		chkEstado.setChecked(true);
		// Recupera el objeto pasado como parametro. 
		tipoSangre = (TipoSangre) Executions.getCurrent().getArg().get("TipoSangre");
		if (tipoSangre == null) {
			tipoSangre = new TipoSangre();
			tipoSangre.setEstado("A");
			rowEstado.setVisible(false);
		}else {
			if(tipoSangre.getEstado().equals("A")) {
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
							tipoSangre.setEstado("A");
						else
							tipoSangre.setEstado("I");
						
						tipoSangreDAO.getEntityManager().getTransaction().begin();			
						if (tipoSangre.getIdTipoSangre() == null) {
							tipoSangreDAO.getEntityManager().persist(tipoSangre);
						}else{
							tipoSangre = (TipoSangre) tipoSangreDAO.getEntityManager().merge(tipoSangre);
						}			
						tipoSangreDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						tipoSangreDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "TipoSangre.buscarPorPatron", null);
		winTipoSangreEditar.detach();
	}
	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtTipo.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el nombre del tipo de sangre","info",txtTipo,"end_center",2000);
				txtTipo.setFocus(true);
				return retorna;
			}
			if(tipoSangre != null) {
				if(tipoSangre.getIdTipoSangre() == null) {
					List<TipoSangre> lista = this.tipoSangreDAO.buscarPorNombre(txtTipo.getValue());
					if(lista.size() > 0) {
						Clients.showNotification("Nombre de tipo de sangre ya existe","info",txtTipo,"end_center",2000);
						return retorna;
					}
				}else {
					List<TipoSangre> lista = this.tipoSangreDAO.buscarPorNombreDiferenteId(txtTipo.getValue(), tipoSangre.getIdTipoSangre());
					if(lista.size() > 0) {
						Clients.showNotification("Nombre de tipo de sangre ya existe","info",txtTipo,"end_center",2000);
						return retorna;
					}
				}
			}
			if(tipoSangre.getIdTipoSangre() != null) {
				if(!chkEstado.isChecked()) {
					TipoSangre ts = tipoSangreDAO.buscarPorId(tipoSangre.getIdTipoSangre());
					if(ts != null) {
						if(ts.getPersonas().size() > 0) {
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
	public TipoSangre getTipoSangre() {
		return tipoSangre;
	}
	public void setTipoSangre(TipoSangre tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

}
