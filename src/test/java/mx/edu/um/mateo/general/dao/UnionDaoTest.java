/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.dao.UnionDao;
import mx.edu.um.mateo.general.model.Union;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.um.edu.mateo.Constantes;
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
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class UnionDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(UnionDaoTest.class);
    @Autowired
    private UnionDao instance;
    @Autowired
    private SessionFactory sessionFactory;
    
   private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    
    /**
     * Test of lista method, of class UnionDao.
     */
    @Test
    public void deberiaMostrarListaDeUnion() {
        log.debug("Debiera mostrar lista de union");

        for (int i = 0; i < 20; i++) {
            Union union = new Union(Constantes.NOMBRE+i, Constantes.STATUS_ACTIVO);
            currentSession().save(union);
            assertNotNull(union);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_UNIONES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Union>) result.get(Constantes.CONTAINSKEY_UNIONES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }
     @Test
    public void debieraObtenerUnion() {
        log.debug("Debiera obtener union");

        String nombre = "test";
        Union union = new Union(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        Long id = union.getId();

        Union result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, union);
    }
     @Test
    public void deberiaCrearUnion() {
        log.debug("Deberia crear Union");

        Union union = new Union(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        assertNotNull(union);

        Union union2 = instance.crea(union);
        assertNotNull(union2);
        assertNotNull(union2.getId());

        assertEquals(union, union2);
    }

    @Test
    public void deberiaActualizarUnion() {
        log.debug("Deberia actualizar Union");

        Union union = new Union(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        assertNotNull(union);
        currentSession().save(union);

        String nombre = "test1";
        union.setNombre(nombre);

        Union union2 = instance.actualiza(union);
        assertNotNull(union2);
        assertEquals(nombre, union.getNombre());

        assertEquals(union, union2);
    }

    @Test
    public void deberiaEliminarUnion() throws UltimoException {
        log.debug("Debiera eliminar Union");

        String nom = "test";
        Union union = new Union(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union);

        String nombre = instance.elimina(union.getId());
        assertEquals(nom, nombre);

        Union prueba = instance.obtiene(union.getId());
        assertNull(prueba);
    }
    
}