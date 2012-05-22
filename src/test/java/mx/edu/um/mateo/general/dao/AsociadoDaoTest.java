/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.*;
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
 * @author gibrandemetrioo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class AsociadoDaoTest {

    private static final Logger log = LoggerFactory.getLogger(AsociadoDao.class);
    @Autowired
    private AsociadoDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class AsociadoDao.
     */
    @Test
    public void debieraMostrarListaDeAsociado() {
        log.debug("Debiera mostrar lista de Asociado");
            Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        for (int i = 0; i < 10; i++) {
           Asociado asociado = new Asociado(Constantes.CLAVE+i,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
            currentSession().save(asociado);
            assertNotNull(asociado);
            
              
            Usuario usuario = new Usuario("test" + i + "@test.com", "test", "test", "test", "test");
            usuario.setAsociacion(asociacion);
            usuario.setRoles(roles);
            usuario.setAsociado(asociado);
            currentSession().save(usuario);
        
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_ASOCIADOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        
        assertEquals(10, ((List<Asociado>) result.get(Constantes.CONTAINSKEY_ASOCIADOS)).size());
        assertEquals(10, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerAsociado() {
        log.debug("Debiera obtener asociado");

        String nombre = Constantes.MUNICIPIO;
        Asociado asociado = new Asociado(Constantes.CLAVE,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        Long id = asociado.getId();

        Asociado result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getColonia());
        assertEquals(result, asociado);
    }

    @Test
    public void deberiaCrearAsociado() {
        log.debug("Deberia crear asociado");

        Asociado asociado = new Asociado(Constantes.CLAVE,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
        assertNotNull(asociado);

        Asociado asociado2 = instance.crea(asociado);
        assertNotNull(asociado2);
        assertNotNull(asociado2.getId());

        assertEquals(asociado, asociado2);
    }

    @Test
    public void deberiaActualizarAsociado() {
        log.debug("Deberia actualizar asociado");

        Asociado asociado = new Asociado(Constantes.CLAVE,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
        assertNotNull(asociado);
        currentSession().save(asociado);

        String nombre = "test1";
        asociado.setColonia(nombre);

        Asociado asociado2 = instance.actualiza(asociado);
        assertNotNull(asociado2);
        assertEquals(nombre, asociado.getColonia());

        assertEquals(asociado, asociado2);
    }

    @Test
    public void deberiaEliminarAsociado() throws UltimoException {
        log.debug("Debiera eliminar Asociado");

        String nom = Constantes.CLAVE;
        Asociado asociado = new Asociado(Constantes.CLAVE,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
        currentSession().save(asociado);
        assertNotNull(asociado);

        String clave = instance.elimina(asociado.getId());
        assertEquals(nom, clave);

        Asociado prueba = instance.obtiene(asociado.getId());
        assertNull(prueba);
    }
}
