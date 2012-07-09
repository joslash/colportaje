/*
 * No Paso Prueba,problemas con type long: asociado
 * 
 */
package mx.edu.um.mateo.general.dao;

import java.util.*;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.Asociacion;
import mx.edu.um.mateo.general.model.Asociado;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Union;
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
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
//        (String username, String password,   String nombre, String apellidoP,
//            String apellidoM, String status, String clave, String telefono, String calle, 
//            String colonia, String municipio)
        for (int i = 0; i < 10; i++) {
           Asociado asociado = new Asociado("test"+i+"@test.com", "test", "test", "test", "test", 
                   Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,
                   Constantes.MUNICIPIO);
            asociado.setAsociacion(asociacion);
            currentSession().save(asociado);
            assertNotNull(asociado.getId());
            
              
//            asociado.setRoles(roles);
            //usuario.setAsociado(asociado);
//            currentSession().save(asociado);
        
        }
        Map<String, Object> params = new HashMap();
        params.put(Constantes.ADDATTRIBUTE_ASOCIACION, asociacion);
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_ASOCIADOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        
        assertEquals(10, ((List<Asociado>) result.get(Constantes.CONTAINSKEY_ASOCIADOS)).size());
        assertEquals(10, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerAsociado() {
        log.debug("Debiera obtener asociado");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        String nombre = Constantes.MUNICIPIO;
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test", 
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO,Constantes.CALLE,
                Constantes.COLONIA,Constantes.MUNICIPIO);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        Long id = asociado.getId();
        assertNotNull(id);

        Asociado result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getMunicipio());
        assertEquals(result, asociado);
    }

    @Test
    public void deberiaCrearAsociado() {
        log.debug("Deberia crear asociado");

        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test", 
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO,Constantes.CALLE,
                Constantes.COLONIA,Constantes.MUNICIPIO);
       asociado.setAsociacion(asociacion);
       currentSession().save(asociado);
       assertNotNull(asociado.getId());
      
        Asociado asociado2 = instance.obtiene(asociado.getId());
        assertNotNull(asociado2);
        assertNotNull(asociado2.getId());
    }

    @Test
    public void deberiaActualizarAsociado() {
        log.debug("Deberia actualizar asociado");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test", 
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO,Constantes.CALLE,
                Constantes.COLONIA,Constantes.MUNICIPIO);
       asociado.setAsociacion(asociacion);
       currentSession().save(asociado);
       assertNotNull(asociado.getId());
       currentSession().save(asociado);

        String nombre = "colonia";
        asociado.setColonia(nombre);

        Asociado asociado2 = instance.actualiza(asociado);
        assertNotNull(asociado2.getId());
        assertEquals(nombre, asociado.getColonia());

        assertEquals(asociado, asociado2);
    }

    @Test
    public void deberiaEliminarAsociado() throws UltimoException {
        log.debug("Debiera eliminar Asociado");

        String nom = Constantes.CLAVE;
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test", 
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO,Constantes.CALLE,
                Constantes.COLONIA,Constantes.MUNICIPIO);
       asociado.setAsociacion(asociacion);
       currentSession().save(asociado);
       assertNotNull(asociado.getId());
     
        String clave = instance.elimina(asociado.getId());
        assertEquals(nom, clave);

        Asociado prueba = instance.obtiene(asociado.getId());
        assertEquals(prueba.getStatus(), Constantes.STATUS_INACTIVO);
    }
}
