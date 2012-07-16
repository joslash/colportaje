/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.Asociacion;
import mx.edu.um.mateo.general.model.Cliente;
import mx.edu.um.mateo.general.model.Union;
import mx.edu.um.mateo.general.test.BaseTest;
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
 * @author lobo4
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class ClienteDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ClienteDaoTest.class);
    @Autowired
    private ClienteDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class ClienteDao.
     */
    @Test
    public void deberiaMostrarListaDeCliente() {
        log.debug("Debiera mostrar lista de union");

        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("test-01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        for (int i = 0; i < 20; i++) {
            Cliente cliente = new Cliente("test", "test", "010101010101", asociacion);
            currentSession().save(cliente);
            assertNotNull(cliente);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_CLIENTES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Cliente>) result.get(Constantes.CONTAINSKEY_CLIENTES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerCliente() {
        log.debug("Debiera obtener union");
        
        String nombre = "test";
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("test-01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        Cliente cliente = new Cliente("test", "test", "010101010101", asociacion);
        currentSession().save(cliente);
        assertNotNull(cliente);
        currentSession().save(cliente);
        assertNotNull(cliente.getId());
        Long id = cliente.getId();

        Cliente result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getNombre());

        assertEquals(result, cliente);
    }

    @Test
    public void deberiaCrearCliente() {
        log.debug("Deberia crear Cliente");

        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("test-01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        Cliente cliente = new Cliente("test", "test", "010101010101", asociacion);
        currentSession().save(cliente);
        assertNotNull(cliente);

        Cliente cliente2 = instance.crea(cliente);
        assertNotNull(cliente2);
        assertNotNull(cliente2.getId());

        assertEquals(cliente, cliente2);
    }

    @Test
    public void deberiaActualizarCliente() {
        log.debug("Deberia actualizar Cliente");

        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("test-01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        Cliente cliente = new Cliente("test", "test", "010101010101", asociacion);
        currentSession().save(cliente);
        assertNotNull(cliente);

        String nombre = "test1";
        cliente.setNombre(nombre);

        Cliente cliente2 = instance.actualiza(cliente);
        assertNotNull(cliente2);
        assertEquals(nombre, cliente.getNombre());

        assertEquals(cliente, cliente2);
    }

    @Test
    public void deberiaEliminarCliente() {
        log.debug("Debiera eliminar Cliente");

        String nom = "test";
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Asociacion asociacion = new Asociacion("test-01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        Cliente cliente = new Cliente("test", "test", "010101010101", asociacion);
        currentSession().save(cliente);
        assertNotNull(cliente);

        String nombre = instance.elimina(cliente.getId());
        assertEquals(nom, nombre);

        Cliente prueba = instance.obtiene(cliente.getId());
         if (prueba != null) {
            fail("Fallo prueba Eliminar");
        }
    }
}