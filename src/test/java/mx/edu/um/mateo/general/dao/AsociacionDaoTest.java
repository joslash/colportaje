/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Asociacion;
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
 * @author gibrandemetrioo
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class AsociacionDaoTest {

    private static final Logger log = LoggerFactory.getLogger(AsociacionDaoTest.class);
    @Autowired
    private AsociacionDao instance;
    @Autowired
    private SessionFactory sessionFactory;
    
   private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    /**
     * Test of lista method, of class AsociacionDao.
     */
    @Test
    public void debieraMostrarListaDeAsociacion() {
        log.debug("Debiera mostrar lista de Asociaciones");
        for (int i = 0; i < 20; i++) {
           Asociacion asociacion = new Asociacion("test"+i, Constantes.STATUS_ACTIVO);
            currentSession().save(asociacion);
            assertNotNull(asociacion);
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_ASOCIACIONES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Asociacion>) result.get(Constantes.CONTAINSKEY_ASOCIACIONES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }
    
    @Test
    public void debieraObtenerAsociacion() {
        log.debug("Debiera obtener asociacion");

        String nombre = "test";
        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO);
        currentSession().save(asociacion);
        assertNotNull(asociacion.getId());
        Long id = asociacion.getId();

        Asociacion result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, asociacion);
    }

    @Test
    public void deberiaCrearAsociacion() {
        log.debug("Deberia crear asociacion");

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO);
        assertNotNull(asociacion);

        Asociacion asociacion2 = instance.crea(asociacion);
        assertNotNull(asociacion2);
        assertNotNull(asociacion2.getId());

        assertEquals(asociacion, asociacion2);
    }

    @Test
    public void deberiaActualizarAsociacion() {
        log.debug("Deberia actualizar asociacion");

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO);
        assertNotNull(asociacion);
        currentSession().save(asociacion);

        String nombre = "test1";
        asociacion.setNombre(nombre);

        Asociacion asociacion2 = instance.actualiza(asociacion);
        assertNotNull(asociacion2);
        assertEquals(nombre, asociacion.getNombre());

        assertEquals(asociacion, asociacion2);
    }

    @Test
    public void deberiaEliminarAsociacion() throws UltimoException {
        log.debug("Debiera eliminar Asociacion");

        String nom = "test";
        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO);
        currentSession().save(asociacion);
        assertNotNull(asociacion);

        String nombre = instance.elimina(asociacion.getId());
        assertEquals(nom, nombre);

        Asociacion prueba = instance.obtiene(asociacion.getId());
        assertNull(prueba);
    }
}

