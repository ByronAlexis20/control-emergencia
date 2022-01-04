package com.emergencia.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.emergencia.model.dao.MenuDAO;
import com.emergencia.model.dao.PermisoDAO;
import com.emergencia.model.dao.UsuarioDAO;
import com.emergencia.model.entity.Menu;
import com.emergencia.model.entity.Permiso;
import com.emergencia.model.entity.Usuario;
import com.emergencia.security.SecurityUtil;

@SuppressWarnings("unchecked")
public class MenuPrincipalControl {
	private Menu opcionSeleccionado;
	private List<Menu> listaOpcion;
	private MenuDAO menuDAO = new MenuDAO();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	List<Menu> listaPermisosPadre = new ArrayList<Menu>();
	List<Permiso> listaPermisosHijo = new ArrayList<Permiso>();

	private PermisoDAO permisoDAO = new PermisoDAO();
	@Wire Tree menu;

	@Wire Include areaContenido;

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		loadTree();
		areaContenido.setSrc("/forms/dashboard/dashboard.zul");
	}

	public void loadTree() throws IOException{
		Usuario usuario = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername().trim()); 
		if (usuario != null){
			//listaPermisosPadre = permisoDAO.getListaPermisosPadre(usuario.getSegPerfil().getIdPerfil());
			listaPermisosHijo = permisoDAO.getListaPermisosHijo(usuario.getPerfil().getIdPerfil());
			
			//obtener la lista de menus padre de cada menu
			Menu mn = new Menu();
			mn.setDescripcion("Dashboard");
			mn.setEstado("A");
			mn.setIdMenu(0);
			mn.setUrl("/forms/dashboard/dashboard.zul");
			mn.setIdMenuPadre(0);
			mn.setIcono("/imagenes/ic_dashboard.png");
			
			listaPermisosPadre.add(mn);
			boolean bandera = false;
			for(Permiso per : listaPermisosHijo) {
				bandera = false;
				List<Menu> listaMenu = menuDAO.getMenuPadre(per.getMenu().getIdMenuPadre());
				if(listaMenu.size() > 0) {
					for(Menu mnu : listaPermisosPadre) {
						for(Menu mnu2 : listaMenu) {
							if(mnu.getIdMenu() == mnu2.getIdMenu())
								bandera = true;
						}
					}
					if(bandera == false)
						listaPermisosPadre.add(listaMenu.get(0));
				}
			}
			
			if (listaPermisosPadre.size() > 0) { //si tiene permisos el usuario
				//capturar solo los menus
				List<Menu> listaMenu = new ArrayList<Menu>();
				for(Menu permiso : listaPermisosPadre) {
					listaMenu.add(permiso);
				}
				Collections.sort(listaMenu, new Comparator<Menu>() {
					@SuppressWarnings("deprecation")
					@Override
					public int compare(Menu p1, Menu p2) {
						return new Integer(p1.getPosicion()).compareTo(new Integer(p2.getPosicion()));
					}
				});
				menu.appendChild(getTreechildren(listaMenu));   
			}
		}
	}

	private Treechildren getTreechildren(List<Menu> listaMenu) {
		Treechildren retorno = new Treechildren();
		for(Menu opcion : listaMenu) {
			Treeitem ti = getTreeitem(opcion);
			ti.setStyle("color: #FFFFFF;");
			retorno.appendChild(ti);
			List<Menu> listaPadreHijo = new ArrayList<Menu>();
			for(Permiso permiso : listaPermisosHijo) {
				if(permiso.getMenu().getIdMenuPadre() == opcion.getIdMenu()) {
					listaPadreHijo.add(permiso.getMenu());
				}
			}
			if (!listaPadreHijo.isEmpty()) {
				Collections.sort(listaPadreHijo, new Comparator<Menu>() {
					@SuppressWarnings("deprecation")
					@Override
					public int compare(Menu p1, Menu p2) {
						return new Integer(p1.getPosicion()).compareTo(new Integer(p2.getPosicion()));
					}
				});
				ti.appendChild(getTreechildren(listaPadreHijo));
			}
		}
		return retorno;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	private Treeitem getTreeitem(Menu opcion) {
		Treeitem retorno = new Treeitem();
		Treerow tr = new Treerow();
		Treecell tc = new Treecell(opcion.getDescripcion());		
		if (opcion.getIcono() != null) {
			tc.setSrc(opcion.getIcono());
		}
		tr.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				opcionSeleccionado = opcion; 
				if(opcionSeleccionado.getUrl() != null){
					loadContenido(opcionSeleccionado);
				}
			}
		});
		tr.appendChild(tc);
		retorno.appendChild(tr);
		retorno.setOpen(false);
		return retorno;
	}

	@NotifyChange({"areaContenido"})
	public void loadContenido(Menu opcion) {
		if (opcion.getUrl().toLowerCase().substring(0, 2).toLowerCase().equals("http")) {
			Sessions.getCurrent().setAttribute("FormularioActual", null);
			Executions.getCurrent().sendRedirect(opcion.getUrl(), "_blank");			
		} else {
			Sessions.getCurrent().setAttribute("FormularioActual", opcion);	
			areaContenido.setSrc(opcion.getUrl());
		}	
	}
	@Command
	public void dashboard() {
		areaContenido.setSrc("/forms/dashboard/dashboard.zul");
	}
	public String getNombreUsuario() {
		Usuario usuario = this.usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername());
		return usuario.getPersona().getApellidos() + " " + usuario.getPersona().getNombres();
	}
	public String getPerfilUsuario() {
		Usuario usuario = this.usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername());
		return usuario.getPerfil().getNombre();
	}
	public List<Menu> getListaOpcion() {
		return listaOpcion;
	}
	public void setListaOpcion(List<Menu> listaOpcion) {
		this.listaOpcion = listaOpcion;
	}
	public Tree getMenu() {
		return menu;
	}
	public void setMenu(Tree menu) {
		this.menu = menu;
	}
	public Include getAreaContenido() {
		return areaContenido;
	}
	public void setAreaContenido(Include areaContenido) {
		this.areaContenido = areaContenido;
	}
	public Menu getOpcionSeleccionado() {
		return opcionSeleccionado;
	}
	public void setOpcionSeleccionado(Menu opcionSeleccionado) {
		this.opcionSeleccionado = opcionSeleccionado;
	}
}