/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;
import mx.edu.um.mateo.general.dao.DocumentoDao;
import mx.edu.um.mateo.general.model.Documento;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.um.edu.mateo.Constantes;
import static org.junit.Assert.assertNotNull;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class DocumentoControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(DocumentoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private DocumentoDao documentoDao;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void debieraMostrarListaDeDocumento() throws Exception {
        log.debug("Debiera monstrar lista de documentos");
        
        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,Constantes.FECHA,Constantes.IMPORTE,Constantes.OBSERVACIONES);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }

        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_DOCUMENTOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

   @Test
    public void debieraMostrarDocumento() throws Exception {
        log.debug("Debiera mostrar documento");
        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,Constantes.FECHA,Constantes.IMPORTE,Constantes.OBSERVACIONES);
        documento = documentoDao.crea(documento);
        assertNotNull(documento);

        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_VER +"/"+ documento.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_DOCUMENTO));
    }

    @Test
    public void debieraCrearDocumento() throws Exception {
        log.debug("Debiera crear documento");

        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_CREA)
                .param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO)
                .param("folio", Constantes.FOLIO)
                .param("fecha",Constantes.FECHA)
                .param("importe",Constantes.IMPORTE)
                .param("observaciones",Constantes.OBSERVACIONES))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.creado.message"));
  
   
    }

    @Test
    public void debieraActualizarDocumento() throws Exception {
        log.debug("Debiera actualizar documento");
        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,Constantes.FECHA,Constantes.IMPORTE,Constantes.OBSERVACIONES);
        documento = documentoDao.crea(documento);
        assertNotNull(documento);

        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_ACTUALIZA)
                .param("id",documento.getId().toString())
                .param("version", documento.getVersion().toString())
                .param("tipoDeDocumento", documento.getTipoDeDocumento())
                .param("folio", documento.getFolio())
                .param("fecha", documento.getFecha())
                .param("importe", documento.getImporte())
                .param("observaciones", documento.getObservaciones()))
                 .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.actualizado.message"));
    
 
     }

    @Test
    public void debieraEliminarDocumento() throws Exception {
        log.debug("Debiera eliminar documento");
        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,Constantes.FECHA,Constantes.IMPORTE,Constantes.OBSERVACIONES);
        documentoDao.crea(documento);
        assertNotNull(documento);

        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_ELIMINA)
                .param("id", documento.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                    .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.eliminado.message"));
    }
}
