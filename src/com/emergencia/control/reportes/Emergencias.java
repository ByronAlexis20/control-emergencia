package com.emergencia.control.reportes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;

import com.emergencia.model.dao.MesDAO;
import com.emergencia.model.entity.Me;
import com.emergencia.util.PrintReport;

@SuppressWarnings({ "serial", "rawtypes" })
public class Emergencias extends GenericForwardComposer{
	@Wire Iframe reporte;
	@Wire Textbox txtAnio;
	@Wire private Radio rbReporteMensual;
	@Wire private Radio rbReporteAnual;
	@Wire private Combobox cboMes;
	List<Me> listaMeses;
	MesDAO mesDAO = new MesDAO();
	Me mesSeleccionado;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		Date date = new Date();

        SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
        String currentYear = getYearFormat.format(date);
		txtAnio.setText(String.valueOf(currentYear));
	}
	
	@Command
	public void reporteMensual(){
		cboMes.setVisible(true);
	}
	
	@Command
	public void reporteAnual(){
		cboMes.setVisible(false);
	}
	
	@Command
	public void imprimir() {
		if(rbReporteMensual.isChecked()) {
			imprimirReporteMensual();
		}else if(rbReporteAnual.isChecked()) {
			imprimirReporteAnual();
		}
	}
	
	private void imprimirReporteMensual() {
		try {
			if (mesSeleccionado == null) {
				Clients.showNotification("Debe seleccionar mes");
				return;
			}
			if(txtAnio.getText().toString().isEmpty()) {
				Clients.showNotification("Debe ingresar el año");
				return;
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("NOMBRE_INSTITUCION", "CUERPO DE BOMBEROS DEL CANTÓN LA LIBERTAD");
			params.put("NOMBRE_REPORTE", "EMERGENCIAS " + txtAnio.getText() + "\nMES: " + mesSeleccionado.getMes());
			params.put("TITULO1", "CLASIFICACIÓN DE EMERGENCIAS " + txtAnio.getText());
			params.put("TITULO2", "TIPOS DE EMERGENCIA");
			params.put("TITULO3", "EMERGENCIAS DE CONTROL DE INCENDIO");
			params.put("TITULO4", "EMERGENCIAS PREHOSPITALARIAS");
			params.put("TITULO5", "EMERGENCIAS DE LABOR SOCIAL");
			params.put("ID_MES_P", mesSeleccionado.getIdMes());
			params.put("ANIO_P", Integer.parseInt(txtAnio.getText()));
			params.put("MES_P", mesSeleccionado.getMes());
			PrintReport report = new PrintReport();
			byte[] arr = report.crearReporte("/reportes/emergencia/rptEmergencias.jasper",mesDAO, params);	
			final AMedia amedia = new AMedia("Reporte-emergencias.pdf", "pdf","application/pdf", arr);
	    	reporte.setContent(amedia);
		}catch(Exception ex) {
			
		}
	}
	
	private void imprimirReporteAnual() {
		try {
			if(txtAnio.getText().toString().isEmpty()) {
				Clients.showNotification("Debe ingresar el año");
				return;
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("NOMBRE_INSTITUCION", "CUERPO DE BOMBEROS DEL CANTÓN LA LIBERTAD");
			params.put("NOMBRE_REPORTE", "EMERGENCIAS DEL AÑO " + txtAnio.getText());
			params.put("TITULO1", "CLASIFICACIÓN DE EMERGENCIAS " + txtAnio.getText());
			params.put("TITULO2", "TIPOS DE EMERGENCIA");
			params.put("TITULO3", "EMERGENCIAS DE CONTROL DE INCENDIO");
			params.put("TITULO4", "EMERGENCIAS PREHOSPITALARIAS");
			params.put("TITULO5", "EMERGENCIAS DE LABOR SOCIAL");
			params.put("ANIO_P", Integer.parseInt(txtAnio.getText()));
			PrintReport report = new PrintReport();
			byte[] arr = report.crearReporte("/reportes/emergenciaAnual/rptEmergencias.jasper",mesDAO, params);	
			final AMedia amedia = new AMedia("Reporte-emergencias.pdf", "pdf","application/pdf", arr);
	    	reporte.setContent(amedia);
		}catch(Exception ex) {
			
		}
	}
	
	public List<Me> getListaMeses() {
		return mesDAO.obtenerMeses();
	}
	public void setListaMeses(List<Me> listaMeses) {
		this.listaMeses = listaMeses;
	}
	public Me getMesSeleccionado() {
		return mesSeleccionado;
	}
	public void setMesSeleccionado(Me mesSeleccionado) {
		this.mesSeleccionado = mesSeleccionado;
	}
}
