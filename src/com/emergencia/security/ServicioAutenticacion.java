package com.emergencia.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.emergencia.model.dao.UsuarioDAO;
import com.emergencia.model.entity.Perfil;
import com.emergencia.model.entity.Permiso;
import com.emergencia.model.entity.Usuario;

public class ServicioAutenticacion implements UserDetailsService {

	/**
	 * Este metodo es invocado en el momento de la autenticacion paraa recuperar 
	 * los datos del usuario.
	 * 
	 */
	@Override
	public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
		try {
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			Usuario usuario; 
			User usuarioSpring;
			List<GrantedAuthority> privilegios; 
			usuario = usuarioDAO.getUsuario(nombreUsuario);
			privilegios = obtienePrivilegios(usuario.getPerfil());
			
			usuarioSpring = new User(usuario.getUsuario(), usuario.getClave(), privilegios);

			return usuarioSpring;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
		
	}

	/**
	 * Elabora una lista de objetos del tipo GrantedAuthority en base a los permisos
	 * del usuario.
	 * 
	 * @param usuario
	 * @return
	 */
	private List<GrantedAuthority> obtienePrivilegios(Perfil tipoUsuario) {
		List<GrantedAuthority> listaPrivilegios = new ArrayList<GrantedAuthority>(); 
		
	    for(Permiso permiso  : tipoUsuario.getPermisos())
	    	listaPrivilegios.add(new SimpleGrantedAuthority(permiso.getPerfil().getNombre()));

		return listaPrivilegios;
	}
}