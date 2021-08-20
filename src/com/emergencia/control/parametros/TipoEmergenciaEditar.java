package com.emergencia.control.parametros;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.emergencia.model.dao.TipoEmergenciaDAO;
import com.emergencia.model.entity.TipoEmergencia;
import com.emergencia.util.Globales;

public class TipoEmergenciaEditar {
	@Wire private Window winTipoEmergenciaEditar;
	@Wire private Textbox txtTipoEmergencia;
	@Wire private Combobox cboGrupo;
	TipoEmergencia tipoEmergencia;
	TipoEmergenciaDAO tipoEmergenciaDAO = new TipoEmergenciaDAO();
	GrupoEmergencia grupoSeleccionado;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		tipoEmergencia = (TipoEmergencia) Executions.getCurrent().getArg().get("TipoEmergencia");
		if (tipoEmergencia == null) {
			tipoEmergencia = new TipoEmergencia();
			tipoEmergencia.setEstado("A");
		}else {
			if(tipoEmergencia.getGrupo().equals(Globales.codigoPrehospitalaria)) {
				cboGrupo.setText("Prehospitalario");
			}else if(tipoEmergencia.getGrupo().equals(Globales.codigoControlIncendio)) {
				cboGrupo.setText("Control de incencio");
			}else if(tipoEmergencia.getGrupo().equals(Globales.codigoLaborSocial)) {
				cboGrupo.setText("Labor Social");
			}
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar(){
		if(isValidarDatos() == true) {
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {						
						tipoEmergenciaDAO.getEntityManager().getTransaction().begin();
						GrupoEmergencia g = (GrupoEmergencia)cboGrupo.getSelectedItem().getValue();
						tipoEmergencia.setGrupo(g.getCodigo());
						if (tipoEmergencia.getIdTipoEmergencia() == null) {
							tipoEmergenciaDAO.getEntityManager().persist(tipoEmergencia);
						}else{
							tipoEmergencia = (TipoEmergencia) tipoEmergenciaDAO.getEntityManager().merge(tipoEmergencia);
						}			
						tipoEmergenciaDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						tipoEmergenciaDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "TipoEmergencia.buscarPorPatron", null);
		winTipoEmergenciaEditar.detach();
	}
	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtTipoEmergencia.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el tipo de emergencia","info",txtTipoEmergencia,"end_center",2000);
				txtTipoEmergencia.setFocus(true);
				return retorna;
			}
			if(cboGrupo.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar grupo","info",cboGrupo,"end_center",2000);
				return retorna;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public TipoEmergencia getTipoEmergencia() {
		return tipoEmergencia;
	}
	public void setTipoEmergencia(TipoEmergencia tipoEmergencia) {
		this.tipoEmergencia = tipoEmergencia;
	}
	public List<GrupoEmergencia> getListaGrupoEmergencia(){
		List<GrupoEmergencia> lista = new ArrayList<>();
		GrupoEmergencia g1 = new GrupoEmergencia();
		g1.setCodigo(Globales.codigoPrehospitalaria);
		g1.setDescripcion("Prehospitalario");
		lista.add(g1);
		GrupoEmergencia g2 = new GrupoEmergencia();
		g2.setCodigo(Globales.codigoControlIncendio);
		g2.setDescripcion("Control de incencio");
		lista.add(g2);
		GrupoEmergencia g3 = new GrupoEmergencia();
		g3.setCodigo(Globales.codigoLaborSocial);
		g3.setDescripcion("Labor Social");
		lista.add(g3);
		return lista;
	}
	public GrupoEmergencia getGrupoSeleccionado() {
		return grupoSeleccionado;
	}
	public void setGrupoSeleccionado(GrupoEmergencia grupoSeleccionado) {
		this.grupoSeleccionado = grupoSeleccionado;
	}

	public class GrupoEmergencia {
		private String codigo;
		private String descripcion;
		
		public GrupoEmergencia() {
			super();
		}
		public GrupoEmergencia(String codigo, String descripcion) {
			super();
			this.codigo = codigo;
			this.descripcion = descripcion;
		}
		public String getCodigo() {
			return codigo;
		}
		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		
	}
}
