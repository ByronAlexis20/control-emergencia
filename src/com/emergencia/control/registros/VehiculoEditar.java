package com.emergencia.control.registros;

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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.TipoVehiculoDAO;
import com.emergencia.model.dao.VehiculoDAO;
import com.emergencia.model.entity.TipoVehiculo;
import com.emergencia.model.entity.Vehiculo;

public class VehiculoEditar {
	@Wire private Window winVehiculoEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtDescripcion;
	@Wire private Combobox cboTipoVehiculo;
	Vehiculo vehiculo;
	VehiculoDAO vehiculoDAO = new VehiculoDAO();
	TipoVehiculoDAO tipoVehiculoDAO = new TipoVehiculoDAO();
	TipoVehiculo tipoVehiculoSeleccionado;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		vehiculo = (Vehiculo) Executions.getCurrent().getArg().get("Vehiculo");
		if (vehiculo == null) {
			vehiculo = new Vehiculo();
			vehiculo.setEstado("A");
		}else {
			cboTipoVehiculo.setText(vehiculo.getTipoVehiculo().getTipoVehiculo());
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
						vehiculoDAO.getEntityManager().getTransaction().begin();
						vehiculo.setTipoVehiculo((TipoVehiculo)cboTipoVehiculo.getSelectedItem().getValue());
						if (vehiculo.getIdVehiculo() == null) {
							vehiculoDAO.getEntityManager().persist(vehiculo);
						}else{
							vehiculo = (Vehiculo) vehiculoDAO.getEntityManager().merge(vehiculo);
						}			
						vehiculoDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						vehiculoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Vehiculo.buscarPorPatron", null);
		winVehiculoEditar.detach();
	}
	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtCodigo.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el código del vehículo","info",txtCodigo,"end_center",2000);
				txtCodigo.setFocus(true);
				return retorna;
			}
			if(cboTipoVehiculo.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar el tipo de vehículo","info",cboTipoVehiculo,"end_center",2000);
				return retorna;
			}
			//validar q no registre el mismo codigo
			if(vehiculo.getIdVehiculo() == null) {
				List<Vehiculo> listaVehiculo = vehiculoDAO.buscarPorCodigo(txtCodigo.getText());
				if(listaVehiculo.size() > 0) {
					Clients.showNotification("Ya existe un vehículo con el codigo " + txtCodigo.getText());
					return retorna;
				}
			}else {
				List<Vehiculo> listaVehiculo = vehiculoDAO.buscarPorCodigo(txtCodigo.getText(),vehiculo.getIdVehiculo());
				if(listaVehiculo.size() > 0) {
					Clients.showNotification("Ya existe un vehículo con el codigo " + txtCodigo.getText());
					return retorna;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<TipoVehiculo> getListaTipoVehiculos(){
		return tipoVehiculoDAO.getTipoVehiculoPorDescripcion("");
	}
	public Vehiculo getVehiculo() {
		return vehiculo;
	}
	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}
	public TipoVehiculo getTipoVehiculoSeleccionado() {
		return tipoVehiculoSeleccionado;
	}
	public void setTipoVehiculoSeleccionado(TipoVehiculo tipoVehiculoSeleccionado) {
		this.tipoVehiculoSeleccionado = tipoVehiculoSeleccionado;
	}
}