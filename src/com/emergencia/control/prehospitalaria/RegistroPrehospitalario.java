package com.emergencia.control.prehospitalaria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.CondicionLlegadaDAO;
import com.emergencia.model.dao.GeneroDAO;
import com.emergencia.model.dao.LocalizacionLesionDAO;
import com.emergencia.model.dao.PrehospitalariaDAO;
import com.emergencia.model.dao.ProcedimientoDAO;
import com.emergencia.model.dao.SignoVitalDAO;
import com.emergencia.model.entity.CondicionLlegada;
import com.emergencia.model.entity.Genero;
import com.emergencia.model.entity.LocalizacionLesion;
import com.emergencia.model.entity.Prehospitalaria;
import com.emergencia.model.entity.Procedimiento;
import com.emergencia.model.entity.SignoVital;

public class RegistroPrehospitalario {
	@Wire Window winRegistroPrehospitalaria;
	@Wire Listbox lstSignosVitales;
	@Wire Listbox lstProcedimiento;
	@Wire Listbox lstLocalizacion;
	@Wire Textbox txtCedulaUsuario;
	@Wire Textbox txtNombreUsuario;
	@Wire Textbox txtEdad;
	@Wire Combobox cboGenero;
	@Wire Datebox dtpFechaAtencion;
	@Wire Datebox dtpFechaEvento;
	@Wire Textbox txtCedulaInformante;
	@Wire Textbox txtNombreInformante;
	@Wire Combobox cboCondicionLLegada;
	@Wire Textbox txtDireccion;
	@Wire Textbox txtLugar;
	@Wire Textbox txtInterrogatorio;
	
	List<SignoVital> listaSignoVital;
	List<Procedimiento> listaProcedimiento;
	List<LocalizacionLesion> listaLocalizacionLesion;
	Prehospitalaria prehospitalario;
	GeneroDAO generoDAO = new GeneroDAO();
	CondicionLlegadaDAO condicionLlegadaDAO = new CondicionLlegadaDAO();
	SignoVitalDAO signoVitalDAO = new SignoVitalDAO();
	ProcedimientoDAO procedimientoDAO = new ProcedimientoDAO();
	LocalizacionLesionDAO localizacionLesionDAO = new LocalizacionLesionDAO();
	PrehospitalariaDAO prehospitalarioDAO = new PrehospitalariaDAO();
	Genero generoSeleccionado;
	CondicionLlegada condicionLlegadaSeleccionado;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		prehospitalario = (Prehospitalaria) Executions.getCurrent().getArg().get("Prehospitalaria");
		if(prehospitalario != null) {
			recuperarDatos();
		}else {
			prehospitalario = new Prehospitalaria();
		}
	}
	private void recuperarDatos() {
		txtCedulaUsuario.setText(prehospitalario.getCedulaUsuario());
		txtNombreUsuario.setText(prehospitalario.getNombreUsuario());
		txtEdad.setText(String.valueOf(prehospitalario.getEdad()));
		cboGenero.setText(prehospitalario.getGenero().getGenero());
		generoSeleccionado = prehospitalario.getGenero();
		dtpFechaAtencion.setValue(prehospitalario.getFechaAtencion());
		dtpFechaEvento.setValue(prehospitalario.getFechaEvento());
		txtCedulaInformante.setText(prehospitalario.getCedulaInformante());
		txtNombreInformante.setText(prehospitalario.getNombreInformante());
		cboCondicionLLegada.setText(prehospitalario.getCondicionLlegada().getCondicionLlegada());
		condicionLlegadaSeleccionado = prehospitalario.getCondicionLlegada();
		txtDireccion.setText(prehospitalario.getDireccionEvento());
		txtLugar.setText(prehospitalario.getLugarEvento());
		txtInterrogatorio.setText(prehospitalario.getInterrogatorio());
		cargarSignosVitales();
		cargarProcedimiento();
		cargarLocalizacion();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar() {
		try {
			if (validarDatos() == false) {
				return;
			}
			Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onYes")) {		
						try {						
							prehospitalarioDAO.getEntityManager().getTransaction().begin();
							cargarDatos();
							if(prehospitalario.getIdPrehospitalaria() == null) {
								prehospitalarioDAO.getEntityManager().persist(prehospitalario);
							}else {
								prehospitalarioDAO.getEntityManager().merge(prehospitalario);
							}
							if(listaSignoVital != null) {
								for(SignoVital sig : listaSignoVital) {
									if(sig.getPrehospitalaria() == null) {
										sig.setPrehospitalaria(prehospitalario);
										prehospitalarioDAO.getEntityManager().persist(sig);
									}
								}
							}
							if(listaProcedimiento != null) {
								for(Procedimiento sig : listaProcedimiento) {
									if(sig.getPrehospitalaria() == null) {
										sig.setPrehospitalaria(prehospitalario);
										prehospitalarioDAO.getEntityManager().persist(sig);
									}
								}
							}
							if(listaLocalizacionLesion != null) {
								for(LocalizacionLesion sig : listaLocalizacionLesion) {
									if(sig.getPrehospitalaria() == null) {
										sig.setPrehospitalaria(prehospitalario);
										prehospitalarioDAO.getEntityManager().persist(sig);
									}
								}
							}
							prehospitalarioDAO.getEntityManager().getTransaction().commit();
							Clients.showNotification("Proceso Ejecutado con exito.");
							BindUtils.postGlobalCommand(null, null, "Prehospitalaria.findAll", null);
							salir();						
						} catch (Exception e) {
							e.printStackTrace();
							prehospitalarioDAO.getEntityManager().getTransaction().rollback();
						}
					}
				}
			});
		}catch(Exception ex) {
			
		}
	}
	private void cargarDatos() {
		prehospitalario.setCedulaUsuario(txtCedulaUsuario.getText());
		prehospitalario.setNombreUsuario(txtNombreUsuario.getText());
		prehospitalario.setEdad(Integer.parseInt(txtEdad.getText()));
		prehospitalario.setGenero((Genero)cboGenero.getSelectedItem().getValue());
		prehospitalario.setFechaAtencion(dtpFechaAtencion.getValue());
		prehospitalario.setFechaEvento(dtpFechaEvento.getValue());
		prehospitalario.setCedulaInformante(txtCedulaInformante.getText());
		prehospitalario.setNombreInformante(txtNombreInformante.getText());
		prehospitalario.setCondicionLlegada((CondicionLlegada)cboCondicionLLegada.getSelectedItem().getValue());
		prehospitalario.setDireccionEvento(txtDireccion.getText());
		prehospitalario.setLugarEvento(txtLugar.getText());
		prehospitalario.setInterrogatorio(txtInterrogatorio.getText());
		prehospitalario.setEstado("A");
	}
	private boolean validarDatos() {
		try {
			boolean band = true;
			if(txtCedulaUsuario.getText().isEmpty()) {
				Clients.showNotification("Debe registrar cedula del usuario","info",txtCedulaUsuario,"end_center",2000);
				txtCedulaUsuario.focus();
				return false;
			}
			if(txtNombreUsuario.getText().isEmpty()) {
				Clients.showNotification("Debe registrar nombre del usuario","info",txtNombreUsuario,"end_center",2000);
				txtNombreUsuario.focus();
				return false;
			}
			if(txtEdad.getText().isEmpty()) {
				Clients.showNotification("Debe registrar edad del usuario","info",txtEdad,"end_center",2000);
				txtEdad.focus();
				return false;
			}
			if(cboGenero.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar el género","info",cboGenero,"end_center",2000);
				return false;
			}
			if(dtpFechaAtencion.getValue() == null) {
				Clients.showNotification("Debe registrar fecha de atención","info",dtpFechaAtencion,"end_center",2000);
				return false;
			}
			if(dtpFechaEvento.getValue() == null) {
				Clients.showNotification("Debe registrar fecha de evento","info",dtpFechaEvento,"end_center",2000);
				return false;
			}
			if(txtCedulaInformante.getText().isEmpty()) {
				Clients.showNotification("Debe registrar cedula del informante","info",txtCedulaInformante,"end_center",2000);
				txtCedulaInformante.focus();
				return false;
			}
			if(txtNombreInformante.getText().isEmpty()) {
				Clients.showNotification("Debe registrar nombre del informante","info",txtNombreInformante,"end_center",2000);
				txtNombreInformante.focus();
				return false;
			}
			if(cboCondicionLLegada.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar el condición de llegada","info",cboCondicionLLegada,"end_center",2000);
				return false;
			}
			return band;
		}catch(Exception ex) {
			return false;
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GlobalCommand("SignoVital.buscarPorPrehospitalario")
	@NotifyChange({"listaSignoVital"})
	public void cargarSignosVitales() {
		try {
			if(listaSignoVital != null)
				listaSignoVital = null;
			listaSignoVital = signoVitalDAO.buscarPorPrehospitalario(prehospitalario.getIdPrehospitalaria());
			lstSignosVitales.setModel(new ListModelList(listaSignoVital));
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GlobalCommand("Procedimiento.buscarPorPrehospitalario")
	@NotifyChange({"listaProcedimiento"})
	public void cargarProcedimiento() {
		try {
			if(listaProcedimiento != null)
				listaProcedimiento = null;
			listaProcedimiento = procedimientoDAO.buscarPorPrehospitalario(prehospitalario.getIdPrehospitalaria());
			lstProcedimiento.setModel(new ListModelList(listaProcedimiento));
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GlobalCommand("LocalizacionLesion.buscarPorPrehospitalario")
	@NotifyChange({"listaLocalizacionLesion"})
	public void cargarLocalizacion() {
		try {
			if(listaLocalizacionLesion != null)
				listaLocalizacionLesion = null;
			listaLocalizacionLesion = localizacionLesionDAO.buscarPorPrehospitalario(prehospitalario.getIdPrehospitalaria());
			lstLocalizacion.setModel(new ListModelList(listaLocalizacionLesion));
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Command
	public void nuevoSignoVital() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("RegistroPrehospitalario", this);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/prehospitalario/signoVital.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaSignoVital"})
	public void agregarSignoVital(SignoVital sig) {
		if(listaSignoVital == null) {
			listaSignoVital = new ArrayList<>();
		}
		listaSignoVital.add(sig);
		lstSignosVitales.setModel(new ListModelList(listaSignoVital));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminarSignoVital() {
		if(lstSignosVitales.getSelectedItem() == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		SignoVital sig = (SignoVital)lstSignosVitales.getSelectedItem().getValue();
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						if(sig.getIdSignoVital() == null) {
							listaSignoVital.remove(sig);
							lstSignosVitales.setModel(new ListModelList(listaSignoVital));
						}else {
							signoVitalDAO.getEntityManager().getTransaction().begin();
							sig.setEstado("I");
							signoVitalDAO.getEntityManager().merge(sig);
							signoVitalDAO.getEntityManager().getTransaction().commit();
							listaSignoVital.remove(sig);
							lstSignosVitales.setModel(new ListModelList(listaSignoVital));
						}
					} catch (Exception e) {
						e.printStackTrace();
						signoVitalDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});	
	}
	@Command
	public void nuevoProcedimiento() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("RegistroPrehospitalario", this);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/prehospitalario/procedimiento.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaProcedimiento"})
	public void agregarProcedimiento(Procedimiento tip) {
		if(listaProcedimiento == null) {
			listaProcedimiento = new ArrayList<>();
		}
		listaProcedimiento.add(tip);
		lstProcedimiento.setModel(new ListModelList(listaProcedimiento));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminarProcedimiento() {
		if(lstProcedimiento.getSelectedItem() == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Procedimiento pro = (Procedimiento)lstProcedimiento.getSelectedItem().getValue();
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						if(pro.getIdProcedimiento() == null) {
							listaProcedimiento.remove(pro);
							lstProcedimiento.setModel(new ListModelList(listaProcedimiento));
						}else {
							procedimientoDAO.getEntityManager().getTransaction().begin();
							pro.setEstado("I");
							procedimientoDAO.getEntityManager().merge(pro);
							procedimientoDAO.getEntityManager().getTransaction().commit();
							listaProcedimiento.remove(pro);
							lstProcedimiento.setModel(new ListModelList(listaProcedimiento));
						}
					} catch (Exception e) {
						e.printStackTrace();
						procedimientoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});	
	}
	@Command
	public void nuevoLocalizacionLesion() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("RegistroPrehospitalario", this);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/prehospitalario/lesiones.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaLocalizacionLesion"})
	public void agregarLocalizacionLesion(LocalizacionLesion les) {
		if(listaLocalizacionLesion == null) {
			listaLocalizacionLesion = new ArrayList<>();
		}
		listaLocalizacionLesion.add(les);
		lstLocalizacion.setModel(new ListModelList(listaLocalizacionLesion));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminarLocalizacionLesion() {
		if(lstLocalizacion.getSelectedItem() == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		LocalizacionLesion les = (LocalizacionLesion)lstLocalizacion.getSelectedItem().getValue();
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						if(les.getIdLocalizacionLesion() == null) {
							listaLocalizacionLesion.remove(les);
							lstLocalizacion.setModel(new ListModelList(listaLocalizacionLesion));
						}else {
							localizacionLesionDAO.getEntityManager().getTransaction().begin();
							les.setEstado("I");
							localizacionLesionDAO.getEntityManager().merge(les);
							localizacionLesionDAO.getEntityManager().getTransaction().commit();
							listaLocalizacionLesion.remove(les);
							lstLocalizacion.setModel(new ListModelList(listaLocalizacionLesion));
						}
					} catch (Exception e) {
						e.printStackTrace();
						localizacionLesionDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});	
	}
	@Command
	public void salir() {
		winRegistroPrehospitalaria.detach();
	}
	public List<Genero> getListaGenero(){
		return generoDAO.buscarGeneros();
	}
	public List<CondicionLlegada> getListaCondicionLlegada(){
		return condicionLlegadaDAO.buscarCondicionLlegada();
	}
	public Prehospitalaria getPrehospitalario() {
		return prehospitalario;
	}
	public void setPrehospitalario(Prehospitalaria prehospitalario) {
		this.prehospitalario = prehospitalario;
	}
	public List<SignoVital> getListaSignoVital() {
		return listaSignoVital;
	}
	public void setListaSignoVital(List<SignoVital> listaSignoVital) {
		this.listaSignoVital = listaSignoVital;
	}
	public List<Procedimiento> getListaProcedimiento() {
		return listaProcedimiento;
	}
	public void setListaProcedimiento(List<Procedimiento> listaProcedimiento) {
		this.listaProcedimiento = listaProcedimiento;
	}
	public List<LocalizacionLesion> getListaLocalizacionLesion() {
		return listaLocalizacionLesion;
	}
	public void setListaLocalizacionLesion(List<LocalizacionLesion> listaLocalizacionLesion) {
		this.listaLocalizacionLesion = listaLocalizacionLesion;
	}
	public Genero getGeneroSeleccionado() {
		return generoSeleccionado;
	}
	public void setGeneroSeleccionado(Genero generoSeleccionado) {
		this.generoSeleccionado = generoSeleccionado;
	}
	public CondicionLlegada getCondicionLlegadaSeleccionado() {
		return condicionLlegadaSeleccionado;
	}
	public void setCondicionLlegadaSeleccionado(CondicionLlegada condicionLlegadaSeleccionado) {
		this.condicionLlegadaSeleccionado = condicionLlegadaSeleccionado;
	}
	
}