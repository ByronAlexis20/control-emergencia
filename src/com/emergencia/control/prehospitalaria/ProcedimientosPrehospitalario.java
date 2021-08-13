package com.emergencia.control.prehospitalaria;

import java.io.IOException;
import java.util.List;

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
import org.zkoss.zul.Window;

import com.emergencia.model.dao.TipoProcedimientoDAO;
import com.emergencia.model.entity.Procedimiento;
import com.emergencia.model.entity.TipoProcedimiento;

public class ProcedimientosPrehospitalario {
	@Wire Window winProcedimiento;
	@Wire Combobox cboTipoProcedimiento;
	TipoProcedimiento tipoProcedimientoSeleccionado;
	TipoProcedimientoDAO tipoProcedimientoDAO = new TipoProcedimientoDAO();
	Procedimiento procedimiento;
	RegistroPrehospitalario registroPrehospitalario;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		registroPrehospitalario = (RegistroPrehospitalario) Executions.getCurrent().getArg().get("RegistroPrehospitalario");
		procedimiento = new Procedimiento();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void agregar() {
		if(cboTipoProcedimiento.getSelectedItem().getValue() == null) {
			Clients.showNotification("Debe seleccionar tipo de procedimineto");
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {
						procedimiento.setEstado("A");
						procedimiento.setTipoProcedimiento((TipoProcedimiento) cboTipoProcedimiento.getSelectedItem().getValue());
						registroPrehospitalario.agregarProcedimiento(procedimiento);
						salir();						
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
		});
	}
	@Command
	public void salir() {
		winProcedimiento.detach();
	}
	public List<TipoProcedimiento> getListaTipoProcedimientos(){
		return tipoProcedimientoDAO.buscarTiposProcedimiento();
	}
	public TipoProcedimiento getTipoProcedimientoSeleccionado() {
		return tipoProcedimientoSeleccionado;
	}
	public void setTipoProcedimientoSeleccionado(TipoProcedimiento tipoProcedimientoSeleccionado) {
		this.tipoProcedimientoSeleccionado = tipoProcedimientoSeleccionado;
	}

	public Procedimiento getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(Procedimiento procedimiento) {
		this.procedimiento = procedimiento;
	}
}