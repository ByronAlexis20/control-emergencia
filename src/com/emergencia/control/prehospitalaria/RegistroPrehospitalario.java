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
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.CantonDAO;
import com.emergencia.model.dao.CondicionLlegadaDAO;
import com.emergencia.model.dao.GeneroDAO;
import com.emergencia.model.dao.LocalizacionLesionDAO;
import com.emergencia.model.dao.ParroquiaDAO;
import com.emergencia.model.dao.PersonalPrehospitalariaDAO;
import com.emergencia.model.dao.PrehospitalariaDAO;
import com.emergencia.model.dao.ProcedimientoDAO;
import com.emergencia.model.dao.ProvinciaDAO;
import com.emergencia.model.dao.SignoVitalDAO;
import com.emergencia.model.dao.TipoEmergenciaDAO;
import com.emergencia.model.dao.UsuarioDAO;
import com.emergencia.model.entity.Canton;
import com.emergencia.model.entity.CondicionLlegada;
import com.emergencia.model.entity.Genero;
import com.emergencia.model.entity.LocalizacionLesion;
import com.emergencia.model.entity.Parroquia;
import com.emergencia.model.entity.PersonalPrehospitalaria;
import com.emergencia.model.entity.Prehospitalaria;
import com.emergencia.model.entity.Procedimiento;
import com.emergencia.model.entity.Provincia;
import com.emergencia.model.entity.SignoVital;
import com.emergencia.model.entity.TipoEmergencia;
import com.emergencia.model.entity.Usuario;
import com.emergencia.util.Globales;

public class RegistroPrehospitalario {
	@Wire Window winRegistroPrehospitalaria;
	@Wire Listbox lstSignosVitales;
	@Wire Listbox lstProcedimiento;
	@Wire Listbox lstLocalizacion;
	@Wire Listbox lstPersonalEmergencia;
	@Wire Textbox txtCedulaUsuario;
	@Wire Textbox txtNombreUsuario;
	@Wire Textbox txtEdad;
	@Wire Combobox cboGenero;
	@Wire Combobox cboInformante;
	@Wire Datebox dtpFechaAtencion;
	@Wire Datebox dtpFechaEvento;
	@Wire Combobox cboCondicionLLegada;
	@Wire Textbox txtDireccion;
	@Wire Textbox txtLugar;
	@Wire Textbox txtInterrogatorio;
	@Wire Combobox cboTipoEmergencia;
	
	@Wire Combobox cboCanton;
	@Wire Combobox cboParroquia;
	@Wire Combobox cboProvincia;
	
	//divs para las flechas de los pasos
	@Wire Div divDatosGenerales;
	@Wire Div divPersonalEmergencia;
	@Wire Div divSignosVitales;
	@Wire Div divLesiones;
	
	@Wire Div winDatosGenerales;
	@Wire Div winPersonalEmergencia;
	@Wire Div winSignosVitales;
	@Wire Div winLesiones;
	
	//botones de control
	@Wire Button btnVolver;
	@Wire Button btnGrabar;
	@Wire Button btnSalir;
	@Wire Button btnSiguiente;
	
	Integer inx = 0;
	
	List<SignoVital> listaSignoVital;
	List<Procedimiento> listaProcedimiento;
	List<LocalizacionLesion> listaLocalizacionLesion;
	List<TipoEmergencia> listaTipoEmergencia;
	List<PersonalPrehospitalaria> listaBomberos;
	
	List<Provincia> listaProvincia;
	List<Canton> listaCanton;
	List<Parroquia> listaParroquia;
	
	Provincia provinciaSeleccionado;
	Canton cantonSeleccionado;
	Parroquia parroquiaSeleccionado;
	
	ProvinciaDAO provinciaDAO = new ProvinciaDAO();
	CantonDAO cantonDAO = new CantonDAO();
	ParroquiaDAO parroquiaDAO = new ParroquiaDAO();
	
	Prehospitalaria prehospitalario;
	GeneroDAO generoDAO = new GeneroDAO();
	CondicionLlegadaDAO condicionLlegadaDAO = new CondicionLlegadaDAO();
	SignoVitalDAO signoVitalDAO = new SignoVitalDAO();
	ProcedimientoDAO procedimientoDAO = new ProcedimientoDAO();
	LocalizacionLesionDAO localizacionLesionDAO = new LocalizacionLesionDAO();
	PrehospitalariaDAO prehospitalarioDAO = new PrehospitalariaDAO();
	TipoEmergenciaDAO tipoEmergenciaDAO = new TipoEmergenciaDAO();
	Genero generoSeleccionado;
	CondicionLlegada condicionLlegadaSeleccionado;
	TipoEmergencia tipoEmergenciaSeleccionado;
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	Usuario usuarioSeleccionado;
	PersonalPrehospitalariaDAO personalPrehospitalarioDAO = new PersonalPrehospitalariaDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		divDatosGenerales.setClass("paso-activo active");
		divPersonalEmergencia.setClass("paso-inactivo");
		divSignosVitales.setClass("paso-inactivo");
		divLesiones.setClass("paso-inactivo");
		
		winDatosGenerales.setVisible(true);
		winPersonalEmergencia.setVisible(false);
		winSignosVitales.setVisible(false);
		winLesiones.setVisible(false);
		
		btnVolver.setVisible(false);
		btnGrabar.setVisible(false);
		
		prehospitalario = (Prehospitalaria) Executions.getCurrent().getArg().get("Prehospitalaria");
		if(prehospitalario != null) {
			recuperarDatos();
		}else {
			prehospitalario = new Prehospitalaria();
		}
	}
	
	@Command
	public void siguiente() {
		inx ++;
		if(inx == 1) {
			if (validarDatos() == false) {
				inx --;
				return;
			}
			moverStep(inx);
		}else {
			moverStep(inx);
		}
	}
	
	@Command
	public void volver() {
		inx --;
		moverStep(inx);
	}
	private void moverStep(Integer i) {
		switch(i) {
			case 0:
				divDatosGenerales.setClass("paso-activo active");
				divPersonalEmergencia.setClass("paso-inactivo");
				divSignosVitales.setClass("paso-inactivo");
				divLesiones.setClass("paso-inactivo");
				
				btnVolver.setVisible(false);
				btnSiguiente.setVisible(true);
				btnGrabar.setVisible(false);
				
				winDatosGenerales.setVisible(true);
				winPersonalEmergencia.setVisible(false);
				winSignosVitales.setVisible(false);
				winLesiones.setVisible(false);
				
				break;
			case 1:
				divDatosGenerales.setClass("paso-completado");
				divPersonalEmergencia.setClass("paso-activo active");
				divSignosVitales.setClass("paso-inactivo");
				divLesiones.setClass("paso-inactivo");
				
				btnVolver.setVisible(true);
				btnSiguiente.setVisible(true);
				btnGrabar.setVisible(false);
				
				winDatosGenerales.setVisible(false);
				winPersonalEmergencia.setVisible(true);
				winSignosVitales.setVisible(false);
				winLesiones.setVisible(false);
				
				break;
			case 2:
				divDatosGenerales.setClass("paso-completado");
				divPersonalEmergencia.setClass("paso-completado");
				divSignosVitales.setClass("paso-activo active");
				divLesiones.setClass("paso-inactivo");
				
				btnVolver.setVisible(true);
				btnSiguiente.setVisible(true);
				btnGrabar.setVisible(false);
				
				winDatosGenerales.setVisible(false);
				winPersonalEmergencia.setVisible(false);
				winSignosVitales.setVisible(true);
				winLesiones.setVisible(false);
				
				break;
			case 3:
				divDatosGenerales.setClass("paso-completado");
				divPersonalEmergencia.setClass("paso-completado");
				divSignosVitales.setClass("paso-completado");
				divLesiones.setClass("paso-activo active");
				
				btnVolver.setVisible(true);
				btnSiguiente.setVisible(false);
				btnGrabar.setVisible(true);
				
				winDatosGenerales.setVisible(false);
				winPersonalEmergencia.setVisible(false);
				winSignosVitales.setVisible(false);
				winLesiones.setVisible(true);
				
				break;
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
		cboInformante.setText(prehospitalario.getInformante().getPersona().getNombres() + " " + prehospitalario.getInformante().getPersona().getApellidos());
		cboCondicionLLegada.setText(prehospitalario.getCondicionLlegada().getCondicionLlegada());
		condicionLlegadaSeleccionado = prehospitalario.getCondicionLlegada();
		txtDireccion.setText(prehospitalario.getDireccionEvento());
		txtLugar.setText(prehospitalario.getLugarEvento());
		txtInterrogatorio.setText(prehospitalario.getInterrogatorio());
		if(prehospitalario.getTipoEmergencia() != null) {
			cboTipoEmergencia.setText(prehospitalario.getTipoEmergencia().getTipoEmergencia());
			tipoEmergenciaSeleccionado = prehospitalario.getTipoEmergencia();
		}
		if(prehospitalario.getParroquia() != null) {
			cboProvincia.setText(prehospitalario.getParroquia().getCanton().getProvincia().getProvincia());
			provinciaSeleccionado = prehospitalario.getParroquia().getCanton().getProvincia();
			seleccionarProvincia();
			cboCanton.setText(prehospitalario.getParroquia().getCanton().getCanton());
			cantonSeleccionado = prehospitalario.getParroquia().getCanton();
			seleccionarCanton();
			cboParroquia.setText(prehospitalario.getParroquia().getParroquia());
		}
		
		cargarSignosVitales();
		cargarProcedimiento();
		cargarLocalizacion();
		cargarBomberos();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar() {
		try {
			if (validarDatos() == false) {
				return;
			}
			Messagebox.show("Desea guardar el registro?", "Confirmaci?n de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
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
							if(listaBomberos != null) {
								for(PersonalPrehospitalaria per : listaBomberos) {
									if(per.getPrehospitalaria() == null) {
										per.setPrehospitalaria(prehospitalario);
										prehospitalarioDAO.getEntityManager().persist(per);
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
		prehospitalario.setParroquia((Parroquia) cboParroquia.getSelectedItem().getValue());
		prehospitalario.setEdad(Integer.parseInt(txtEdad.getText()));
		prehospitalario.setGenero((Genero)cboGenero.getSelectedItem().getValue());
		prehospitalario.setFechaAtencion(dtpFechaAtencion.getValue());
		prehospitalario.setFechaEvento(dtpFechaEvento.getValue());
		prehospitalario.setInformante((Usuario)cboInformante.getSelectedItem().getValue());
		prehospitalario.setCondicionLlegada((CondicionLlegada)cboCondicionLLegada.getSelectedItem().getValue());
		prehospitalario.setTipoEmergencia((TipoEmergencia)cboTipoEmergencia.getSelectedItem().getValue());
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
			if(cboProvincia.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Provincia","info",cboProvincia,"end_center",2000);
				return false;
			}
			if(cboCanton.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Canton","info",cboCanton,"end_center",2000);
				return false;
			}
			if(cboParroquia.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Parroquia","info",cboParroquia,"end_center",2000);
				return false;
			}
			if(cboGenero.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar el g?nero","info",cboGenero,"end_center",2000);
				return false;
			}
			if(dtpFechaAtencion.getValue() == null) {
				Clients.showNotification("Debe registrar fecha de atenci?n","info",dtpFechaAtencion,"end_center",2000);
				return false;
			}
			if(dtpFechaEvento.getValue() == null) {
				Clients.showNotification("Debe registrar fecha de evento","info",dtpFechaEvento,"end_center",2000);
				return false;
			}
			if(cboInformante.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Informante","info",cboInformante,"end_center",2000);
				return false;
			}
			if(cboCondicionLLegada.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar el condici?n de llegada","info",cboCondicionLLegada,"end_center",2000);
				return false;
			}
			if(cboTipoEmergencia.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar el tipo de emergencia","info",cboTipoEmergencia,"end_center",2000);
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
	@GlobalCommand("PersonalPrehospitalaria.buscarPorEmergencia")
	@NotifyChange({"listaBomberos"})
	public void cargarBomberos() {
		try {
			if(listaBomberos != null)
				listaBomberos = null;
			listaBomberos = personalPrehospitalarioDAO.buscarPorEmergencia(prehospitalario.getIdPrehospitalaria());
			lstPersonalEmergencia.setModel(new ListModelList(listaBomberos));
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
			Clients.showNotification("Seleccione una opci?n de la lista.");
			return;
		}
		SignoVital sig = (SignoVital)lstSignosVitales.getSelectedItem().getValue();
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmaci?n de Eliminaci?n", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
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
	public void nuevoBombero() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("RegistroPrehospitalario", this);
		//pasar tambien por parametros los bomberos que ya estan agregados para que no las muestre
		List<Usuario> bomberos = new ArrayList<>();
		if(listaBomberos != null) {
			for(PersonalPrehospitalaria per : listaBomberos) {
				bomberos.add(per.getBombero());
			}
		}
		params.put("Bomberos", bomberos);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/prehospitalario/seleccionarBombero.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaBomberos"})
	public void agregarBombero(PersonalPrehospitalaria bom) {
		if(listaBomberos == null) {
			listaBomberos = new ArrayList<>();
		}
		listaBomberos.add(bom);
		lstPersonalEmergencia.setModel(new ListModelList(listaBomberos));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminarBombero() {
		if(lstPersonalEmergencia.getSelectedItem() == null) {
			Clients.showNotification("Seleccione una opci?n de la lista.");
			return;
		}
		PersonalPrehospitalaria usu = (PersonalPrehospitalaria) lstPersonalEmergencia.getSelectedItem().getValue();
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmaci?n de Eliminaci?n", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						if(usu.getIdPersonalPrehospitalaria() == null) {
							listaBomberos.remove(usu);
							lstPersonalEmergencia.setModel(new ListModelList(listaBomberos));
						}else {
							personalPrehospitalarioDAO.getEntityManager().getTransaction().begin();
							usu.setEstado("I");
							personalPrehospitalarioDAO.getEntityManager().merge(usu);
							personalPrehospitalarioDAO.getEntityManager().getTransaction().commit();
							listaBomberos.remove(usu);
							lstPersonalEmergencia.setModel(new ListModelList(listaBomberos));
						}
					} catch (Exception e) {
						e.printStackTrace();
						personalPrehospitalarioDAO.getEntityManager().getTransaction().rollback();
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
			Clients.showNotification("Seleccione una opci?n de la lista.");
			return;
		}
		Procedimiento pro = (Procedimiento)lstProcedimiento.getSelectedItem().getValue();
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmaci?n de Eliminaci?n", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
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
			Clients.showNotification("Seleccione una opci?n de la lista.");
			return;
		}
		LocalizacionLesion les = (LocalizacionLesion)lstLocalizacion.getSelectedItem().getValue();
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmaci?n de Eliminaci?n", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaCanton"})
	@Command
	public void seleccionarProvincia(){
		if(listaCanton != null)
			listaCanton = new ArrayList<>();
		
		listaCanton = cantonDAO.buscarPorIdProvincia(provinciaSeleccionado.getIdProvincia());
		cboCanton.setModel(new ListModelList(listaCanton));
		listaParroquia = new ArrayList<>();
		cboParroquia.setModel(new ListModelList(listaParroquia));
		cboCanton.setText("");
		cboParroquia.setText("");
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaParroquia"})
	@Command
	public void seleccionarCanton(){
		if(listaParroquia != null)
			listaParroquia = new ArrayList<>();
		listaParroquia = parroquiaDAO.buscarPorIdCanton(cantonSeleccionado.getIdCanton());
		cboParroquia.setModel(new ListModelList(listaParroquia));
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
	
	public TipoEmergencia getTipoEmergenciaSeleccionado() {
		return tipoEmergenciaSeleccionado;
	}
	
	public void setTipoEmergenciaSeleccionado(TipoEmergencia tipoEmergenciaSeleccionado) {
		this.tipoEmergenciaSeleccionado = tipoEmergenciaSeleccionado;
	}
	
	public List<Usuario> getUsuariosBomberos(){
		return usuarioDAO.buscarBomberoEmergencias();
	}
	
	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}
	
	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}
	
	public List<PersonalPrehospitalaria> getListaBomberos() {
		return listaBomberos;
	}
	
	public void setListaBomberos(List<PersonalPrehospitalaria> listaBomberos) {
		this.listaBomberos = listaBomberos;
	}
	
	public List<TipoEmergencia> getListaTipoEmergencia() {
		List<TipoEmergencia> listaTodos = tipoEmergenciaDAO.obtenerTodos();
		List<TipoEmergencia> lista = new ArrayList<>();
		for(TipoEmergencia t : listaTodos) {
			if(t.getGrupo().equals(Globales.codigoPrehospitalaria)) {
				lista.add(t);
			}
		}
		return lista;
	}
	
	public List<Provincia> getListaProvincia() {
		return provinciaDAO.obtenerProvincias();
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
	
	public List<Parroquia> getListaParroquia() {
		return listaParroquia;
	}
	
	public void setListaParroquia(List<Parroquia> listaParroquia) {
		this.listaParroquia = listaParroquia;
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

	public Parroquia getParroquiaSeleccionado() {
		return parroquiaSeleccionado;
	}

	public void setParroquiaSeleccionado(Parroquia parroquiaSeleccionado) {
		this.parroquiaSeleccionado = parroquiaSeleccionado;
	}
}