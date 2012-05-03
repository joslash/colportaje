/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.Colegio;
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
public class ColegioDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ColegioDaoTest.class);
    @Autowired
    private ColegioDao instance;
    @Autowired
    private SessionFactory sessionFactory;
    
   private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    
    /**
     * Test of lista method, of class ColegioDao.
     */
    @Test
    public void deberiaMostrarListaDeColegio() {
        log.debug("Debiera mostrar lista de colegio");

        for (int i = 0; i < 20; i++) {
            Colegio colegio = new Colegio(Constantes.NOMBRE+i, Constantes.STATUS_ACTIVO);
            currentSession().save(colegio);
            assertNotNull(colegio);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_COLEGIOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Colegio>) result.get(Constantes.CONTAINSKEY_COLEGIOS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }
     @Test
    public void debieraObtenerColegio() {
        log.debug("Debiera obtener colegio");

        String nombre = "test";
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        assertNotNull(colegio.getId());
        Long id = colegio.getId();

        Colegio result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, colegio);
    }
     @Test
    public void deberiaCrearColegio() {
        log.debug("Deberia crear Colegio");

        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        assertNotNull(colegio);

        Colegio colegio2 = instance.crea(colegio);
        assertNotNull(colegio2);
        assertNotNull(colegio2.getId());

        assertEquals(colegio, colegio2);
    }

    @Test
    public void deberiaActualizarColegio() {
        log.debug("Deberia actualizar Colegio");

        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        assertNotNull(colegio);
        currentSession().save(colegio);

        String nombre = "test1";
        colegio.setNombre(nombre);

        Colegio colegio2 = instance.actualiza(colegio);
        assertNotNull(colegio2);
        assertEquals(nombre, colegio.getNombre());

        assertEquals(colegio, colegio2);
    }

    @Test
    public void deberiaEliminarColegio() throws UltimoException {
        log.debug("Debiera eliminar Colegio");

        String nom = "test";
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        assertNotNull(colegio);

        String nombre = instance.elimina(colegio.getId());
        assertEquals(nom, nombre);

        Colegio prueba = instance.obtiene(colegio.getId());
        assertNull(prueba);
    }
    
}