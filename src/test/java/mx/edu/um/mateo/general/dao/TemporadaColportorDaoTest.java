/*
 * TODO problemas con el constructor 
 */
package mx.edu.um.mateo.general.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.*;
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

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        Usuario colportor = new Colportor("test--1@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        currentSession().save(colportor);

        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);

        Temporada temporada = new Temporada("test");
        temporada.setAsociacion(asociacion);
        currentSession().save(temporada);
        
        Colegio colegio = new Colegio("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        for (int i = 0; i < 20; i++) {
            TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO + i, "TEST", "TEST");
            temporadacolportor.setColportor((Colportor) colportor);
            temporadacolportor.setAsociacion(asociacion);
            temporadacolportor.setAsociado(asociado);
            temporadacolportor.setTemporada(temporada);
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
        log.debug("Debiera obtener Temporada Colportor por Id");
        String nombre = "test";
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        Usuario colportor = new Colportor("test--1@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        currentSession().save(colportor);

        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setAsociacion(asociacion);

        currentSession().save(asociado);
        Temporada test4 = new Temporada("test5");
        test4.setAsociacion(asociacion);
        currentSession().save(test4);
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "test", "test");
        temporadacolportor.setColportor((Colportor) colportor);
        temporadacolportor.setAsociacion(asociacion);
        temporadacolportor.setAsociado(asociado);
        temporadacolportor.setTemporada(test4);
        temporadacolportor.setUnion(union);
        temporadacolportor.setColegio(colegio);
        currentSession().save(temporadacolportor);
        assertNotNull(temporadacolportor.getId());
        Long id = temporadacolportor.getId();

        TemporadaColportor result = instance.obtiene(id);
        assertNotNull(result.getId());
        assertEquals(nombre, result.getObservaciones());

        assertEquals(result, temporadacolportor);
    }

    @Test
    public void debieraObtenerTemporadaColportorPorColportor() {
        log.debug("Debiera obtener Temporada Colportor por Colportor");
        String nombre = "test";
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        Usuario colportor = new Colportor("test--1@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        currentSession().save(colportor);

        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);

        Temporada test4 = new Temporada("test5");
        test4.setAsociacion(asociacion);
        currentSession().save(test4);
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);

        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "test", "test");
        temporadacolportor.setColportor((Colportor) colportor);
        temporadacolportor.setAsociacion(asociacion);
        temporadacolportor.setAsociado(asociado);
        temporadacolportor.setTemporada(test4);
        temporadacolportor.setUnion(union);
        temporadacolportor.setColegio(colegio);
        currentSession().save(temporadacolportor);
        assertNotNull(temporadacolportor.getId());

        TemporadaColportor result = instance.obtiene((Colportor) colportor);
        assertNotNull(result);

        assertEquals(result, temporadacolportor);
    }

   @Test
    public void debieraObtenerTemporadaColportorPorColportoryTemporada() {
        log.debug("Debiera obtener Temporada Colportor por Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        
        Usuario colportor = new Colportor("test--1@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                    "8262652626", "test", "test", "10706" , "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        currentSession().save(colportor);
        
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test", 
                   Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,
                   Constantes.MUNICIPIO);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        Temporada temporada= new Temporada ("test");
        temporada.setAsociacion(asociacion);
        Temporada temporada2= new Temporada ("test");
        temporada2.setAsociacion(asociacion);
        currentSession().save(temporada);
        currentSession().save(temporada2);
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
         TemporadaColportor temporadacolportor=null;
         TemporadaColportor temporadacolportor2=null;
         for(int i=0; i<3; i++){
        temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO,"test","test");
        temporadacolportor.setColportor((Colportor)colportor);
        temporadacolportor.setAsociacion(asociacion);
        temporadacolportor.setAsociado(asociado);
        temporadacolportor.setTemporada(temporada);
        temporadacolportor.setUnion(union);
        temporadacolportor.setColegio(colegio);
        currentSession().save(temporadacolportor);
        assertNotNull(temporadacolportor.getId());
         }
            temporadacolportor2 = new TemporadaColportor(Constantes.STATUS_ACTIVO,"test","test");
        temporadacolportor2.setColportor((Colportor)colportor);
        temporadacolportor2.setAsociacion(asociacion);
        temporadacolportor2.setAsociado(asociado);
        temporadacolportor2.setTemporada(temporada2);
        temporadacolportor2.setUnion(union);
        temporadacolportor2.setColegio(colegio);
        currentSession().save(temporadacolportor2);
        log.debug("Temporada Colporor 2"+temporadacolportor2.getId());
        assertNotNull(temporadacolportor2.getId());
        TemporadaColportor result = instance.obtiene((Colportor)colportor, temporada2);
        log.debug("Obteniendo Temporada Colportor"+result.getId());
        assertNotNull(result);
      
        assertEquals(result, temporadacolportor2);
    }
//   

        
   
   
   
   @Test

    public void deberiaCrearTemporadaColportor() {
        log.debug("Deberia crear Temporada Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        Usuario colportor = new Colportor("test--1@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        currentSession().save(colportor);

        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);

        Temporada test4 = new Temporada("test5");
        test4.setAsociacion(asociacion);
        currentSession().save(test4);
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "test", "test");
        temporadacolportor.setColportor((Colportor) colportor);
        temporadacolportor.setAsociacion(asociacion);
        temporadacolportor.setAsociado(asociado);
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
    public void deberiaActualizarTseemporadaColportor() {
        log.debug("Deberia actualizar Temporada Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        Usuario colportor = new Colportor("test--1@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        currentSession().save(colportor);

        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);

        Temporada test4 = new Temporada("test5");
        test4.setAsociacion(asociacion);
        currentSession().save(test4);
        Colegio colegio = new Colegio("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "test", "test");
        temporadacolportor.setColportor((Colportor) colportor);
        temporadacolportor.setAsociacion(asociacion);
        temporadacolportor.setAsociado(asociado);
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

        Asociacion asociacion = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        Colportor colportor = new Colportor("test--1@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(asociacion);
        currentSession().save(colportor);

        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);

        Temporada test4 = new Temporada("test5");
        test4.setAsociacion(asociacion);
        currentSession().save(test4);
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        String nom = Constantes.STATUS_ACTIVO;
        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "test", "test");
        temporadacolportor.setColportor(colportor);
        temporadacolportor.setAsociacion(asociacion);
        temporadacolportor.setAsociado(asociado);
        temporadacolportor.setTemporada(test4);
        temporadacolportor.setUnion(union);
        temporadacolportor.setColegio(colegio);
        currentSession().save(temporadacolportor);
        assertNotNull(temporadacolportor);

        String nombre = instance.elimina(temporadacolportor.getId());
        assertEquals(nom, nombre);

        TemporadaColportor prueba = instance.obtiene(temporadacolportor.getId());
        if (prueba != null) {
            fail("Fallo la prueba Eliminar");
        }
    }
}
