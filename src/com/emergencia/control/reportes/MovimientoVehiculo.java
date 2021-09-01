package com.emergencia.control.reportes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;

import com.emergencia.model.dao.VehiculoDAO;
import com.emergencia.util.PrintReport;

public class MovimientoVehiculo {
	@Wire Datebox dtpFechaInicio;
	@Wire Datebox dtpFechaFin;
	VehiculoDAO vechiculoDAO = new VehiculoDAO();
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		dtpFechaInicio.setValue(new Date());
		dtpFechaFin.setValue(new Date());
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void imprimir() {
		try {
			if (dtpFechaInicio.getValue() == null) {
				Clients.showNotification("Debe seleccionar Fecha de incio");
				return;
			}
			if(dtpFechaFin.getValue() == null) {
				Clients.showNotification("Debe seleccionar fecha fin");
				return;
			}
			SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
			
			Messagebox.show("Descargar reporte?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onYes")) {		
						try {						
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("NOMBRE_INSTITUCION", "CUERPO DE BOMBEROS DEL CANTÓN LA LIBERTAD");
							params.put("NOMBRE_REPORTE", "EMERGENCIAS POR VEHÍCULOS\nDel " + formatoFecha.format(dtpFechaInicio.getValue()) + " al " + formatoFecha.format(dtpFechaFin.getValue()));
							params.put("FECHA_INICIO", dtpFechaInicio.getValue());
							params.put("FECHA_FIN", dtpFechaFin.getValue());
							PrintReport report = new PrintReport();
							report.crearReporte("/reportes/rptVehiculos.jasper",vechiculoDAO, params);	
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		}catch(Exception ex) {
			
		}
	}
}
