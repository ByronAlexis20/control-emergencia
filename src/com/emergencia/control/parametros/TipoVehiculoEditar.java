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

import com.emergencia.model.dao.TipoVehiculoDAO;
import com.emergencia.model.entity.TipoVehiculo;

public class TipoVehiculoEditar {
	@Wire private Window winTipoEditar;
	@Wire private Textbox txtTipo;
	@Wire private Checkbox chkEstado;
	@Wire private Row rowEstado;
	TipoVehiculo tipoVehiculo;
	TipoVehiculoDAO tipoVehiculoDAO = new TipoVehiculoDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		chkEstado.setChecked(true);
		// Recupera el objeto pasado como parametro. 
		tipoVehiculo = (TipoVehiculo) Executions.getCurrent().getArg().get("TipoVehiculo");
		if (tipoVehiculo == null) {
			tipoVehiculo = new TipoVehiculo();
			tipoVehiculo.setEstado("A");
			rowEstado.setVisible(false);
		}else {
			if(tipoVehiculo.getEstado().equals("A")) {
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
							tipoVehiculo.setEstado("A");
						else
							tipoVehiculo.setEstado("I");
						
						tipoVehiculoDAO.getEntityManager().getTransaction().begin();			
						if (tipoVehiculo.getIdTipoVehiculo() == null) {
							tipoVehiculoDAO.getEntityManager().persist(tipoVehiculo);
						}else{
							tipoVehiculo = (TipoVehiculo) tipoVehiculoDAO.getEntityManager().merge(tipoVehiculo);
						}			
						tipoVehiculoDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						tipoVehiculoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "TipoVehiculo.buscarPorPatron", null);
		winTipoEditar.detach();
	}
	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtTipo.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el tipo de vehículo","info",txtTipo,"end_center",2000);
				txtTipo.setFocus(true);
				return retorna;
			}
			if(tipoVehiculo != null) {
				if(tipoVehiculo.getIdTipoVehiculo() == null) {
					List<TipoVehiculo> lista = this.tipoVehiculoDAO.buscarPorNombre(txtTipo.getValue());
					if(lista.size() > 0) {
						Clients.showNotification("Nombre de tipo de vehículo ya existe","info",txtTipo,"end_center",2000);
						return retorna;
					}
				}else {
					List<TipoVehiculo> lista = this.tipoVehiculoDAO.buscarPorNombreDiferenteId(txtTipo.getValue(), tipoVehiculo.getIdTipoVehiculo());
					if(lista.size() > 0) {
						Clients.showNotification("Nombre de tipo de vehículo ya existe","info",txtTipo,"end_center",2000);
						return retorna;
					}
				}
			}
			if(tipoVehiculo.getIdTipoVehiculo() != null) {
				if(!chkEstado.isChecked()) {
					TipoVehiculo tv = tipoVehiculoDAO.buscarPorId(tipoVehiculo.getIdTipoVehiculo());
					if(tv != null) {
						if(tv.getVehiculos().size() > 0) {
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
	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}
	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

}