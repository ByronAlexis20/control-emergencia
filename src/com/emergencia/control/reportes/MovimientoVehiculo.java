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
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Iframe;

import com.emergencia.model.dao.VehiculoDAO;
import com.emergencia.util.PrintReport;

@SuppressWarnings({ "serial", "rawtypes" })
public class MovimientoVehiculo extends GenericForwardComposer{
	@Wire Iframe reporte;
	
	@Wire Datebox dtpFechaInicio;
	@Wire Datebox dtpFechaFin;
	VehiculoDAO vechiculoDAO = new VehiculoDAO();
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		dtpFechaInicio.setValue(new Date());
		dtpFechaFin.setValue(new Date());
	}
	
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
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("NOMBRE_INSTITUCION", "CUERPO DE BOMBEROS DEL CANTÓN LA LIBERTAD");
			params.put("NOMBRE_REPORTE", "EMERGENCIAS POR VEHÍCULOS\nDel " + formatoFecha.format(dtpFechaInicio.getValue()) + " al " + formatoFecha.format(dtpFechaFin.getValue()));
			params.put("FECHA_INICIO", dtpFechaInicio.getValue());
			params.put("FECHA_FIN", dtpFechaFin.getValue());
			PrintReport report = new PrintReport();
			byte[] arr = report.crearReporte("/reportes/rptVehiculos.jasper",vechiculoDAO, params);
			final AMedia amedia = new AMedia("Control-Vehiculo.pdf", "pdf","application/pdf", arr);
	    	reporte.setContent(amedia);
		}catch(Exception ex) {
			
		}
	}
}
