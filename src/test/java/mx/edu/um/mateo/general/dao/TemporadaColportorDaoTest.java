/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.utils.UltimoException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;
/**
 *
 * @author gibrandemetrioo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class TemporadaColportorDaoTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(TemporadaColportorDao.class);
    @Autowired
    private TemporadaColportorDao instance;
    @Autowired
    private SessionFactory sessionFactory;
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
   @Test
    public void debieraMostrarListaDeTemporadaColportor() {
        log.debug("Debiera mostrar lista Temporada Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Colportor test = new Colportor(Constantes.TIPO_COLPORTOR,Constantes.MATRICULA, Constantes.STATUS_ACTIVO, Constantes.CLAVE,Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,Constantes.MUNICIPIO);
        currentSession().save(test);
        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);
        Asociado test3 = new Asociado(Constantes.CLAVE,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
        currentSession().save(test3);
        Temporada test4 = new Temporada ("test");
        currentSession().save(test4);
        Colegio colegio = new Colegio("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        for (int i = 0; i < 20; i++) {
           TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO+i,"TEST","TEST");
            temporadacolportor.setColportor(test);
            temporadacolportor.setAsociacion(test2);
            temporadacolportor.setAsociado(test3);
            temporadacolportor.setTemporada(test4);
            temporadacolportor.setUnion(union);
            temporadacolportor.setColegio(colegio);
            currentSession().save(temporadacolportor);
            assertNotNull(temporadacolportor);
        }
        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_TEMPORADACOLPORTORES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<TemporadaColportor>) result.get(Constantes.CONTAINSKEY_TEMPORADACOLPORTORES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }
   @Test
    public void debieraObtenerTemporadaColportor() {
        log.debug("Debiera obtener Temporada Colportor");
        String nombre = "test";
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Colportor test = new Colportor(Constantes.TIPO_COLPORTOR,Constantes.MATRICULA, Constantes.STATUS_ACTIVO, Constantes.CLAVE,Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,Constantes.MUNICIPIO);
        currentSession().save(test);
        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);
        Asociado test3 = new Asociado(Constantes.CLAVE,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
        currentSession().save(test3);
        Temporada test4 = new Temporada ("test5");
        currentSession().save(test4);
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO,"test","test");
        temporadacolportor.setColportor(test);
        temporadacolportor.setAsociacion(test2);
        temporadacolportor.setAsociado(test3);
        temporadacolportor.setTemporada(test4);
        temporadacolportor.setUnion(union);
        temporadacolportor.setColegio(colegio);
        currentSession().save(temporadacolportor);
        assertNotNull(temporadacolportor.getId());
        Long id = temporadacolportor.getId();

        TemporadaColportor result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getObservaciones());

        assertEquals(result, temporadacolportor);
    }
   @Test
    public void deberiaCrearTemporadaColportor() {
        log.debug("Deberia crear Temporada Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Colportor test = new Colportor(Constantes.TIPO_COLPORTOR,Constantes.MATRICULA, Constantes.STATUS_ACTIVO, Constantes.CLAVE,Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,Constantes.MUNICIPIO);
        currentSession().save(test);
        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);
        Asociado test3 = new Asociado(Constantes.CLAVE,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
        currentSession().save(test3);
        Temporada test4 = new Temporada ("test5");
        currentSession().save(test4);
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO,"test","test");
        temporadacolportor.setColportor(test);
        temporadacolportor.setAsociacion(test2);
        temporadacolportor.setAsociado(test3);
        temporadacolportor.setTemporada(test4);
        temporadacolportor.setUnion(union);
        temporadacolportor.setColegio(colegio);
        assertNotNull(temporadacolportor);

        TemporadaColportor temporadacolportor2 = instance.crea(temporadacolportor);
        assertNotNull(temporadacolportor2);
        assertNotNull(temporadacolportor2.getId());

        assertEquals(temporadacolportor, temporadacolportor2);
    }

    @Test
    public void deberiaActualizarTemporadaColportor() {
        log.debug("Deberia actualizar Temporada Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Colportor test = new Colportor(Constantes.TIPO_COLPORTOR,Constantes.MATRICULA,Constantes.STATUS_ACTIVO, Constantes.CLAVE,Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,Constantes.MUNICIPIO);
        currentSession().save(test);
        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);
        Asociado test3 = new Asociado(Constantes.CLAVE,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
        currentSession().save(test3);
        Temporada test4 = new Temporada ("test5");
        currentSession().save(test4);
        Colegio colegio = new Colegio("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO,"test","test");
        temporadacolportor.setColportor(test);
        temporadacolportor.setAsociacion(test2);
        temporadacolportor.setAsociado(test3);
        temporadacolportor.setTemporada(test4);
        temporadacolportor.setUnion(union);
        temporadacolportor.setColegio(colegio);
        assertNotNull(temporadacolportor);
        currentSession().save(temporadacolportor);

        String nombre = "test1";
        temporadacolportor.setObservaciones(nombre);

        TemporadaColportor temporadacolportor2 = instance.actualiza(temporadacolportor);
        assertNotNull(temporadacolportor2);
        assertEquals(nombre, temporadacolportor.getObservaciones());

        assertEquals(temporadacolportor, temporadacolportor2);
    }

    @Test
    public void deberiaEliminarTemporadaColportor() throws UltimoException {
        log.debug("Debiera eliminar Temporada Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Colportor test = new Colportor(Constantes.TIPO_COLPORTOR,Constantes.MATRICULA, Constantes.STATUS_ACTIVO, Constantes.CLAVE,Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,Constantes.MUNICIPIO);
        currentSession().save(test);
        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);
        Asociado test3 = new Asociado(Constantes.CLAVE,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
        currentSession().save(test3);
        Temporada test4 = new Temporada ("test5");
        currentSession().save(test4);
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        String nom = Constantes.STATUS_ACTIVO;
        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO,"test","test");
        temporadacolportor.setColportor(test);
        temporadacolportor.setAsociacion(test2);
        temporadacolportor.setAsociado(test3);
        temporadacolportor.setTemporada(test4);
        temporadacolportor.setUnion(union);
        temporadacolportor.setColegio(colegio);
        currentSession().save(temporadacolportor);
        assertNotNull(temporadacolportor);

        String nombre = instance.elimina(temporadacolportor.getId());
        assertEquals(nom, nombre);

        TemporadaColportor prueba = instance.obtiene(temporadacolportor.getId());
        assertNull(prueba);
    }
}
