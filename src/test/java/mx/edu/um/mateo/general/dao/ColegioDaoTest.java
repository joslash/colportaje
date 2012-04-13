/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Colegio;
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
        log.debug("Debiera mostrar lista de union");

        for (int i = 0; i < 20; i++) {
            Colegio union = new Colegio(Constantes.NOMBRE+i, Constantes.STATUS_ACTIVO);
            currentSession().save(union);
            assertNotNull(union);
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
        log.debug("Debiera obtener union");

        String nombre = "test";
        Colegio union = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union.getId());
        Long id = union.getId();

        Colegio result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, union);
    }
     @Test
    public void deberiaCrearColegio() {
        log.debug("Deberia crear Colegio");

        Colegio union = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        assertNotNull(union);

        Colegio union2 = instance.crea(union);
        assertNotNull(union2);
        assertNotNull(union2.getId());

        assertEquals(union, union2);
    }

    @Test
    public void deberiaActualizarColegio() {
        log.debug("Deberia actualizar Colegio");

        Colegio union = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        assertNotNull(union);
        currentSession().save(union);

        String nombre = "test1";
        union.setNombre(nombre);

        Colegio union2 = instance.actualiza(union);
        assertNotNull(union2);
        assertEquals(nombre, union.getNombre());

        assertEquals(union, union2);
    }

    @Test
    public void deberiaEliminarColegio() throws UltimoException {
        log.debug("Debiera eliminar Colegio");

        String nom = "test";
        Colegio union = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        assertNotNull(union);

        String nombre = instance.elimina(union.getId());
        assertEquals(nom, nombre);

        Colegio prueba = instance.obtiene(union.getId());
        assertNull(prueba);
    }
    
}