/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.dao;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Documento;
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
public class DocumentoDaoTest extends BaseTest{
     private static final Logger log = LoggerFactory.getLogger(DocumentoDaoTest.class);
    @Autowired
    private DocumentoDao instance;
    @Autowired
    private SessionFactory sessionFactory;
    
   private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
   
   @Test
    public void deberiaMostrarListaDeDocumento() {
        log.debug("Debiera mostrar lista de documento");

      for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,Constantes.FECHA,Constantes.IMPORTE,Constantes.OBSERVACIONES);
            currentSession().save(documento);
            assertNotNull(documento);
        }

        Map<String, Object> params = null;
        Map result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_DOCUMENTOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Documento>) result.get(Constantes.CONTAINSKEY_DOCUMENTOS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue()); 
    }

    @Test
    public void debieraObtenerDocumento() {
        log.debug("Debiera obtener documento");

        String folio = "test";
        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,Constantes.FECHA,Constantes.IMPORTE,Constantes.OBSERVACIONES);
        currentSession().save(documento);
        assertNotNull(documento.getId());
        Long id = documento.getId();

        Documento result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(folio, result.getFolio());

        assertEquals(result, documento);
    }

    @Test
    public void deberiaCrearDocumento() {
        log.debug("Deberia crear Documento");

        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,Constantes.FECHA,Constantes.IMPORTE,Constantes.OBSERVACIONES);
        assertNotNull(documento);

        Documento documento2 = instance.crea(documento);
        assertNotNull(documento2);
        assertNotNull(documento2.getId());

        assertEquals(documento, documento2);
    }

    @Test
    public void deberiaActualizarDocumento() {
        log.debug("Deberia actualizar Documento");

        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,Constantes.FECHA,Constantes.IMPORTE,Constantes.OBSERVACIONES);
        assertNotNull(documento);
        currentSession().save(documento);

        String folio = "test1";
        documento.setFolio(folio);

        Documento documento2 = instance.actualiza(documento);
        assertNotNull(documento2);
        assertEquals(folio, documento.getFolio());

        assertEquals(documento, documento2);
    }

    @Test
    public void deberiaEliminarDocumento() throws UltimoException {
        log.debug("Debiera eliminar Documento");

        String fol = "test";
        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,Constantes.FECHA,Constantes.IMPORTE,Constantes.OBSERVACIONES);
        currentSession().save(documento);
        assertNotNull(documento);

        String folio = instance.elimina(documento.getId());
        assertEquals(fol, folio);

        Documento prueba = instance.obtiene(documento.getId());
        assertNull(prueba);
    }
    
}
