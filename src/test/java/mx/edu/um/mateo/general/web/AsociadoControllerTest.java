/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.AsociadoDao;
import mx.edu.um.mateo.general.model.Asociado;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
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
 * @author gibrandemetrioo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class AsociadoControllerTest extends BaseTest{
    private static final Logger log = LoggerFactory.getLogger(AsociadoControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private AsociadoDao asociadoDao;

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
    public void debieraMostrarListaDeAsociado() throws Exception {
        log.debug("Debiera monstrar lista asociado");
        
        for (int i = 0; i < 20; i++) {
           Asociado asociado = new Asociado(Constantes.CLAVE+i,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
            asociadoDao.crea(asociado);
            assertNotNull(asociado);
        }

        this.mockMvc.perform(get(Constantes.PATH_ASOCIADO))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ASOCIADO_LISTA+ ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_ASOCIADOS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }
    @Test
    public void debieraMostrarAsociado() throws Exception {
        log.debug("Debiera mostrar  asociado");
        Asociado asociado = new Asociado(Constantes.CLAVE,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
        asociado = asociadoDao.crea(asociado);
        assertNotNull(asociado);

        this.mockMvc.perform(get(Constantes.PATH_ASOCIADO_VER +"/"+ asociado.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_ASOCIADO_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_ASOCIADO));
    }
    @Test
    public void debieraCrearAsociado() throws Exception {
        log.debug("Debiera crear asociado");

        this.mockMvc.perform(post(Constantes.PATH_ASOCIADO_CREA)
                .param("clave", "test")
                .param("telefono", "test")
                .param("correo", "test@tes.tes")
                .param("calle", "test1")
                .param("colonia", "test1")
                .param("municipio", "test1")
                .param("status", Constantes.STATUS_ACTIVO))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.creado.message"));
    }
    @Test
    public void debieraActualizarAsociado() throws Exception {
        log.debug("Debiera actualizar  asociado");
        Asociado asociado = new Asociado(Constantes.CLAVE,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
        asociado = asociadoDao.crea(asociado);
        assertNotNull(asociado);

        this.mockMvc.perform(post(Constantes.PATH_ASOCIADO_ACTUALIZA)
                .param("id",asociado.getId().toString())
                .param("version", asociado.getVersion().toString())
                .param("calle", "test1")
                .param("colonia", "test1")
                .param("municipio", "test1")
                .param("clave", "test2")
                .param("telefono", "test4")
                .param("status", asociado.getStatus()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.actualizado.message"));
    }
    @Test
    public void debieraEliminarAsociacion() throws Exception {
        log.debug("Debiera eliminar  asociado");
        Asociado asociado = new Asociado(Constantes.CLAVE,Constantes.TELEFONO, Constantes.STATUS_ACTIVO,Constantes.COLONIA,Constantes.MUNICIPIO,Constantes.CALLE);
        asociadoDao.crea(asociado);
        assertNotNull(asociado);

        this.mockMvc.perform(post(Constantes.PATH_ASOCIADO_ELIMINA)
                .param("id", asociado.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "asociado.eliminado.message"));
    }
}
