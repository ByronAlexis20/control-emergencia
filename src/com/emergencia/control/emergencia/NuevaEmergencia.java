package com.emergencia.control.emergencia;

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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.BarrioDAO;
import com.emergencia.model.dao.CantonDAO;
import com.emergencia.model.dao.EmergenciaDAO;
import com.emergencia.model.dao.FormaAvisoDAO;
import com.emergencia.model.dao.MesDAO;
import com.emergencia.model.dao.ParroquiaDAO;
import com.emergencia.model.dao.PersonalEmergenciaDAO;
import com.emergencia.model.dao.ProvinciaDAO;
import com.emergencia.model.dao.TipoEmergenciaDAO;
import com.emergencia.model.dao.UsuarioDAO;
import com.emergencia.model.entity.Barrio;
import com.emergencia.model.entity.Canton;
import com.emergencia.model.entity.Emergencia;
import com.emergencia.model.entity.FormaAviso;
import com.emergencia.model.entity.Me;
import com.emergencia.model.entity.Parroquia;
import com.emergencia.model.entity.PersonalEmergencia;
import com.emergencia.model.entity.Provincia;
import com.emergencia.model.entity.TipoEmergencia;
import com.emergencia.model.entity.Usuario;
import com.emergencia.util.Globales;

public class NuevaEmergencia {
	
	@Wire Combobox cboBarrio;
	@Wire Combobox cboInformante;
	@Wire Window winNuevaEmergencia;
	@Wire Combobox cboCanton;
	@Wire Combobox cboParroquia;
	@Wire Combobox cboProvincia;
	@Wire Combobox cboTipoEmergencia;
	@Wire Textbox txtDia;
	@Wire Combobox cboMes;
	@Wire Textbox txtAnio;
	@Wire Combobox cboReportadoPor;
	@Wire Combobox cboConfirmacionLlamada;
	@Wire Textbox txtTelefono;
	@Wire Textbox txtDireccion;
	@Wire Textbox txtReferencia;
	@Wire Textbox txtAvenida;
	@Wire Textbox txtDescripcionOperaciones;
	@Wire Textbox txtNovedades;
	@Wire Listbox lstPersonalEmergencia;
	
	List<Me> listaMeses;
	List<Provincia> listaProvincia;
	List<Canton> listaCanton;
	List<Parroquia> listaParroquia;
	List<TipoEmergencia> listaTipoEmergencia;
	List<FormaAviso> listaFormaAviso;
	
	List<PersonalEmergencia> listaBomberos;
	
	Me mesSeleccionado;
	Provincia provinciaSeleccionado;
	Canton cantonSeleccionado;
	Parroquia parroquiaSeleccionado;
	TipoEmergencia tipoEmergenciaSeleccionado;
	FormaAviso formaAvisoSeleccionado;
	Barrio barrioSeleccionado;
	Usuario usuarioSeleccionado;
	
	MesDAO mesDAO = new MesDAO();
	ProvinciaDAO provinciaDAO = new ProvinciaDAO();
	CantonDAO cantonDAO = new CantonDAO();
	ParroquiaDAO parroquiaDAO = new ParroquiaDAO();
	TipoEmergenciaDAO tipoEmergenciaDAO = new TipoEmergenciaDAO();
	FormaAvisoDAO formaAvisoDAO = new FormaAvisoDAO();
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	EmergenciaDAO emergenciaDAO = new EmergenciaDAO();
	BarrioDAO barrioDAO = new BarrioDAO();
	PersonalEmergenciaDAO personalEmergenciaDAO = new PersonalEmergenciaDAO();
	
	Emergencia emergencia;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		emergencia = (Emergencia) Executions.getCurrent().getArg().get("Emergencia");
		if(emergencia != null) {
			recuperarDatos();
		}else {
			emergencia = new Emergencia();
		}
	}
	
	private void recuperarDatos() {
		cboProvincia.setText(emergencia.getParroquia().getCanton().getProvincia().getProvincia());
		provinciaSeleccionado = emergencia.getParroquia().getCanton().getProvincia();
		seleccionarProvincia();
		cboCanton.setText(emergencia.getParroquia().getCanton().getCanton());
		cantonSeleccionado = emergencia.getParroquia().getCanton();
		seleccionarCanton();
		if(emergencia.getBarrio() != null) {
			cboBarrio.setText(emergencia.getBarrio().getBarrio());
			barrioSeleccionado = emergencia.getBarrio();
		}
		cboParroquia.setText(emergencia.getParroquia().getParroquia());
		parroquiaSeleccionado = emergencia.getParroquia();
		cboTipoEmergencia.setText(emergencia.getTipoEmergencia().getTipoEmergencia());
		tipoEmergenciaSeleccionado = emergencia.getTipoEmergencia();
		txtDia.setText(String.valueOf(emergencia.getDia()));
		cboMes.setText(emergencia.getMe().getMes());
		mesSeleccionado = emergencia.getMe();
		txtAnio.setText(String.valueOf(emergencia.getAnio()));
		cboReportadoPor.setText(emergencia.getFormaAviso().getFormaAviso());
		formaAvisoSeleccionado = emergencia.getFormaAviso();
		cboConfirmacionLlamada.setText(emergencia.getConfirmacionLlamada());
		txtTelefono.setText(emergencia.getTelefono());
		txtDireccion.setText(emergencia.getDescripcionOperaciones());
		txtReferencia.setText(emergencia.getReferencias());
		txtAvenida.setText(emergencia.getAvenida());
		txtDescripcionOperaciones.setText(emergencia.getDescripcionOperaciones());
		txtNovedades.setText(emergencia.getNovedades());
		if(emergencia.getUsuario() != null) {
			cboInformante.setText(emergencia.getUsuario().getPersona().getNombres() + " " + emergencia.getUsuario().getPersona().getApellidos());
			usuarioSeleccionado = emergencia.getUsuario();	
		}
		cargarBomberos();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GlobalCommand("PersonalEmergencia.buscarPorEmergencia")
	@NotifyChange({"listaBomberos"})
	public void cargarBomberos() {
		try {
			if(listaBomberos != null)
				listaBomberos = null;
			listaBomberos = personalEmergenciaDAO.buscarPorEmergencia(emergencia.getIdEmergencia());
			lstPersonalEmergencia.setModel(new ListModelList(listaBomberos));
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
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
							emergenciaDAO.getEntityManager().getTransaction().begin();
							cargarDatos();
							if(emergencia.getIdEmergencia() == null) {
								emergenciaDAO.getEntityManager().persist(emergencia);
							}else {
								emergenciaDAO.getEntityManager().merge(emergencia);
							}
							
							if(listaBomberos != null) {
								for(PersonalEmergencia per : listaBomberos) {
									if(per.getEmergencia() == null) {
										per.setEmergencia(emergencia);
										emergenciaDAO.getEntityManager().persist(per);
									}
								}
							}
							
							emergenciaDAO.getEntityManager().getTransaction().commit();
							Clients.showNotification("Proceso Ejecutado con exito.");
							BindUtils.postGlobalCommand(null, null, "Emergencia.findAll", null);
							salir();						
						} catch (Exception e) {
							e.printStackTrace();
							emergenciaDAO.getEntityManager().getTransaction().rollback();
						}
					}
				}
			});
		}catch(Exception ex) {
			
		}
	}
	private void cargarDatos() {
		emergencia.setAnio(Integer.parseInt(txtAnio.getText()));
		emergencia.setAvenida(txtAvenida.getText());
		emergencia.setConfirmacionLlamada(cboConfirmacionLlamada.getText());
		emergencia.setDescripcionOperaciones(txtDescripcionOperaciones.getText());
		emergencia.setDia(Integer.parseInt(txtDia.getText()));
		emergencia.setDireccionEvento(txtDireccion.getText());
		emergencia.setEstado("A");
		emergencia.setFormaAviso((FormaAviso)cboReportadoPor.getSelectedItem().getValue());
		emergencia.setBarrio((Barrio)cboBarrio.getSelectedItem().getValue());
		emergencia.setMe((Me)cboMes.getSelectedItem().getValue());
		emergencia.setNovedades(txtNovedades.getText());
		emergencia.setParroquia((Parroquia) cboParroquia.getSelectedItem().getValue());
		emergencia.setReferencias(txtReferencia.getText());
		emergencia.setTelefono(txtTelefono.getText());
		emergencia.setTipoEmergencia((TipoEmergencia)cboTipoEmergencia.getSelectedItem().getValue());
		emergencia.setUsuario((Usuario)cboInformante.getSelectedItem().getValue());
	}
	private boolean validarDatos() {
		try {
			boolean band = true;
			if(txtDia.getText().isEmpty()) {
				Clients.showNotification("Debe registrar el dia","info",txtDia,"end_center",2000);
				txtDia.focus();
				return false;
			}
			if(cboMes.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar el Mes","info",cboMes,"end_center",2000);
				return false;
			}
			if(txtAnio.getText().isEmpty()) {
				Clients.showNotification("Debe registrar el año","info",txtAnio,"end_center",2000);
				txtDia.focus();
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
			if(cboTipoEmergencia.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Tipo de emergencia","info",cboTipoEmergencia,"end_center",2000);
				return false;
			}
			if(cboReportadoPor.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Reportado por","info",cboReportadoPor,"end_center",2000);
				return false;
			}
			if(cboConfirmacionLlamada.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Confirmacion de llamada","info",cboConfirmacionLlamada,"end_center",2000);
				return false;
			}
			if(cboBarrio.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar barrio","info",cboBarrio,"end_center",2000);
				return false;
			}
			if(cboInformante.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Informante","info",cboInformante,"end_center",2000);
				return false;
			}
			return band;
		}catch(Exception ex) {
			return false;
		}
	}
	
	@Command
	public void nuevoBombero() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("NuevaEmergencia", this);
		//pasar tambien por parametros los bomberos que ya estan agregados para que no las muestre
		List<Usuario> bomberos = new ArrayList<>();
		for(PersonalEmergencia per : listaBomberos) {
			bomberos.add(per.getBombero());
		}
		params.put("Bomberos", bomberos);
		Window ventanaCargar = (Window) Executions.createComponents("/forms/emergencias/seleccionarBombero.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaBomberos"})
	public void agregarBombero(PersonalEmergencia bom) {
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
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		PersonalEmergencia usu = (PersonalEmergencia) lstPersonalEmergencia.getSelectedItem().getValue();
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						if(usu.getIdPersonalEmergencia() == null) {
							listaBomberos.remove(usu);
							lstPersonalEmergencia.setModel(new ListModelList(listaBomberos));
						}else {
							personalEmergenciaDAO.getEntityManager().getTransaction().begin();
							usu.setEstado("I");
							personalEmergenciaDAO.getEntityManager().merge(usu);
							personalEmergenciaDAO.getEntityManager().getTransaction().commit();
							listaBomberos.remove(usu);
							lstPersonalEmergencia.setModel(new ListModelList(listaBomberos));
						}
					} catch (Exception e) {
						e.printStackTrace();
						personalEmergenciaDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});	
	}
	
	@Command
	public void salir() {
		winNuevaEmergencia.detach();
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
		List<TipoEmergencia> listaTodos = tipoEmergenciaDAO.obtenerTodos();
		List<TipoEmergencia> lista = new ArrayList<>();
		for(TipoEmergencia t : listaTodos) {
			if(t.getGrupo().equals(Globales.codigoControlIncendio) || t.getGrupo().equals(Globales.codigoLaborSocial)) {
				lista.add(t);
			}
		}
		return lista;
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
		return formaAvisoDAO.obtenerTodos();
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
	public List<Barrio> getListaBarrios(){
		return barrioDAO.getBarrioPorDescripcion("");
	}
	public Barrio getBarrioSeleccionado() {
		return barrioSeleccionado;
	}
	public void setBarrioSeleccionado(Barrio barrioSeleccionado) {
		this.barrioSeleccionado = barrioSeleccionado;
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
	public List<PersonalEmergencia> getListaBomberos() {
		return listaBomberos;
	}
	public void setListaBomberos(List<PersonalEmergencia> listaBomberos) {
		this.listaBomberos = listaBomberos;
	}
	
}