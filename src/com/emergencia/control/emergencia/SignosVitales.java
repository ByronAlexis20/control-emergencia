package com.emergencia.control.emergencia;

import java.io.IOException;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import com.emergencia.model.entity.Emergencia;
import com.emergencia.model.entity.SignoVitalEmergencia;

public class SignosVitales {
	@Wire Window winSignosVitales;
	@Wire Timebox tmHora;
	@Wire Textbox txtPresionArterial;
	@Wire Textbox txtPulsoMin;
	@Wire Textbox txtTemperaturaCorporal;
	@Wire Textbox txtFrecuenciaRespiratoria;
	@Wire Textbox txtLlenadoCapilar;
	@Wire Textbox txtSaturacionOxigeno;
	@Wire Textbox txtEscalaGlasgow;

	SignoVitalEmergencia signoVital;
	Emergencia emergencia;
	NuevaEmergencia nuevaEmergencia;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		emergencia = (Emergencia) Executions.getCurrent().getArg().get("Emergencia");
		nuevaEmergencia = (NuevaEmergencia) Executions.getCurrent().getArg().get("NuevaEmergencia");
		signoVital = (SignoVitalEmergencia) Executions.getCurrent().getArg().get("SignoVital");
		if(signoVital != null) {
			recuperarDatos();
		}
	}
	private void recuperarDatos() {
		txtPresionArterial.setText(signoVital.getPresionArterial());
		txtPulsoMin.setText(String.valueOf(signoVital.getPulsoMinimo()));
		txtTemperaturaCorporal.setText(String.valueOf(signoVital.getTemperaturaCorporal()));
		txtFrecuenciaRespiratoria.setText(String.valueOf(signoVital.getFrecuenciaRespiratoria()));
		txtLlenadoCapilar.setText(String.valueOf(signoVital.getLlenadoCapilar()));
		txtSaturacionOxigeno.setText(String.valueOf(signoVital.getSaturacionOxigeno()));
		txtEscalaGlasgow.setText(String.valueOf(signoVital.getEscalaGlasgow()));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void agregar() {
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {
						SignoVitalEmergencia signos = new SignoVitalEmergencia();
						signos.setEscalaGlasgow(Integer.parseInt(txtEscalaGlasgow.getText()));
						signos.setEstado("A");
						signos.setFrecuenciaRespiratoria(Integer.parseInt(txtFrecuenciaRespiratoria.getText()));
						signos.setIdSignoVital(null);
						signos.setLlenadoCapilar(Integer.parseInt(txtLlenadoCapilar.getText()));
						signos.setPresionArterial(txtPresionArterial.getText());
						signos.setPulsoMinimo(Integer.parseInt(txtPulsoMin.getText()));
						signos.setSaturacionOxigeno(Integer.parseInt(txtSaturacionOxigeno.getText()));
						signos.setTemperaturaCorporal(Integer.parseInt(txtTemperaturaCorporal.getText()));
						nuevaEmergencia.agregarSignoVital(signos);
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
		winSignosVitales.detach();
	}
	public SignoVitalEmergencia getSignoVital() {
		return signoVital;
	}
	public void setSignoVital(SignoVitalEmergencia signoVital) {
		this.signoVital = signoVital;
	}
	public Emergencia getEmergencia() {
		return emergencia;
	}
	public void setEmergencia(Emergencia emergencia) {
		this.emergencia = emergencia;
	}
}