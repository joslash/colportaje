/*
 * The MIT License
 *
 * Copyright 2012 J. David Mendoza <jdmendoza@um.edu.mx>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.general.utils;

import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import mx.edu.um.mateo.general.dao.TemporadaColportorDao;
import mx.edu.um.mateo.general.model.TemporadaColportor;
import mx.edu.um.mateo.general.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 * Constantes de Sesion:
 * Union
 * Asociacion
 * Asociado
 * Colportor
 * Constantes de objetos temporales en Sesion:
 * ColportorTmp
 */
@Component
public class Ambiente {

    private static final Logger log = LoggerFactory.getLogger(Ambiente.class);
     @Autowired
    private TemporadaColportorDao tempColportorDao;

    public void actualizaSesion(HttpServletRequest request) {
        Usuario usuario = obtieneUsuario();
        this.actualizaSesion(request, usuario);
    }

    public void actualizaSesion(HttpServletRequest request, Usuario usuario) {
        log.debug("Actualizando sesion");
        if (usuario != null) {
            request.getSession().setAttribute("unionLabel", usuario.getAsociacion().getUnion().getNombre());
            request.getSession().setAttribute("asociacionLabel", usuario.getAsociacion().getNombre());
            request.getSession().setAttribute("unionId", usuario.getAsociacion().getUnion());
            request.getSession().setAttribute("asociacionId", usuario.getAsociacion());
        }
        
    }

    public Usuario obtieneUsuario() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuario;
    }
    
    public boolean esAsociadoEnSesion(){
        boolean esAsociado = false;
        GrantedAuthority ga=null;
        Usuario usuario = obtieneUsuario();
        Iterator  it= usuario.getAuthorities().iterator();
        while(it.hasNext()){
            ga=(GrantedAuthority)it.next();
            
            if((ga).getAuthority().equals("ROLE_ASO")){
            esAsociado = true;
                break;
            }
        
        }
        return esAsociado;
    }
    
    public boolean esAsociado(Usuario usuario){
        boolean esAsociado = false;
        if(usuario.getAsociado() != null){
            esAsociado = true;
        }
        return esAsociado;
    }
    
    public boolean esColportorEnSesion(){
        log.debug("COLPORTOR EN SECION");
        boolean esColportor = false;
        GrantedAuthority ga=null;
        Usuario usuario = obtieneUsuario();
        Iterator  it= usuario.getAuthorities().iterator();
        while(it.hasNext()){
            ga=(GrantedAuthority)it.next();
            
            if((ga).getAuthority().equals("ROLE_COL")){
            esColportor = true;
                break;
            }
        
        }
        
        return esColportor;
    }
    
    public boolean esColportor(Usuario usuario){
        boolean esColportor = false;
        if(usuario.getColportor() != null){
            esColportor = true;
        }
        return esColportor;
    }
    
 
}
