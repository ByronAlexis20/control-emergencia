package com.emergencia.control;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.emergencia.model.dao.ControlVehiculoDAO;
import com.emergencia.model.dao.EmergenciaDAO;
import com.emergencia.model.dao.PrehospitalariaDAO;
import com.emergencia.model.dao.UsuarioDAO;
import com.emergencia.model.dao.VehiculoDAO;
import com.emergencia.model.entity.ControlVehiculo;
import com.emergencia.model.entity.Emergencia;
import com.emergencia.model.entity.Prehospitalaria;
import com.emergencia.model.entity.Usuario;
import com.emergencia.model.entity.Vehiculo;

public class Dashboard {
	@Wire Datebox dtpFecha;
	@Wire Label lblTotalEmergencias;
	@Wire Label lblEmergenciaLaborSocial;
	@Wire Label lblEmergenciaPrehospitalaria;
	@Wire Label lblEmergenciaControlIncendio;
	@Wire Image imGraficoResumenEmergencia;
	@Wire Image imGraficoTiempoEmergencia;
	@Wire Listbox lstEmergenciaVehiculo;
	@Wire Listbox lstEmergenciaBomberos;
	List<Control> listaControlVehiculo;
	List<BomberoEmergencia> listaBomberosEmergencia;
	
	EmergenciaDAO emergenciaDAO = new EmergenciaDAO();
	PrehospitalariaDAO prehospitalariaDAO = new PrehospitalariaDAO();
	ControlVehiculoDAO controlDAO = new ControlVehiculoDAO();
	VehiculoDAO vehiculoDAO = new VehiculoDAO();
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		Date fecha = new Date();
		dtpFecha.setValue(fecha);
		cargarDatosIniciales(fecha);
	}
	@Command
	public void actualizar(){
		if(dtpFecha.getValue() == null) {
			Clients.showNotification("Seleccione una fecha");
			return;
		}
		cargarDatosIniciales(dtpFecha.getValue());
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void cargarDatosIniciales(Date fecha){
		try {
			LocalDate localDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int anio  = localDate.getYear();
			int mes = localDate.getMonthValue();
			int dia   = localDate.getDayOfMonth();
			//calcular el total de emergencias por fecha*****************************************************************
			List<Emergencia> listaEmergencia = emergenciaDAO.buscarPorFecha(dia, mes, anio);
			List<Prehospitalaria> listaPrehospitalario = prehospitalariaDAO.buscarPorFechaAtencion(fecha);
			Integer totalEmergencia = listaEmergencia.size() + listaPrehospitalario.size();
			lblTotalEmergencias.setValue(String.valueOf(totalEmergencia));
			//calcular por labor social***********************************************************************************
			List<Emergencia> listaLaborSocial = emergenciaDAO.buscarPorFechaYTipo(dia, mes, anio, "LS");
			lblEmergenciaLaborSocial.setValue(String.valueOf(listaLaborSocial.size()));
			//calcular prehospitalaria************************************************************************************
			lblEmergenciaPrehospitalaria.setValue(String.valueOf(listaPrehospitalario.size()));
			//calcular control de incencio********************************************************************************
			List<Emergencia> listaControlIncendio = emergenciaDAO.buscarPorFechaYTipo(dia, mes, anio, "CI");
			lblEmergenciaControlIncendio.setValue(String.valueOf(listaControlIncendio.size()));
			//realizar la grafica*****************************************************************************************
			DefaultPieDataset pieDataset = new DefaultPieDataset();
			double tLaborSocial  = 100.0;
			double tPrehospitalario = 0.0;
			double tControlIncendio = 0.0;
			if(totalEmergencia > 0) {
				tLaborSocial = (listaLaborSocial.size() * 100) / totalEmergencia;
				tPrehospitalario = (listaPrehospitalario.size() * 100) / totalEmergencia;
				tControlIncendio = (listaControlIncendio.size() * 100) / totalEmergencia;
			}
			pieDataset.setValue("Labor Social (" + listaLaborSocial.size() + ")", tLaborSocial);
			pieDataset.setValue("Prehospitalario (" + listaPrehospitalario.size() + ")", tPrehospitalario);
			pieDataset.setValue("Control Incendio (" + listaControlIncendio.size() + ")", tControlIncendio);
			pieDataset.setNotify(true);
			JFreeChart chart = ChartFactory.createPieChart3D("Emergencias", pieDataset,true,true,false);
			PiePlot3D plot = (PiePlot3D) chart.getPlot();
			plot.setForegroundAlpha(0.9f);
			BufferedImage bi = chart.createBufferedImage(500, 250, BufferedImage.SCALE_REPLICATE , null);
			byte[] bytes = EncoderUtil.encode(bi, ImageFormat.JPEG, true);
			AImage image = new AImage("Pie Chart", bytes);
			imGraficoResumenEmergencia.setContent(image);
			//listado de control de vehiculos ****************************************************************************
			Integer cont = 0;
			List<ControlVehiculo> listaControl = controlDAO.buscarPorFecha(fecha);
			List<Control> listaAgregarCotrolVehiculo = new ArrayList<>();
			List<Vehiculo> listaVehiculo = vehiculoDAO.getVehiculoPorDescripcion("");
			for(Vehiculo v : listaVehiculo) {
				cont = 0;
				Control c = new Control();
				c.setVehiculo(v);
				for(ControlVehiculo con : listaControl) {
					if(con.getVehiculo().getIdVehiculo() == v.getIdVehiculo())
						cont ++;
				}
				c.setCantidad(cont);
				listaAgregarCotrolVehiculo.add(c);
			}
			listaControlVehiculo = listaAgregarCotrolVehiculo;
			lstEmergenciaVehiculo.setModel(new ListModelList(listaControlVehiculo));
			//bomberos por emergencia ************************************************************************************
			List<Usuario> listaBomberos = usuarioDAO.buscarBomberoEmergencias();
			List<BomberoEmergencia> listaBomberosAgregar = new ArrayList<>();
			for(Usuario us : listaBomberos) {
				cont = 0;
				BomberoEmergencia bom = new BomberoEmergencia();
				bom.setBombero(us);
				for(Prehospitalaria p : listaPrehospitalario) {
					if(p.getInformante().getIdUsuario() == us.getIdUsuario())
						cont ++;
				}
				bom.setContAPH(cont);
				cont = 0;
				for(Emergencia e : listaLaborSocial) {
					if(e.getUsuario().getIdUsuario() == us.getIdUsuario()) 
						cont ++;
				}
				bom.setContLS(cont);
				cont = 0;
				for(Emergencia e : listaControlIncendio) {
					if(e.getUsuario().getIdUsuario() == us.getIdUsuario()) 
						cont ++;
				}
				bom.setContCI(cont);
				listaBomberosAgregar.add(bom);
			}
			listaBomberosEmergencia = listaBomberosAgregar;
			lstEmergenciaBomberos.setModel(new ListModelList(listaBomberosEmergencia));
			//linea de tiempo *************************************************************************************************
			generarGraficoLineal(fecha);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void generarGraficoLineal(Date fecha) {
		Date fechaActual = fecha;
		
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		c.add(Calendar.MONTH, -1);
		Date fechaMenosUno = c.getTime();
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(fecha);
		c1.add(Calendar.MONTH, -2);
		Date fechaMenosDos = c1.getTime();
		
	}
	public List<Control> getListaControlVehiculo() {
		return listaControlVehiculo;
	}
	public void setListaControlVehiculo(List<Control> listaControlVehiculo) {
		this.listaControlVehiculo = listaControlVehiculo;
	}
	public List<BomberoEmergencia> getListaBomberosEmergencia() {
		return listaBomberosEmergencia;
	}
	public void setListaBomberosEmergencia(List<BomberoEmergencia> listaBomberosEmergencia) {
		this.listaBomberosEmergencia = listaBomberosEmergencia;
	}

	public class Control {
		private Vehiculo vehiculo;
		private Integer cantidad;
		
		public Control() {
			super();
		}
		public Control(Vehiculo vehiculo, Integer cantidad) {
			super();
			this.vehiculo = vehiculo;
			this.cantidad = cantidad;
		}
		public Vehiculo getVehiculo() {
			return vehiculo;
		}
		public void setVehiculo(Vehiculo vehiculo) {
			this.vehiculo = vehiculo;
		}
		public Integer getCantidad() {
			return cantidad;
		}
		public void setCantidad(Integer cantidad) {
			this.cantidad = cantidad;
		}
	}
	public class BomberoEmergencia {
		private Usuario bombero;
		private Integer contAPH;
		private Integer contCI;
		private Integer contLS;
		public BomberoEmergencia() {
			super();
		}
		public BomberoEmergencia(Usuario bombero, Integer contAPH, Integer contCI, Integer contLS) {
			super();
			this.bombero = bombero;
			this.contAPH = contAPH;
			this.contCI = contCI;
			this.contLS = contLS;
		}
		public Usuario getBombero() {
			return bombero;
		}
		public void setBombero(Usuario bombero) {
			this.bombero = bombero;
		}
		public Integer getContAPH() {
			return contAPH;
		}
		public void setContAPH(Integer contAPH) {
			this.contAPH = contAPH;
		}
		public Integer getContCI() {
			return contCI;
		}
		public void setContCI(Integer contCI) {
			this.contCI = contCI;
		}
		public Integer getContLS() {
			return contLS;
		}
		public void setContLS(Integer contLS) {
			this.contLS = contLS;
		}
		
	}
}