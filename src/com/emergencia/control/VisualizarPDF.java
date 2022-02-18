package com.emergencia.control;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;

@SuppressWarnings({ "serial", "rawtypes" })

public class VisualizarPDF extends GenericForwardComposer{

		Iframe reporte;
		byte[] arr;
		String nombreArchivo;
		
		@SuppressWarnings("unchecked")
		public void doAfterCompose(Component comp) throws Exception {
			super.doAfterCompose(comp);
			
			arr = (byte[]) Executions.getCurrent().getArg().get("Archivo");
			nombreArchivo = (String) Executions.getCurrent().getArg().get("NombreArchivo");
			final AMedia amedia = new AMedia(nombreArchivo, "pdf","application/pdf", arr);
	    	reporte.setContent(amedia);
		}

		public void onClick$btn(Event e) throws InterruptedException{
			Messagebox.show("Hi btn");
		}
}
