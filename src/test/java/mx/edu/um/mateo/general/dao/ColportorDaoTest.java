/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Colportor;
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
 * 
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class ColportorDaoTest extends BaseTest{

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
    public void deberiaMostrarListaDeColportor() {
        log.debug("Debiera mostrar lista de colportor");

        for (int i = 0; i < 20; i++) {
            Colportor colportor = new Colportor(Constantes.NOMBRE+i, Constantes.STATUS_ACTIVO, Constantes.CLAVE+i,Constantes.DIRECCION,Constantes.CORREO,Constantes.TELEFONO);
            currentSession().save(colportor);
            assertNotNull(colportor);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_COLPORTORES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Colportor>) result.get(Constantes.CONTAINSKEY_COLPORTORES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerColportor() {
        log.debug("Debiera obtener colportor");

        String nombre = "test";
        Colportor colportor = new Colportor(Constantes.NOMBRE, Constantes.STATUS_ACTIVO, Constantes.CLAVE,Constantes.DIRECCION,Constantes.CORREO,Constantes.TELEFONO);
        currentSession().save(colportor);
        assertNotNull(colportor.getId());
        Long id = colportor.getId();

        Colportor result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, colportor);
    }

    @Test
    public void deberiaCrearColportor() {
        log.debug("Deberia crear Colportor");

        Colportor colportor = new Colportor(Constantes.NOMBRE, Constantes.STATUS_ACTIVO, Constantes.CLAVE,Constantes.DIRECCION,Constantes.CORREO,Constantes.TELEFONO);
        assertNotNull(colportor);

        Colportor colportor2 = instance.crea(colportor);
        assertNotNull(colportor2);
        assertNotNull(colportor2.getId());

        assertEquals(colportor, colportor2);
    }

    @Test
    public void deberiaActualizarColportor() {
        log.debug("Deberia actualizar Colportor");

        Colportor colportor = new Colportor(Constantes.NOMBRE, Constantes.STATUS_ACTIVO, Constantes.CLAVE,Constantes.DIRECCION,Constantes.CORREO,Constantes.TELEFONO);
        assertNotNull(colportor);
        currentSession().save(colportor);

        String nombre = "test1";
        colportor.setNombre(nombre);

        Colportor colportor2 = instance.actualiza(colportor);
        assertNotNull(colportor2);
        assertEquals(nombre, colportor.getNombre());

        assertEquals(colportor, colportor2);
    }

    @Test
    public void deberiaEliminarColportor() throws UltimoException {
        log.debug("Debiera eliminar Colportor");

        String nom = "test";
        Colportor colportor = new Colportor(Constantes.NOMBRE, Constantes.STATUS_ACTIVO, Constantes.CLAVE,Constantes.DIRECCION,Constantes.CORREO,Constantes.TELEFONO);
        currentSession().save(colportor);
        assertNotNull(colportor);

        String nombre = instance.elimina(colportor.getId());
        assertEquals(nom, nombre);

        Colportor prueba = instance.obtiene(colportor.getId());
        assertNull(prueba);
    }
}
   