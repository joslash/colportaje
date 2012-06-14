/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * TODO problemas con nullpointerexception
 */
package mx.edu.um.mateo.general.web;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.AsociadoDao;
import mx.edu.um.mateo.general.dao.DocumentoDao;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.general.dao.TemporadaColportorDao;
import mx.edu.um.mateo.general.dao.UnionDao;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    @Autowired
    private RolDao rolDao;
    @Autowired
    private UnionDao unionDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private TemporadaColportorDao temporadaColportorDao;
    @Autowired
    private AsociadoDao asociadoDao;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

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
    public void debieraMostrarListaDeDocumentoDeColportor() throws Exception {
        log.debug("Debiera monstrar lista de documentos");
        Union union = new Union("test");
        union = unionDao.crea(union);
        Rol rol = new Rol("ROLE_COL");
        rol = rolDao.crea(rol);
        Usuario usuario = new Usuario("test@test.com", "test", "test", "test", "test");
        Long asociacionId = 0l;
        actualizaUsuario:
        for (Asociacion asociacion : union.getAsociaciones()) {
            asociacionId = asociacion.getId();
            break actualizaUsuario;
        }
        usuario = usuarioDao.crea(usuario, asociacionId, new String[]{rol.getAuthority()});
        Long id = usuario.getId();
        assertNotNull(id);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));
        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,new Date(),Constantes.IMPORTE, Constantes.OBSERVACIONES,null);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }
        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_LISTA))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_DOCUMENTOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
    @Test
    public void debieraMostrarListaDeDocumentoDeColportorAlAsociado() throws Exception {
        log.debug("Debiera monstrar lista de documentos de un colportor al asociado");
        Union union = new Union("test");
        union = unionDao.crea(union);
        Rol rol = new Rol("ROLE_ASO");
        rol = rolDao.crea(rol);
        Usuario usuario = new Usuario("test--3@test.com", "test", "test", "test", "test");
        Long asociacionId = 0l;
        actualizaUsuario:
        for (Asociacion asociacion : union.getAsociaciones()) {
            asociacionId = asociacion.getId();
            break actualizaUsuario;
        }
        usuario = usuarioDao.crea(usuario, asociacionId, new String[]{rol.getAuthority()});
        Long id = usuario.getId();
        assertNotNull(id);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Colportor colportorTmp = new Colportor("test@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportorTmp.setAsociacion(asociacion);
        currentSession().save(colportorTmp);

        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));
       
        for (int i = 0; i < 20; i++) {
            Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,
                    new Date(), Constantes.IMPORTE, Constantes.OBSERVACIONES, null);
            documentoDao.crea(documento);
            assertNotNull(documento);
        }
        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_LISTA)
                .sessionAttr("colportorTmp", colportorTmp))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_DOCUMENTOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
    @Test
    public void deberiaMostrarDocumentosDelaClaveDelComportorAlAsociado() throws Exception{
        log.debug("Mostrando documentos de un colportor seleccionado por su clave");
        Union union = new Union("test");
        union = unionDao.crea(union);
        Rol rol = new Rol("ROLE_ASO");
        rol = rolDao.crea(rol);
        Usuario usuario = new Asociado("test@um.edu.mx", "test", "test", "test", "test", "A", "98745", "8262630900", "test", "test", "tset");
        Long asociacionId = 0l;
        actualizaUsuario:
        for (Asociacion asociacion : union.getAsociaciones()) {
            asociacionId = asociacion.getId();
            break actualizaUsuario;
        }
        usuario = usuarioDao.crea(usuario, asociacionId, new String[]{rol.getAuthority()});
        Long id = usuario.getId();
        assertNotNull(id);
        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));

        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Colportor colportor = null;
        TemporadaColportor temporadaColportor = null;
        String clave = "12345";
        Temporada temporada = new Temporada("test");
        currentSession().save(temporada);
        Colegio colegio = new Colegio("test3", Constantes.STATUS_ACTIVO);
        currentSession().save(colegio);
        for (int i = 0; i < 10; i++) {
            colportor = new Colportor("test" + i + "@test.com", "test", "test", "test",
                    "test", "1234" + i, Constantes.STATUS_ACTIVO, "8262652626", "test", "test",
                    "10706" + i, "test", "test001", new Date());
            colportor.setAsociacion(asociacion);
            currentSession().save(colportor);
            assertNotNull(colportor.getId());
            temporadaColportor = new TemporadaColportor(colportor, asociacion, usuario, temporada, union, colegio);
            temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
            temporadaColportor.setObjetivo("Objetivo");
            temporadaColportor.setObservaciones("Observaciones");
            temporadaColportor.setFecha(new Date());
            temporadaColportorDao.crea(temporadaColportor);
            assertNotNull(temporadaColportor);
            log.debug("TemporadaColportor" + temporadaColportor);
        }
            this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO)
                .param( "clave", clave))
                .andExpect(request().sessionAttribute("temporadaColportorTmp", temporadaColportor))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/lista" ));    
//                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_LISTA + ".jsp"));
//                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_DOCUMENTOS))
//                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
//                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
//                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    
        
    }

    @Test
    public void debieraMostrarDocumento() throws Exception {
        log.debug("Debiera mostrar documento");
        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,new Date(),Constantes.IMPORTE, Constantes.OBSERVACIONES,null);
        documento = documentoDao.crea(documento);
        assertNotNull(documento);

        this.mockMvc.perform(get(Constantes.PATH_DOCUMENTO_VER + "/" + documento.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_DOCUMENTO_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_DOCUMENTO));
    }

    @Test
    public void debieraCrearDocumento() throws Exception {
        log.debug("Debiera crear documento");

       // this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_CREA).param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO)).andExpect(status().isOk()).andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE)).andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.creado.message"));
        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_CREA).
                param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO).
                param("folio", Constantes.FOLIO).
                param("importe", "0.0").
                param("fecha", "05/05/2010").
                param("observaciones", Constantes.OBSERVACIONES)).
               
                
                andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.creado.message"));

    }

    @Test
    public void debieraActualizarDocumento() throws Exception {
        log.debug("Debiera actualizar documento");
        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,new Date(),Constantes.IMPORTE, Constantes.OBSERVACIONES,null);
        documento = documentoDao.crea(documento);
        assertNotNull(documento);

        //this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_ACTUALIZA).param("id", documento.getId().toString()).param("version", documento.getVersion().toString()).param("tipoDeDocumento", documento.getTipoDeDocumento())).andExpect(status().isOk()).andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE)).andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.actualizado.message"));
        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_CREA).
                param("tipoDeDocumento", Constantes.TIPO_DOCUMENTO).
                param("folio", Constantes.FOLIO).
                param("importe", "0.0").
                param("fecha", "05/05/2010").
                param("observaciones", Constantes.OBSERVACIONES))
                .andExpect(status().isOk()).andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.creado.message"));

    }

    @Test
    public void debieraEliminarDocumento() throws Exception {
        log.debug("Debiera eliminar documento");
        Documento documento = new Documento(Constantes.TIPO_DOCUMENTO, Constantes.FOLIO,new Date(),Constantes.IMPORTE, Constantes.OBSERVACIONES,null);
        documentoDao.crea(documento);
        assertNotNull(documento);

        this.mockMvc.perform(post(Constantes.PATH_DOCUMENTO_ELIMINA)
                .param("id", documento.getId().toString()))
                .andExpect(status().isOk()).andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "documento.eliminado.message"));
    }
}
