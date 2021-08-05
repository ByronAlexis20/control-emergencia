package com.emergencia.control.emergencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.emergencia.model.entity.Canton;
import com.emergencia.model.entity.FormaAviso;
import com.emergencia.model.entity.Me;
import com.emergencia.model.entity.Parroquia;
import com.emergencia.model.entity.Provincia;
import com.emergencia.model.entity.SignoVitalEmergencia;
import com.emergencia.model.entity.TipoEmergencia;

public class NuevaEmergencia {
	
	@Wire Listbox lstSignosVitales;
	
	List<Me> listaMeses;
	List<Provincia> listaProvincia;
	List<Canton> listaCanton;
	List<Parroquia> listaParroquia;
	List<TipoEmergencia> listaTipoEmergencia;
	List<FormaAviso> listaFormaAviso;
	List<SignoVitalEmergencia> listaSignosVitales;
	
	Me mesSeleccionado;
	Provincia provinciaSeleccionado;
	Canton cantonSeleccionado;
	Parroquia parroquiaSeleccionado;
	TipoEmergencia tipoEmergenciaSeleccionado;
	FormaAviso formaAvisoSeleccionado;
	SignoVitalEmergencia signoVitalSeleccionado;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}
	@Command
	public void nuevoSignoVital() {
		Window ventanaCargar = (Window) Executions.createComponents("/forms/emergencias/signosVitales.zul", null, null);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaSignosVitales"})
	public void agregarSignoVital(SignoVitalEmergencia sig) {
		if(listaSignosVitales == null) {
			listaSignosVitales = new ArrayList<>();
		}
		listaSignosVitales.add(sig);
		lstSignosVitales.setModel(new ListModelList(listaSignosVitales));
	}
	public List<Me> getListaMeses() {
		return listaMeses;
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
	public List<Provincia> getListaProvincia() {
		return listaProvincia;
	}
	public void setListaProvincia(List<Provincia> listaProvincia) {
		this.listaProvincia = listaProvincia;
	}
	public List<Canton> getListaCanton() {
		return listaCanton;
	}
	public void setListaCanton(List<Canton> listaCanton) {
		this.listaCanton = listaCanton;
	}
	public Provincia getProvinciaSeleccionado() {
		return provinciaSeleccionado;
	}
	public void setProvinciaSeleccionado(Provincia provinciaSeleccionado) {
		this.provinciaSeleccionado = provinciaSeleccionado;
	}
	public Canton getCantonSeleccionado() {
		return cantonSeleccionado;
	}
	public void setCantonSeleccionado(Canton cantonSeleccionado) {
		this.cantonSeleccionado = cantonSeleccionado;
	}
	public List<Parroquia> getListaParroquia() {
		return listaParroquia;
	}
	public void setListaParroquia(List<Parroquia> listaParroquia) {
		this.listaParroquia = listaParroquia;
	}
	public Parroquia getParroquiaSeleccionado() {
		return parroquiaSeleccionado;
	}
	public void setParroquiaSeleccionado(Parroquia parroquiaSeleccionado) {
		this.parroquiaSeleccionado = parroquiaSeleccionado;
	}
	public List<TipoEmergencia> getListaTipoEmergencia() {
		return listaTipoEmergencia;
	}
	public void setListaTipoEmergencia(List<TipoEmergencia> listaTipoEmergencia) {
		this.listaTipoEmergencia = listaTipoEmergencia;
	}
	public TipoEmergencia getTipoEmergenciaSeleccionado() {
		return tipoEmergenciaSeleccionado;
	}
	public void setTipoEmergenciaSeleccionado(TipoEmergencia tipoEmergenciaSeleccionado) {
		this.tipoEmergenciaSeleccionado = tipoEmergenciaSeleccionado;
	}
	public List<FormaAviso> getListaFormaAviso() {
		return listaFormaAviso;
	}
	public void setListaFormaAviso(List<FormaAviso> listaFormaAviso) {
		this.listaFormaAviso = listaFormaAviso;
	}
	public FormaAviso getFormaAvisoSeleccionado() {
		return formaAvisoSeleccionado;
	}
	public void setFormaAvisoSeleccionado(FormaAviso formaAvisoSeleccionado) {
		this.formaAvisoSeleccionado = formaAvisoSeleccionado;
	}

	public List<SignoVitalEmergencia> getListaSignosVitales() {
		return listaSignosVitales;
	}

	public void setListaSignosVitales(List<SignoVitalEmergencia> listaSignosVitales) {
		this.listaSignosVitales = listaSignosVitales;
	}

	public SignoVitalEmergencia getSignoVitalSeleccionado() {
		return signoVitalSeleccionado;
	}

	public void setSignoVitalSeleccionado(SignoVitalEmergencia signoVitalSeleccionado) {
		this.signoVitalSeleccionado = signoVitalSeleccionado;
	}
	
}