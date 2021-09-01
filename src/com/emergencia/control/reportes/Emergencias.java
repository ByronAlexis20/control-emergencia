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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.emergencia.model.dao.MesDAO;
import com.emergencia.model.entity.Me;
import com.emergencia.util.PrintReport;

public class Emergencias {
	@Wire Textbox txtAnio;
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void imprimir() {
		try {
			if (mesSeleccionado == null) {
				Clients.showNotification("Debe seleccionar mes");
				return;
			}
			if(txtAnio.getText().toString().isEmpty()) {
				Clients.showNotification("Debe ingresar el año");
				return;
			}
			Messagebox.show("Descargar reporte?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onYes")) {		
						try {						
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
							report.crearReporte("/reportes/rptEmergencias.jasper",mesDAO, params);	
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
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
