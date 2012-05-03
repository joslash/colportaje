/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.Almacen;
import mx.edu.um.mateo.general.model.Asociacion;
import mx.edu.um.mateo.general.model.Union;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.utils.UltimoException;
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
public class AlmacenDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(AlmacenDaoTest.class);
    @Autowired
    private AlmacenDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class AlmacenDao.
     */
    @Test
    public void deberiaMostrarListaDeAlmacen() {
        log.debug("Debiera mostrar lista de almacen");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);

        for (int i = 0; i < 20; i++) {
            Almacen almacen = new Almacen(Constantes.CLAVE + i, Constantes.NOMBRE);
            almacen.setAsociacion(test2);
            currentSession().save(almacen);
            assertNotNull(almacen);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_ALMACENES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Almacen>) result.get(Constantes.CONTAINSKEY_ALMACENES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerAlmacen() {
        log.debug("Debiera obtener almacen");
          Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);

        String nombre = "test";
        Almacen almacen = new Almacen(Constantes.CLAVE, Constantes.NOMBRE);
        almacen.setAsociacion(test2);
        currentSession().save(almacen);
        assertNotNull(almacen.getId());
        Long id = almacen.getId();

        Almacen result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, almacen);
    }

    @Test
    public void deberiaCrearAlmacen() {
        log.debug("Deberia crear Almacen");
     Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);

        Almacen almacen = new Almacen(Constantes.CLAVE, Constantes.NOMBRE);
        almacen.setAsociacion(test2);
        assertNotNull(almacen);

        Almacen almacen2 = instance.crea(almacen);
        assertNotNull(almacen2);
        assertNotNull(almacen2.getId());

        assertEquals(almacen, almacen2);
    }

    @Test
    public void deberiaActualizarAlmacen() {
        log.debug("Deberia actualizar Almacen");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);

        Almacen almacen = new Almacen(Constantes.CLAVE, Constantes.NOMBRE);
        almacen.setAsociacion(test2);
        assertNotNull(almacen);
        currentSession().save(almacen);

        String nombre = "test1";
        almacen.setNombre(nombre);

        Almacen almacen2 = instance.actualiza(almacen);
        assertNotNull(almacen2);
        assertEquals(nombre, almacen.getNombre());

        assertEquals(almacen, almacen2);
    }

    @Test
    public void deberiaEliminarAlmacen() throws UltimoException {
        log.debug("Debiera eliminar Almacen");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);

        String cla = Constantes.CLAVE;
        Almacen almacen = new Almacen(Constantes.CLAVE, Constantes.NOMBRE);
        almacen.setAsociacion(test2);
        currentSession().save(almacen);
        assertNotNull(almacen);

        String clave = instance.elimina(almacen.getId());
        assertEquals(cla, clave);

        Almacen prueba = instance.obtiene(almacen.getId());
        assertNull(prueba);
    }
}