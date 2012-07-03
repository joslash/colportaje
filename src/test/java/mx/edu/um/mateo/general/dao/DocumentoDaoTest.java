/*
 * TODO problemas con el constructor
 * 
 */
package mx.edu.um.mateo.general.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class DocumentoDaoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(DocumentoDaoTest.class);
    @Autowired
    private DocumentoDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

//    @Test
//    public void deberiaMostrarListaDeDocumento() {
//        log.debug("Debiera mostrar lista de documento");
//        
//        Union union = new Union("test");
//        union.setStatus(Constantes.STATUS_ACTIVO);
//        currentSession().save(union);
//        Usuario colportor = new Usuario("test", Constantes.STATUS_ACTIVO, "8262652626", "test", "1070666");
//        currentSession().save(colportor);
//        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
//        currentSession().save(test2);
//        Asociado test3 = new Asociado("test@test.com", "test", "test", "test", "test", 
//                   Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,
//                   Constantes.MUNICIPIO);
//        currentSession().save(test3);
//        Temporada test4 = new Temporada ("test5");
//        currentSession().save(test4);
//        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
//        currentSession().save(colegio);
//        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO,"test","test");
//        temporadacolportor.setColportor((Colportor)colportor);
//        temporadacolportor.setAsociacion(test2);
//        temporadacolportor.setAsociado(test3);
//        temporadacolportor.setTemporada(test4);
//        temporadacolportor.setUnion(union);
//        temporadacolportor.setColegio(colegio);
//        currentSession().save(temporadacolportor);
//        assertNotNull(temporadacolportor.getId());
//
//        for (int i = 0; i < 20; i++) {
//            Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,new Date(),Constantes.IMPORTE, Constantes.OBSERVACIONES, temporadacolportor);
//            currentSession().save(documento);
//            assertNotNull(documento);
//        }
//
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("temporadaColportor",temporadacolportor);
//        Map result = instance.lista(params);
//        assertNotNull(result.get(Constantes.CONTAINSKEY_DOCUMENTOS));
//        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
//
//        assertEquals(10, ((List<Documento>) result.get(Constantes.CONTAINSKEY_DOCUMENTOS)).size());
//        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
//    }
//    
//        @Test
//    public void deberiaTraerListaVacia() {
//        log.debug("Debiera traer lista Vacia");
//        
//        Union union = new Union("test");
//        union.setStatus(Constantes.STATUS_ACTIVO);
//        currentSession().save(union);
//        Usuario colportor = new Usuario("test", Constantes.STATUS_ACTIVO, "8262652626", "test", "1070666");
//        currentSession().save(colportor);
//        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
//        currentSession().save(test2);
//        Asociado test3 = new Asociado("test@test.com", "test", "test", "test", "test", 
//                   Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,
//                   Constantes.MUNICIPIO);
//        currentSession().save(test3);
//        Temporada test4 = new Temporada ("test5");
//        currentSession().save(test4);
//        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
//        currentSession().save(colegio);
//        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO,"test","test");
//        temporadacolportor.setColportor((Colportor)colportor);
//        temporadacolportor.setAsociacion(test2);
//        temporadacolportor.setAsociado(test3);
//        temporadacolportor.setTemporada(test4);
//        temporadacolportor.setUnion(union);
//        temporadacolportor.setColegio(colegio);
//        currentSession().save(temporadacolportor);
//        assertNotNull(temporadacolportor.getId());
//
//        for (int i = 0; i < 20; i++) {
//            Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,new Date(),Constantes.IMPORTE, Constantes.OBSERVACIONES, temporadacolportor);
//            currentSession().save(documento);
//            assertNotNull(documento);
//        }
//
//        Map<String, Object> params = null;
//         Map result = instance.lista(params);
//        assertNotNull(result.get(Constantes.CONTAINSKEY_DOCUMENTOS));
//        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
//
//        assertEquals(0, ((List<Documento>) result.get(Constantes.CONTAINSKEY_DOCUMENTOS)).size());
//        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
//    }
//    
//
//    @Test
//    public void debieraObtenerDocumento() {
//        log.debug("Debiera obtener documento");
//        
//        Union union = new Union("test");
//        union.setStatus(Constantes.STATUS_ACTIVO);
//        currentSession().save(union);
//        Usuario colportor = new Usuario("test", Constantes.STATUS_ACTIVO, "8262652626", "test", "1070666");
//        currentSession().save(colportor);
//        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
//        currentSession().save(test2);
//        Asociado test3 = new Asociado("test@test.com", "test", "test", "test", "test", 
//                   Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,
//                   Constantes.MUNICIPIO);
//        currentSession().save(test3);
//        Temporada test4 = new Temporada ("test5");
//        currentSession().save(test4);
//        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
//        currentSession().save(colegio);
//        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO,"test","test");
//        temporadacolportor.setColportor((Colportor)colportor);
//        temporadacolportor.setAsociacion(test2);
//        temporadacolportor.setAsociado(test3);
//        temporadacolportor.setTemporada(test4);
//        temporadacolportor.setUnion(union);
//        temporadacolportor.setColegio(colegio);
//        currentSession().save(temporadacolportor);
//        assertNotNull(temporadacolportor.getId());
//
//        String folio = "test";
//        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,new Date(),  Constantes.IMPORTE, Constantes.OBSERVACIONES, temporadacolportor);
//        currentSession().save(documento);
//        assertNotNull(documento.getId());
//        Long id = documento.getId();
//
//        Documento result = instance.obtiene(id);
//        assertNotNull(result);
//        assertEquals(folio, result.getFolio());
//
//        assertEquals(result, documento);
//    }
//
//    @Test
//    public void deberiaCrearDocumento() {
//        log.debug("Deberia crear Documento");
//
//        Union union = new Union("test");
//        union.setStatus(Constantes.STATUS_ACTIVO);
//        currentSession().save(union);
//        Usuario colportor = new Usuario("test", Constantes.STATUS_ACTIVO, "8262652626", "test", "1070666");
//        currentSession().save(colportor);
//        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
//        currentSession().save(test2);
//        Asociado test3 = new Asociado("test@test.com", "test", "test", "test", "test", 
//                   Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,
//                   Constantes.MUNICIPIO);
//        currentSession().save(test3);
//        Temporada test4 = new Temporada ("test5");
//        currentSession().save(test4);
//        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
//        currentSession().save(colegio);
//        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO,"test","test");
//        temporadacolportor.setColportor((Colportor)colportor);
//        temporadacolportor.setAsociacion(test2);
//        temporadacolportor.setAsociado(test3);
//        temporadacolportor.setTemporada(test4);
//        temporadacolportor.setUnion(union);
//        temporadacolportor.setColegio(colegio);
//        currentSession().save(temporadacolportor);
//        assertNotNull(temporadacolportor.getId());
//
//        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, temporadacolportor);
//        assertNotNull(documento);
//
//        Documento documento2 = instance.crea(documento);
//        assertNotNull(documento2);
//        assertNotNull(documento2.getId());
//
//        assertEquals(documento, documento2);
//    }
//
//    @Test
//    public void deberiaActualizarDocumento() {
//        log.debug("Deberia actualizar Documento");
//          Union union = new Union("test");
//        union.setStatus(Constantes.STATUS_ACTIVO);
//        currentSession().save(union);
//        Usuario colportor = new Usuario("test", Constantes.STATUS_ACTIVO, "8262652626", "test", "1070666");
//        currentSession().save(colportor);
//        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
//        currentSession().save(test2);
//        Asociado test3 = new Asociado("test@test.com", "test", "test", "test", "test", 
//                   Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO,Constantes.CALLE,Constantes.COLONIA,
//                   Constantes.MUNICIPIO);
//        currentSession().save(test3);
//        Temporada test4 = new Temporada ("test5");
//        currentSession().save(test4);
//        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
//        currentSession().save(colegio);
//        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO,"test","test");
//        temporadacolportor.setColportor((Colportor)colportor);
//        temporadacolportor.setAsociacion(test2);
//        temporadacolportor.setAsociado(test3);
//        temporadacolportor.setTemporada(test4);
//        temporadacolportor.setUnion(union);
//        temporadacolportor.setColegio(colegio);
//        currentSession().save(temporadacolportor);
//        assertNotNull(temporadacolportor.getId());
//
//        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,new Date(),  Constantes.IMPORTE, Constantes.OBSERVACIONES, temporadacolportor);
//        assertNotNull(documento);
//        currentSession().save(documento);
//
//        String folio = "test1";
//        documento.setFolio(folio);
//
//        Documento documento2 = instance.actualiza(documento);
//        assertNotNull(documento2);
//        assertEquals(folio, documento.getFolio());
//
//        assertEquals(documento, documento2);
//    }
//
    @Test
    public void deberiaEliminarDocumento() throws UltimoException {
        log.debug("Debiera eliminar Documento");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);

        Asociacion test2 = new Asociacion("test", Constantes.STATUS_ACTIVO, union);
        currentSession().save(test2);

        Colportor colportor = new Colportor("test--@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setAsociacion(test2);
        currentSession().save(colportor);

        Asociado test3 = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                Constantes.MUNICIPIO);
        test3.setAsociacion(test2);
        currentSession().save(test3);

        Temporada test4 = new Temporada("test5");
        currentSession().save(test4);
        Colegio colegio = new Colegio(Constantes.NOMBRE, Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        TemporadaColportor temporadacolportor = new TemporadaColportor(Constantes.STATUS_ACTIVO, "test", "test");
        temporadacolportor.setColportor(colportor);
        temporadacolportor.setAsociacion(test2);
        temporadacolportor.setAsociado(test3);
        temporadacolportor.setTemporada(test4);
        temporadacolportor.setUnion(union);
        temporadacolportor.setColegio(colegio);
        currentSession().save(temporadacolportor);
        assertNotNull(temporadacolportor.getId());

        String fol = "test";
        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO, new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, temporadacolportor);
        currentSession().save(documento);
        assertNotNull(documento);

        String folio = instance.elimina(documento.getId());
        assertEquals(fol, folio);

        Documento prueba = instance.obtiene(documento.getId());
        if (prueba != null) {
            fail("Fallo prueba Eliminar");
        }
    }
}
