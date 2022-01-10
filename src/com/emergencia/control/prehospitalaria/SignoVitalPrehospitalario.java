package com.emergencia.control.prehospitalaria;

import java.io.IOException;
import java.sql.Time;

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

import com.emergencia.model.entity.Prehospitalaria;
import com.emergencia.model.entity.SignoVital;

public class SignoVitalPrehospitalario {
	@Wire Window winSignosVitales;
	@Wire Textbox txtPresionArterial;
	@Wire Textbox txtPulsoMin;
	@Wire Textbox txtTemperaturaCorporal;
	@Wire Textbox txtFrecuenciaRespiratoria;
	@Wire Textbox txtLlenadoCapilar;
	@Wire Textbox txtSaturacionOxigeno;
	@Wire Textbox txtEscalaGlasgow;
	@Wire Timebox tmHora;
	SignoVital signoVital;
	Prehospitalaria prehospitalario;
	RegistroPrehospitalario registroPrehospitalario;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		prehospitalario = (Prehospitalaria) Executions.getCurrent().getArg().get("Prehospitalaria");
		registroPrehospitalario = (RegistroPrehospitalario) Executions.getCurrent().getArg().get("RegistroPrehospitalario");
		signoVital = (SignoVital) Executions.getCurrent().getArg().get("SignoVital");
		if(signoVital != null) {
			recuperarDatos();
		}
	}
	private void recuperarDatos() {
		txtPresionArterial.setText(String.valueOf(signoVital.getPresionArterial()));
		txtPulsoMin.setText(String.valueOf(signoVital.getPulsoMin()));
		txtTemperaturaCorporal.setText(String.valueOf(signoVital.getTemperatura()));
		txtFrecuenciaRespiratoria.setText(String.valueOf(signoVital.getFrecuenciaRespiratoria()));
		txtLlenadoCapilar.setText(String.valueOf(signoVital.getLlenadoCapilar()));
		txtSaturacionOxigeno.setText(String.valueOf(signoVital.getSaturacionOxigeno()));
		txtEscalaGlasgow.setText(signoVital.getEscalaGlasgow());
		tmHora.setValue(signoVital.getHora());
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void agregar() {
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {
						SignoVital signos = new SignoVital();
						signos.setEstado("A");
						signos.setFrecuenciaRespiratoria(Integer.parseInt(txtFrecuenciaRespiratoria.getText()));
						signos.setIdSignoVital(null);
						signos.setLlenadoCapilar(Integer.parseInt(txtLlenadoCapilar.getText()));
						signos.setPresionArterial(txtPresionArterial.getText());
						signos.setPulsoMin(Integer.parseInt(txtPulsoMin.getText()));
						signos.setSaturacionOxigeno(Integer.parseInt(txtSaturacionOxigeno.getText()));
						signos.setTemperatura(txtTemperaturaCorporal.getText());
						signos.setHora(new Time(tmHora.getValue().getTime()));
						signos.setEscalaGlasgow(txtEscalaGlasgow.getText());
						registroPrehospitalario.agregarSignoVital(signos);
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
	public SignoVital getSignoVital() {
		return signoVital;
	}
	public void setSignoVital(SignoVital signoVital) {
		this.signoVital = signoVital;
	}
	public Prehospitalaria getPrehospitalario() {
		return prehospitalario;
	}
	public void setPrehospitalario(Prehospitalaria prehospitalario) {
		this.prehospitalario = prehospitalario;
	}
	
}
