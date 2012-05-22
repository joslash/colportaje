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
package mx.edu.um.mateo.general.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class ColportorDaoTest {

    private static final Logger log = LoggerFactory.getLogger(ColportorDaoTest.class);
    @Autowired
    private ColportorDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class ColportorDao.
     */
     @Test
    public void debieraMostrarListaDeColportor() {
        log.debug("Debiera mostrar lista de Colportor");
            Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol("ROLE_COL");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        for (int i = 0; i < 10; i++) {
           Colportor colportor = new Colportor("test"+i, Constantes.STATUS_ACTIVO, "8262652626", "test", "10706"+i);
            
            currentSession().save(colportor);
            assertNotNull(colportor);
            
              
            Usuario usuario = new Usuario("test" + i + "@test.com", "test", "test", "test", "test");
            usuario.setAsociacion(asociacion);
            usuario.setRoles(roles);
            usuario.setColportor(colportor);
            currentSession().save(usuario);
       
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_COLPORTORES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        
        assertEquals(10, ((List<Colportor>) result.get(Constantes.CONTAINSKEY_COLPORTORES)).size());
        assertEquals(10, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerColportor() {
        log.debug("Debiera obtener un Colportor");

        Colportor colportor = new Colportor("test", Constantes.STATUS_ACTIVO, "8262652626", "test", "1070666");
        currentSession().save(colportor);
        Long id = colportor.getId();
        assertNotNull(id);

        Colportor result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(result, colportor);
        assertEquals("test", result.getClave());
    }

    @Test
    public void deberiaCrearColportor() {
        log.debug("Deberia crear un Colportor");

        Colportor colportor = new Colportor("test", Constantes.STATUS_ACTIVO, "8262652626", "test", "1070666");
        assertNotNull(colportor);
        Colportor colportor2 = instance.crea(colportor);
        assertNotNull(colportor2);
        assertNotNull(colportor2.getId());

        assertEquals(colportor, colportor2);
    }

    @Test
    public void deberiaActualizarColportor() {
        log.debug("Deberia actualizar Colportor");

        Colportor colportor = new Colportor("test", Constantes.STATUS_ACTIVO, "8262652626", "test", "1070666");
        assertNotNull(colportor);
        currentSession().save(colportor);
        
        String colonia = Constantes.COLONIA;
        colportor.setColonia(colonia);

        Colportor colportor2 = instance.actualiza(colportor);
        assertNotNull(colportor2);
        assertEquals(colonia, colportor.getColonia());

        assertEquals(colportor, colportor2);
    }

    @Test
    public void deberiaEliminarColportor() {
        log.debug("Debiera eliminar Colportor");
        String nom = Constantes.STATUS_ACTIVO;
        Colportor colportor = new Colportor("test", Constantes.STATUS_ACTIVO, "8262652626", "test", "1070666");
        currentSession().save(colportor);
        assertNotNull(colportor);

        String clave = instance.elimina(colportor.getId());
        assertEquals(colportor.getClave(), clave);

        colportor = instance.obtiene(colportor.getId());
        assertEquals(Constantes.STATUS_INACTIVO,colportor.getStatus());
    }
}
