/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
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
package mx.edu.um.mateo.general.web;

import mx.edu.um.mateo.general.dao.ReporteDao;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.general.dao.UnionDao;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.Asociacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Union;
import mx.edu.um.mateo.general.model.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author jdmr
 */
@Controller
@RequestMapping("/inicializa")
public class InicializaController {

    private static final Logger log = LoggerFactory.getLogger(InicializaController.class);
    @Autowired
    private UnionDao unionDao;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private ReporteDao reporteDao;

    @RequestMapping
    public String inicia() {
        return "/inicializa/index";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public String guarda(
            @RequestParam String username,
            @RequestParam String password) {

        Transaction transaction = null;
        try {
            transaction = currentSession().beginTransaction();
            reporteDao.inicializa();
            Union union = new Union("Únion Mexicana del Norte");
            union = unionDao.crea(union);
            Rol rol = new Rol("ROLE_ADMIN");
            rol = rolDao.crea(rol);
            Usuario usuario = new Usuario(
                    username,
                    password,
                    "Nombre",
                    "ApellidoP",
                    "ApellidoM");
            Long asosiacionId = 0l;
            actualizaUsuario:
            for (Asociacion asociacion : union.getAsociaciones()) {
                asosiacionId = asociacion.getId();
                break actualizaUsuario;
            }
            usuarioDao.crea(usuario, asosiacionId, new String[]{rol.getAuthority()});
            rol = new Rol("ROLE_ASO");
            rolDao.crea(rol);
            rol = new Rol("ROLE_COL");
            rolDao.crea(rol);
            rol = new Rol("ROLE_USER");
            rolDao.crea(rol);

            transaction.commit();
        } catch (Exception e) {
            log.error("No se pudo inicializar la aplicacion", e);
            transaction.rollback();
        }

        return "redirect:/";
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}
